/**
 * 
 */
package in.sabnar.gateway.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import in.sabnar.gateway.service.AuthenticationFacadeService;

/**
 * @author bidesi
 *
 */
@Component
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
