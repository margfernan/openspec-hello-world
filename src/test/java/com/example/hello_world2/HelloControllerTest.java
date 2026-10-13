package com.example.hello_world2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(HelloController.class)
@Import(GreetingService.class)
class HelloControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void greetsWorldWithoutParameters() throws Exception {
		expectGreeting(mockMvc.perform(get("/hello")), "Hello, World!");
	}

	@Test
	void greetsGivenName() throws Exception {
		expectGreeting(mockMvc.perform(get("/hello").param("name", "Ana")), "Hello, Ana!");
	}

	@Test
	void trimsSurroundingWhitespace() throws Exception {
		expectGreeting(mockMvc.perform(get("/hello").param("name", "  Ana  ")), "Hello, Ana!");
	}

	@Test
	void returnsSpecialCharactersAsJsonString() throws Exception {
		mockMvc.perform(get("/hello").param("name", "<b>Ana</b>"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value("Hello, <b>Ana</b>!"));
	}

	@Test
	void emptyNameUsesDefault() throws Exception {
		expectGreeting(mockMvc.perform(get("/hello").param("name", "")), "Hello, World!");
	}

	@Test
	void blankNameUsesDefault() throws Exception {
		expectGreeting(mockMvc.perform(get("/hello").param("name", "   ")), "Hello, World!");
	}

	@Test
	void acceptsNameAtMaxLength() throws Exception {
		String name = "a".repeat(50);
		expectGreeting(mockMvc.perform(get("/hello").param("name", name)), "Hello, " + name + "!");
	}

	@Test
	void rejectsNameOverMaxLength() throws Exception {
		mockMvc.perform(get("/hello").param("name", "a".repeat(51)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void surroundingWhitespaceDoesNotCountTowardsMaxLength() throws Exception {
		String name = "a".repeat(50);
		expectGreeting(mockMvc.perform(get("/hello").param("name", "  " + name + "  ")), "Hello, " + name + "!");
	}

	private void expectGreeting(ResultActions result, String message) throws Exception {
		result.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json("{\"message\":\"" + message + "\"}", true));
	}

}