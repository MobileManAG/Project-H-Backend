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
 * PhoneNumber.java
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

import com.mobileman.projecth.domain.ModelBean;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * Represent phone number
 * @author mobileman
 *
 */
@Embeddable
public class PhoneNumber extends ModelBean {

	/**
	 * Country code of the phone number (+41, +423, 0041...)
	 */
	private String countryCode;
	
	/**
	 * 10 digits number
	 */
	private String number;
	
	/**
	 * 
	 */
	public PhoneNumber() {
		super();
	}

	/**
	 * @param countryCode
	 * @param number
	 */
	public PhoneNumber(String countryCode, String number) {
		super();
		setCountryCode(countryCode);
		setNumber(number);
	}

	/**
	 * @return Country code of the phone number (+41, +423, 0041...)
	 */
	@Column(name = "phone_country_code", length=5, nullable=true)
	protected String getCountryCodeEncoded() {
		return countryCode;
	}

	/**
	 * @param countryCodeEncoded Country code of the phone number (+41, +423, 0041...)
	 */
	public void setCountryCodeEncoded(String countryCodeEncoded) {
		this.countryCode = countryCodeEncoded;
	}
	
	/**
	 * @return Country code of the phone number (+41, +423, 0041...)
	 */
	@Transient
	public String getCountryCode() {
		return getDecryptedValue("countryCode", getCountryCodeEncoded());
	}

	/**
	 * @param countryCode Country code of the phone number (+41, +423, 0041...)
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = AESEncoderDecoder.encode(countryCode);
		setDecryptedValue("countryCode", countryCode);
	}
	
	/**
	 * @return 10 digits number
	 */
	@Column(name = "phone_number", length=5, nullable=true)
	protected String getNumberEncoded() {
		return number;
	}

	/**
	 * @param numberEncoded 10 digits number
	 */
	public void setNumberEncoded(String numberEncoded) {
		this.number = numberEncoded;
	}

	/**
	 * @return 10 digits number
	 */
	@Transient
	public String getNumber() {
		return getDecryptedValue("number", getNumberEncoded());
	}

	/**
	 * @param number 10 digits number
	 */
	public void setNumber(String number) {
		this.number = AESEncoderDecoder.encode(number);
		setDecryptedValue("number", number);
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.countryCode == null) ? 0 : this.countryCode.hashCode());
		result = prime * result + ((this.number == null) ? 0 : this.number.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhoneNumber other = (PhoneNumber) obj;
		if (this.countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!this.countryCode.equals(other.countryCode))
			return false;
		if (this.number == null) {
			if (other.number != null)
				return false;
		} else if (!this.number.equals(other.number))
			return false;
		return true;
	}
	
	/**
	 * @return number format
	 */
	public String format() {
		String result = "";
		
		if (getCountryCode() != null) {
			result += getCountryCode();
		}
		
		if (getNumber() != null) {
			result += " " + getNumber();
		}
		
		return result;
	}
}
