package in.co.bel.olms;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import in.co.bel.olms.transaction.file.util.FileStorageProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EntityScan(basePackages = { "in.co.bel.olms.master.entity", "in.co.bel.olms.transaction.entity", "in.co.bel.olms.ate.entity", "in.co.bel.olms.iol.entity" })
@EnableJpaRepositories(basePackages = { "in.co.bel.olms.master.repository", "in.co.bel.olms.transaction.repository", "in.co.bel.olms.ate.repository", "in.co.bel.olms.iol.repository" })
@ComponentScan(basePackages = {"in.co.bel.olms", "in.co.bel.olms.iol.jwt" })
@OpenAPIDefinition(info = @Info(title = "WIQAS TRANSACTION APIs", version = "1.0", description = "APIs for Transaction module of WIQAS"))
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableEurekaClient
public class OlmsTranServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlmsTranServicesApplication.class, args);
	}

	@Bean
	public Mapper dozerMapper() {
		return DozerBeanMapperSingletonWrapper.getInstance();
	}

}
