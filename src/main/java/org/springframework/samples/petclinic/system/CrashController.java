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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {

	private static final Log LOG = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	@Autowired
	private CrashService crashService;

	@GetMapping("/oups")
	public String triggerException() {
		throw new RuntimeException(
				"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

	@GetMapping("/crash1")
	public String crash1() {
		return crashService.crash1();
	}

	@GetMapping("/crash2")
	public String crash2() {
		return crashService.crash2();
	}

	@GetMapping("/crash3")
	public String crash3() {
		return crashService.crash3();
	}

	@ExceptionHandler(OutOfMemoryError.class)
	public String outOfMemoryError(OutOfMemoryError e) {
		LOG.warn("OutOfMemory", e);
		return "error";
	}

}
