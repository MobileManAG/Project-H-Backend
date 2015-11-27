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
 * PatientMedicationServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.impl;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.PatientMedicationService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.medication.PatientMedication;
import com.mobileman.projecth.domain.util.medication.MedicationFrequency;
import com.mobileman.projecth.persistence.DiseaseDao;
import com.mobileman.projecth.persistence.MedicationDao;
import com.mobileman.projecth.persistence.patient.PatientDao;
import com.mobileman.projecth.persistence.patient.PatientMedicationDao;

/**
 * Implements {@link PatientMedicationService}
 * @author mobileman
 */
@Service(ComponentNames.PATIENT_MEDICATION_SERVICE)
public class PatientMedicationServiceImpl extends BusinessServiceImpl<PatientMedication> implements PatientMedicationService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientMedicationServiceImpl.class);
	
	@Autowired
	private PatientDao patientDao;
		
	@Autowired
	private MedicationDao medicationDao;
	
	@Autowired
	private DiseaseDao diseaseDao;
	
	private PatientMedicationDao patientMedicationDao;
	
	/**
	 * Setter for patientMedicationDao
	 *
	 * @param patientMedicationDao new value of patientMedicationDao
	 */
	@Autowired
	public void setPatientMedicationDao(
			PatientMedicationDao patientMedicationDao) {
		if (log.isDebugEnabled()) {
			log.debug("setPatientMedicationDao(PatientMedicationDao) - start"); //$NON-NLS-1$
		}

		super.setDao(patientMedicationDao);
		this.patientMedicationDao = patientMedicationDao;

		if (log.isDebugEnabled()) {
			log.debug("setPatientMedicationDao(PatientMedicationDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#addConsumedMedication(Long, Long, Long, double, Date, String)
	 */
	@Override
	public void addConsumedMedication(Long patientId, Long diseaseId,
			Long medicationId, double standarUnitsTaken, Date consumptionDate, String comment)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(" + patientId + ", " + diseaseId + ", " + medicationId + ", " 
					+ standarUnitsTaken + ", " + consumptionDate + ", " + comment + ") - start"); //$NON-NLS-1$
		}
		
		addConsumedMedication(patientId, diseaseId, medicationId, standarUnitsTaken, MedicationFrequency.ONE_TIME,
				consumptionDate, null, comment);

		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(Long, Long, Long, double, Date, String) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#findAllConsumedMedications(Long, Long)
	 */
	@Override
	public List<Medication> findAllConsumedMedications(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAllConsumedMedications(Long, Long) - start"); //$NON-NLS-1$
		}

		List<Medication> returnList = this.patientMedicationDao.findConsumedMedications(patientId, diseaseId);
		if (log.isDebugEnabled()) {
			log.debug("findAllConsumedMedications(Long, Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#findConsumedMedicationsDiary(Long, Long, Date, Date)
	 */
	@Override
	public List<Object[]> findConsumedMedicationsDiary(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedicationsDiary(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = this.patientMedicationDao.findPatientConsumedMedicationsDiary(patientId, diseaseId, startDate, endDate);
		for (Object[] row : result) {
			row[1] = medicationDao.findById(Long.class.cast(row[1]));
			row[2] = new Long(Number.class.cast(row[2]).longValue());
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedicationsDiary(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#findConsumedMedicationsDiary(Long, Long, Date, Date)
	 */
	@Override
	public List<Object[]> findConsumedMedicationsDiary(Long patientId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedicationsDiary(" + patientId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = this.patientMedicationDao.findPatientConsumedMedicationsDiary(patientId, startDate, endDate);
		for (Object[] row : result) {
			row[1] = medicationDao.findById(Long.class.cast(row[1]));
			row[2] = new Long(Number.class.cast(row[2]).longValue());
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedicationsDiary(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#findAllForDisease(Long, Long)
	 */
	@Override
	public List<PatientMedication> findAllForDisease(Long patientId, Long diseaseId) throws IllegalArgumentException {
		return this.patientMedicationDao.findAllForDisease(patientId, diseaseId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#findAllConsumedMedicationsForDisease(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Object[]> findAllConsumedMedicationsForDisease(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		return this.patientMedicationDao.findAllConsumedMedicationsForDisease(patientId, diseaseId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientMedicationService#addConsumedMedication(java.lang.Long, java.lang.Long, java.lang.Long, double, com.mobileman.projecth.domain.util.medication.MedicationFrequency, java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public void addConsumedMedication(Long patientId, Long diseaseId, Long medicationId, double standarUnitsTaken,
			MedicationFrequency frequency, Date startDate, Date endDate, String comment)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(" + patientId + ", " + diseaseId + ", " + medicationId + ", " 
					+ standarUnitsTaken + ", " + frequency + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId must not be null");
		}
		
		if (diseaseId == null) {
			throw new IllegalArgumentException("diseaseId must not be null");
		}
		
		if (medicationId == null) {
			throw new IllegalArgumentException("medicationId must not be null");
		}
		
		if (standarUnitsTaken <= 0.0d) {
			throw new IllegalArgumentException("standarUnitsTaken must not be zero or negative value");
		}
		
		if (frequency == null) {
			throw new IllegalArgumentException("frequency must not be null");
		}
		
		if (endDate == null) {
			endDate = startDate;
		}
		
		Patient patient = patientDao.findById(patientId);
		patient.setLastUpdate(new Date());
		Medication medication = medicationDao.findById(medicationId);
		Disease disease = diseaseDao.findById(diseaseId);
		
		Date date = startDate;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		Date timestamp = new Date();
		while (date.before(endDate) || date.equals(endDate)) {
			PatientMedication patientMedication = new PatientMedication();
			patientMedication.setTimestamp(timestamp);
			patientMedication.setPatient(patient);
			patientMedication.setMedication(medication);
			patientMedication.setComment(comment);
			patientMedication.setConsumptionDate(date);
			patientMedication.setDisease(disease);
			patientMedication.setAmount(new BigDecimal(standarUnitsTaken));
			patientMedicationDao.save(patientMedication);
			
			if (frequency.equals(MedicationFrequency.ONE_TIME)) {
				break;
			}
			
			switch (frequency) {
			case DAILY:
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				break;
			case WEEKLY:
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
				break;
			case BI_WEEKLY:
				calendar.add(Calendar.WEEK_OF_YEAR, 2);
				break;
			default:
				break;
			}
			
			date = calendar.getTime();
		}

		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(Long, Long, Long, double) - returns"); //$NON-NLS-1$
		}
	}

}
