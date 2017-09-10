package rewards.internal.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rewards.internal.monitor.Monitor;
import rewards.internal.monitor.MonitorFactory;

// 	TODO-02: REQUIREMENT #1: Use AOP to log a message before
//           any find...() method is invoked.
//  Indicate this class is an aspect.
//	Also mark it as a component.  
//	Place an @Autowired annotation on the constructor.
@Aspect
@Component
public class LoggingAspect {

	private Logger logger = Logger.getLogger(getClass());
	private MonitorFactory monitorFactory;

	@Autowired
	public LoggingAspect(MonitorFactory monitorFactory) {
		super();
		this.monitorFactory = monitorFactory;
	}

	//	TODO-03: Pointcut Expression
 	//  Write a pointcut expression that selects only find* methods.
    //	Decide which advice type is most appropriate to the requirement.
    //
	//  HINT: The pointcut expression can be very hard to work out. If
	//  you get stuck refer to the examples in the slides and read the
    //  detailed instructions in the lab-notes.
	@Before("execution(public * *..*Repository.find*(..))")
	public void implLogging(JoinPoint joinPoint) {
		logger.info("'Before' advice implementation - " + joinPoint.getTarget().getClass() + //
				"; Executing before " + joinPoint.getSignature().getName() + //
				"() method");
	}
	
	
    //	TODO-07: REQUIREMENT #2: Use AOP to time update...() methods.
    //
    //  Mark this method as around advice.  Write a pointcut
	//	expression to match on all update* methods on Repository classes.
	//
	//  HINT: Again, the pointcut expression can be hard to work out, so if
	//  you get stuck, refer to the pointcut expression you wrote above for 
	//  implLogging(), this one is similar.
	// 
	//  If you are really stuck, PLEASE ask a colleague or your instructor.
	@Around("execution(public * *..*Repository.update*(..))")
	public Object monitor(ProceedingJoinPoint repositoryMethod) throws Throwable {
		String name = createJoinPointTraceName(repositoryMethod);
		Monitor monitor = monitorFactory.start(name);
		try {
			// Invoke repository method ...

			return repositoryMethod.proceed();
			//  TODO-08: Add the logic to proceed with the target method invocation.
			//  Be sure to return the target method's return value to the caller
			//  and delete the line below.

		} finally {
			monitor.stop();
			logger.info("'Around' advice implementation - " + monitor);
		}
	}
		
	private String createJoinPointTraceName(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		StringBuilder sb = new StringBuilder();
		sb.append(signature.getDeclaringType().getSimpleName());
		sb.append('.').append(signature.getName());
		return sb.toString();
	} 
}