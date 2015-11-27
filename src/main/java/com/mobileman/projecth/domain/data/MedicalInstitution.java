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
 * MedicalInstitution.java
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

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.ModelBean;
import com.mobileman.projecth.domain.index.country.Country;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * Describes medical institution
 * @author mobileman
 *
 */
@Embeddable
public class MedicalInstitution extends ModelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the institution
	 */
	private String name;
	
	/**
	 * Homepage of the institution
	 */
	private String homePageUrl;
	
	private PhoneNumber phoneNumber;
	
	private PhoneNumber faxNumber;
	
	/**
	 * Country of the medical institution
	 */
	private Country country;
	
	/**
	 * Address of the doctor
	 */
	private Address address;
	
	/**
	 * @return Name of the institution
	 */
	@Column(name = "medic_instit_name", length=255, nullable=true)
	protected String getNameEncoded() {
		return this.name;
	}

	/**
	 * @param nameEncoded Name of the institution
	 */
	protected void setNameEncoded(String nameEncoded) {
		this.name = nameEncoded;
	}
	
	/**
	 * @return Name of the institution
	 */
	@Transient
	public String getName() {
		return getDecryptedValue("name", this.name);
	}

	/**
	 * @param name Name of the institution
	 */
	public void setName(String name) {
		this.name = AESEncoderDecoder.encode(name);
		setDecryptedValue("name", name);
	}

	/**
	 * @return Homepage of the institution
	 */
	@Column(name = "medic_instit_homepage_url", length=255, nullable=true)
	protected String getHomePageUrlEncoded() {
		return this.homePageUrl;
	}

	/**
	 * @param homePageUrl Homepage of the institution
	 */
	protected void setHomePageUrlEncoded(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}
	
	/**
	 * @return Homepage of the institution
	 */
	@Transient
	public String getHomePageUrl() {
		return getDecryptedValue("homePageUrl", this.homePageUrl);
	}

	/**
	 * @param homePageUrl Homepage of the institution
	 */
	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = AESEncoderDecoder.encode(homePageUrl);
		setDecryptedValue("homePageUrl", homePageUrl);
	}

	/**
	 * @return phoneNumber
	 */
	@Embedded
	@AttributeOverrides(value={
		       @AttributeOverride(name="countryCodeEncoded", column=@Column(name="medic_instit_phone_country_code", length=5, nullable=true)),
		       @AttributeOverride(name="numberEncoded", column=@Column(name="medic_instit_phone_number", length=10, nullable=true))
		   })
	public PhoneNumber getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @param phoneNumber new value of phoneNumber
	 */
	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return faxNumber
	 */
	@Embedded()
	@AttributeOverrides(value={
	       @AttributeOverride(name="countryCodeEncoded", column=@Column(name="medic_instit_fax_country_code", length=5, nullable=true)),
	       @AttributeOverride(name="numberEncoded", column=@Column(name="medic_instit_fax_number", length=10, nullable=true))
	   })
	public PhoneNumber getFaxNumber() {
		return this.faxNumber;
	}

	/**
	 * @param faxNumber new value of faxNumber
	 */
	public void setFaxNumber(PhoneNumber faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	/**
	 * @return Country of the user
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	@JoinColumn(name = "medic_instit_country_id", nullable=true)
	@ForeignKey(name="fk_medic_instit_country_id")
	public Country getCountry() {
		return this.country;
	}

	/**
	 * @param country Country of the user
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
	
	/**
	 * @return address
	 */
	@Embedded
	@AttributeOverrides(value={
		       @AttributeOverride(name="addressEncoded", column=@Column(name="medic_instit_address", length=255, nullable=true)),
		       @AttributeOverride(name="numberEncoded", column=@Column(name="medic_instit_address_number", length=100, nullable=true)),
		       @AttributeOverride(name="postalCode.codeEncoded", column=@Column(name="medic_instit_address_postal_code", length=10, nullable=true)),
		       @AttributeOverride(name="placeEncoded", column=@Column(name="medic_instit_address_place", length=255, nullable=true))
		   })
	public Address getAddress() {
		return this.address;
	}

	/**
	 * @param address new value of address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
}
