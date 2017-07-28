package io.raycom.components.jms.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import io.raycom.common.config.Constant;
import io.raycom.components.jms.destination.RaycomDestinationFactory;
import io.raycom.components.support.properties.JmsProperties;
import io.raycom.components.util.log.RaycomLog;

public class RaycomMessageListenerContainer extends
		DefaultMessageListenerContainer implements MessageListener {
	
	@Autowired
	private RaycomDestinationFactory raycomDestination;
	
	@Resource(name="connectionFactory")
	private SingleConnectionFactory connectionFactory;
	
	@Autowired
	private JmsProperties properties;
	
	@Autowired
	private RaycomLog msgLog;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMsg = (TextMessage) message;   
		try {
			msgLog.insertMQLog(textMsg.getText(), getRealDestinationName(), "MQ Receive");
		} catch (JMSException e) {
			msgLog.insertExceptionLog(e.getMessage(), getRealDestinationName(), "MQ Exception");
			e.printStackTrace();
		}
		messageHandle(textMsg);
	}
	
	public void messageHandle(TextMessage message) {
	}
	
	@PostConstruct
	public void init() {
		setMessageListener(this);
		setDestination(raycomDestination.getMQQueue(getRealDestinationName()));
		setConnectionFactory(connectionFactory);
	}

	@Override
	public void afterPropertiesSet() {
		if(isShouldStop()){
			System.out.println("jms服务："+getRealDestinationName()+"关闭");
			stop();
		}else{
			super.afterPropertiesSet();
		}
	}
	
	public String getDestinationName() {
		return "-1";
	}
	
	private String getRealDestinationName() {
		String destinationName=getDestinationName();
		if(Constant.MQ_MODEL_DEV.equals(properties.mqModel)){
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				destinationName = addr.getHostAddress().toString()+getDestinationName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return destinationName;
	}
	
	private boolean isShouldStop() {
		boolean stop = properties.include.indexOf(getDestinationName())<0;
		if(stop)
			stop =  Constant.N_VALUE.equals(properties.isRun)||properties.exclude.indexOf(getDestinationName())>0;
		return stop;
	}



}
