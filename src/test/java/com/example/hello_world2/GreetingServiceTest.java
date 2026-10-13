package com.example.hello_world2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class GreetingServiceTest {

	private final GreetingService service = new GreetingService();

	@Test
	void nullNameUsesDefault() {
		assertThat(service.greet(null)).isEqualTo("Hello, World!");
	}

	@Test
	void greetsGivenName() {
		assertThat(service.greet("Ana")).isEqualTo("Hello, Ana!");
	}

	@Test
	void trimsSurroundingWhitespace() {
		assertThat(service.greet("  Ana  ")).isEqualTo("Hello, Ana!");
	}

	@Test
	void emptyNameUsesDefault() {
		assertThat(service.greet("")).isEqualTo("Hello, World!");
	}

	@Test
	void blankNameUsesDefault() {
		assertThat(service.greet("   ")).isEqualTo("Hello, World!");
	}

	@Test
	void acceptsNameAtMaxLength() {
		String name = "a".repeat(GreetingService.MAX_NAME_LENGTH);
		assertThat(service.greet(name)).isEqualTo("Hello, " + name + "!");
	}

	@Test
	void rejectsNameOverMaxLength() {
		String name = "a".repeat(GreetingService.MAX_NAME_LENGTH + 1);
		assertThatThrownBy(() -> service.greet(name)).isInstanceOf(InvalidNameException.class);
	}

	@Test
	void surroundingWhitespaceDoesNotCountTowardsMaxLength() {
		String name = "a".repeat(GreetingService.MAX_NAME_LENGTH);
		assertThat(service.greet("  " + name + "  ")).isEqualTo("Hello, " + name + "!");
	}

}