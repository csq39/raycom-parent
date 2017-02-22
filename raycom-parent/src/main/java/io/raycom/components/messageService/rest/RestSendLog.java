package io.raycom.components.messageService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.raycom.common.mapper.JsonMapper;
import io.raycom.components.messageService.bean.ResponseRestBean;
import io.raycom.components.util.log.RaycomLog;

@Component
public class RestSendLog {

	@Autowired
	private RaycomLog msgLog;
	
	/**
	 * 发送数据写入日志
	 * @param url
	 * @param request
	 * @param responseType
	 */
	public void restSendLog(ResponseRestBean<?> restBean,String title){
		msgLog.insertRestLog(JsonMapper.getInstance().toJson(restBean.getData()),title,"REST Send");
		if(restBean.hasError()){
			msgLog.insertRestLog(JsonMapper.getInstance().toJson(restBean.getErrMsg().getParams()),title,"REST Error");
		}
	}
}
