package in.co.bel.olms.iol.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.co.bel.olms.iol.constants.StringLiteralConstants;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7); // Remove "Bearer" prefix

			try {
				Map<String, Object> claims = jwtUtils.parseToken(token);

				if (claims != null && claims.get("empId") != null) {
					// Set entire claims as principal
					
					String role = (String) claims.get("roles"); // Ensure this contains roles like ["EMP", "ADMIN"]
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
					
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(claims,
							null, authorities);

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
				return;
			}
		}

		chain.doFilter(request, response);
	}
}