package org.springframework.samples.petclinic.system;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class ProblemService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

	private static byte[] tryAllocate(int size) {
		try {
			return new byte[size];
		}
		catch (OutOfMemoryError e) {
			return null;
		}
	}

}
