/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.system.framework.persistence;

import io.raycom.common.config.Global;
import io.raycom.system.framework.collection.RData;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页类
 * @author caoshengquan
 * @version 2013-7-2
 * @param <RData>
 */
public class Page<T> {
	
	private int draw = 0; // 当前页码
	
	private int start = 0; // 起始条数

	private int length = -1;//  页面大小，设置为“-1”表示不进行分页（分页无效）
	
	private long recordsTotal;// 即没有过滤的记录数（数据库里总共记录数）
	private long recordsFiltered;//过滤后的记录数（如果有接收到前台的过滤条件，则返回的是过滤后的记录数）
	
	private List<T> data = new ArrayList<T>();//表中中需要显示的数据。这是一个对象数组，也可以只是数组，区别在于 纯数组前台就不需要用 columns绑定数据，会自动按照顺序去显示 ，而对象数组则需要使用 columns绑定数据才能正常显示
	
	public RData param = new RData();//存放查询条件
	
	public RData sessionRdata = new RData();//存放查询条件
	
	private String error = ""; //可选。你可以定义一个错误来描述服务器出了问题后的友好提示

	public int getDraw() {
		return draw;
	}
	
	public  Page() {
	}
	
	public  Page(RData param ) {
		if(param!=null)
		this.param = param;
		this.draw = param.getInt("draw");
		this.start = param.getInt("start");
		this.length = param.getInt("length");
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsTotal;//返回总行数
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getStart() {
		return start ;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	public RData getParam() {
		return param;
	}

	public void setParam(RData param) {
		this.param = param;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	
}
