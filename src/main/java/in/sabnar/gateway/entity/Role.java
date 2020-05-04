package in.sabnar.gateway.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Role {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "NAME")
	private RoleType name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@CreatedDate
	@Column(name = "CREATED_ON")
	private Long createdOn;
	
	@CreatedDate
	@Column(name = "MODIFIED_ON")
	private Long modifiedOn;
	
	public Role() {
		super();
	}

	public Role(RoleType name, String description) {
		this.name = name;
		this.description = description;
	}

	public RoleType getName() {
		return name;
	}

	public void setName(RoleType name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
