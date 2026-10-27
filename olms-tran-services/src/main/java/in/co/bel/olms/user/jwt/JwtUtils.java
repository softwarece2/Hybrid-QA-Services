package in.co.bel.olms.user.jwt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.co.bel.olms.transaction.service.upload.SOUploadService;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtils {

	@Value("${bel.jwt.secret}")
	private String jwtSecret;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	public Integer getEmpIdFromJwtToken(Object principal) {

		if (principal instanceof Map) {
			Map<String, Object> claims = (Map<String, Object>) principal;
			if (claims.get("empId") instanceof Number) {
				return ((Number) claims.get("empId")).intValue();
			}
		}
		return null;

	}

	public Map<String, Object> parseToken(String token) {
		try {
			Jws<?> jws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

			return (Map<String, Object>) jws.getBody();
		} catch (Exception e) {
			logger.error("Error decoding token: " , e.getMessage());
			return null;
		}
	}

}