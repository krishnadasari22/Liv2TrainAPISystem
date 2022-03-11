package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "factset_ondemand_session_info")
public class FactSetOnDemandSessionInfo {

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="environment")
	private String environment;
	
	@Column(name="access_level")
	private String accessLevel;
	
	@Column(name="session_token")
	private String sessionToken;
	
	@Column(name="counter")
	private String counter;
	
	@Column(name="Key_id")
	private String keyId;
	
	@Column(name="factset_key")
	private String factSetKey;
	
	@Column(name="username")
	private String userName;

	@Column(name="serial")
	private String serial;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getFactSetKey() {
		return factSetKey;
	}

	public void setFactSetKey(String factSetKey) {
		this.factSetKey = factSetKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Override
	public String toString() {
		return "FactSetOnDemandSessionInfo [id=" + id + ", environment=" + environment + ", accessLevel=" + accessLevel
				+ ", sessionToken=" + sessionToken + ", counter=" + counter + ", keyId=" + keyId + ", factSetKey="
				+ factSetKey + ", userName=" + userName + ", serial=" + serial + "]";
	}

}
