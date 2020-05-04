/**
 * 
 */
package in.sabnar.gateway.dto;

import java.util.List;

/**
 * @author bidesi
 *
 */
public class UserDto {

	private String id;
	private String name;
	private String username;
	private String password;
	private String email;
	private List<String> role;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", email="
				+ email + ", role=" + role + "]";
	}

}
