package org.springframework.samples.petclinic.servlet;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class JfrRepositoryConfiguration {

	@Bean
	public Advisor jfrRepositoryAdvisor() {
		var pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.*(..))");
		return new DefaultPointcutAdvisor(pointcut, new JfrRepositoryInterceptor());
	}

}
