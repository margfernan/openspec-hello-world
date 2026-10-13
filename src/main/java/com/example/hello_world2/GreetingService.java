package com.example.hello_world2;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

	static final int MAX_NAME_LENGTH = 50;

	private static final String DEFAULT_NAME = "World";

	public String greet(String rawName) {
		String name = (rawName == null) ? "" : rawName.strip();
		if (name.isEmpty()) {
			name = DEFAULT_NAME;
		}
		if (name.length() > MAX_NAME_LENGTH) {
			throw new InvalidNameException("name must be at most " + MAX_NAME_LENGTH + " characters");
		}
		return "Hello, " + name + "!";
	}

}