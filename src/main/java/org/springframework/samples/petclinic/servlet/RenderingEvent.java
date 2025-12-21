package org.springframework.samples.petclinic.servlet;

import jdk.jfr.Description;
import jdk.jfr.Label;

@Label("View Rendering")
@Description("Rendering of a Spring WebMVC view")
class RenderingEvent extends HttpEvent {

	@Label("View Name")
	@Description("eturn the view name to be resolved by the DispatcherServlet via a ViewResolver, or null if we are using a View object.")
	private String viewName;

	@Label("Has View")
	@Description("Indicate whether the ModelAndView has a view, either  as a view name or as a direct View instance")
	private boolean hasView;

	String getViewName() {
		return viewName;
	}

	void setViewName(String viewName) {
		this.viewName = viewName;
	}

	boolean isHasView() {
		return hasView;
	}

	void setHasView(boolean hasView) {
		this.hasView = hasView;
	}

}
