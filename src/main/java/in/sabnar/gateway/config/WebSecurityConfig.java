package in.sabnar.gateway.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import in.sabnar.gateway.entity.Role;
import in.sabnar.gateway.entity.RoleType;
import in.sabnar.gateway.entity.User;
import in.sabnar.gateway.repository.RoleRepository;
import in.sabnar.gateway.repository.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource(name = "userService")
	private UserDetailsService userDetailsService;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
        .csrf().disable()
        .anonymous().disable()
        .authorizeRequests()
        .antMatchers("/api-docs/**").permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow eureka client to be accessed without authentication
		web.ignoring().antMatchers("/*/")//
				.antMatchers("/eureka/**")//
				.antMatchers(HttpMethod.OPTIONS, "/**"); // Request type options should be allowed.
	}

	@Autowired
	@Transactional
	void initUsers(RoleRepository roleRepository, UserRepository userRepository) {

		Role roleAdmin = roleRepository.findByName(RoleType.ADMIN).orElse(null);
		if (roleAdmin == null) {
			roleAdmin = new Role(RoleType.ADMIN, "Admin User Role");
			roleRepository.save(roleAdmin);
		}

		Role roleUser = roleRepository.findByName(RoleType.USER).orElse(null);
		if (roleUser == null) {
			roleUser = new Role(RoleType.USER, "Login User Role");
			roleRepository.save(roleUser);
		}

		Role roleGuest = roleRepository.findByName(RoleType.GUEST).orElse(null);
		if (roleGuest == null) {
			roleRepository.save(new Role(RoleType.GUEST, "Guest User Role"));
		}

		User userAdmin = userRepository.findByEmail("admin@admin.com");
		if (userAdmin == null) {
			userAdmin = new User();
			userAdmin.setName("Admin");
			userAdmin.setUsername("admin");
			userAdmin.setEmail("admin@admin.com");
			userAdmin.setPassword("$2a$04$HIlVktLQ5tu05RUP/5/jsOfUZUbFwsj24LAMS71Fz8/Z114Iapdk2");// https://www.devglan.com/online-tools/bcrypt-hash-generator

			Set<Role> userRole = new HashSet<Role>();
			userRole.add(roleAdmin);
			userAdmin.setRoles(userRole);

			userRepository.save(userAdmin);
		}

		User regUser = userRepository.findByEmail("bidesi@gmail.com");
		if (regUser == null) {
			regUser = new User();
			regUser.setName("Bidesi Gauda");
			regUser.setUsername("bidesi");
			regUser.setEmail("bidesi@gmail.com");
			regUser.setPassword("$2a$04$xw0PM3JaT9.NCJ1ummAZXezHgZqnxDlZ/QdoqK42ExdgBsrRDskEO");

			Set<Role> userRole = new HashSet<Role>();
			userRole.add(roleUser);
			regUser.setRoles(userRole);

			userRepository.save(regUser);
		}
	}

}
