/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.system.security.shiro.realm;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.raycom.common.bean.Principal;
import io.raycom.common.bean.SystemRole;
import io.raycom.common.bean.SystemUser;
import io.raycom.common.config.Constant;
import io.raycom.common.config.Global;
import io.raycom.common.servlet.ValidateCodeServlet;
import io.raycom.common.utils.string.Encodes;
import io.raycom.common.utils.user.UserUtils;
import io.raycom.common.web.Servlets;
import io.raycom.components.context.event.listener.RaycomEventPublisher;
import io.raycom.components.context.event.sysEvent.LoginEvent;
import io.raycom.components.util.log.util.LogUtils;
import io.raycom.system.security.LoginValidater;
import io.raycom.system.security.service.SecurityService;
import io.raycom.system.security.shiro.authc.UsernamePasswordToken;
import io.raycom.system.security.shiro.session.SessionDAO;

/**
 * 系统安全认证实现类
 * @author caoshengquan
 * @version 2014-7-5
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SecurityService securityService;
	
	private SessionDAO sessionDao;

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		//int activeSessionSize = securityService.getSessionDao().getActiveSessions(false).size();
		int activeSessionSize = sessionDao.getActiveSessions(false).size();
		if (logger.isDebugEnabled()){
			logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
		}
		
		// 校验登录验证码
		if (LoginValidater.isValidateCodeLogin(token.getUsername(), false, false)){
			Session session = UserUtils.getSession();
			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new AuthenticationException("msg:验证码错误, 请重试.");
			}
		}
		
		// 校验用户名密码
		SystemUser user = securityService.getUserByLoginName(token.getUsername());
		if (user != null) {
			List<SystemRole> userRole = securityService.findRole(user.getId());
			if (Global.NO.equals(user.getLoginFlag())){
				throw new AuthenticationException("msg:该帐号已禁止登录.");
			}
			byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin(),userRole), 
					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
		} else {
			throw new AuthenticationException("msg:该用户名不存在，请确认.");
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))){
			Collection<Session> sessions = sessionDao.getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0){
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()){
					for (Session session : sessions){
						sessionDao.delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else{
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
					//throw new AuthenticationException("msg:账号验证有误，请重新登录。");
				}
			}
		}
		SystemUser user = securityService.getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			
			// 添加用户角色信息
			for (SystemRole role : user.getRoleList()){
				if("admin".equals(role.getRoleType())){
					user.setAdmin(true);
					user.setSysData(true);
				}
				info.addRole(role.getEnname());
			}
			
			List<SystemRole> roleList= user.getRoleList();
			for (SystemRole systemRole : roleList) {
				if("Y".equals(systemRole.getSysData())){
					user.setSysData(true);
					break;
				}
			}
						
			List<String> list = securityService.getPermissionByUserId();
			for (String permission : list){
				if (StringUtils.isNotBlank(permission)){
					// 添加基于Permission的权限信息
					for (String per : StringUtils.split(permission,",")){
						info.addStringPermission(per);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 更新登录IP和时间
			securityService.updateUserLoginInfo(user);
			// 记录登录日志
			LogUtils.saveLog(Servlets.getRequest(), "系统登录");  
			RaycomEventPublisher.publishEvent(new LoginEvent(user));
			return info;
		} else {
			return null;
		}
	}
	
	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}
	
	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
        		authorizationValidate(permission);
            }
        }
		return super.isPermitted(permissions, info);
	}
	
	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		return super.isPermitted(principals, permission);
	}
	
	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
            	authorizationValidate(permission);
            }
        }
		return super.isPermittedAll(permissions, info);
	}
	
	/**
	 * 授权验证方法
	 * @param permission
	 */
	private void authorizationValidate(Permission permission){
		// 模块授权预留接口
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constant.HASH_ALGORITHM);
		matcher.setHashIterations(Constant.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空所有关联认证
	 * @Deprecated 不需要清空，授权缓存保存到session中
	 */
	@Deprecated
	public void clearAllCachedAuthorizationInfo() {
//		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
//		if (cache != null) {
//			for (Object key : cache.keys()) {
//				cache.remove(key);
//			}
//		}
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	public void setSessionDao(SessionDAO sessionDao) {
		this.sessionDao = sessionDao;
	}
	
	

}
