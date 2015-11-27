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
import com.mobileman.projecth.business.patient.PatientKPIService;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.persistence.DiseaseDao;
import com.mobileman.projecth.persistence.index.IndexDao;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;
import com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.PATIENT_KPI_SERVICE)
public class PatientKPIServiceImpl extends BusinessServiceImpl<PatientKeyPerformanceIndicator>
		implements PatientKPIService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientKPIServiceImpl.class);
	
	
	@Autowired
	private IndexDao<KeyPerformanceIndicatorType> indexDao;
	
	@Autowired
	private PatientQuestionAnswerDao patientQuestionAnswerDao;
	
	@Autowired
	private DiseaseDao diseaseDao;
	
	@SuppressWarnings("unused")
	private PatientKPIDao patientKPIDao;
	
	/**
	 * @param patientKPIDao new value of patientKPIDao
	 */
	@Autowired
	public void setPatientKPIDao(PatientKPIDao patientKPIDao) {
		if (log.isDebugEnabled()) {
			log.debug("setPatientKPIDao(PatientKPIDao) - start"); //$NON-NLS-1$
		}

		this.patientKPIDao = patientKPIDao;
		setDao(patientKPIDao);

		if (log.isDebugEnabled()) {
			log.debug("setPatientKPIDao(PatientKPIDao) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientKPIService#computeKPI(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public BigDecimal computeKPI(Long patientId, Long kpiTypeId, Date startDate, Date endDate)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("computeKPI(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		KeyPerformanceIndicatorType kpiType = indexDao.findById(KeyPerformanceIndicatorType.class, kpiTypeId);
		
		final BigDecimal result;
		switch (kpiType.getType()) {
		case BASDAI:{
			result = patientQuestionAnswerDao.computeBASDAI(patientId, kpiType.getDisease().getId(), startDate, endDate);
		}
			break;
		case CDAI: {
			result = patientQuestionAnswerDao.computeCDAI(patientId, kpiType.getDisease().getId(), startDate, endDate);
		}			
			break;
		default:
			result = null;
			break;
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeKPI(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientKPIService#computeKPITimeline(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<KeyPerformanceIndicatorStatistics> computeKPITimeline(Long patientId, Long kpiTypeId, Date startDate,
			Date endDate) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("computeKPITimeline(" + patientId + ", " + kpiTypeId + ", " + startDate + ", " + endDate + ") - "); //$NON-NLS-1$
		}
		
		KeyPerformanceIndicatorType kpiType = indexDao.findById(KeyPerformanceIndicatorType.class, kpiTypeId);
		
		final List<KeyPerformanceIndicatorStatistics> result;
		switch (kpiType.getType()) {
		case BASDAI:{
			result = patientQuestionAnswerDao.computeBASDAITimeline(patientId, kpiType.getDisease().getId(), startDate, endDate);
		}
			break;
		case CDAI: {
			result = patientQuestionAnswerDao.computeCDAITimeline(patientId, kpiType.getDisease().getId(), startDate, endDate);
		}			
			break;
		case PASI:{
			result = patientQuestionAnswerDao.computePASITimeline(patientId, kpiType.getDisease().getId(), startDate, endDate);
		}
			break;
		default:
			result = null;
			break;
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeKPITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientKPIService#computeKPIAverageTimeline(Long, Date, Date)
	 */
	@Override
	public List<KeyPerformanceIndicatorStatistics> computeKPIAverageTimeline(Long kpiTypeId, Date startDate,
			Date endDate) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("computeKPITimeline(" + kpiTypeId + ", " + startDate + ", " + endDate + ") - "); //$NON-NLS-1$
		}
		
		KeyPerformanceIndicatorType kpiType = indexDao.findById(KeyPerformanceIndicatorType.class, kpiTypeId);
		
		final List<KeyPerformanceIndicatorStatistics> result;
		switch (kpiType.getType()) {
		case BASDAI:{
			result = patientQuestionAnswerDao.computeBASDAIAverageTimeline(kpiType.getDisease().getId(), startDate, endDate);
		}
			break;
		case CDAI: {
			result = patientQuestionAnswerDao.computeCDAIAverageTimeline(kpiType.getDisease().getId(), startDate, endDate);
		}			
			break;
		case PASI:{
			result = patientQuestionAnswerDao.computePASIAverageTimeline(kpiType.getDisease().getId(), startDate, endDate);
		}
			break;
		default:
			result = null;
			break;
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeKPITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
}
