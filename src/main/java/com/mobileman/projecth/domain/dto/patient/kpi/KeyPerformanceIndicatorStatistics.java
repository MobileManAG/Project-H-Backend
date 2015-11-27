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
 * KeyPerformanceIndicatorStatistics.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.dto.patient.kpi;

import java.math.BigDecimal;
import java.util.Date;

import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;

/**
 * DTO for KeyPerformanceIndicator statistics
 * 
 * @author mobileman
 *
 */
public class KeyPerformanceIndicatorStatistics {

	private BigDecimal value;
	
	private final Date date;
	
	private final KeyPerformanceIndicatorType keyPerformanceIndicatorType;

	/**
	 * @param value
	 * @param date
	 * @param keyPerformanceIndicatorType 
	 */
	public KeyPerformanceIndicatorStatistics(BigDecimal value, Date date, KeyPerformanceIndicatorType keyPerformanceIndicatorType) {
		super();
		this.value = value == null ? new BigDecimal("0.0") : value;
		this.date = date;
		this.keyPerformanceIndicatorType = keyPerformanceIndicatorType;
	}
	
	/**
	 * @return keyPerformanceIndicatorType
	 */
	public KeyPerformanceIndicatorType getKeyPerformanceIndicatorType() {
		return this.keyPerformanceIndicatorType;
	}

	/**
	 * @return value
	 */
	public BigDecimal getValue() {
		return this.value;
	}
	
	/**
	 * @param value new value of value
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * @return getValue().doubleValue()
	 */
	public double getDoubleValue() {
		return getValue().doubleValue();
	}
}
