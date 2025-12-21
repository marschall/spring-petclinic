package org.springframework.samples.petclinic.servlet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Relational;
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

	@Label("GC Time")
	@Description("Time spent in STW GCs during the request")
	@Timespan(Timespan.MILLISECONDS)
	long gcTime;

	@Label("GC Count")
	@Description("Number of STW GCs during the request")
	long gcCount;

	@Label("Request Id")
	@Description("HTTP request id to track request handling and related rendering event")
	@RequestId
	long requestId;

	// transient -> not recorded
	private transient long cpuStart;

	private transient long allocationsStart;

	private transient long gcCountStart;

	private transient long gcTimeStart;

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

	long getGcTime() {
		return gcTime;
	}

	void setGcTime(long gcTime) {
		this.gcTime = gcTime;
	}

	long getGcCount() {
		return gcCount;
	}

	void setGcCount(long gcCount) {
		this.gcCount = gcCount;
	}

	long getRequestId() {
		return requestId;
	}

	void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	long getGcCountStart() {
		return gcCountStart;
	}

	void setGcCountStart(long gcCountStart) {
		this.gcCountStart = gcCountStart;
	}

	long getGcTimeStart() {
		return gcTimeStart;
	}

	void setGcTimeStart(long gcDurationStart) {
		this.gcTimeStart = gcDurationStart;
	}

	@Label("Request Id")
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Relational
	@interface RequestId {

	}

}
