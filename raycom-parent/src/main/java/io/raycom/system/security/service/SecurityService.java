package io.raycom.system.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.raycom.common.bean.SystemMenu;
import io.raycom.common.bean.SystemRole;
import io.raycom.common.bean.SystemUser;
import io.raycom.common.config.Constant;
import io.raycom.common.config.Global;
import io.raycom.common.service.BaseService;
import io.raycom.common.utils.cache.CacheUtils;
import io.raycom.common.utils.string.Encodes;
import io.raycom.common.utils.string.StringUtils;
import io.raycom.common.utils.user.UserUtils;
import io.raycom.common.web.Servlets;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.security.Digests;
import io.raycom.system.web.dao.SecurityDao;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author caoshengquan
 * @version 2016-02-01
 */
@Service
@Transactional(readOnly = true)
public class SecurityService extends BaseService implements InitializingBean {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private SecurityDao securityDao;
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public SystemUser getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public SystemUser getUserByLoginName(String loginName) {
		SystemUser   user = securityDao.getByLoginName(loginName);	
		if (user == null){
				return null;
		}
		user.setRoleList(securityDao.getRoleListByUserId(user.getId()));
		CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_ID_ + user.getId(), user);
		CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		return user;
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	//-- Role Service --//
	
	public SystemRole getRole(String id) {
		return securityDao.getRole(id);
	}

	public SystemRole getRoleByName(String name) {
		return securityDao.getRoleByName(name);
	}
	
	public List<SystemRole> findRole(String id){
		return securityDao.getRoleListByUserId(id);
	}
	
	public List<SystemRole> findAllRole(){
		@SuppressWarnings("unchecked")
		List<SystemRole> roleList = (List<SystemRole>)UserUtils.getCache(Constant.CACHE_ROLE_LIST);
		if (roleList == null){
			SystemUser user = UserUtils.getUser();
			if (user.isAdmin()){
				roleList = securityDao.getAllRoleList();
			}else{
				roleList = securityDao.getRoleListByUserId(user.getId());
			}
			UserUtils.putCache(Constant.CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	//-- Menu Service --//
	
	public SystemMenu getMenu(String id) {
		return securityDao.getMenu(id);
	}

	@SuppressWarnings("unchecked")
	public List<String> getPermissionByUserId(){
		/*List<String> strList = (List<String>)UserUtils.getCache(Constant.CACHE_PERMISSION_LIST);
		if (strList == null){*/
		List<String> strList = new ArrayList<String>();
		SystemUser user = UserUtils.getUser();
		if (user.isAdmin(user.getId())){
			strList.add("*");
		}else{
			List<RData> permissionData  = securityDao.getPermissionByUserId(user.getId());
			for (RData rData : permissionData) {
				String  permisson="";
				if(rData!=null&&rData.getString("permissionCode")!=null){
					for (String per : StringUtils.split(rData.getString("permissionCode"),",")){
						//permisson = permisson+rData.getString("permission")+":"+per+",";
						strList.add(rData.getString("permission")+":"+per);
					}
				}
			}
		}
		/*	UserUtils.putCache(Constant.CACHE_PERMISSION_LIST, strList);
		}*/
		return strList;
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(SystemUser user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
		user.setLoginDate(new Date());
		securityDao.updateLoginInfo(user);
	}
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		//String home = servletContext.getRealPath("/");
		System.setProperty("log4j.home",Global.getConfig("log4j.home"));
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By http://csvalue.cn\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	
}
