package org.springframework.samples.petclinic.servlet;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Timespan;
import jdk.jfr.DataAmount;

@Category("PetClinic")
abstract class HttpEvent extends Event {

	// package protected to enable inheritance
	@Label("Method")
	@Description("HTTP method of the request")
	String method;

	@Label("URL")
	@Description("URL of the HTTP request")
	String url;

	@Label("Status")
	@Description("HTTP status code of the response")
	int status;

	@Label("CPU Time")
	@Description("CPU Time used by the request")
	@Timespan(Timespan.NANOSECONDS)
	long cpuTime;

	@Label("Allocations")
	@Description("Heap allocations done by the request")
	@DataAmount(DataAmount.BYTES)
	long allocatedBytes;

	// transient -> not recorded
	private transient long cpuStart;

	private transient long allocationsStart;

	String getMethod() {
		return method;
	}

	void setMethod(String method) {
		this.method = method;
	}

	String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}

	int getStatus() {
		return status;
	}

	void setStatus(int status) {
		this.status = status;
	}

	long getCpuTime() {
		return cpuTime;
	}

	void setCpuTime(long cpuTime) {
		this.cpuTime = cpuTime;
	}

	long getAllocatedBytes() {
		return allocatedBytes;
	}

	void setAllocatedBytes(long allocatedBytes) {
		this.allocatedBytes = allocatedBytes;
	}

	long getCpuStart() {
		return cpuStart;
	}

	void setCpuStart(long cpuStart) {
		this.cpuStart = cpuStart;
	}

	long getAllocationsStart() {
		return allocationsStart;
	}

	void setAllocationsStart(long allocationsStart) {
		this.allocationsStart = allocationsStart;
	}

}
