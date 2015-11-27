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
 * PatientQuestionAnswerService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.business.SearchService;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;

/**
 * Declares business layer for the {@link PatientQuestionAnswer}
 * 
 * @author mobileman
 *
 */
public interface PatientQuestionAnswerService extends SearchService<PatientQuestionAnswer> {

	/**
	 * Creates and persists new PatientQuestionAnswer entity
	 * @param patientId
	 * @param questionId 
	 * @param answerId 
	 * @param customAnswer 
	 * @param logDate
	 * @return id of new PatientQuestionAnswer entity
	 */
	Long saveAnswer(Long patientId, Long questionId, Long answerId, String customAnswer, Date logDate);
	
	/**
	 * Creates and persists new PatientQuestionAnswer entities
	 * @param patientId
	 * @param diseaseId 
	 * @param answers
	 * @param logDate
	 * @return id of new PatientQuestionAnswer entity
	 */
	Long saveAnswers(Long patientId, Long diseaseId, Date logDate, List<PatientQuestionAnswerHolder> answers);
	
	/**
	 * Exports patients answers for given time window
	 * @param haqId - optional
	 * @param startDate
	 * @param endDate
	 * @return patient answer data export
	 */
	List<PatientQuestionAnswer> exportData(Long haqId, Date startDate, Date endDate);
	
	/**
	 * Finds patients answers for given time window and question
	 * @param userId 
	 * @param questionId - optional
	 * @param startDate
	 * @param endDate
	 * @return patient answer data export
	 */
	List<PatientQuestionAnswer> findAll(Long userId, Long questionId, Date startDate, Date endDate);
	
}
