package ar.com.gtsoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GTSoftwareBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GTSoftwareBootApplication.class, args);
	}

}
