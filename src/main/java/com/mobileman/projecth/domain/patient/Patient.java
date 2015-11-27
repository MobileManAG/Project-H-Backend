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
package com.mobileman.projecth.domain.patient;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;

/**
 * The patient is a private user who has a chronical disease and resides in Germany, Switzerland or Austria
 */
@Entity
@DiscriminatorValue("P")
@Indexed
public class Patient extends User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String resultid;
	private String activationcode;
	private Date initialsymptomdate;
	private Date initialdiagnosisdate;
	private Integer timesymptomdiagnostic;
	
	private Set<PatientKeyPerformanceIndicatorValidation> patientAnswerOverrides = new HashSet<PatientKeyPerformanceIndicatorValidation>(0);
	
	private Set<PatientMedicineInitial> medicines = new HashSet<PatientMedicineInitial>(0);
	
	
	/**
	 * 
	 */
	public Patient() {
		super(UserType.P);
	}

	@Column(name = "resultid", length = 50)
	public String getResultid() {
		return this.resultid;
	}

	public void setResultid(String resultid) {
		this.resultid = resultid;
	}

	@Column(name = "activationcode", length = 50)
	public String getActivationcode() {
		return this.activationcode;
	}

	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "initialsymptomdate", length = 13)
	public Date getInitialsymptomdate() {
		return this.initialsymptomdate;
	}

	public void setInitialsymptomdate(Date initialsymptomdate) {
		this.initialsymptomdate = initialsymptomdate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "initialdiagnosisdate", length = 13)
	public Date getInitialdiagnosisdate() {
		return this.initialdiagnosisdate;
	}

	public void setInitialdiagnosisdate(Date initialdiagnosisdate) {
		this.initialdiagnosisdate = initialdiagnosisdate;
	}

	@Column(name = "timesymptomdiagnostic")
	public Integer getTimesymptomdiagnostic() {
		return this.timesymptomdiagnostic;
	}

	public void setTimesymptomdiagnostic(Integer timesymptomdiagnostic) {
		this.timesymptomdiagnostic = timesymptomdiagnostic;
	}
	
	/**
	 * Gets a patientAnswerOverrides
	 *
	 * @return patientAnswerOverrides
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval= true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<PatientKeyPerformanceIndicatorValidation> getPatientAnswerOverrides() {
		return this.patientAnswerOverrides;
	}
	
	/**
	 * Setter for patientAnswerOverrides
	 *
	 * @param patientAnswerOverrides new value of patientAnswerOverrides
	 */
	public void setPatientAnswerOverrides(
			Set<PatientKeyPerformanceIndicatorValidation> patientAnswerOverrides) {
		this.patientAnswerOverrides = patientAnswerOverrides;
	}
	
	/**
	 * Gets a medecines
	 *
	 * @return medecines
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval= true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<PatientMedicineInitial> getMedicines() {
		return this.medicines;
	}
	
	/**
	 * Setter for medecines
	 *
	 * @param medicines new value of medecines
	 */
	public void setMedicines(Set<PatientMedicineInitial> medicines) {
		this.medicines = medicines;
	}
}
