/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import io.raycom.common.config.Global;
import io.raycom.common.utils.lang.Collections3;
import io.raycom.system.framework.collection.RData;

/**
 * 系统用户，用于权限认证
 * @author caoshengquan
 * @version 2015-11-12
 */
public class SystemUser implements Serializable  {

	
	private static final long serialVersionUID = 1L;
	protected String id;    // 实体编号（唯一标识）
	private String loginName;// 登录名
	private String password;// 密码

	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像
	
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	
	private boolean isAdmin=false;	// 是否是管理员
	
	private boolean isSysData=false;	// 是否是管理员
	
	private List<SystemRole> roleList = Lists.newArrayList(); // 拥有角色列表
	private List<RData> whList = Lists.newArrayList(); // 拥有角色列表
	private List<RData> roleOfficeList = Lists.newArrayList(); // 拥有角色列表
	private String roleOfficeId; // 拥有角色列表
	
	private String whId  ; // 拥有角色列表
	
	private String no;		// 工号
	private String companyId;	// 归属公司
	private String officeId;	// 归属部门
	

	public SystemUser() {
		super();
		this.loginFlag = Global.YES;
	}
	
	public List<RData> getWhList() {
		return whList;
	}

	public void setWhList(List<RData> whList) {
		this.whList = whList;
		String whId  ="";
		for (RData rData : whList) {
			whId+=rData.getString("whId")+",";
		}
		if(whId.length()>0)
			this.whId = whId.substring(0, whId.length()-1);
	}

	public SystemUser(String id){
		this();
		this.id = id;
	}

	public SystemUser(String id, String loginName){
		this();
		this.id = id;
		this.loginName = loginName;
	}
	
	public boolean isSysData() {
		return isSysData||"10000001".equals(id);
	}

	public void setSysData(boolean isSysData) {
		this.isSysData = isSysData;
	}


	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	public String getId() {
		return id;
	}


	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<RData> getRoleOfficeList() {
		return roleOfficeList;
	}

	public void setRoleOfficeList(List<RData> roleOfficeList) {
		this.roleOfficeList = roleOfficeList;
		String roleOfficeIdStr  ="";
		for (RData rData : roleOfficeList) {
			roleOfficeIdStr+=rData.getString("officeId")+",";
		}
		if(roleOfficeIdStr.length()>0)
			this.roleOfficeId = roleOfficeIdStr.substring(0, roleOfficeIdStr.length()-1);
	}

	public String getRoleOfficeId() {
		return roleOfficeId;
	}

	public void setRoleOfficeId(String roleOfficeId) {
		this.roleOfficeId = roleOfficeId;
	}

	public String getUserType() {
		return userType;
	}

	public void setSystemUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	@JsonIgnore
	public List<SystemRole> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<SystemRole> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (SystemRole role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			SystemRole role = new SystemRole();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ",");
	}
	
	public boolean isAdmin(){
		return isAdmin(id);
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public  boolean isAdmin(String id){
		return this.isAdmin||(id != null && "10000001".equals(id));
	}
	
	@Override
	public String toString() {
		return id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getWhId() {
		return whId;
	}

	public void setWhId(String whId) {
		this.whId = whId;
	}
	
}