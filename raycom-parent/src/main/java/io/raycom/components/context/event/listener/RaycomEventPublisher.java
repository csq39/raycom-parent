package io.raycom.components.context.event.listener;

import io.raycom.common.utils.SpringContextHolder;
import io.raycom.components.context.event.RayComEvent;

public class RaycomEventPublisher {

	 public static RaycomEventListenerContent  eventListenerContent=SpringContextHolder.getBean(RaycomEventListenerContent.class);
	 /** 
     * 发布事件的静态方法 
     */  
    public static void publishEvent(RayComEvent event){  
    	eventListenerContent.publishEvent(event);
    }  
    
}
