package io.raycom.components.messageService.bean;

import io.raycom.system.framework.collection.RData;

public class MsgErrorBean implements MsgBean {
	
	private String msg = "-1";
	private MsgBean params ;
	
	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MsgBean getParams() {
		return params;
	}

	public void setParams(MsgBean params) {
		this.params = params;
	}

	@Override
	public void init(RData rdata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RData toRData() {
		// TODO Auto-generated method stub
		return null;
	}

}
