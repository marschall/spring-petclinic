package org.springframework.samples.petclinic.system;

import java.lang.foreign.MemorySegment;

import org.junit.jupiter.api.Test;

class CrashServiceTests {
	
	@Test
	void requestAndReleaseMemory() {
		MemorySegment segment = CrashService.requestMemory();
		CrashService.releaseMemory(segment);
	}

}
