package in.co.bel.olms.core.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OlmsMeshGatewayApplication {

	@Autowired
	private AuthenticationFilter filter;
	
	public static void main(String[] args) {
		SpringApplication.run(OlmsMeshGatewayApplication.class, args);
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/olms-master-services/**")
						.filters(f -> f.filter(filter))
						.uri("lb://olms-master-services/"))
				.route(p -> p.path("/olms-tran-services/**")
						.filters(f -> f.filter(filter))
						.uri("lb://olms-tran-services/"))
				.route(p -> p.path("/olms-rpt-services/**")
						.filters(f -> f.filter(filter))
						.uri("lb://olms-rpt-services/"))
				.build();
	}

}
