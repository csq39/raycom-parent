package io.raycom.components.messageService.bean;

import io.raycom.system.framework.collection.RData;

public interface MsgBean {

	public void init(RData rdata);
	
	public RData toRData();
}
