package in.co.bel.olms.core.gateway;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import com.google.gson.Gson;

import in.co.bel.olms.core.gateway.dto.ReturnTokenResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

	@Autowired
	private RouterValidator routerValidator;

	@Autowired
	private JwtUtils jwtUtil;

	Gson gson = new Gson();

	@Value("${authurl}")
	private String authurl;

	private static final String AUTH_INVALID_MSG = "Authorization header is invalid";
	// Rate Limiting Constants
	// Limit to 5 requests per minute per client
	private static final int MAX_REQUESTS_PER_SECOND = 50;
	private static final int TIME_WINDOW_SECONDS = 1;

	private final InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter(MAX_REQUESTS_PER_SECOND, TIME_WINDOW_SECONDS);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();

		// Identifying client by IP address
		String clientId = request.getRemoteAddress().getAddress().getHostAddress();

		// Checking if the request is within the allowed rate limit - Verma
		if (!rateLimiter.isAllowed(clientId, request.getURI().getPath())) {
			return this.onError(exchange, "Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS);
		}

		if (routerValidator.isSecured.test(request)) {
			if (this.isAuthMissing(request)) {
				return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
			}

			final String token = this.getAuthHeader(request);
			boolean isTokenValid = jwtUtil.validateJwtToken(token);

			if (!isTokenValid) {
				return this.onError(exchange, AUTH_INVALID_MSG, HttpStatus.UNAUTHORIZED);
			}

			try {
				RestTemplate restTemplate = new RestTemplate();
				//String resourceUrl = "http://localhost:8088/user/validateToken"; //Master service port number
				HttpEntity<String> requestbodytoken = new HttpEntity<String>(token);
				// Send the request body in HttpEntity for HTTP POST request
				String productCreateResponse = restTemplate.postForObject(authurl, requestbodytoken, String.class);
				ReturnTokenResponse returnTokenResponse = gson.fromJson(productCreateResponse,
						ReturnTokenResponse.class);
				if (org.apache.commons.lang.StringUtils.isEmpty(returnTokenResponse.getUsername())) {
					return this.onError(exchange, AUTH_INVALID_MSG, HttpStatus.UNAUTHORIZED);
				}
			} catch (RestClientException e) {
				log.error("RestClientException: {}", e.getMessage());
				return this.onError(exchange, AUTH_INVALID_MSG, HttpStatus.UNAUTHORIZED);
			} catch (Exception e) {
				log.error("Exception: {}", e.getMessage());
				return this.onError(exchange, AUTH_INVALID_MSG, HttpStatus.UNAUTHORIZED);
			}


		}
		ServerHttpRequest req = exchange.getRequest();
		addOriginalRequestUrl(exchange, req.getURI());
		String path = req.getURI().getRawPath();
		// removing service name from the path
		String newPath = path.substring(path.indexOf("/", 1));
		request = req.mutate().path(newPath).build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
		return chain.filter(exchange.mutate().request(request).build());
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private String getAuthHeader(ServerHttpRequest request) {
		String headerAuth = request.getHeaders().getOrEmpty("Authorization").get(0);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

}
