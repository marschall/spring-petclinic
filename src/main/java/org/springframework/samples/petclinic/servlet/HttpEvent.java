package org.springframework.samples.petclinic.servlet;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("PetClinic")
abstract class HttpEvent extends Event {

	@Label("Method")
	@Description("HTTP method of the request")
	private String method;

	@Label("URL")
	@Description("URL of the HTTP request")
	private String url;

	@Label("Status")
	@Description("HTTP status code of the response")
	private int status;

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

}
