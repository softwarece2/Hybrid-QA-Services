package in.co.bel.core.security;


import io.jsonwebtoken.Jwts;
import org.springframework.util.StringUtils;
public class HmisAppSecurity {

	public String getRoleFromJwtToken(String token) {
		return Jwts.parser().setSigningKey("sj^*d29!*u@b^e").parseClaimsJws(token).getBody().get("roles", String.class);
	}
	
	
	public String getUserNameFromJwtToken(String token) {
	    return Jwts.parser().setSigningKey("sj^*d29!*u@b^e").parseClaimsJws(token).getBody().getSubject();
	  }
	
	public Integer getEmpIdFromJwtToken(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token= token.substring(7, token.length());
		}
		return Jwts.parser().setSigningKey("sj^*d29!*u@b^e").parseClaimsJws(token).getBody().get("empId", Integer.class);
	}
	
	public Integer getOrgIdFromJwtToken(String token) {		
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token= token.substring(7, token.length());
		}
		return Jwts.parser().setSigningKey("sj^*d29!*u@b^e").parseClaimsJws(token).getBody().get("orgId", Integer.class);
	}
	
	public Integer getRoleIdFromJwtToken(String token) {		
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token= token.substring(7, token.length());
		}
		return Jwts.parser().setSigningKey("sj^*d29!*u@b^e").parseClaimsJws(token).getBody().get("roleId", Integer.class);
	}
}
