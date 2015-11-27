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
 * PatientPASIService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 19.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient.impl.kpi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;
import com.mobileman.projecth.domain.util.questionary.MorbusBechterewQuestionaryUtil;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;

/**
 * Service that precomputes patient's PASI value based on a list of patient answers
 * @author mobileman
 *
 */
@Service
public class BASDAIKPIPrecomputationService extends KeyPerformanceIndicatorServiceBase implements KeyPerformanceIndicatorService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(BASDAIKPIPrecomputationService.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorServiceBase#getKPIType()
	 */
	@Override
	protected Type getKPIType() {
		return Type.BASDAI;
	}

	
	/** 
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorService#processAnswers(com.mobileman.projecth.domain.patient.Patient, com.mobileman.projecth.domain.disease.Disease, java.util.Date, java.util.List)
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
		
		Map<Long, Answer> answerCache = new HashMap<Long, Answer>();
		List<Long> value2Ids = MorbusBechterewQuestionaryUtil.getValue2QuestionsId();
		double val1=0.0d, val2=0.0d, val3=0.0d;
		Date logDate = null;
		for (ProjectHPatientDailyPost answerData : posts) {
			if (logDate == null) {
				logDate = answerData.getLogDate();
			}
			
			for (int i = 0; i < answerData.getQuestionsId().length; i++) {
				Long quesitonId = answerData.getQuestionsId()[i];
				Answer answer = getAnswer(answerData.getAnswersId()[i], answerCache);
				if (answer == null || !answer.isActive()) {
					continue;
				}
								
				if (MorbusBechterewQuestionaryUtil.getMorgensteifigkeitQuestionId().equals(quesitonId)) {
					val1 = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (MorbusBechterewQuestionaryUtil.getMorgensteifigkeitLangeQuestionId().equals(quesitonId)) {
					val2 = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (value2Ids.contains(quesitonId)) {
					val3 += new BigDecimal(answer.getAnswer()).doubleValue();
				}
			}
		}
		
		double basdai = ((val1+val2)/2 + (val3)) / 5;
		savePatientKPIValue(patient.getId(), disease.getId(), basdai, logDate);

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
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - start"); //$NON-NLS-1$
		}

		if (answers == null || answers.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
			}
			return;
		}
		
		Map<Long, Answer> answerCache = new HashMap<Long, Answer>();
		List<Long> value2Ids = MorbusBechterewQuestionaryUtil.getValue2QuestionsId();
		double val1=0.0d, val2=0.0d, val3=0.0d;
		
		for (PatientQuestionAnswerHolder answerData : answers) {
			Long quesitonId = answerData.getQuestionId();
			Answer answer = getAnswer(answerData.getAnswerId(), answerCache);
			if (answer == null || !answer.isActive()) {
				continue;
			}
							
			if (MorbusBechterewQuestionaryUtil.getMorgensteifigkeitQuestionId().equals(quesitonId)) {
				val1 = getAnswerValue(val3, answerData, answer);
			} else if (MorbusBechterewQuestionaryUtil.getMorgensteifigkeitLangeQuestionId().equals(quesitonId)) {
				val2 = getAnswerValue(val3, answerData, answer);
			} else if (value2Ids.contains(quesitonId)) {
				val3 = getAnswerValue(val3, answerData, answer);
			}
		}
		
		double basdai = ((val1+val2)/2 + (val3)) / 5;
		savePatientKPIValue(patient.getId(), disease.getId(), basdai, logDate);

		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}
}
