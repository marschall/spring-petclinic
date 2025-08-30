/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import static java.lang.foreign.MemorySegment.NULL;
import static org.springframework.samples.petclinic.ffi.Mman.mmap;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.samples.petclinic.ffi.Mman;
import org.springframework.samples.petclinic.ffi.Unistd;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {

	private static final Log LOG = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	@GetMapping("/oups")
	public String triggerException() {
		throw new RuntimeException(
				"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

	@GetMapping("/crash1")
	public String crash1() {
		List<ByteBuffer> buffers = new ArrayList<>();
		for (int i = 0; i < 1024; i++) {
			buffers.add(ByteBuffer.allocate(Integer.MAX_VALUE));
		}
		return buffers.toString();
	}

	@GetMapping("/crash2")
	public String crash2() {
		List<ByteBuffer> buffers = new ArrayList<>();
		for (int i = 0; i < 1024; i++) {
			buffers.add(ByteBuffer.allocateDirect(Integer.MAX_VALUE));
		}
		return buffers.toString();
	}

	@GetMapping("/crash3")
	public String crash3() {
		List<MemorySegment> segments = new ArrayList<>();
		MemorySegment failed = MemorySegment.ofAddress(-1L);
		MemorySegment segment = requestMemory();
		while (!segment.equals(failed)) {
			segments.add(segment);
			segment = requestMemory();
		}
		touchPages(segments);
		return segments.toString();
	}

	private static void touchPages(List<MemorySegment> segments) {
		int pageSize = Unistd.getpagesize();
		for (MemorySegment each : segments) {
			// touch the pages
			for (long i = 0; i < each.byteSize(); i += pageSize) {
				each.setAtIndex(ValueLayout.JAVA_BYTE, i, (byte) 1);
			}
		}
	}

	private static MemorySegment requestMemory() {
		// request multiple pages
		long len = (long) Unistd.getpagesize() * 1024L; // 4 MB on Linux, 16 MB on macOS
		int prot = Mman.PROT_READ() | Mman.PROT_WRITE();
		int flags = Mman.MAP_ANON() | Mman.MAP_ANON();
		return mmap(NULL, len, prot, flags, -1, 0L);
	}

	@ExceptionHandler(OutOfMemoryError.class)
	public String outOfMemoryError(OutOfMemoryError e) {
		LOG.warn("OutOfMemory", e);
		return "error";
	}

}
