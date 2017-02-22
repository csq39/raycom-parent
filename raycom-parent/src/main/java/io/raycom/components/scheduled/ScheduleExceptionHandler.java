package io.raycom.components.scheduled;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;

public class ScheduleExceptionHandler {


    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")  
    public void proxyAspect() {

    }

    @AfterThrowing(pointcut="proxyAspect()",throwing="ex")
    public void doException(JoinPoint joinPoint,Exception ex){
        Object target = joinPoint.getTarget();
        //this.saveException(target, ex);
    }

    /* public void saveException(Object target,Exception ex){
        try {
            Date date = new Date();
            TbScheduledExcuteLog log = new TbScheduledExcuteLog();
            log.setClassName(target.getClass().getName());
            log.setExcuteDate(date);
            log.setExcuteTime(date);
            log.setIsError(true);
            log.setErrorInfo(ExceptionTools.getExceptionDetails(ex).toString());
            if (target instanceof DxpScheduled) {
                DxpScheduled scheduled = (DxpScheduled) target;
                Task task = scheduled.getClass().getAnnotation(Task.class);
                log.setContentName(task.value());
            }
            this.tbScheduledExcuteLogDao.saveAndCommit(log);
        } catch (DXPException e) {
            e.printStackTrace();
        }
    }*/
}
