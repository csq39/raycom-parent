package io.raycom.components.scheduled;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScheduledStatisticsHandler {

	@Value("${scheduled.run}")
	public String scheduledRun;
	
	@Value("${scheduled.close}")
	public String scheduledClose;

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")  
    public void proxyAspect() {

    }

    @Around("proxyAspect()")
    public Object doInvoke(ProceedingJoinPoint joinPoint) throws Throwable{
    	Object result=null;
    	//long start = System.currentTimeMillis();
    	String test = joinPoint.getTarget().getClass().getSimpleName()+"."+joinPoint.getSignature().getName();
    	if("Y".equals(scheduledRun)&&scheduledClose.indexOf(test)<0){
    		result = joinPoint.proceed();
    	}
    
    	return result;
    }
}
