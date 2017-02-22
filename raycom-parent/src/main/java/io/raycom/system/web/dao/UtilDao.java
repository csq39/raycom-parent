package io.raycom.system.web.dao;

import io.raycom.common.bean.SystemRole;
import io.raycom.common.bean.SystemUser;
import io.raycom.system.framework.persistence.BaseDao;
import io.raycom.system.framework.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 用于Util包访问数据库用
 * @author caoshengquan
 * @version 2014-05-16
 */
@MyBatisDao
public interface UtilDao extends BaseDao {

	public SystemUser getByLoginName(String loginName);
	
	public SystemUser getUser(String id);

	public List<SystemRole> getRoleListByUserId(String userId);
	
	public String getOfficeNameById(String officeId);
	

}
