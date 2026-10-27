package in.co.bel.olms.iol.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;


@Configuration
public class FeignMultipartConfig {
	
	@Bean
	public Encoder feignFormEncoder() {
		return new SpringFormEncoder();
	}

}
