/*******************************************************************************
 * Copyright 2015 MobileMan GmbH
 * www.mobileman.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mobileman.projecth.domain.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * UserAccount
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "user_account", schema = "public")
@Indexed
public class UserAccount extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2723287503667357608L;
	
	private User user;
	
	private String password;
	private String login;
	private String email;
	private Date created;

	/**
	 * Constructor
	 */
	public UserAccount() {
	}
	
	/**
	 * @return entity id
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return super.getId();
	}

	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return login
	 */
	@Column(name = "login", length = 100, insertable=true, updatable=false)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	protected String getLoginEncoded() {
		return this.login;
	}

	/**
	 * @param login
	 */
	protected void setLoginEncoded(String login) {
		this.login = login;
	}
	
	/**
	 * @return login
	 */
	@Transient
	public String getLogin() {
		return getDecryptedValue("login", this.login);
	}

	/**
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = AESEncoderDecoder.encode(login);
		setDecryptedValue("login", login);
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * @return user's email
	 */
	@Column(name = "email", nullable = false, length = 100, insertable=true, updatable=false)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	protected String getEmailEncoded() {
		return this.email;
	}

	/**
	 * @param email user's email
	 */
	protected void setEmailEncoded(String email) {
		this.email = email;
	}
	
	/**
	 * @return user's email
	 */
	@Transient
	public String getEmail() {
		return getDecryptedValue("email", this.email);
	}

	/**
	 * @param email user's email
	 */
	public void setEmail(String email) {
		this.email = AESEncoderDecoder.encode(email);
		setDecryptedValue("email", email);
	}

	/**
	 * @return user
	 */
	@OneToOne(mappedBy = "userAccount", cascade={}, fetch = FetchType.EAGER, optional=false)
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user new value of user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
