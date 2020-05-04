package in.sabnar.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import in.sabnar.gateway.filter.AuthenticatedFilter;

//@EnableDiscoveryClient
//@EnableWebSecurity
//@EnableRedisHttpSession
@EnableZuulProxy
@EnableEurekaServer
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public AuthenticatedFilter authenticatedFilter() {
		return new AuthenticatedFilter();
	}

}
