package in.co.bel.olms.iol.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.filter.OncePerRequestFilter;


/*
 * @author:Krishnaprasad Shettigar T
 * @author Manasa B
 * @date: 11-10-2021
 * @modified date 29-07-2023
 * @purpose: Disable OPTIONS method in REST API's- VAPT observation
 */

@Component
public class MethodFilter extends OncePerRequestFilter {

	private static final Logger authLogger = LoggerFactory.getLogger(MethodFilter.class);

	in.co.bel.core.security.HmisAppSecurity HmisAppSecurity = new in.co.bel.core.security.HmisAppSecurity();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);			
			try {
				if(jwt !=null) {
					String role = HmisAppSecurity.getRoleFromJwtToken(jwt);
					String userfrmjwt=HmisAppSecurity.getUserNameFromJwtToken(jwt);	
					List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));
					var authentication = new UsernamePasswordAuthenticationToken(userfrmjwt, null,
							authorities);					
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (RestClientException e) {
				authLogger.error("Rest client exception during user authentication: {}", e.getMessage());
			}

		} catch (Exception e) {
			authLogger.error("Cannot set user authentication: {}", e.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		filterChain.doFilter(request, response);
 
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}