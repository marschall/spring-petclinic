package org.springframework.samples.petclinic.servlet;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.function.LongSupplier;

import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A {@link HandlerInterceptor} that generates JFR events.
 */
public final class JfrHandlerInterceptor implements HandlerInterceptor {

	static final LongSupplier ALLOCATION_SUPPLIER;

	static final LongSupplier CPU_TIME_SUPPLIER;

	static {
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		if (threadMXBean.isCurrentThreadCpuTimeSupported()) {
			if (!threadMXBean.isThreadCpuTimeEnabled()) {
				threadMXBean.setThreadCpuTimeEnabled(true);
			}
			CPU_TIME_SUPPLIER = threadMXBean::getCurrentThreadCpuTime;
		}
		else {
			CPU_TIME_SUPPLIER = () -> -1L;
		}
		if (threadMXBean instanceof com.sun.management.ThreadMXBean jdkThreadBean
				&& jdkThreadBean.isThreadAllocatedMemorySupported()) {
			if (!jdkThreadBean.isThreadAllocatedMemoryEnabled()) {
				jdkThreadBean.setThreadAllocatedMemoryEnabled(true);
			}

			ALLOCATION_SUPPLIER = jdkThreadBean::getCurrentThreadAllocatedBytes;
		}
		else {
			ALLOCATION_SUPPLIER = () -> -1L;
		}
	}

	private final ThreadLocal<RequestHandlingEvent> currentRequestHandlingEvent;

	private final ThreadLocal<RenderingEvent> currentRenderingEvent;

	JfrHandlerInterceptor() {
		this.currentRequestHandlingEvent = new ThreadLocal<>();
		this.currentRenderingEvent = new ThreadLocal<>();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		var event = new RequestHandlingEvent();
		initializeEvent(event, request);
		this.currentRequestHandlingEvent.set(event);
		event.begin();
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) {
		stopEventIfExsits(this.currentRequestHandlingEvent, response);
		var event = new RenderingEvent();
		initializeEvent(event, request);
		if (modelAndView != null) {
			event.setHasView(modelAndView.hasView());
			event.setViewName(modelAndView.getViewName());
		}
		else {
			event.setHasView(false);
		}
		event.setCpuStart(CPU_TIME_SUPPLIER.getAsLong());
		event.setAllocationsStart(ALLOCATION_SUPPLIER.getAsLong());
		this.currentRenderingEvent.set(event);
		event.begin();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		stopEventIfExsits(this.currentRenderingEvent, response);
	}

	private static void initializeEvent(HttpEvent event, HttpServletRequest request) {
		event.setMethod(request.getMethod());
		event.setUrl(request.getRequestURI());
	}

	private static void stopEventIfExsits(ThreadLocal<? extends HttpEvent> threadLocal, HttpServletResponse response) {
		HttpEvent event = threadLocal.get();
		if (event != null) {
			event.end();
			if (event.getAllocationsStart() != -1L) {
				event.setAllocatedBytes(ALLOCATION_SUPPLIER.getAsLong() - event.getAllocationsStart());
			}
			if (event.getCpuStart() != -1L) {
				event.setCpuTime(CPU_TIME_SUPPLIER.getAsLong() - event.getCpuStart());
			}
			event.setStatus(response.getStatus());
			event.commit();
			threadLocal.remove();
		}
	}

}
