package in.sabnar.gateway.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

	static final String CLIENT_ID = "audio-client";
	// static final String CLIENT_SECRET = "password";
	static final String CLIENT_SECRET = "$2a$04$io4Hr9G5pi3a9q4cUg7E8e.MaP.H8BpkSHHC.FkP/Lvy6nGQfN/RO";
	static final String GRANT_TYPE_PASSWORD = "password";
	static final String AUTHORIZATION_CODE = "authorization_code";
	static final String REFRESH_TOKEN = "refresh_token";
	static final String IMPLICIT = "implicit";
	static final String SCOPE_READ = "read";
	static final String SCOPE_WRITE = "write";
	static final String TRUST = "trust";
	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
	static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;

	@Autowired
	private AuthenticationManager authenticationManager;

//	@Autowired
//	private TokenStore redisTokenStore;
	
	@Autowired
    public PasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private TokenStore jwtTokenStore;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Autowired
	private TokenEnhancer jwtTokenEnhancer;

	@Bean
	public TokenEnhancer jwtTokenEnhancer(){
	    return new JWTokenEnhancer();
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//endpoints.authenticationManager(authenticationManager).tokenStore(redisTokenStore);
		TokenEnhancerChain  enhancerChain   = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer(), jwtAccessTokenConverter));
		
		endpoints
			.authenticationManager(authenticationManager)
			.tokenStore(jwtTokenStore)
			.accessTokenConverter(jwtAccessTokenConverter)
			.tokenEnhancer(enhancerChain);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory()
//   				.withClient(CLIENT_ID)
//				.secret(CLIENT_SECRET)
//				.authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
//				.scopes(SCOPE_READ, SCOPE_WRITE, TRUST).accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
//				.refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
		
		JdbcClientDetailsServiceBuilder jcsb = clients.jdbc(dataSource);
	    jcsb.passwordEncoder(passwordEncoder);
	}

}
