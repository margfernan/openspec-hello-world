package com.example.hello_world2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final GreetingService greetingService;

	public HelloController(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@GetMapping("/hello")
	public GreetingResponse hello(@RequestParam(required = false) String name) {
		return new GreetingResponse(greetingService.greet(name));
	}

	@ExceptionHandler(InvalidNameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProblemDetail handleInvalidName(InvalidNameException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

}