package org.springframework.samples.petclinic.servlet;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
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

	private static final AtomicLong REQUEST_ID_GENERATOR = new AtomicLong();

	private static final Set<String> STW_GC_NAMES = Set.of(
			// Serial
			"Copy", "MarkSweepCompact",
			// Parallel
			"PS MarkSweep", "PS Scavenge",
			// G1
			"G1 Young Generation", "G1 Old Generation",
			// ZGC
			"ZGC Minor Pauses", "ZGC Major Pauses",
			// Shenandoah
			"Shenandoah Pauses");

	private static final LongSupplier GC_COUNT_SUPPLIER;

	private static final LongSupplier GC_TIME_SUPPLIER;

	private static final LongSupplier ALLOCATION_SUPPLIER;

	private static final LongSupplier CPU_TIME_SUPPLIER;

	static {
		List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans()
			.stream()
			.filter(JfrHandlerInterceptor::isStopTheWorld)
			.toList();
		GC_COUNT_SUPPLIER = () -> getCollectionCount(gcs);
		GC_TIME_SUPPLIER = () -> getCollectionTime(gcs);
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

	private static long getCollectionCount(List<GarbageCollectorMXBean> gcs) {
		long acc = -1L;
		// avoid streams to keep allocations to a minimum
		for (GarbageCollectorMXBean gc : gcs) {
			acc = accumulate(acc, gc.getCollectionCount());
		}
		return acc;
	}

	private static long getCollectionTime(List<GarbageCollectorMXBean> gcs) {
		long acc = -1L;
		// avoid streams to keep allocations to a minimum
		for (GarbageCollectorMXBean gc : gcs) {
			acc = accumulate(acc, gc.getCollectionTime());
		}
		return acc;

	}

	private static long difference(long initial, long current) {
		if (initial == -1L || current == -1L) {
			return -1L;
		}
		return current - initial;
	}

	private static long accumulate(long a, long b) {
		if (a == -1L) {
			return b;
		}
		if (b == -1L) {
			return a;
		}
		return a + b;
	}

	private static boolean isStopTheWorld(GarbageCollectorMXBean gc) {
		return STW_GC_NAMES.contains(gc.getName());
	}

	private static long nextRequestId() {
		return REQUEST_ID_GENERATOR.incrementAndGet();
	}

	private final ThreadLocal<RequestHandlingEvent> currentRequestHandlingEvent;

	private final ThreadLocal<RenderingEvent> currentRenderingEvent;

	JfrHandlerInterceptor() {
		this.currentRequestHandlingEvent = new ThreadLocal<>();
		this.currentRenderingEvent = new ThreadLocal<>();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		var requestHandlingEvent = new RequestHandlingEvent();
		initializeEvent(requestHandlingEvent, request);
		requestHandlingEvent.setRequestId(nextRequestId());
		this.currentRequestHandlingEvent.set(requestHandlingEvent);
		requestHandlingEvent.begin();
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) {
		HttpEvent requestHandlingEvent = stopEventIfExsits(this.currentRequestHandlingEvent, response);
		var renderingEvent = new RenderingEvent();
		initializeEvent(renderingEvent, request);
		if (modelAndView != null) {
			renderingEvent.setHasView(modelAndView.hasView());
			renderingEvent.setViewName(modelAndView.getViewName());
		}
		else {
			renderingEvent.setHasView(false);
		}
		if (requestHandlingEvent != null) {
			renderingEvent.setRequestId(requestHandlingEvent.getRequestId());
		}
		this.currentRenderingEvent.set(renderingEvent);
		renderingEvent.begin();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		stopEventIfExsits(this.currentRenderingEvent, response);
	}

	private static void initializeEvent(HttpEvent event, HttpServletRequest request) {
		event.setMethod(request.getMethod());
		event.setUrl(request.getRequestURI());
		event.setCpuStart(CPU_TIME_SUPPLIER.getAsLong());
		event.setAllocationsStart(ALLOCATION_SUPPLIER.getAsLong());
		event.setGcTimeStart(GC_TIME_SUPPLIER.getAsLong());
		event.setGcCountStart(GC_COUNT_SUPPLIER.getAsLong());
	}

	private static @Nullable HttpEvent stopEventIfExsits(ThreadLocal<? extends HttpEvent> threadLocal,
			HttpServletResponse response) {
		HttpEvent event = threadLocal.get();
		if (event != null) {
			event.end();
			if (event.getAllocationsStart() != -1L) {
				event.setAllocatedBytes(ALLOCATION_SUPPLIER.getAsLong() - event.getAllocationsStart());
			}
			if (event.getCpuStart() != -1L) {
				event.setCpuTime(CPU_TIME_SUPPLIER.getAsLong() - event.getCpuStart());
			}
			if (event.getGcTimeStart() != -1L) {
				event.setGcTime(difference(event.getGcTimeStart(), GC_TIME_SUPPLIER.getAsLong()));
			}
			if (event.getGcCountStart() != -1L) {
				event.setGcCount(difference(event.getGcCountStart(), GC_COUNT_SUPPLIER.getAsLong()));
			}
			event.setStatus(response.getStatus());
			event.commit();
			threadLocal.remove();
			return event;
		}
		return null;
	}

}
