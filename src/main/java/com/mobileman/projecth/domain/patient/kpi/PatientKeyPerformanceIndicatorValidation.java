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
// default package
// Generated Oct 29, 2010 1:35:35 PM by Hibernate Tools 3.4.0.Beta1

package com.mobileman.projecth.domain.patient.kpi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.Entity;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;

/**
 * KPI values validated by doctor
 */
@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "patient_key_performance_indicator_validation", schema = "public")
public class PatientKeyPerformanceIndicatorValidation extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Patient patient;
	private KeyPerformanceIndicatorType keyPerformanceIndicatorType;
	private Date logDate;
	private BigDecimal value;
	
	private List<BigDecimal> data;
	
	/**
	 * Doctor who validates a KPI value
	 */
	private Doctor doctor;

	/**
	 * @return Doctor who validates a KPI value
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	@JoinColumn(name = "doctor_id", nullable = true)
	@ForeignKey(name="fk_patient_kpi_doctor_id")
	public Doctor getDoctor() {
		return this.doctor;
	}

	/**
	 * @param doctor Doctor who validates a KPI value
	 */
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
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

	/**
	 * @return data
	 */
	@ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name = "patient_kpi_validation_data", joinColumns = @JoinColumn(name = "patient_kpi_id"))
    @ForeignKey(name="fk_patient_kpi_data_pat_kpi_id")
    @OrderColumn(name="idx")
	public List<BigDecimal> getData() {
		return this.data;
	}

	/**
	 * @param data new value of data
	 */
	public void setData(List<BigDecimal> data) {
		this.data = data;
	}
	
	
}
