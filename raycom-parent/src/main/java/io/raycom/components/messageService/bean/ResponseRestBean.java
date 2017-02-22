package io.raycom.components.messageService.bean;

import java.util.ArrayList;

import io.raycom.system.framework.collection.RData;

public class ResponseRestBean<T> implements MsgBean {
	
	private ArrayList<T> data = new ArrayList<T>();
	
	private MsgErrorBean errMsg = new MsgErrorBean();

	public void addMsgBean(T msgBean) {
		data.add(msgBean);
	}
	
	public ArrayList<T> getData() {
		return data;
	}
	
	public void setData(ArrayList<T> data) {
		this.data = data;
	}
	
	public MsgErrorBean getErrMsg() {
		return errMsg;
	}
	
	public void setErrMsg(MsgErrorBean errMsg) {
		this.errMsg = errMsg;
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
	
	public boolean hasNoError(){
		return "-1".equals(errMsg.getMsg());
	}
	public boolean hasError(){
		return !hasNoError();
	}

}
