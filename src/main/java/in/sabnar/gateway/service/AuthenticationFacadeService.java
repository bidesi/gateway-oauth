package in.sabnar.gateway.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeService {
	Authentication getAuthentication();
}
