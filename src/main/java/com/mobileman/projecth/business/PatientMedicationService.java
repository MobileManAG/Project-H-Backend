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
 * PatientMedicationService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.medication.PatientMedication;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.util.medication.MedicationFrequency;

/**
 * Declares business layer for the consumed {@link Medication} by the
 * {@link Patient} ({@link PatientMedication})
 * 
 * @author mobileman
 * 
 */
public interface PatientMedicationService extends
		EntityService<PatientMedication>, SearchService<PatientMedication> {

	/**
	 * <code>UC5 Medication</code> Creates an association between the usage of
	 * medication and given patient associated disease
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param medicationId
	 * @param standarUnitsTaken
	 * @param consumptionDate 
	 * @param comment 
	 * @throws IllegalArgumentException
	 *             <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             medicationId == null</li> <li>standarUnitsTaken less or equal
	 *             zero</li>
	 */
	void addConsumedMedication(Long patientId, Long diseaseId,
			Long medicationId, double standarUnitsTaken, Date consumptionDate, String comment)
			throws IllegalArgumentException;
	
	/**
	 * <code>UC5 Medication</code> Creates an association between the usage of
	 * medication and given patient associated disease
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param medicationId
	 * @param standarUnitsTaken
	 * @param frequency 
	 * @param startDate 
	 * @param endDate 
	 * @param comment 
	 * @throws IllegalArgumentException
	 *             <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             medicationId == null</li> <li>standarUnitsTaken less or equal
	 *             zero</li>
	 */
	void addConsumedMedication(Long patientId, Long diseaseId,
			Long medicationId, double standarUnitsTaken, MedicationFrequency frequency, Date startDate, Date endDate, String comment)
			throws IllegalArgumentException;

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
	List<Medication> findAllConsumedMedications(Long patientId, Long diseaseId)
			throws IllegalArgumentException;

	/**
	 * Computes patient's consumed madications diary based on date
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return {@link Date}, {@link Medication} and amount ({@link Integer}) of
	 *         given medication in given day (count of medication's standard
	 *         unit size)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	List<Object[]> findConsumedMedicationsDiary(Long patientId,
			Long diseaseId, Date startDate, Date endDate)
			throws IllegalArgumentException;
	
	/**
	 * Computes patient's consumed madications diary based on date
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return {@link Date}, {@link Medication} and amount ({@link Integer}) of
	 *         given medication in given day (count of medication's standard
	 *         unit size)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	List<Object[]> findConsumedMedicationsDiary(Long patientId,
			Date startDate, Date endDate)
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
