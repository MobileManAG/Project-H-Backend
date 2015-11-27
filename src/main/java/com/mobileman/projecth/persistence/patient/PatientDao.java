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
package com.mobileman.projecth.persistence.patient;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link Patient} entity.
 * 
 * @author mobileman
 *
 */
public interface PatientDao extends Dao<Patient> {

	/**
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return computed CDAI value
	 */
	double computeCDAI(Long patientId, Date startDate, Date endDate);
	
	/**
	 * Dynamicaly computes list of patient's CDAI for each day in given time interval. 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return list of patient's CDAI for each day in given time interval. 
	 */
	List<Object[]> computeCDAITimeline(Long patientId, Date startDate, Date endDate);
	
	/**
	 * @return all patients with account (not deleted)
	 */
	List<Patient> findAllPatientsWithAccounts();
}
