package io.raycom.components.util.log.dao;

import io.raycom.components.util.log.bean.Log;
import io.raycom.system.framework.persistence.annotation.MyBatisDao;

/**
 * 日志DAO接口
 * @author caoshengquan
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao {
	
	public int insert(Log entity);

}
