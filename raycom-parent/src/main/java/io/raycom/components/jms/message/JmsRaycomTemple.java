package io.raycom.components.jms.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import io.raycom.common.config.Constant;
import io.raycom.common.mapper.JsonMapper;
import io.raycom.components.jms.destination.RaycomDestinationFactory;
import io.raycom.components.support.properties.JmsProperties;
import io.raycom.components.util.log.RaycomLog;

@Component
public class JmsRaycomTemple {
	

	@Resource(name="jmsTemplate")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private RaycomLog msgLog;
	
	@Autowired
	private JmsProperties properties;
	
	@Autowired
	private RaycomDestinationFactory raycomDestination;

	//发送jms消息
	public void sendMessage(final String message,String destinationName){
		destinationName = getRealDestinationName(destinationName);
		msgLog.insertMQLog(message, destinationName, "MQ SEND");
		jmsTemplate.send(raycomDestination.getMQQueue(destinationName), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}
	 
	//发送jms消息
	public void sendMessage(final Object obj,String destinationName){
		destinationName = getRealDestinationName(destinationName);
		msgLog.insertMQLog(JsonMapper.getInstance().toJson(obj), destinationName, "MQ SEND");
		jmsTemplate.send(raycomDestination.getMQQueue(destinationName), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JsonMapper.toJsonString(obj));
			}
		});
	}
	
	private String getRealDestinationName(String destinationName) {
		if(Constant.MQ_MODEL_DEV.equals(properties.mqModel)){
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				destinationName = addr.getHostAddress().toString()+destinationName;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return destinationName;
	}

}
