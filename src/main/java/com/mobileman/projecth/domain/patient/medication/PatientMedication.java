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
 * PatientMedication.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 25.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.patient.medication;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.Patient;

/**
 * Represents medication consumed by a patient.
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "patient_medication", schema = "public" )
@Indexed
public class PatientMedication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Patient patient;
	
	private Disease disease;
	
	private Medication medication;
	
	private Date timestamp;
	
	private Date consumptionDate;
	
	private BigDecimal amount;
	
	private String comment;

	/**
	 * @return id of an entity
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return patient to whom the medication belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "patient_id", nullable = false)
	@ForeignKey(name = "fk_patient_medication_patient_id")
	public Patient getPatient() {
		return this.patient;
	}

	/**
	 * @param patient patient to whom the medication belongs
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	/**
	 * Gets a disease
	 *
	 * @return disease
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "disease_id", nullable = false)
	@ForeignKey(name = "fk_patient_medication_disease_id")
	public Disease getDisease() {
		return this.disease;
	}
	
	/**
	 * Setter for disease
	 *
	 * @param disease new value of disease
	 */
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
	/**
	 * @return consumed medicine
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medicine_id", nullable = false)
	@ForeignKey(name = "fk_patient_medication_medicine_id")
	public Medication getMedication() {
		return this.medication;
	}

	/**
	 * @param medicine consumed medicine
	 */
	public void setMedication(Medication medicine) {
		this.medication = medicine;
	}
	
	/**
	 * @return creation date/time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"timestamp\"", nullable = false)
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * @param timestamp creation date/time
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return amount amount of consumed medicine
	 */
	@Column(name = "amount", nullable = false)
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	/**
	 * @param amount amount of consumed medicine
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return comment
	 */
	@Column(name = "comment", nullable = true)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getComment() {
		return this.comment;
	}

	/**
	 * @param comment new value of comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return consumptionDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cumsuption_date", nullable = false)
	public Date getConsumptionDate() {
		return this.consumptionDate;
	}

	/**
	 * @param consumptionDate new value of cumsuptionDate
	 */
	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}
	
	
}
