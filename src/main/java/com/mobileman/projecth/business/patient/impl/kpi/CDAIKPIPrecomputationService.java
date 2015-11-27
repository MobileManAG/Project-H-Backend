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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;
import com.mobileman.projecth.domain.util.questionary.RheumaQuestionaryUtil;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;

/**
 * Service that computes patient's CDAI value based on a list of patient answers
 * @author mobileman
 *
 */
@Service
public class CDAIKPIPrecomputationService extends KeyPerformanceIndicatorServiceBase implements KeyPerformanceIndicatorService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(CDAIKPIPrecomputationService.class);
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorServiceBase#getKPIType()
	 */
	@Override
	protected Type getKPIType() {
		return Type.CDAI;
	}


	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorService#process(com.mobileman.projecth.domain.patient.Patient, com.mobileman.projecth.domain.disease.Disease, java.util.List)
	 */
	private void process(Patient patient, Disease disease, List<ProjectHPatientDailyPost> posts) {
		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - start"); //$NON-NLS-1$
		}

		if (posts == null || posts.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
			}
			return;
		}
		
		int cdai = 0;
		Date logDate = null;
		for (ProjectHPatientDailyPost postItem : posts) {
			if (logDate == null) {
				logDate = postItem.getLogDate();
			}
			
			Long[] questionsId = postItem.getQuestionsId();
			Long[] answersId = postItem.getAnswersId();
			
			if (postItem.getHaqId().equals(RheumaQuestionaryUtil.getSwellungId())
					|| postItem.getHaqId().equals(RheumaQuestionaryUtil.getDruckempflindlichkeitId())) {
				for (Long answerId : answersId) {
					Answer answer = answerDao.findById(answerId);
					if (answer.isActive()) {
						++cdai;
					}
				}
			}
			
			if (postItem.getHaqId().equals(RheumaQuestionaryUtil.getAllgemeinUrteilId())) {
				for (int i = 0; i < questionsId.length; i++) {
					for (Long answerId : answersId) {
						Answer answer = answerDao.findById(answerId);
						if (answer.isActive()) {
							cdai += Integer.valueOf(answer.getAnswer());
						}
					}
				}
			}
		}
		
		savePatientKPIValue(patient.getId(), disease.getId(), cdai, logDate);

		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}


	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorService#processAnswers(com.mobileman.projecth.domain.patient.Patient, com.mobileman.projecth.domain.disease.Disease, java.util.Date, java.util.List)
	 */
	@Override
	public void processAnswers(Patient patient, Disease disease, Date logDate, List<PatientQuestionAnswerHolder> answers) {
		if (log.isDebugEnabled()) {
			log.debug("processAnswers(Patient, Disease, List<projecthPatientDailyPost>) - start"); //$NON-NLS-1$
		}

		if (answers == null || answers.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
			}
			return;
		}
		
		int cdai = 0;
		for (PatientQuestionAnswerHolder postItem : answers) {
			if (postItem.getHaqId() != null) {
				if (postItem.getHaqId().equals(RheumaQuestionaryUtil.getSwellungId())
						|| postItem.getHaqId().equals(RheumaQuestionaryUtil.getDruckempflindlichkeitId())) {
					if (answerDao.findById(postItem.getAnswerId()).isActive()) {
						++cdai;
					}
				}
			}			
		}
			
		for (PatientQuestionAnswerHolder postItem : answers) {
			if (postItem.getHaqId() != null) {
				if (postItem.getHaqId() != null && postItem.getHaqId().equals(RheumaQuestionaryUtil.getAllgemeinUrteilId())) {
					Answer answer = answerDao.findById(postItem.getAnswerId());
					if (answer.isActive()) {
						cdai += getAnswerValue(cdai, postItem, answer);
					}
				}
			}			
		}
		
		savePatientKPIValue(patient.getId(), disease.getId(), cdai, logDate);

		if (log.isDebugEnabled()) {
			log.debug("processAnswers(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}
}
