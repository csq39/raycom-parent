/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.common.utils.user;

import io.raycom.common.bean.Principal;
import io.raycom.common.bean.SystemUser;
import io.raycom.common.config.Constant;
import io.raycom.common.utils.SpringContextHolder;
import io.raycom.common.utils.cache.CacheUtils;
import io.raycom.common.utils.string.StringUtils;
import io.raycom.system.web.dao.UtilDao;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 用户工具类
 * @author caoshengquan
 * @version 2013-12-05
 */
public class UserUtils {

	private static UtilDao utilDao = SpringContextHolder.getBean(UtilDao.class);

	
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static SystemUser get(String id){
		SystemUser user = (SystemUser)CacheUtils.get(Constant.USER_CACHE, Constant.USER_CACHE_ID_ + id);
		if (user ==  null){
			user = utilDao.getUser(id);
			if (user == null){
				return null;
			}
			user.setRoleList(utilDao.getRoleListByUserId(id));
			CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static SystemUser getByLoginName(String loginName){
		SystemUser user = (SystemUser)CacheUtils.get(Constant.USER_CACHE, Constant.USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			user = utilDao.getByLoginName(loginName);
			if (user == null){
				return null;
			}
			user.setRoleList(utilDao.getRoleListByUserId(user.getId()));
			CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static String getOfficeNameById(String officeId){
		if(officeId==null)
			officeId = getUser().getOfficeId();
		if(StringUtils.isEmpty(officeId)) return "";
		
		String officeName = (String)CacheUtils.get(Constant.USER_CACHE, Constant.USER_CACHE_OFFICE_ID_ + officeId);
		if (officeName == null){
			officeName = utilDao.getOfficeNameById(officeId);
			if (officeName == null){
				return "";
			}
			CacheUtils.put(Constant.USER_CACHE, Constant.USER_CACHE_OFFICE_ID_ + officeId, officeName);
		}
		return officeName;
	}
	public static String getOfficeName(){
		return getOfficeNameById(null);
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(Constant.CACHE_ROLE_LIST);
		removeCache(Constant.CACHE_MENU_LIST);
		removeCache(Constant.CACHE_AREA_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(SystemUser user){
		CacheUtils.remove(Constant.USER_CACHE, Constant.USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(Constant.USER_CACHE, Constant.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
	}
	/**
	 * 获取当前用户
	 * @return 取不到返回 new SystemUser()
	 */
	public static SystemUser getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			SystemUser user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new SystemUser();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new SystemUser();
	}

	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== SystemUser Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}
	
	
}
