package in.sabnar.gateway.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private AuthorizationServerTokenServices tokenServices;
	@Autowired
	private TokenStore tokenStore;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	
	@GetMapping("/encode")
	public List<String> encode(@RequestParam List<String> values) {
		List<String> encodedValues = new ArrayList<String>();
		values.forEach(value -> {
			encodedValues.add(new BCryptPasswordEncoder().encode(value));
		});
		// System.out.println(new BCryptPasswordEncoder().encode("android-client"));
		// System.out.println(new BCryptPasswordEncoder().encode("angular-client"));
		return encodedValues;
	}

	@GetMapping("/jwt")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public Object jwtParser(OAuth2Authentication authentication) {

		System.out.println("authentication--->" + authentication);

		authentication.getCredentials();
		System.out.println("authentication--1->" + authentication);
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		System.out.println("authenticati2on--->" + details);
		String jwtToken = details.getTokenValue();
		System.out.println("jwtToken--->" + jwtToken);
		Claims claims = Jwts.parser().setSigningKey("dev".getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwtToken)
				.getBody();
		System.out.println(claims.get("user-info"));

		return claims;

	}
}
