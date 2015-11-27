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
/**
 * Name.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 16.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.ModelBean;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * Represent name
 * @author mobileman
 *
 */
@Embeddable
public class Name extends ModelBean implements Cloneable {

	/**
	 * Name
	 */
	private String name;
	
	/**
	 * Surname
	 */
	private String surname;
	
	/**
	 * 
	 */
	public Name() {
		super();
	}

	/**
	 * @param name
	 * @param surname
	 */
	public Name(String name, String surname) {
		this();
		setName(name);
		setSurname(surname);
	}

	/**
	 * @return name
	 */
	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Column(name = "name", length=255, nullable=true)
	protected String getNameEncoded() {
		return this.name;
	}

	/**
	 * @param nameEncoded new value of name
	 */
	protected void setNameEncoded(String nameEncoded) {
		this.name = nameEncoded;
	}
	
	/**
	 * @return name
	 */
	@Transient
	public String getName() {
		return getDecryptedValue("name", name);
	}

	/**
	 * @param name new value of name
	 */
	public void setName(String name) {
		this.name = AESEncoderDecoder.encode(name);
		setDecryptedValue("name", name);
	}

	/**
	 * @return surname
	 */
	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Column(name = "surname", length=255, nullable=true)
	protected String getSurnameEncoded() {
		return this.surname;
	}

	/**
	 * @param surnameEncoded new value of surname
	 */
	public void setSurnameEncoded(String surnameEncoded) {
		this.surname = surnameEncoded;
	}
	
	/**
	 * @return surname
	 */
	@Transient
	public String getSurname() {
		return getDecryptedValue("surname", getSurnameEncoded());
	}

	/**
	 * @param surname new value of surname
	 */
	public void setSurname(String surname) {
		this.surname = AESEncoderDecoder.encode(surname);
		setDecryptedValue("surname", surname);
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Name clone() throws CloneNotSupportedException {
		return (Name)super.clone();
	}
	
	
}
