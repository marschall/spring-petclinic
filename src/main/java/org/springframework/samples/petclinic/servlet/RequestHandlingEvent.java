package org.springframework.samples.petclinic.servlet;

import jdk.jfr.Description;
import jdk.jfr.Label;

@Label("Request Processing")
@Description("Processing of an HTTP request in Spring WebMVC")
class RequestHandlingEvent extends HttpEvent {

}
