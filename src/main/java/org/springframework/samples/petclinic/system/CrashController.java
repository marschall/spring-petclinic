/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private CrashService crashService;

	@Autowired
	private ProblemService problemService;

	@GetMapping("/oups")
	public String triggerException() {
		throw new RuntimeException(
				"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

	@GetMapping("/crash1")
	public String crash1() {
		LOG.debug("crash 1 {}", System.identityHashCode(crashService.crash1()));
		return "error";
	}

	@GetMapping("/crash2")
	public String crash2() {
		LOG.debug("crash 2 {}", System.identityHashCode(crashService.crash2()));
		return "error";
	}

	@GetMapping("/crash3")
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
