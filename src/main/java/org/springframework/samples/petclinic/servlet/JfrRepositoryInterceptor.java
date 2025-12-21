package org.springframework.samples.petclinic.servlet;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Slice;

/**
 * Generates {@link RepositoryEvent}s around repository invocations.
 */
public class JfrRepositoryInterceptor implements MethodInterceptor {

	@Override
	public @Nullable Object invoke(MethodInvocation invocation) throws Throwable {
		RepositoryEvent event = new RepositoryEvent();
		Method method = invocation.getMethod();
		event.setRepository(method.getDeclaringClass());
		event.setMethod(method.getName());
		event.begin();
		int count = 0;
		try {
			@Nullable Object result = invocation.proceed();
			count = extractCount(result);
			return result;
		}
		finally {
			event.setCount(count);
			event.commit();
		}
	}

	private static int extractCount(@Nullable Object result) {
		return switch (result) {
			case null -> 0;
			case Optional<?> o -> o.isPresent() ? 1 : 0;
			case Collection<?> c -> c.size();
			case Map<?, ?> m -> m.size();
			case Slice<?> s -> s.getNumberOfElements();
			default -> 1;
		};
	}

}
