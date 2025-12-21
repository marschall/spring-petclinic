package org.springframework.samples.petclinic.servlet;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("Repository")
@Description("Invocation of a Spring Data JPA repository")
@Category("PetClinic")
class RepositoryEvent extends Event {

	@Label("Repository")
	@Description("Repository interface name")
	private Class<?> repository;

	@Label("Method")
	@Description("Repository method name")
	private String method;

	@Label("Count")
	@Description("Number of objects returned")
	private int count;

	String getMethod() {
		return method;
	}

	void setMethod(String method) {
		this.method = method;
	}

	Class<?> getRepository() {
		return repository;
	}

	void setRepository(Class<?> clazz) {
		this.repository = clazz;
	}

	int getCount() {
		return count;
	}

	void setCount(int count) {
		this.count = count;
	}

}
