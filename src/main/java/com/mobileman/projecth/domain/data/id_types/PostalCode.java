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
 * ZipCode.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 16.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data.id_types;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.mobileman.projecth.domain.ModelBean;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * 
 * @author mobileman
 *
 */
@Embeddable
public class PostalCode extends ModelBean {

	/**
	 * Code 
	 */
	private String code;

	/**
	 * 
	 */
	public PostalCode() {
		super();
	}

	/**
	 * @param code
	 */
	public PostalCode(String code) {
		super();
		setCode(code);
	}

	/**
	 * @return code
	 */
	@Transient
	public String getCode() {
		return getDecryptedValue("code", code);
	}

	/**
	 * @param code new value of code
	 */
	public void setCode(String code) {
		this.code = AESEncoderDecoder.encode(code);
		setDecryptedValue("code", code);
	}
	
	/**
	 * @return code
	 */
	@Column(name = "code", length=10, nullable=true)
	protected String getCodeEncoded() {
		return this.code;
	}

	/**
	 * @param code new value of code
	 */
	protected void setCodeEncoded(String code) {
		this.code = code;
	}

	/**
	 * @return formated postal code
	 */
	public String format() {
		return getCode();
	}
}
