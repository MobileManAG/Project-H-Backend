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
 * PznNumber.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data.id_types;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

/**
 * Represents the PZN number of a medication (barcode)
 * @author mobileman
 *
 */
@Embeddable
public class PznBarcode {

	private String number;
	
	/**
	 * Gets a number
	 *
	 * @return number
	 */
	@Column(name = "pzn_number", length = 7, nullable = true)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getNumber() {
		return this.number;
	}
	
	/**
	 * Setter for pzn
	 *
	 * @param number new value of number
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @return formats the number value with prefix like <code>PZN-xxxxxxx</code>
	 */
	public String format() {
		return "PZN-" + getNumber();
	}
}
