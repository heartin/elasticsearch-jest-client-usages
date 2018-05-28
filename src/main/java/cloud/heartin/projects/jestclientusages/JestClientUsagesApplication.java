package cloud.heartin.projects.jestclientusages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Suppressing checkstyle warnings in Spring Boot Main to keep it as is.
 */
@SuppressWarnings({"checkstyle:hideutilityclassconstructor",
        "checkstyle:filetabcharacter",
        "checkstyle:javadocmethod",
        "checkstyle:finalparameters"})
@SpringBootApplication
public class JestClientUsagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JestClientUsagesApplication.class, args);
	}
}
