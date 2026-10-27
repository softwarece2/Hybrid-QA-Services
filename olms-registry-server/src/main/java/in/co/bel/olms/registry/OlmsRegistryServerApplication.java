package in.co.bel.olms.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class OlmsRegistryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlmsRegistryServerApplication.class, args);
	}

}
