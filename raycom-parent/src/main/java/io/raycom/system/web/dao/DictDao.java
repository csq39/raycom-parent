package io.raycom.system.web.dao;

import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.persistence.CrudDao;
import io.raycom.system.framework.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 字典DAO接口
 * @author caoshengquan
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao {

	public List<RData> findTypeList(RData dict);
	
	public List<RData> findList(RData dict);
	
	public List<RData> findWHList(RData user);
	
	public List<RData> findOfficeList(RData user);
	
	public List<RData> findUnitList();
	
	public List<RData> findManufactureList();
	
	public List<RData> findCompanyList();
	
	public List<RData> findNationList();
	
	
}
