package org.springframework.samples.petclinic.system;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.marschall.problematic.service.CrashService;
import com.github.marschall.problematic.service.CrashType;
import com.github.marschall.problematic.service.ProblemService;
import com.github.marschall.problematic.service.ProblemType;

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
		return runCrash(CrashType.CRASH_1);
	}

	@GetMapping("/problems/crash2")
	public String crash2() {
		return runCrash(CrashType.CRASH_2);
	}

	@GetMapping("/problems/crash3")
	public String crash3() {
		return runCrash(CrashType.CRASH_3);
	}

	@GetMapping("/problems/crash4")
	public String crash4() {
		return runCrash(CrashType.CRASH_4);
	}

	@GetMapping("/problems/crash5")
	public String crash5() {
		return runCrash(CrashType.CRASH_5);
	}

	private String runCrash(CrashType crashType) {
		LOG.debug("crash {}", crashType.name(), System.identityHashCode(this.crashService.crash(crashType)));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	private String runProblem(ProblemType problemType) {
		LOG.debug("problem {}", problemType.name(),
				System.identityHashCode(this.problemService.withHighStrength(problemType)));
		return VIEWS_PROBLEMS_PROBLEM_LIST;
	}

	@GetMapping("/problems/problem1")
	public String problem1() {
		return runProblem(ProblemType.PROBLEM_1);
	}

	@GetMapping("/problems/problem2")
	public String problem2() {
		return runProblem(ProblemType.PROBLEM_2);
	}

	@GetMapping("/problems/problem3")
	public String problem3() {
		return runProblem(ProblemType.PROBLEM_3);
	}

	@GetMapping("/problems/problem4")
	public String problem4() {
		return runProblem(ProblemType.PROBLEM_4);
	}

	@GetMapping("/problems/problem5")
	public String problem5() {
		return runProblem(ProblemType.PROBLEM_5);
	}

	@GetMapping("/problems/problem6")
	public String problem6() {
		return runProblem(ProblemType.PROBLEM_6);
	}

	@GetMapping("/problems/problem7")
	public String problem7() {
		return runProblem(ProblemType.PROBLEM_7);
	}

	@GetMapping("/problems/problem9")
	public String problem9() {
		return runProblem(ProblemType.PROBLEM_9);
	}

	@GetMapping("/problems/problem10")
	public String problem10() {
		return runProblem(ProblemType.PROBLEM_10);
	}

	@GetMapping("/problems/problem11")
	public String problem11() {
		return runProblem(ProblemType.PROBLEM_11);
	}

	@GetMapping("/problems/problem12")
	public String problem12() {
		return runProblem(ProblemType.PROBLEM_12);
	}

	@GetMapping("/problems/problem13")
	public String problem13() {
		return runProblem(ProblemType.PROBLEM_13);
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
