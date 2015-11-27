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
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.patient.impl;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.patient.PatientKPIValidationDao;

/**
 * @author mobileman
 *
 */
@Repository("patientKPIValidationDao")
public class PatientKeyPerformanceIndicatorValiationDaoImpl extends DaoImpl<PatientKeyPerformanceIndicatorValidation> implements PatientKPIValidationDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientKeyPerformanceIndicatorValiationDaoImpl.class);

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientKPIValidationDao#findValidatedValues(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long patientId, Long questionaryValidationTypeId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		List<PatientKeyPerformanceIndicatorValidation> result = (List)getHibernateTemplate().find(
				"select qv " +
				"from PatientKeyPerformanceIndicatorValidation qv " +
				"where qv.patient.id=? " +
				"and qv.keyPerformanceIndicatorType.id = ? " +
				"and date(qv.logDate) between date(?) and date(?)", 
				new Object[] { 
					patientId, 
					questionaryValidationTypeId, 
					startDate, 
					endDate });

		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientKPIValidationDao#findValidatedValues(java.lang.Long, java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long doctorId, Long patientId,
			Long questionaryValidationTypeId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		List<PatientKeyPerformanceIndicatorValidation> result = (List)getHibernateTemplate().find(
				"select qv " +
				"from PatientKeyPerformanceIndicatorValidation qv " +
				"where qv.patient.id=? " +
				"and qv.doctor.id=? " +
				"and qv.keyPerformanceIndicatorType.id = ? " +
				"and date(qv.logDate) between date(?) and date(?)", 
				new Object[] { 
					patientId, 
					doctorId,
					questionaryValidationTypeId, 
					startDate, 
					endDate });

		if (log.isDebugEnabled()) {
			log.debug("findValidatedValues(Long, Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
