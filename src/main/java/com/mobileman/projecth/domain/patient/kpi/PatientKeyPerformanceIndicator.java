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
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.patient.kpi;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.Entity;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;

/**
 * KeyPerformanceIndicator for patient.
 * 
 * @author mobileman
 *
 */
@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "patient_key_performance_indicator", schema = "public")
public class PatientKeyPerformanceIndicator extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Patient patient;
	private KeyPerformanceIndicatorType keyPerformanceIndicatorType;
	private Date timestamp;
	private Date logDate;
	private BigDecimal value;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.domain.Entity#getId()
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return super.getId();
	}

	/**
	 * @return patient
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "patient_id", nullable = false)
	@ForeignKey(name="fk_patient_kpi_patient_id")
	public Patient getPatient() {
		return this.patient;
	}

	/**
	 * @param patient
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @return timestamp
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestampcreated", nullable = false, length = 29)
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestampcreated
	 */
	public void setTimestamp(Date timestampcreated) {
		this.timestamp = timestampcreated;
	}
	
	/**
	 * Gets a logDate
	 *
	 * @return logDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "log_date", nullable = false, length = 29)
	public Date getLogDate() {
		return this.logDate;
	}
	
	/**
	 * Setter for logDate
	 *
	 * @param logDate new value of logDate
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	/**
	 * Gets a newValue
	 *
	 * @return newValue
	 */
	@Column(name = "kpi_value", nullable = false)
	public BigDecimal getValue() {
		return this.value;
	}
	
	/**
	 * @param value new value of patient computed kpi
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	/**
	 * Gets a questionaryValidationType
	 *
	 * @return questionaryValidationType
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "validation_type_id", nullable = false)
	@ForeignKey(name="fk_patient_kpi_kpit_id")
	public KeyPerformanceIndicatorType getKeyPerformanceIndicatorType() {
		return this.keyPerformanceIndicatorType;
	}
	
	/**
	 * Setter for questionaryValidationType
	 *
	 * @param questionaryValidationType new value of questionaryValidationType
	 */
	public void setKeyPerformanceIndicatorType(
			KeyPerformanceIndicatorType questionaryValidationType) {
		this.keyPerformanceIndicatorType = questionaryValidationType;
	}

}
