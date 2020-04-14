package ar.com.gtsoftware.configuration;

import ar.com.gtsoftware.GTSoftwareBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GTSoftwareBootApplication.class);
	}

}
