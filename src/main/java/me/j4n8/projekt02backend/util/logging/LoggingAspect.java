package me.j4n8.projekt02backend.util.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	/***
	 * Log before entering a method
	 * @param joinPoint the method
	 */
	@Before("execution(* me.j4n8.projekt02backend..*Controller.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Entering method: {}", joinPoint.getSignature().toShortString());
	}
	
	/***
	 * Log after exiting a method
	 * @param joinPoint the method
	 * @param result the result of the method
	 */
	@AfterReturning(pointcut = "execution(* me.j4n8.projekt02backend..*Controller.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
	}
	
	/***
	 * Log after throwing an exception
	 * @param joinPoint the method
	 * @param exception the exception thrown
	 */
	@AfterThrowing(pointcut = "execution(* me.j4n8.projekt02backend..*Controller.*(..))", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		logger.error("Error in method: {}", joinPoint.getSignature().toShortString(), exception);
	}
}
