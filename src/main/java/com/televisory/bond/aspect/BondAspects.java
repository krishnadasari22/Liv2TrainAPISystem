package com.televisory.bond.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@Component
@Aspect
@EnableAspectJAutoProxy
public class BondAspects {

	Logger _log = Logger.getLogger(BondAspects.class);

	/**This is print log before execution of method to check the performance of method @throws Throwable *//*
	@Around("execution(* com.televisory.bond.controller..*(..))" )
	public void trackExecutionTime(ProceedingJoinPoint jp) throws Throwable{
		org.joda.time.LocalDateTime startTime = new org.joda.time.LocalDateTime();
		jp.proceed();
		org.joda.time.LocalDateTime endtime = new org.joda.time.LocalDateTime();
		_log.info("Method execution ::: " + jp.getSignature() + " took :: " + (endtime.getMillisOfSecond()-startTime.getMillisOfSecond()) + " MillisOfSecond");
	}


	*//**This is print log before execution of method to check the performance of method @throws Throwable *//*
	@Before("execution(* com.televisory.bond.controller..*(..))" )
	public void startLogger(JoinPoint jp) throws Throwable{
		_log.info("Before Operations");
	}


	*//**This is print log after execution of method to check the performance of method*//*
	@After("execution(* com.televisory.bond.controller..*(..))")
	public void endLogger(JoinPoint jp){
		_log.info("After Operations");	
	}


	*//**This is print log before execution of method to check the performance of method @throws Throwable *//*
	@Around("@annotation(com.televisory.bond.aspect.TrackExecutionTime)" )
	public void annotationBasedExecutionTimeTracking(ProceedingJoinPoint jp) throws Throwable{
		long startTime = System.currentTimeMillis(); 
		jp.proceed();
		long endtime = System.currentTimeMillis(); 
		_log.info("Method execution ::: " + jp.getSignature() + " took :: " + (endtime-startTime) + " MillisOfSecond");
	}*/

}
