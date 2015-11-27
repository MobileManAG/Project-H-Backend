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
 * PatientQuestionAnswerServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.ImageService;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.patient.PatientQuestionAnswerService;
import com.mobileman.projecth.business.patient.impl.kpi.BASDAIKPIPrecomputationService;
import com.mobileman.projecth.business.patient.impl.kpi.CDAIKPIPrecomputationService;
import com.mobileman.projecth.business.patient.impl.kpi.PASIKPIPrecomputationService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;
import com.mobileman.projecth.persistence.patient.PatientDao;
import com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao;
import com.mobileman.projecth.persistence.questionary.QuestionDao;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.PATIENT_QUESTION_ANSWER_SERVICE)
public class PatientQuestionAnswerServiceImpl extends BusinessServiceImpl<PatientQuestionAnswer> implements PatientQuestionAnswerService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientQuestionAnswerServiceImpl.class);
	
	private PatientQuestionAnswerDao patientQuestionAnswerDao;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private CDAIKPIPrecomputationService cdaikpiPrecomputationService;
	
	@Autowired
	private PASIKPIPrecomputationService pasikpiPrecomputationService;
	
	@Autowired
	private BASDAIKPIPrecomputationService basdaikpiPrecomputationService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	/**
	 * @param patientQuestionAnswerDao  
	 */
	@Autowired
	public void setPatientQuestionAnswerDao(PatientQuestionAnswerDao patientQuestionAnswerDao) {
		if (log.isDebugEnabled()) {
			log.debug("setPatientQuestionAnswerDao(PatientQuestionAnswerDao) - start"); //$NON-NLS-1$
		}

		this.patientQuestionAnswerDao = patientQuestionAnswerDao;
		super.setDao(patientQuestionAnswerDao);		

		if (log.isDebugEnabled()) {
			log.debug("setPatientQuestionAnswerDao(PatientQuestionAnswerDao) - returns"); //$NON-NLS-1$
		}
	}

	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientQuestionAnswerService#saveAnswer(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.util.Date)
	 */
	@Override
	public Long saveAnswer(Long patientId, Long questionId, Long answerId, String customAnswer, Date logDate) {
		if (log.isDebugEnabled()) {
			log.debug("saveAnswer(Long, Long, Long, String, Date) - start"); //$NON-NLS-1$
		}
		
		if (patientId == null) {
			throw new IllegalArgumentException("patientId must not be null");
		}
		
		if (questionId == null) {
			throw new IllegalArgumentException("questionId must not be null, patientId=" + patientId);
		}

		if (answerId == null) {
			throw new IllegalArgumentException("answerId must not be null, questionId=" + questionId + ", patientId=" + patientId);
		}
		
		Patient patient = patientDao.findById(patientId);
		Question question = questionDao.findById(questionId);
		Answer answer = questionDao.findAnswerById(answerId);
		
		if (!Question.Kind.CUSTOM.equals(question.getKind()) 
				&& Haq.Kind.ONE_TIME.equals(question.getHaq().getKind())) {
			patientQuestionAnswerDao.removeOneTimeQuestionAnswers(patientId, questionId);
		}
		
		if (QuestionType.AnswerDataType.IMAGE.equals(question.getQuestionType().getAnswerDataType())) {
			if (answer.isActive()) {
				String relativePath = patientId + File.separator + question.getHaq().getDisease().getId();
				String relativeFileFullPath = imageService.copyImage(customAnswer, relativePath);
				customAnswer = relativeFileFullPath;
			} else {
				customAnswer = null;
			}
		}
		
		PatientQuestionAnswer pqa = new PatientQuestionAnswer();
		pqa.setAnswer(answer);
		pqa.setCustomAnswer(customAnswer);
		pqa.setLogDate(logDate);
		pqa.setPatient(patient);
		pqa.setQuestion(question);
		
		Long id = getDao().save(pqa);
		

		if (log.isDebugEnabled()) {
			log.debug("saveAnswer(Long, Long, Long, String, Date) - returns"); //$NON-NLS-1$
		}
		return id;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientQuestionAnswerService#exportData(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionAnswer> exportData(Long haqId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("exportData(Long, Date, Date) - start"); //$NON-NLS-1$
		}

		List<PatientQuestionAnswer> result = patientQuestionAnswerDao.find(haqId, startDate, endDate);

		if (log.isDebugEnabled()) {
			log.debug("exportData(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientQuestionAnswerService#saveAnswers(java.lang.Long, java.lang.Long, java.util.Date, java.util.List)
	 */
	@Override
	public Long saveAnswers(Long patientId, Long diseaseId, Date logDate, List<PatientQuestionAnswerHolder> answers) {
		
		for (PatientQuestionAnswerHolder holder : answers) {
			saveAnswer(patientId, holder.getQuestionId(), holder.getAnswerId(), holder.getCustomAnswer(), logDate);
		}
		
		Patient patient = this.patientDao.findById(patientId);
		Disease disease = diseaseService.findById(diseaseId);
		if (disease != null) {
			if (DiseaseCodes.RHEUMA_CODE.equals(disease.getCode())
					|| DiseaseCodes.RHEUMA_CODE.startsWith(disease.getCode())) {
				cdaikpiPrecomputationService.processAnswers(patient, disease, logDate, answers);
			} else if (DiseaseCodes.PSORIASIS_CODE.equals(disease.getCode())
					|| DiseaseCodes.PSORIASIS_CODE.startsWith(disease.getCode())) {
				pasikpiPrecomputationService.processAnswers(patient, disease, logDate, answers);
			} else if (DiseaseCodes.MORBUS_BECHTEREW_CODE.equals(disease.getCode())
					|| DiseaseCodes.MORBUS_BECHTEREW_CODE.startsWith(disease.getCode())) {
				basdaikpiPrecomputationService.processAnswers(patient, disease, logDate, answers);
			}
		}
		
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.PatientQuestionAnswerService#findAll(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionAnswer> findAll(Long userId, Long questionId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(" + userId + ", " + questionId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		List<PatientQuestionAnswer> result = patientQuestionAnswerDao.findAll(userId, questionId, startDate, endDate);

		if (log.isDebugEnabled()) {
			log.debug("findAll(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
}
