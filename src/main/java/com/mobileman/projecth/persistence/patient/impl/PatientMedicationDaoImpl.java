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
 * PatientMedicationDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.patient.impl;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.medication.PatientMedication;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.patient.PatientMedicationDao;

/**
 * @author mobileman
 * 
 */
@Repository("patientMedicationDao")
public class PatientMedicationDaoImpl extends DaoImpl<PatientMedication>
		implements PatientMedicationDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientMedicationDaoImpl.class);
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientMedicationDao#findConsumedMedications(Long, Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Medication> findConsumedMedications(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedications(Long, Long) - start"); //$NON-NLS-1$
		}
		
		List<Medication> result = (List)getHibernateTemplate().find(
				"select distinct pm.medication " +
				"from PatientMedication " +
				"pm where pm.patient.id = ? " +
				"and pm.disease.id = ?", 
				new Object[]{ patientId, diseaseId });
		

		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedications(Long, Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientMedicationDao#findPatientConsumedMedicationsDiary(Long, Long, Date, Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findPatientConsumedMedicationsDiary(Long patientId, Long diseaseId,
			Date startDate, Date endDate) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientConsumedMedicationsDiary(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId id must not be null ");
		}
		
		if (startDate == null) {
			throw new IllegalArgumentException("startDate id must not be null ");
		}
		
		if (endDate == null) {
			throw new IllegalArgumentException("endDate id must not be null ");
		}
		
		final List<Object[]> result;
		if (diseaseId != null) {
			result = (List)getHibernateTemplate().find(
					"select cast(pm.consumptionDate as date), med.id, sum(pm.amount) " +
					"from PatientMedication pm " +
					"join pm.medication med " +
					"join pm.disease dis " +
					"where pm.patient.id = ? and dis.id = ? " +
					"and cast(pm.consumptionDate as date) between ? and ? " +
					"group by cast(pm.consumptionDate as date), med.id " +
					"order by 1", 
					new Object[]{ patientId, diseaseId, startDate, endDate });
		} else {
			result = (List)getHibernateTemplate().find(
					"select cast(pm.consumptionDate as date), med.id, sum(pm.amount) " +
					"from PatientMedication pm " +
					"join pm.medication med " +
					"join pm.disease dis " +
					"where pm.patient.id = ? " +
					"and cast(pm.consumptionDate as date) between ? and ? " +
					"group by cast(pm.consumptionDate as date), med.id " +
					"order by 1", 
					new Object[]{ patientId, startDate, endDate });
		}	

		if (log.isDebugEnabled()) {
			log.debug("findPatientConsumedMedicationsDiary(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientMedicationDao#findPatientConsumedMedicationsDiary(Long, Long, Date, Date)
	 */
	@Override
	public List<Object[]> findPatientConsumedMedicationsDiary(Long patientId,
			Date startDate, Date endDate) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientConsumedMedicationsDiary(" + patientId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = findPatientConsumedMedicationsDiary(patientId, null, startDate, endDate);
		

		if (log.isDebugEnabled()) {
			log.debug("findPatientConsumedMedicationsDiary(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientMedicationDao#findAllForDisease(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PatientMedication> findAllForDisease(Long patientId, Long diseaseId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAllForDisease(" + patientId + ", " + diseaseId + ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId id must not be null ");
		}
		
		if (diseaseId == null) {
			throw new IllegalArgumentException("diseaseId id must not be null ");
		}
		
		List<PatientMedication> result = (List)getHibernateTemplate().find(
				"select pm " +
				"from PatientMedication pm " +
				"join pm.disease dis " +
				"where pm.patient.id = ? and dis.id = ? " +
				"order by pm.timestamp desc", 
				new Object[]{ patientId, diseaseId });
		

		if (log.isDebugEnabled()) {
			log.debug("findAllForDisease(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientMedicationDao#findAllConsumedMedicationsForDisease(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findAllConsumedMedicationsForDisease(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAllForDisease(" + patientId + ", " + diseaseId + ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId id must not be null ");
		}
		
		if (diseaseId == null) {
			throw new IllegalArgumentException("diseaseId id must not be null ");
		}
		
		List<Object[]> result = (List)getHibernateTemplate().find(
				"select pm.id, pm.consumptionDate, med.name, pm.amount " +
				"from PatientMedication pm " +
				"join pm.disease dis " +
				"join pm.medication med " +
				"where pm.patient.id = ? and dis.id = ? " +
				"order by pm.consumptionDate desc", 
				new Object[]{ patientId, diseaseId });
		

		if (log.isDebugEnabled()) {
			log.debug("findAllForDisease(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
