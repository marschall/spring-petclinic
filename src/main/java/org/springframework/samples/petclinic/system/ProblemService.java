package org.springframework.samples.petclinic.system;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.classfile.ClassFile;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.ClassDesc;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessFlag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
class ProblemService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final CyclicBarrier barrier;

	private final Thread backgroundThread;

	private final AtomicInteger classCounter;

	ProblemService() {
		this.barrier = new CyclicBarrier(2);
		this.backgroundThread = new Thread(this::spin, "background-thread");
		this.backgroundThread.setDaemon(true);
		this.classCounter = new AtomicInteger();
	}

	private void spin() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
				this.barrier.await();
			} catch (InterruptedException e) {
				LOG.info("interrupted, existing", e);
				return;
			} catch (BrokenBarrierException e) {
				LOG.info("broken barrier, existing", e);
				return;
			}
		}
	}

	@PostConstruct
	void startBackgroundThread() {
		this.backgroundThread.start();
	}

	@PreDestroy
	void stopBackgroundThread() {
		this.backgroundThread.interrupt();
	}

	Object problem1() {
		// 1 MB live set
		// -> allocation pressure
		byte[][] buffers = new byte[1024][];
		for (int i = 0; i < 1024 * 100; i++) {
			for (int j = 0; j < buffers.length; j++) {
				buffers[j] = new byte[1024];
			}
		}
		return buffers;
	}

	Object problem2() {
		// allocate until we get OutOfMemoryError
		// remove from live set and add new
		// -> lots of GC churn due to high heap occupancy
		List<byte[]> buffers = new LinkedList<>();
		byte[] buffer = tryAllocate(1024);
		while (buffer != null) {
			buffers.add(buffer);
			buffer = tryAllocate(1024);
		}

		for (int i = 0; i < 1024 * 1024; i++) {
			buffers.remove(0);
			buffer = tryAllocate(1024);
			if (buffer != null) {
				buffers.add(buffer);
			}
		}

		return buffers;
	}

	private static byte[] tryAllocate(int size) {
		try {
			return new byte[size];
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	Object problem3() {
		// excessive debug logging
		for (int i = 0; i < 1024 * 1024; i++) {
			LOG.debug("at iteration: " + i);
		}
		return "OK";
	}

	Object problem4() {
		// logger lookup
		for (int i = 0; i < 1024 * 1024; i++) {
			Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass());
			if (logger.isDebugEnabled()) {
				LOG.debug("at iteration: {}", i);
			}
		}
		return "OK";
	}

	Object problem5() {
		// regex matching
		return isNumeric("1234567890.0") ? "true" : false;
	}

	private static boolean isNumeric(String s) {
		return s.matches("(\\d|\\d\\d)+");
	}

	Object problem6() {
		// wait on lock
		try {
			this.barrier.await(10L, TimeUnit.SECONDS);
		} catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
			LOG.debug("wait failed: {}", e);
		}
		return "OK";
	}
	
	Object problem7() {
		// too many file descriptors
		return "OK";
	}
	
	Object problem8() {
		// too many threads
		return "OK";
	}

	Object problem9() throws IllegalAccessException {
		// too many classes
		var constantPoolBuilder = ConstantPoolBuilder.of();
		var classEntry = constantPoolBuilder.classEntry(
				ClassDesc.of(this.getClass().getPackageName(), "Generated" + this.classCounter.incrementAndGet()));
		var classFile = ClassFile.of();
		byte[] byteCode = classFile.build(classEntry, constantPoolBuilder, classBuilder -> {
			classBuilder.withFlags(AccessFlag.FINAL);
		});
		MethodHandles.lookup().defineClass(byteCode);
		return "OK";
	}

	Object problem10() throws IOException {
		// OuputStreamWriter
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Map<String, Object> map = new HashMap<>();
		List<Integer> value = List.of(1, 2, 3);
		for (int i = 0; i < 1_000; i++) {
			map.put(Integer.toString(i), value);
		}
		try (var writer = new OutputStreamWriter(bos, UTF_8)) {
			new ObjectMapper().writeValue(writer, map);
		}
		return "OK";
	}
	
	Object problem11() throws IOException {
		// Too many exceptions
		boolean isSymlink = true;
		Path p = Path.of("/typo");
		for (int i = 0; i < 1024; i++) {
			isSymlink &= Files.isSymbolicLink(p);
		}
		return isSymlink ? "failed" : "OK";
	}

}
