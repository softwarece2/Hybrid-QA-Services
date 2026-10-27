package in.co.bel.olms.iol;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EntityScan(basePackages = { "in.co.bel.olms.master.entity", "in.co.bel.olms.iol.entity", "in.co.bel.olms.transaction.entity"," in.co.bel.olms.iol.ate.entity" , "in.co.bel.olms.ate.entity" })
@EnableJpaRepositories(basePackages = { "in.co.bel.olms.master.repository", "in.co.bel.olms.iol.repository", "in.co.bel.olms.transaction.repository"," in.co.bel.olms.iol.ate.repository", "in.co.bel.olms.ate.repository" ,"in.co.bel.olms.iol.ate.repository" })
@ComponentScan(basePackages = "in.co.bel.olms")
public class OlmsIolServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlmsIolServicesApplication.class, args);
	}

	@Bean
	public Mapper dozerMapper() {
		return DozerBeanMapperSingletonWrapper.getInstance();
	}

}
