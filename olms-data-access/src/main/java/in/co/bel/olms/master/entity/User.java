package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The persistent class for the user database table.
 * 
 * @author Manasa B
 */
@Entity
@Table(name = "user", schema = "mdms")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
	@SequenceGenerator(name = "user_id_seq", schema = "mdms", sequenceName = "user_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@NotNull(message = "User name can not be null")
	@Size(min = 2, max = 100, message = "User name must be between 2 and 100 characters")
	// @Pattern(regexp="^[a-zA-Z0-9_.]*$" , message= "User name is not valid (Only
	// Aplha numeric is allowed) ")
	private String username;

	@Column(name = "hash_password")
	@NotNull(message = "User hash password can not be null")
	@Size(min = 0, max = 100, message = "User hash password length be between 0 and 100 characters")
	private String hashPassword;

	@Column(name = "salt_password")
	@Size(min = 0, max = 100, message = "User salt password length be between 0 and 100 characters")
	private String saltPassword;

	// bi-directional many-to-one association to Staff
	@ManyToOne
	@JoinColumn(name = "emp_id")
	private Employee emp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	@NotNull(message = "User created date can not be null")
	private Date createdDatetime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;

	private Boolean active;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login")
	private Date lastLogin;

	@Column(name = "login_status")
	private Boolean loginStatus;

	@Column(name = "last_updated_by")
	private Integer lastUpdatedBy;

	@Column(name = "failed_attempts")
	private Integer failedAttempts;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lock_time")
	private Date lockTime;

	@Column(name = "token_value")
	@Size(min = 0, max = 300, message = "User token length be between 0 and 300 characters")
	private String tokenValue;

	private Boolean locked;
	
	@Column(name = "logged_in_ip")
	private String userIP;
	
	

	public User() {
		// default no-arg constructor for Hibernate to initialize the entity
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Integer getFailedAttempts() {
		return failedAttempts;
	}

	public void setFailedAttempts(Integer failedAttempts) {
		this.failedAttempts = failedAttempts;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public String getSaltPassword() {
		return saltPassword;
	}

	public void setSaltPassword(String saltPassword) {
		this.saltPassword = saltPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	
	

}