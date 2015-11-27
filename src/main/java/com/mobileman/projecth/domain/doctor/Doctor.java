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
package com.mobileman.projecth.domain.doctor;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

import com.mobileman.projecth.domain.data.MedicalInstitution;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;

/**
 * The doctor is a professional user who is curing and conducting therapies and resides in Germany, Switzerland or Austria 
 */
@Entity
@DiscriminatorValue("D")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed
public class Doctor extends User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Medical institution of the doctor
	 */
	private MedicalInstitution medicalInstitution;

	/**
	 * 
	 */
	public Doctor() {
		super(UserType.D);
	}

	/**
	 * @return medicalInstitution
	 */
	@Embedded
	public MedicalInstitution getMedicalInstitution() {
		return this.medicalInstitution;
	}

	/**
	 * @param medicalInstitution new value of medicalInstitution
	 */
	public void setMedicalInstitution(MedicalInstitution medicalInstitution) {
		this.medicalInstitution = medicalInstitution;
	}
	
}
