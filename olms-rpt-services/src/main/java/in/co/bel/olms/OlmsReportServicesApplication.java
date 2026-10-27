package in.co.bel.olms;


import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EntityScan(basePackages = { "in.co.bel.olms.master.entity", "in.co.bel.olms.transaction.entity", "in.co.bel.olms.ate.entity" })
@EnableJpaRepositories(basePackages = { "in.co.bel.olms.master.repository", "in.co.bel.olms.transaction.repository", "in.co.bel.olms.stats.repository"  })
@ComponentScan(basePackages = "in.co.bel.olms")
@OpenAPIDefinition(info = @Info(title = "CQAE OLMS", version = "0.0.1-SNAPSHOT", description = "APIs for PO module of CQAE OLMS"))
@EnableEurekaClient
public class OlmsReportServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlmsReportServicesApplication.class, args);
	}

	@Bean
	public Mapper dozerMapper() {
		return DozerBeanMapperSingletonWrapper.getInstance();
	}

}
