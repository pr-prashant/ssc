package microservices.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Once modified, this is all you need to run a Eureka registration server.
 * <p>
 * Start this process FIRST.
 */
@SpringBootApplication
@EnableEurekaServer
// Older versions of STS/Eclipse may generate an httpMapperProperties deprecated
// warning, which can safely be ignored.
public class RegistrationServer extends SpringBootServletInitializer {

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistrationServer.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RegistrationServer.class);
	}

}
