package io.raycom.components.context.event;

import io.raycom.components.context.event.listener.RaycomEventPublisher;

public class RayComEvent {

	private Object source;  
    public RayComEvent() {  
    }  
      
    public RayComEvent(Object source) {  
        this.source = source;  
    }  
  
    public Object getSource() {  
        return source;  
    }  
    
    public  void publishEvent(){  
    	RaycomEventPublisher.publishEvent(this);
    }
}
