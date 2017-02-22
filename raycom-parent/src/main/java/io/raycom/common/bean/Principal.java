package io.raycom.common.bean;

import java.io.Serializable;
import java.util.List;

import io.raycom.common.utils.user.UserUtils;

/**
	 * 授权用户信息
	 */
public class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		private boolean mobileLogin; // 是否手机登录
		private List<SystemRole> role; // 是否手机登录
		

		public Principal(SystemUser user, boolean mobileLogin,List<SystemRole> role) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
			this.role = role;
		}

		public List<SystemRole> getRole() {
			return role;
		}

		public void setRole(List<SystemRole> role) {
			this.role = role;
		}


		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try{
				return (String) UserUtils.getSession().getId();
			}catch (Exception e) {
				return "";
			}
		}
		
		@Override
		public String toString() {
			return id;
		}

	}