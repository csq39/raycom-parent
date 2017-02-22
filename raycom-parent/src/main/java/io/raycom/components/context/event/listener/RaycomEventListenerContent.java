package io.raycom.components.context.event.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.raycom.components.context.event.RayComEvent;

@Component
public class RaycomEventListenerContent implements ApplicationListener<ContextRefreshedEvent>{

	private HashMap<String, List<IRayComEventListener>> eventList = new HashMap<String, List<IRayComEventListener>>();
	
	/**
	 * 被动注册方式，由监听器主动向容器进行注册。
	 * 目前测试bean 容器进行遍历的方式实现注册，暂时不用该方法
	 * @param eventListener
	 */
	@Deprecated
	 public void regEventListener(IRayComEventListener eventListener){
		 for (String eventName : eventListener.getEventClasses()) {
			 if(eventList.containsKey(eventName))
				 eventList.get(eventName).add(eventListener);
			 else{
				 ArrayList<IRayComEventListener> listenerList = new ArrayList<IRayComEventListener>();
				 listenerList.add(eventListener);
				 eventList.put(eventName, listenerList);
			 }
		}
	 } 
	 /**
	  * 采用bean的方式主动搜寻事件监听器,但存在一个顺序问题,即在搜索时,bean并wei创建完成,故采用主动注册的机制,暂时不做主动搜寻.
	  */
	 public void onApplicationEvent(ContextRefreshedEvent event) {
	        //从SPRING中得到实现了某接口的业务类  
	        Map<String,IRayComEventListener> beans =  event.getApplicationContext().getBeansOfType(IRayComEventListener.class);  
	        if(beans==null || beans.size()==0)  
	            return;  
	        //下面循环进行归类  
	        Collection<IRayComEventListener> services = beans.values();  
	        for (IRayComEventListener service : services) {  
	            List<String> eventClasses = service.getEventClasses();  
	            if(eventClasses==null || eventClasses.size()==0)  
	                continue;  
	              
	            for (int i = 0; i < eventClasses.size(); i++) {  
	                List<IRayComEventListener> list = eventList.get(eventClasses.get(i));  
	                if(list==null){  
	                    list = new ArrayList<IRayComEventListener>();  
	                    eventList.put(eventClasses.get(i), list);  
	                }
	                 
	                if(!list.contains(service)){  
	                        list.add(service);  
                    }  
                }  
            }  
	    } 
	 
	 /** 
     * 发布事件的静态方法 
     */  
	 @Async
    public void publishEvent(RayComEvent event){  
        //根据实际事件名称，从eventList中找出监听了此事件的业务类，调用之  
        List<IRayComEventListener> list = eventList.get(event.getClass().getName());  
        if(list!=null && list.size()>0){  
            for (IRayComEventListener listener : list) {  
                //此处不能捕捉异常，因为任何一个处理类实例出错都应该全部回滚  
                listener.onRayComEvent(event);  
            }  
        }  
    }  
}
