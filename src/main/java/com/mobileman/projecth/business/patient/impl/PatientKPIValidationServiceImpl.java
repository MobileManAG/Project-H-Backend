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
 * PatientKeyPerformanceIndicatorServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient.impl;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.patient.PatientKPIValidationService;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;
import com.mobileman.projecth.persistence.doctor.DoctorDao;
import com.mobileman.projecth.persistence.patient.PatientDao;
import com.mobileman.projecth.persistence.patient.PatientKPIValidationDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.PATIENT_KPI_VALIDATION_SERVICE)
public class PatientKPIValidationServiceImpl extends BusinessServiceImpl<PatientKeyPerformanceIndicatorValidation>
		implements PatientKPIValidationService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientKPIValidationServiceImpl.class);
	
	private PatientKPIValidationDao patientKPIValidationDao;
	
	@Autowired
	private DoctorDao doctorDao;
	
	@Autowired
	private PatientDao patientDao;
	
	/**
	 * @param patientKPIValidationDao new value of patientKPIValidationDao
	 */
	@Autowired
	public void setPatientKPIValidationDao(PatientKPIValidationDao patientKPIValidationDao) {
		if (log.isDebugEnabled()) {
			log.debug("setPatientKPIValidationDao(PatientKPIValidationDao) - start"); //$NON-NLS-1$
		}

		this.patientKPIValidationDao = patientKPIValidationDao;
		setDao(patientKPIValidationDao);

		if (log.isDebugEnabled()) {
			log.debug("setPatientKPIValidationDao(PatientKPIValidationDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long patientId, Long kpiTypeId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		List<PatientKeyPerformanceIndicatorValidation> returnList = patientKPIValidationDao.findValidatedValues(patientId, kpiTypeId, startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientKPIValidationService#findValidatedValues(java.lang.Long, java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long doctorId, Long patientId,
			Long kpiTypeId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		List<PatientKeyPerformanceIndicatorValidation> returnList = patientKPIValidationDao.findValidatedValues(doctorId, patientId, kpiTypeId, startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal) - start"); //$NON-NLS-1$
		}

		PatientKeyPerformanceIndicatorValidation entity = new PatientKeyPerformanceIndicatorValidation();
		Patient patient = patientDao.findById(patientId);
		entity.setPatient(patient);
		entity.setLogDate(date);
		entity.setKeyPerformanceIndicatorType(kpi);
		entity.setValue(validationValue);
		entity.setDoctor(doctorDao.findById(doctorId));
		
		this.patientKPIValidationDao.save(entity);

		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue, List<BigDecimal> data) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal, List<BigDecimal>) - start"); //$NON-NLS-1$
		}

		PatientKeyPerformanceIndicatorValidation entity = new PatientKeyPerformanceIndicatorValidation();
		Patient patient = patientDao.findById(patientId);
		entity.setPatient(patient);
		entity.setLogDate(date);
		entity.setKeyPerformanceIndicatorType(kpi);
		entity.setValue(validationValue);
		entity.setDoctor(doctorDao.findById(doctorId));
		entity.setData(data);
		
		this.patientKPIValidationDao.save(entity);

		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal, List<BigDecimal>) - returns"); //$NON-NLS-1$
		}
	}

}
