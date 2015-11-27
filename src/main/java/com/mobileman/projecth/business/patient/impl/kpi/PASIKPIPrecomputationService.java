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
import com.mobileman.projecth.domain.util.questionary.PsoriasisQuestionaryUtil;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;

/**
 * Service that precomputes patient's PASI value based on a list of patient answers
 * @author mobileman
 *
 */
@Service
public class PASIKPIPrecomputationService extends KeyPerformanceIndicatorServiceBase implements KeyPerformanceIndicatorService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PASIKPIPrecomputationService.class);
	
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
		
		Map<Long, Answer> answerCache = new HashMap<Long, Answer>();
		// Kopf , Korper, Arme, Beine
		// Head,  Trunk,  Arms, Legs
		Long[] umfangIds = PsoriasisQuestionaryUtil.getUmfangIds();
		Long[] desquamationIds = PsoriasisQuestionaryUtil.getDesquamationIds();
		Long[] erythemaIds = PsoriasisQuestionaryUtil.getErythemaIds();
		Long[] indurationIds = PsoriasisQuestionaryUtil.getIndurationIds();
		
		double eh=0.0d, ih=0.0d, dh=0.0d, ea=0.0d, ia=0.0d, da=0.0d, et=0.0d, it=0.0d, dt=0.0d, el=0.0d, il=0.0d, dl=0.0d;
		double ah=0.0d, aa=0.0d, at=0.0d, al=0.0d;
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
								
				if (answerData.getCustomAnswers()[i] != null) {
					if (umfangIds[0].equals(quesitonId)) {
						ah = new BigDecimal(answerData.getCustomAnswers()[i]).doubleValue();
					} else if (umfangIds[1].equals(quesitonId)) {
						at = new BigDecimal(answerData.getCustomAnswers()[i]).doubleValue();
					} else if (umfangIds[2].equals(quesitonId)) {
						aa = new BigDecimal(answerData.getCustomAnswers()[i]).doubleValue();
					} else if (umfangIds[3].equals(quesitonId)) {
						al = new BigDecimal(answerData.getCustomAnswers()[i]).doubleValue();
					}
				} else {
					if (desquamationIds[0].equals(quesitonId)) {
						dh = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (desquamationIds[1].equals(quesitonId)) {
						dt = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (desquamationIds[2].equals(quesitonId)) {
						da = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (desquamationIds[3].equals(quesitonId)) {
						dl = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (erythemaIds[0].equals(quesitonId)) {
						eh = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (erythemaIds[1].equals(quesitonId)) {
						et = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (erythemaIds[2].equals(quesitonId)) {
						ea = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (erythemaIds[3].equals(quesitonId)) {
						el = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (indurationIds[0].equals(quesitonId)) {
						ih = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (indurationIds[1].equals(quesitonId)) {
						it = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (indurationIds[2].equals(quesitonId)) {
						ia = new BigDecimal(answer.getAnswer()).doubleValue();
					} else if (indurationIds[3].equals(quesitonId)) {
						il = new BigDecimal(answer.getAnswer()).doubleValue();
					}
				}
			}
		}
		
		double pasi = 0.1d * (eh + ih + dh)*ah + 0.2d*(ea + ia + da)*aa + 0.3d*(et + it + dt)*at + 0.4*(el + il + dl)*al;
		savePatientKPIValue(patient.getId(), disease.getId(), pasi, logDate);

		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorServiceBase#getKPIType()
	 */
	@Override
	protected Type getKPIType() {
		return Type.PASI;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.patient.impl.kpi.KeyPerformanceIndicatorService#processAnswers(Patient, Disease, Date, List)
	 */
	@Override
	public void processAnswers(Patient patient, Disease disease, Date logDate, List<PatientQuestionAnswerHolder> answers) {
		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - start"); //$NON-NLS-1$
		}

		if (answers == null || answers.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("processAnswers(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
			}
			return;
		}
		
		Map<Long, Answer> answerCache = new HashMap<Long, Answer>();
		// Kopf , Korper, Arme, Beine
		// Head,  Trunk,  Arms, Legs
		Long[] umfangIds = PsoriasisQuestionaryUtil.getUmfangIds();
		Long[] desquamationIds = PsoriasisQuestionaryUtil.getDesquamationIds();
		Long[] erythemaIds = PsoriasisQuestionaryUtil.getErythemaIds();
		Long[] indurationIds = PsoriasisQuestionaryUtil.getIndurationIds();
		
		double eh=0.0d, ih=0.0d, dh=0.0d, ea=0.0d, ia=0.0d, da=0.0d, et=0.0d, it=0.0d, dt=0.0d, el=0.0d, il=0.0d, dl=0.0d;
		double ah=0.0d, aa=0.0d, at=0.0d, al=0.0d;
		double scaleDiv = 6.0d/100.0d;
		
		for (PatientQuestionAnswerHolder answerData : answers) {
			Long quesitonId = answerData.getQuestionId();
			Answer answer = getAnswer(answerData.getAnswerId(), answerCache);
			if (answer == null || !answer.isActive()) {
				continue;
			}
							
			if (answerData.getCustomAnswer() != null) {
				if (umfangIds[0].equals(quesitonId)) {
					ah = new BigDecimal(answerData.getCustomAnswer()).doubleValue();
				} else if (umfangIds[1].equals(quesitonId)) {
					at = new BigDecimal(answerData.getCustomAnswer()).doubleValue();
				} else if (umfangIds[2].equals(quesitonId)) {
					aa = new BigDecimal(answerData.getCustomAnswer()).doubleValue();
				} else if (umfangIds[3].equals(quesitonId)) {
					al = new BigDecimal(answerData.getCustomAnswer()).doubleValue();
				}
			} else {
				if (desquamationIds[0].equals(quesitonId)) {
					dh = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (desquamationIds[1].equals(quesitonId)) {
					dt = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (desquamationIds[2].equals(quesitonId)) {
					da = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (desquamationIds[3].equals(quesitonId)) {
					dl = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (erythemaIds[0].equals(quesitonId)) {
					eh = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (erythemaIds[1].equals(quesitonId)) {
					et = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (erythemaIds[2].equals(quesitonId)) {
					ea = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (erythemaIds[3].equals(quesitonId)) {
					el = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (indurationIds[0].equals(quesitonId)) {
					ih = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (indurationIds[1].equals(quesitonId)) {
					it = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (indurationIds[2].equals(quesitonId)) {
					ia = new BigDecimal(answer.getAnswer()).doubleValue();
				} else if (indurationIds[3].equals(quesitonId)) {
					il = new BigDecimal(answer.getAnswer()).doubleValue();
				}
			}
		}
		
		double pasi = 0.1d * (eh + ih + dh)*(ah*scaleDiv) + 0.2d*(ea + ia + da)*(aa*scaleDiv) + 0.3d*(et + it + dt)*(at*scaleDiv) + 0.4*(el + il + dl)*(al*scaleDiv);
		savePatientKPIValue(patient.getId(), disease.getId(), pasi, logDate);
		
		if (log.isDebugEnabled()) {
			log.debug("process(Patient, Disease, List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}

}
