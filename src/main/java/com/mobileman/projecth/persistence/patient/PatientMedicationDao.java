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
 * PatientMedicationDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.patient;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.medication.PatientMedication;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link PatientMedication} entity.
 * 
 * @author mobileman
 * 
 */
public interface PatientMedicationDao extends Dao<PatientMedication> {

	/**
	 * Finds all medications consumed by given patient (consumed medication
	 * history)
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return all medications consumed by given patient (consumed medication
	 *         history)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	List<Medication> findConsumedMedications(Long patientId, Long diseaseId)
			throws IllegalArgumentException;

	/**
	 * Computes patient's consumed madications diary based on date
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return {@link Date}, ID of a {@link Medication} and amount ({@link Integer}) of
	 *         given medication in given day (count of medication's standard
	 *         unit size)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	List<Object[]> findPatientConsumedMedicationsDiary(Long patientId,
			Long diseaseId, Date startDate, Date endDate)
			throws IllegalArgumentException;
	
	/**
	 * Computes patient's consumed madications diary based on date
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return {@link Date}, ID of a {@link Medication} and amount ({@link Integer}) of
	 *         given medication in given day (count of medication's standard
	 *         unit size)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	List<Object[]> findPatientConsumedMedicationsDiary(Long patientId, Date startDate, Date endDate)
			throws IllegalArgumentException;
	
	/**
	 * Finds all medications consumed by patient
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return all medications consumed by patient
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	List<PatientMedication> findAllForDisease(Long patientId, Long diseaseId)
			throws IllegalArgumentException;
	
	/**
	 * Finds all medications consumed by patient Id, Date, Name, Amount)
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return all medications consumed by patient (Id, Date, Name, Amount)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	List<Object[]> findAllConsumedMedicationsForDisease(Long patientId, Long diseaseId)
			throws IllegalArgumentException;
}
