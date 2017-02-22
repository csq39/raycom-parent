package io.raycom.system.web.dao;

import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.persistence.CrudDao;
import io.raycom.system.framework.persistence.annotation.MyBatisDao;

/**
 * FileDao
 * @author caoshengquan
 * @version 2014-05-16
 */
@MyBatisDao
public interface FileDao extends CrudDao {

	public void addFile(RData rdata);
	
	public RData getFile(RData rdata);
	
	
}
