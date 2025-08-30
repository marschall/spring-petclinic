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

	private static final String VIEWS_ERROR = "error";

	private static final String VIEWS_PROBLEMS_PROBLEM_LIST = "problems/problemList";

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final CrashService crashService;

	private final ProblemService problemService;

	public ProblemController(CrashService crashService, ProblemService problemService) {
		this.crashService = crashService;
		this.problemService = problemService;
	}

	@GetMapping("/problems.html")
	public String showProblemList(Model model) {
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problems/crash1")
	public String crash1() {
		LOG.debug("crash 1 {}", System.identityHashCode(crashService.crash1()));
		return VIEWS_ERROR;
	}

	@GetMapping("/problems/crash2")
	public String crash2() {
		LOG.debug("crash 2 {}", System.identityHashCode(crashService.crash2()));
		return VIEWS_ERROR;
	}

	@GetMapping("/problems/crash3")
	public String crash3() {
		LOG.debug("crash 3 {}", System.identityHashCode(crashService.crash3()));
		return VIEWS_ERROR;
	}

	@GetMapping("/problem1")
	public String problem1() {
		LOG.debug("problem 1 {}", System.identityHashCode(this.problemService.problem1()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem2")
	public String problem2() {
		LOG.debug("problem 2 {}", System.identityHashCode(this.problemService.problem2()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem3")
	public String problem3() {
		LOG.debug("problem 3 {}", System.identityHashCode(this.problemService.problem3()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem4")
	public String problem4() {
		LOG.debug("problem 4 {}", System.identityHashCode(this.problemService.problem4()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem5")
	public String problem5() {
		LOG.debug("problem 5 {}", System.identityHashCode(this.problemService.problem5()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem6")
	public String problem6() {
		LOG.debug("problem 6 {}", System.identityHashCode(this.problemService.problem6()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem9")
	public String problem9() {
		LOG.debug("problem 9 {}", System.identityHashCode(this.problemService.problem9()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem10")
	public String problem10() {
		LOG.debug("problem 10 {}", System.identityHashCode(this.problemService.problem10()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem11")
	public String problem11() {
		LOG.debug("problem 11 {}", System.identityHashCode(this.problemService.problem11()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem12")
	public String problem12() {
		LOG.debug("problem 12 {}", System.identityHashCode(this.problemService.problem12()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problem13")
	public String problem13() {
		LOG.debug("problem 13 {}", System.identityHashCode(this.problemService.problem13()));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@ExceptionHandler(OutOfMemoryError.class)
	public ModelAndView outOfMemoryError(OutOfMemoryError e) {
		LOG.warn("OutOfMemory", e);
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.setViewName(VIEWS_ERROR);
		return mav;
	}

}
