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
 * Address.java
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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;

import com.mobileman.projecth.domain.ModelBean;
import com.mobileman.projecth.domain.data.id_types.PostalCode;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * Represents some address
 * 
 * @author mobileman
 *
 */
@Embeddable
public class Address extends ModelBean {

	/**
	 * Postal code of the address
	 */
	private PostalCode postalCode;
	
	/**
	 * Address number
	 */
	private String number;
	
	/**
	 * Address - street
	 */
	private String address;
	
	/**
	 * Address - place
	 */
	private String place;

	/**
	 * @return Postal code of the address
	 */
	@Embedded
	@AttributeOverrides(value={
		       @AttributeOverride(name="code", column=@Column(name="address_postal_code", length = 10, nullable = true))
		   })
	public PostalCode getPostalCode() {
		return this.postalCode;
	}

	/**
	 * @param postalCode Postal code of the address
	 */
	public void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
	}
	
	/**
	 * @return Address number
	 */
	@Column(name="address_number", length=100, nullable=true)
	protected String getNumberEncoded() {
		return number;
	}

	/**
	 * @param number Address number
	 */
	protected void setNumberEncoded(String number) {
		this.number = number;
	}

	/**
	 * @return Address number
	 */
	@Transient
	public String getNumber() {
		return getDecryptedValue("number", number);
	}

	/**
	 * @param number Address number
	 */
	public void setNumber(String number) {
		this.number = AESEncoderDecoder.encode(number);
		setDecryptedValue("number", number);
	}
	
	/**
	 * @return Address - street
	 */
	@Column(name = "address", length=255, nullable=true)
	protected String getAddressEncoded() {
		return this.address;
	}
	
	/**
	 * @param address Address - street
	 */
	protected void setAddressEncoded(String address) {
		this.address = address;
	}

	/**
	 * @return Address - street
	 */
	@Transient
	public String getAddress() {
		return getDecryptedValue("address", address);
	}

	/**
	 * @param address Address - street
	 */
	public void setAddress(String address) {
		this.address = AESEncoderDecoder.encode(address);
		setDecryptedValue("address", address);
	}

	/**
	 * @return place
	 */
	@Column(name = "place", length=255, nullable=true)
	protected String getPlaceEncoded() {
		return place;
	}

	/**
	 * @param place new value of place
	 */
	protected void setPlaceEncoded(String place) {
		this.place = place;
	}
	
	/**
	 * @return place
	 */
	@Transient
	public String getPlace() {
		return getDecryptedValue("place", place);
	}

	/**
	 * @param place new value of place
	 */
	public void setPlace(String place) {
		this.place = AESEncoderDecoder.encode(place);
		setDecryptedValue("place", place);
	}
}
