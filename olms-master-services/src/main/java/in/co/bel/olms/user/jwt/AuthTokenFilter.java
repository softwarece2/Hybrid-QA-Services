package in.co.bel.olms.user.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import in.co.bel.olms.master.exception.UserException;
import in.co.bel.olms.user.controller.UserController;
import in.co.bel.olms.user.helper.UserHelper;


/**
 * Filter the request based on the request method type and the validity of the jwt token
 * @author Manasa B
 * @since 21-10-22
 */
@Component

public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private  JwtUtils jwtUtils;

	@Autowired
	private  UserController userDetailsService;

	@Autowired
	private  UserHelper userHelper;


	private static final Logger authLogger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			if (request.getMethod().equals("HEAD") 
					|| request.getMethod().equals("DELETE") || request.getMethod().equals("PATCH")) {
				response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			} else {
				String jwt = parseJwt(request);
				if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
					String username = jwtUtils.getUserNameFromJwtToken(jwt);

					var userDetails = userDetailsService.loadUserByUsername(username);
					
					// To be enabled after full integration
					boolean isValidToken = userHelper.validateUserToken(username, jwt);
					if (!isValidToken) {
						throw new UserException("User Token expired/invalid");
					}
					var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				filterChain.doFilter(request, response);
			}

		} catch (Exception e) {
			authLogger.error("Cannot set user authentication: {}", e.getMessage());
		}
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}
