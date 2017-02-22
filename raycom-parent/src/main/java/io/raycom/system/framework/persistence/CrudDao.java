/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.system.framework.persistence;

import io.raycom.system.framework.collection.RData;

import java.util.List;

/**
 * DAO支持类实现
 * @author caoshengquan
 * @version 2014-05-16
 * @param 
 */
public interface CrudDao extends BaseDao {

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public RData get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public RData get(RData entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<RData>());
	 * @param entity
	 * @return
	 */
	public List<RData> findList(RData entity);
	
	/**
	 * 查询所有数据列表
	 * @param entity
	 * @return
	 */
	public List<RData> findAllList(RData entity);
	
	/**
	 * 查询所有数据列表
	 * @see public List<RData> findAllList(RData entity)
	 * @return
	 */
	public List<RData> findAllList();
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public int insert(RData entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public int update(RData entity);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param id
	 * @see public int delete(RData entity)
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 * @return
	 */
	public int delete(RData entity);
	
}