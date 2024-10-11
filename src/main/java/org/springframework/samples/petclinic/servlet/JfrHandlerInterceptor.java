package org.springframework.samples.petclinic.servlet;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A {@link HandlerInterceptor} that generates JFR events.
 */
public final class JfrHandlerInterceptor implements HandlerInterceptor {

	private final ThreadLocal<RequestHandlingEvent> currentRequestHandlingEvent;

	private final ThreadLocal<RenderingEvent> currentRenderingEvent;

	JfrHandlerInterceptor() {
		this.currentRequestHandlingEvent = new ThreadLocal<>();
		this.currentRenderingEvent = new ThreadLocal<>();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		var event = new RequestHandlingEvent();
		initializeEvent(event, request);
		this.currentRequestHandlingEvent.set(event);
		event.begin();
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
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
			event.setStatus(response.getStatus());
			event.commit();
			threadLocal.remove();
		}
	}

}
