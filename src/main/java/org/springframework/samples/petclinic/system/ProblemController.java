package org.springframework.samples.petclinic.system;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.marschall.problematic.service.CrashService;
import com.github.marschall.problematic.service.ProblemService;

/**
 * Triggers problematic code. Diagnosing is left as an exercise to the reader.
 */
@Controller
public class ProblemController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private CrashService crashService;

	@Autowired
	private ProblemService problemService;

	@GetMapping("/problems.html")
	public String showProblemList(Model model) {
		return "problems/problemList";
	}

	@GetMapping("/problems/crash1")
	public String crash1() {
		LOG.debug("crash 1 {}", System.identityHashCode(crashService.crash1()));
		return "error";
	}

	@GetMapping("/problems/crash2")
	public String crash2() {
		LOG.debug("crash 2 {}", System.identityHashCode(crashService.crash2()));
		return "error";
	}

	@GetMapping("/problems/crash3")
	public String crash3() {
		LOG.debug("crash 3 {}", System.identityHashCode(crashService.crash3()));
		return "error";
	}

	@GetMapping("/problem1")
	public String problem1() {
		LOG.debug("problem 1 {}", System.identityHashCode(this.problemService.problem1()));
		return "error";
	}

	@GetMapping("/problem2")
	public String problem2() {
		LOG.debug("problem 2 {}", System.identityHashCode(this.problemService.problem2()));
		return "error";
	}

	@ExceptionHandler(OutOfMemoryError.class)
	public ModelAndView outOfMemoryError(OutOfMemoryError e) {
		LOG.warn("OutOfMemory", e);
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.setViewName("error");
		return mav;
	}

}
