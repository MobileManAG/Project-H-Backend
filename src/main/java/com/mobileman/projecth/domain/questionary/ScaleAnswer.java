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
 * ScaleAnswer.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 13.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.questionary;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Answer - represents scale of values with min/max and step
 * @author mobileman
 *
 */
@Entity
@DiscriminatorValue("1")
public class ScaleAnswer extends Answer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal minValue;

	private BigDecimal maxValue;
	
	private BigDecimal step;
	
	private String minValueTitle;

	private String maxValueTitle;
	
	private String stepTitle;
	
	private String selectedValueTitle;

	/**
	 * 
	 */
	public ScaleAnswer() {
		super(Kind.SCALE);
	}

	/**
	 * @param id
	 */
	public ScaleAnswer(Long id) {
		super(id);
	}

	/**
	 * @return minValue
	 */
	@Column(name = "min_value", nullable = true)
	public BigDecimal getMinValue() {
		return this.minValue;
	}

	/**
	 * @param minValue new value of minValue
	 */
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return maxValue
	 */
	@Column(name = "max_value", nullable = true)
	public BigDecimal getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @param maxValue new value of maxValue
	 */
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return step
	 */
	@Column(name = "step", nullable = true)
	public BigDecimal getStep() {
		return this.step;
	}

	/**
	 * @param step new value of step
	 */
	public void setStep(BigDecimal step) {
		this.step = step;
	}

	/**
	 * @return minValueTitle
	 */
	@Column(name = "min_value_title", nullable = true)
	public String getMinValueTitle() {
		return this.minValueTitle;
	}

	/**
	 * @param minValueTitle new value of minValueTitle
	 */
	public void setMinValueTitle(String minValueTitle) {
		this.minValueTitle = minValueTitle;
	}

	/**
	 * @return maxValueTitle
	 */
	@Column(name = "max_value_title", nullable = true)
	public String getMaxValueTitle() {
		return this.maxValueTitle;
	}

	/**
	 * @param maxValueTitle new value of maxValueTitle
	 */
	public void setMaxValueTitle(String maxValueTitle) {
		this.maxValueTitle = maxValueTitle;
	}

	/**
	 * @return stepTitle
	 */
	@Column(name = "step_title", nullable = true)
	public String getStepTitle() {
		return this.stepTitle;
	}

	/**
	 * @param stepTitle new value of stepTitle
	 */
	public void setStepTitle(String stepTitle) {
		this.stepTitle = stepTitle;
	}

	/**
	 * @return selectedValueTitle
	 */
	@Column(name = "selected_value_title", nullable = true)
	public String getSelectedValueTitle() {
		return this.selectedValueTitle;
	}

	/**
	 * @param selectedValueTitle new value of selectedValueTitle
	 */
	public void setSelectedValueTitle(String selectedValueTitle) {
		this.selectedValueTitle = selectedValueTitle;
	}
	
	
}
