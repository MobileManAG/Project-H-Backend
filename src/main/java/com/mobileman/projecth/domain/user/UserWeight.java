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
 * UserWeight.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 17.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.user;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents user's weight in time
 * @author mobileman
 *
 */
@Embeddable
public class UserWeight {

	/**
	 * User's weight
	 */
	private BigDecimal weight;
	
	private Date date;

	/**
	 * @return weight
	 */
	@Column(name="weight", nullable=false)
	public BigDecimal getWeight() {
		return this.weight;
	}

	/**
	 * @param weight new value of weight
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	/**
	 * @return date
	 */
	@Column(name="date", nullable=false)
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date new value of date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
