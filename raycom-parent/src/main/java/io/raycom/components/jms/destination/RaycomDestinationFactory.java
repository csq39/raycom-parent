package io.raycom.components.jms.destination;

import io.raycom.system.framework.collection.RData;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.stereotype.Component;

@Component
public class RaycomDestinationFactory {

	RData activeMQQueueList = new RData();
	
	public ActiveMQQueue getMQQueue(String name){
		ActiveMQQueue mq = (ActiveMQQueue) activeMQQueueList.get(name);
		if(mq==null){
			mq = new ActiveMQQueue(name);
			activeMQQueueList.set(name, mq);
		}
		return mq;
	}
}
