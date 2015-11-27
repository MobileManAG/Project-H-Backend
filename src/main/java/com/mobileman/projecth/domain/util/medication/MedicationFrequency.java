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
 * MedicationFrequency.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 28.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.medication;

/**
 * @author mobileman
 *
 */
public enum MedicationFrequency {

	/**
	 * One time
	 */
	ONE_TIME("one-time"),
	
	/**
	 * Daily
	 */
	DAILY("daily"),
	
	/**
	 * Weekly
	 */
	WEEKLY("weekly"),
	
	/**
	 * Bi-weekly
	 */
	BI_WEEKLY("bi-weekly");
	
	private final String value;

	private MedicationFrequency(String value) {
		this.value = value;
		
	}
	
	/**
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}
}
