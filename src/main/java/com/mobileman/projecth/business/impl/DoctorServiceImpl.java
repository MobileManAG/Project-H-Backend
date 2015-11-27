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
package com.mobileman.projecth.business.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.DoctorService;
import com.mobileman.projecth.business.patient.PatientKPIValidationService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.persistence.UserDao;
import com.mobileman.projecth.persistence.doctor.DoctorDao;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;
import com.mobileman.projecth.persistence.questionary.QuestionDao;

/**
 *
 */
@Service(ComponentNames.DOCTOR_SERVICE)
public class DoctorServiceImpl extends BusinessServiceImpl<Doctor> implements DoctorService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(DoctorServiceImpl.class);
	
	private DoctorDao doctorDao;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	private QuestionTypeService questionTypeService;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PatientKPIValidationService patientKPIValidationService;
	
	@Autowired
	private PatientKPIDao patientKPIDao;

	/**
	 * @param doctorDao
	 */
	@Autowired
	public void setDoctorDao(DoctorDao doctorDao) {
		if (log.isDebugEnabled()) {
			log.debug("setDoctorDao(DoctorDao) - start"); //$NON-NLS-1$
		}

		this.doctorDao = doctorDao;
		setDao(doctorDao);

		if (log.isDebugEnabled()) {
			log.debug("setDoctorDao(DoctorDao) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#addCustomQuestion(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Long addCustomQuestion(Long doctorId, Long patientId, Long diseaseId, String questionText, String explanation,
			Long questionTypeId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(Long, Long, Long, String, Long) - start"); //$NON-NLS-1$
		}

		Doctor doctor = findById(doctorId);
		Disease disease = diseaseService.findById(diseaseId);
		QuestionType questionType = questionTypeService.findById(questionTypeId);
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText(questionText);
		customQuestion.setDisease(disease);
		customQuestion.setUser(userDao.findById(patientId));
		customQuestion.setCreator(doctor);
		customQuestion.setQuestionType(questionType);
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setSortOrder(0);
		customQuestion.setExplanation(explanation);
		
		Long id = questionDao.save(customQuestion);

		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(Long, Long, Long, String, Long) - returns"); //$NON-NLS-1$
		}
		return id;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#saveValidatedKpiValue(java.lang.Long, java.lang.Long, java.util.Date, com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType, java.math.BigDecimal)
	 */
	@Override
	public void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal) - start"); //$NON-NLS-1$
		}
		
		this.patientKPIValidationService.saveValidatedKpiValue(doctorId, patientId, date, kpi, validationValue);

		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#saveValidatedKpiValue(java.lang.Long, java.lang.Long, java.util.Date, com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType, java.math.BigDecimal, java.util.List)
	 */
	@Override
	public void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue, List<BigDecimal> data) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal, List<BigDecimal>) - start"); //$NON-NLS-1$
		}

		this.patientKPIValidationService.saveValidatedKpiValue(doctorId, patientId, date, kpi, validationValue, data);

		if (log.isDebugEnabled()) {
			log.debug("saveValidatedKpiValue(Long, Long, Date, KeyPerformanceIndicatorType, BigDecimal, List<BigDecimal>) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findPatientsCountByDisease(java.lang.Long)
	 */
	@Override
	public List<Object[]> findPatientsCountByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByDisease(Long) - start"); //$NON-NLS-1$
		}

		List<Object[]> returnList = doctorDao.findPatientsCountByDisease(doctorId);
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findPatientsCountByGenderByDisease(java.lang.Long)
	 */
	@Override
	public List<Object[]> findPatientsCountByGenderByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByGenderByDisease(Long) - start"); //$NON-NLS-1$
		}

		List<Object[]> returnList = doctorDao.findPatientsCountByGenderByDisease(doctorId);
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByGenderByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findPatientsCountByAgeByDisease(java.lang.Long)
	 */
	@Override
	public List<Object[]> findPatientsCountByAgeByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByAgeByDisease(Long) - start"); //$NON-NLS-1$
		}

		List<Object[]> returnList = doctorDao.findPatientsCountByAgeByDisease(doctorId);
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByAgeByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findPatientsKpiAverageScoreTimelineByDisease(Long, Date, Date)
	 */
	@Override
	public List<KeyPerformanceIndicatorStatistics> findPatientsKpiAverageScoreTimelineByDisease(Long doctorId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreTimelineByDisease(" + doctorId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorStatistics> result = patientKPIDao.findPatientsKpiAverageScoreTimelineByDisease(doctorId, startDate, endDate);
		
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreTimelineByDisease(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findPatientsKpiAverageScoreByDisease(java.lang.Long)
	 */
	@Override
	public List<Object[]> findPatientsKpiAverageScoreByDisease(Long doctorId) {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreByDisease(Long) - start"); //$NON-NLS-1$
		}

		List<Object[]> returnList = patientKPIDao.findPatientsKpiAverageScoreByDisease(doctorId);
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#addCustomQuestion(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, com.mobileman.projecth.domain.questionary.QuestionType)
	 */
	@Override
	public Long addCustomQuestion(Long doctorId, Long patientId, Long diseaseId, String text,
			String explanation, QuestionType questionType) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(" + patientId + ", " + diseaseId + ", " + text + ", " 
					+ explanation + ", " + questionType + ") - start"); //$NON-NLS-1$
		}
		
		if (doctorId == null) {
			throw new IllegalArgumentException("doctorId is undefined");
		}
		
		if (patientId == null) {
			throw new IllegalArgumentException("patientId is undefined");
		}
		
		if (diseaseId == null) {
			throw new IllegalArgumentException("diseaseId is undefined");
		}
		
		if (text == null || text.trim().length() == 0) {
			throw new IllegalArgumentException("text is undefined");
		}
		
		if (questionType == null) {
			throw new IllegalArgumentException("QuestionType is undefined");
		}

		User doctor = findById(doctorId);
		User patient = findById(patientId);
		Disease disease = diseaseService.findById(diseaseId);
		
		questionTypeService.save(doctorId, questionType);
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText(text);
		customQuestion.setDisease(disease);
		customQuestion.setUser(patient);
		customQuestion.setCreator(doctor);
		customQuestion.setQuestionType(questionType);
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setSortOrder(0);
		customQuestion.setExplanation(explanation);
		
		Long customQuestionId = questionDao.save(customQuestion);

		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(Long, Long, String, String, QuestionType) - returns: " + customQuestionId); //$NON-NLS-1$
		}
		
		return customQuestionId;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DoctorService#findAllDoctorsWithAccounts()
	 */
	@Override
	public List<Doctor> findAllDoctorsWithAccounts() {
		return doctorDao.findAllDoctorsWithAccounts();
	}

}
