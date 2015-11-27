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
 * AnswerFrequency.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 11.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.dto.patient;

import java.io.Serializable;
import java.util.Date;

/**
 * Data transfer object for frequency of an answer in given time
 * @author mobileman
 *
 */
public class AnswerFrequency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date date;
	
	/**
	 * 
	 */
	private Long count;
	
	

	/**
	 * @param date
	 * @param count
	 */
	public AnswerFrequency(Date date, Long count) {
		super();
		this.date = date;
		this.count = count;
	}

	/**
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date new value of date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return count
	 */
	public Long getCount() {
		return this.count;
	}

	/**
	 * @param count new value of count
	 */
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
