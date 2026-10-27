package in.co.bel.olms.iol.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.co.bel.olms.iol.security.MethodFilter;

/**
 * @author Manasa B
 * @since 18-10-22
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	   @Override
	   protected void configure(HttpSecurity http) throws Exception {
	      
		   http.cors().and().csrf().disable().exceptionHandling().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
			.antMatchers("/").permitAll();		
	      
	      http.addFilterBefore(new MethodFilter(), UsernamePasswordAuthenticationFilter.class);
	   }
	   
//	   @Autowired
//	   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//	      auth
//	         .inMemoryAuthentication()
//	         .withUser("user").password("password").roles("USER");
//	   }
	}