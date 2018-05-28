package cloud.heartin.projects.esjestclientdemo;

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
public class ESJestClientDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ESJestClientDemoApplication.class, args);
	}
}
