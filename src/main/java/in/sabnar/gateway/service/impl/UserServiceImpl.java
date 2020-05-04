package in.sabnar.gateway.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.sabnar.gateway.dto.UserDto;
import in.sabnar.gateway.entity.Role;
import in.sabnar.gateway.entity.RoleType;
import in.sabnar.gateway.entity.User;
import in.sabnar.gateway.repository.RoleRepository;
import in.sabnar.gateway.repository.UserRepository;
import in.sabnar.gateway.service.UserService;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userDao;

	@Autowired
	private RoleRepository roleDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUsername(userName);
		if (user == null) {
			log.error("Invalid username or password.");
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				grantedAuthorities);
	}

	private Set<GrantedAuthority> getAuthorities(User user) {
		Set<Role> roles = user.getRoles();
		final Set<GrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase()))
				.collect(Collectors.toSet());
		return authorities;
	}

	public List<UserDto> findAll() {
		List<UserDto> users = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(user -> users.add(user.toUserDto()));
		return users;
	}

	@Override
	public User findOne(String id) {
		return userDao.findById(id).get();
	}

	@Override
	public void delete(String id) {
		userDao.deleteById(id);
	}

	@Override
	public UserDto save(UserDto userDto) {
		User userWithDuplicateUsername = userDao.findByUsername(userDto.getUsername());
		if (userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
			log.error(String.format("Duplicate username %", userDto.getUsername()));
			throw new RuntimeException("Duplicate username.");
		}
		User userWithDuplicateEmail = userDao.findByEmail(userDto.getEmail());
		if (userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
			log.error(String.format("Duplicate email %", userDto.getEmail()));
			throw new RuntimeException("Duplicate email.");
		}
		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		List<RoleType> roleTypes = new ArrayList<>();
		userDto.getRole().stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
		user.setRoles(new HashSet<>(roleDao.findAllById(userDto.getRole())));
		userDao.save(user);
		return userDto;
	}

}
