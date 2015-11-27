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

package com.mobileman.projecth.business.patient.impl.kpi;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;
import com.mobileman.projecth.persistence.DiseaseDao;
import com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao;
import com.mobileman.projecth.persistence.patient.PatientDao;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;
import com.mobileman.projecth.persistence.questionary.AnswerDao;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
public abstract class KeyPerformanceIndicatorServiceBase implements KeyPerformanceIndicatorService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(KeyPerformanceIndicatorServiceBase.class);
	
	@Autowired
	private PatientKPIDao patientKPIDao;
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private KeyPerformanceIndicatorTypeDao keyPerformanceIndicatorTypeDao;
	
	@Autowired
	protected AnswerDao answerDao;
	
	@Autowired
	protected DiseaseDao diseaseDao;
	
	protected abstract KeyPerformanceIndicatorType.Type getKPIType();
	
	/**
	 * @param val3
	 * @param holder
	 * @param answer
	 * @return anser number value
	 */
	protected double getAnswerValue(double val3, PatientQuestionAnswerHolder holder, Answer answer) {
		if (answer.getQuestionType().getType().equals(QuestionType.Type.SCALE)) {
			val3 += new BigDecimal(holder.getCustomAnswer()).doubleValue();
		} else {
			val3 += new BigDecimal(answer.getAnswer()).doubleValue();
		}
		
		return val3;
	}
	
	/**
	 * @param answerId
	 * @param cache
	 * @return Answer
	 */
	protected Answer getAnswer(Long answerId, Map<Long, Answer> cache) {
		if (log.isDebugEnabled()) {
			log.debug("getAnswer(Long, Map<Long,Answer>) - start"); //$NON-NLS-1$
		}

		if (answerId == null) {
			if (log.isDebugEnabled()) {
				log.debug("getAnswer(Long, Map<Long,Answer>) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		
		Answer result = cache.get(answerId);
		if (result == null) {
			result = answerDao.findById(answerId);
			cache.put(answerId, result);
		}

		if (log.isDebugEnabled()) {
			log.debug("getAnswer(Long, Map<Long,Answer>) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	protected static String getDiseaseSubgroupCode(String diseaseCode) {
		if (log.isDebugEnabled()) {
			log.debug("getDiseaseSubgroupCode(String) - start"); //$NON-NLS-1$
		}

		diseaseCode = diseaseCode == null ? "" : diseaseCode;
		String[] codes = diseaseCode.split("[.]");		

		String returnString = codes.length == 0 ? "" : codes[0];
		if (log.isDebugEnabled()) {
			log.debug("getDiseaseSubgroupCode(String) - returns"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * @param diseaseCode
	 * @return true if given disease sub group code represents Rheuma 
	 */
	public static boolean isRheuma(String diseaseCode) {
		if (log.isDebugEnabled()) {
			log.debug("isRheuma(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = DiseaseCodes.RHEUMA_CODE.equals(diseaseCode) || DiseaseCodes.RHEUMA_CODE.startsWith(diseaseCode);
		if (log.isDebugEnabled()) {
			log.debug("isRheuma(String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/**
	 * @param diseaseCode
	 * @return true if given disease sub group code represents Psoriasis
	 */
	public static boolean isPsoriasis(String diseaseCode) {
		if (log.isDebugEnabled()) {
			log.debug("isPsoriasis(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = DiseaseCodes.PSORIASIS_CODE.equals(diseaseCode) || DiseaseCodes.PSORIASIS_CODE.startsWith(diseaseCode);
		if (log.isDebugEnabled()) {
			log.debug("isPsoriasis(String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/**
	 * @param diseaseCode
	 * @return true if given disease sub group code (disease code) represents Morbus Bechterew
	 */
	public static boolean isMorbusBechterew(String diseaseCode) {
		if (log.isDebugEnabled()) {
			log.debug("isMorbusBechterew(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = DiseaseCodes.MORBUS_BECHTEREW_CODE.equals(diseaseCode) || DiseaseCodes.MORBUS_BECHTEREW_CODE.startsWith(diseaseCode);
		if (log.isDebugEnabled()) {
			log.debug("isMorbusBechterew(String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/**
	 * @param patientId
	 * @param value
	 * @param logDate
	 */
	protected void savePatientKPIValue(Long patientId, Long diseaseId, int value, Date logDate) {
		if (log.isDebugEnabled()) {
			log.debug("savePatientKPIValue(Long, Long, int, Date) - start"); //$NON-NLS-1$
		}

		if (patientId != null) {
			KeyPerformanceIndicatorType kpit = keyPerformanceIndicatorTypeDao.find(getKPIType(), diseaseId);
			PatientKeyPerformanceIndicator pkpi = new PatientKeyPerformanceIndicator();
			pkpi.setPatient(patientDao.findById(patientId));
			pkpi.setTimestamp(new Date());
			pkpi.setLogDate(logDate);
			pkpi.setValue(new BigDecimal(value));
			pkpi.setKeyPerformanceIndicatorType(kpit);
			patientKPIDao.save(pkpi);
		}		

		if (log.isDebugEnabled()) {
			log.debug("savePatientKPIValue(Long, Long, int, Date) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param patientId
	 * @param value
	 * @param logDate
	 */
	protected void savePatientKPIValue(Long patientId, Long diseaseId, double value, Date logDate) {
		if (log.isDebugEnabled()) {
			log.debug("savePatientKPIValue(Long, Long, double, Date) - start"); //$NON-NLS-1$
		}

		if (patientId != null) {
			KeyPerformanceIndicatorType kpiCdai = keyPerformanceIndicatorTypeDao.find(getKPIType(), diseaseId);
			PatientKeyPerformanceIndicator pkpi = new PatientKeyPerformanceIndicator();
			pkpi.setPatient(patientDao.findById(patientId));
			pkpi.setTimestamp(new Date());
			pkpi.setLogDate(logDate);
			pkpi.setValue(new BigDecimal(value));
			pkpi.setKeyPerformanceIndicatorType(kpiCdai);
			patientKPIDao.save(pkpi);
		}		

		if (log.isDebugEnabled()) {
			log.debug("savePatientKPIValue(Long, Long, double, Date) - returns"); //$NON-NLS-1$
		}
	}
}
