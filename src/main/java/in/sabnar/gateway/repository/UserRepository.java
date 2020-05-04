package in.sabnar.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sabnar.gateway.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	User findByEmail(String email);
	User findByUsername(String username);
	
}
