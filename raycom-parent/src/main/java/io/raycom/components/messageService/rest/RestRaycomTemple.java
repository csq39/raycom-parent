package io.raycom.components.messageService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.raycom.common.mapper.JaxbMapper;
import io.raycom.common.mapper.JsonMapper;
import io.raycom.components.messageService.bean.MsgBean;
import io.raycom.components.util.log.RaycomLog;

@Component
public class RestRaycomTemple {

	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	private RaycomLog msgLog;
	
	/**
	 *  restRaycomTemple.postForJsonObject("http://127.0.0.1:8080/raycom/admin/location/locattion.json", formEntity, String.class);
	 * @param url
	 * @param request
	 * @param responseType
	 * @return
	 */
	public <T> T postForJsonObjectWithLog(String url, Object msg, Class<T> responseType,String title){
		msgLog.insertRestLog(JsonMapper.getInstance().toJson(msg),title,"REST  Request");
		T obj = restTemplate.postForObject(url,setHeader(msg),responseType);
		msgLog.insertRestLog(JsonMapper.getInstance().toJson(obj),title,"REST  Receive");
		return obj;
	}
	
	public <T> T getForObjectWithLog(String url, MsgBean msg, Class<T> responseType,String title){
		msgLog.insertRestLog(JsonMapper.getInstance().toJson(msg),title,"REST  Request");
		T obj = restTemplate.getForObject(url,responseType,setHeader(msg));
		msgLog.insertRestLog(JsonMapper.getInstance().toJson(obj),title,"REST  Receive");
		return obj;
	}
	
	private HttpEntity<String> setHeader(Object msg) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> formEntity = new HttpEntity<String>(JsonMapper.toJsonString(msg), headers);
		return formEntity;
	}
	
	
	public <T> T postForXmlObjectWithLog(String url, Object msg, Class<T> responseType,String title){
		msgLog.insertRestLog(JaxbMapper.toXml(msg),title,"xml  Request");
		T obj = restTemplate.postForObject(url,setXmlHeader(msg),responseType);
		msgLog.insertRestLog(JaxbMapper.toXml(obj),title,"xml  Receive");
		return obj;
	}
	
	public <T> T getForXmlObjectWithLog(String url, MsgBean msg, Class<T> responseType,String title){
		msgLog.insertRestLog(JaxbMapper.toXml(msg),title,"xml  Request");
		T obj = restTemplate.getForObject(url,responseType,setHeader(msg));
		msgLog.insertRestLog(JaxbMapper.toXml(obj),title,"xml  Receive");
		return obj;
	}
	
	private HttpEntity<String> setXmlHeader(Object msg) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/xml; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_XML.toString());
		HttpEntity<String> formEntity = new HttpEntity<String>(JaxbMapper.toXml(msg), headers);
		return formEntity;
	}	
	
}
