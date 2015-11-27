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
 * PatientQuestionAnswerDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.patient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.dto.patient.AnswerFrequency;
import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.persistence.Dao;
import com.mobileman.projecth.util.Pair;

/**
 * Represents DAO for the {@link PatientQuestionAnswer} entity.
 * 
 * @author mobileman
 * 
 */
public interface PatientQuestionAnswerDao extends Dao<PatientQuestionAnswer> {

	/**
	 * Computes time based patient positive answers frequency report for given
	 * questionary in given time window - count of answers to all hag's
	 * questions
	 * 
	 * @param patientId
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return count of positive answers for each day
	 */
	List<AnswerFrequency> computePositiveAnswerFrequencyReport(Long patientId, Long haqId, Date startDate, Date endDate);

	/**
	 * Computes questionary answers time statistics for given patient
	 * 
	 * @param patientId
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return questionary answers time statistics for given patient
	 */
	List<PatientQuestionaryAnswerStatistic> computeQuestionaryAnswersReport(Long patientId, Long haqId,
			Date startDate, Date endDate);
	
	/**
	 * Computes question's answers time statistics for given patient and question
	 * 
	 * @param patientId
	 * @param questionId
	 * @param startDate
	 * @param endDate
	 * @return question's answers time statistics for given patient and question
	 */
	List<PatientQuestionaryAnswerStatistic> computeQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate);
	
	/**
	 * Computes question's answers time statistics for given patient and question
	 * 
	 * @param patientId
	 * @param questionId
	 * @param startDate
	 * @param endDate
	 * @return question's answers time statistics for given patient and question
	 */
	List<PatientQuestionaryAnswerStatistic> computeCustomQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate);

	/**
	 * Computes questionary answers statistics for given patient for all
	 * possible answers
	 * 
	 * @param patientId
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return questionary statistics for given patient for all possible answers
	 */
	List<Object[]> computeAllAnswersFrequencyReport(Long patientId, Long haqId, Date startDate, Date endDate);
	
	/**
	 * Computes questionary answers statistics for given patient and
	 * all question's possible answers 
	 * 
	 * @param patientId
	 * @param questionId
	 * @param startDate
	 * @param endDate
	 * @return questionary statistics for given patient for all possible answers
	 */
	List<Object[]> computeQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate, Date endDate);
	
	/**
	 * Computes questionary answers statistics for given patient and
	 * all question's possible answers 
	 * 
	 * @param patientId
	 * @param questionId
	 * @param startDate
	 * @param endDate
	 * @return questionary statistics for given patient for all possible answers
	 */
	List<Object[]> computeCustomQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate, Date endDate);

	/**
	 * Computes Rheuma CDAI KPI value
	 * @param patientId
	 * @param diseaseId rheuma ID
	 * @param startDate
	 * @param endDate
	 * @return Rheuma CDAI KPI value
	 */
	BigDecimal computeCDAI(Long patientId, Long diseaseId, Date startDate, Date endDate);
	
	/**
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based rheuma CDAI KPI values 
	 */
	List<KeyPerformanceIndicatorStatistics> computeCDAITimeline(Long patientId, Long diseaseId, Date startDate,
			Date endDate);
	
	/**
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based rheuma CDAI KPI average values 
	 */
	List<KeyPerformanceIndicatorStatistics> computeCDAIAverageTimeline(Long diseaseId, Date startDate,
			Date endDate);
	
	/**
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based BASDAI KPI values 
	 */
	List<KeyPerformanceIndicatorStatistics> computeBASDAITimeline(Long patientId, Long diseaseId, Date startDate,
			Date endDate);
	
	/**
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based BASDAI KPI average values 
	 */
	List<KeyPerformanceIndicatorStatistics> computeBASDAIAverageTimeline(Long diseaseId, Date startDate,
			Date endDate);
	
	/**
	 * Computes Morbus Bechterew BASDAI KPI value
	 * @param patientId
	 * @param diseaseId rheuma ID
	 * @param startDate
	 * @param endDate
	 * @return Morbus Bechterew BASDAI KPI value
	 */
	public BigDecimal computeBASDAI(Long patientId, Long diseaseId, Date startDate, Date endDate);
	
	/**
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based PASI KPI values 
	 */
	List<KeyPerformanceIndicatorStatistics> computePASITimeline(Long patientId, Long diseaseId, Date startDate,
			Date endDate);
	
	/**
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return computes time based PASI KPI average values 
	 */
	List<KeyPerformanceIndicatorStatistics> computePASIAverageTimeline(Long diseaseId, Date startDate,
			Date endDate);

	/**
	 * @param patientId
	 * @param diseaseId 
	 * @param logDate
	 * @return true if exists patient's answer for giben disease and date
	 */
	boolean answerExists(Long patientId, Long diseaseId, Date logDate);
	
	/**
	 * 
	 * @param patientId
	 * @return the oldest patient's answer date
	 */
	Date findOldestPatientAnswerDate(Long patientId);
	
	/**
	 * @return the oldest answer date of any patient in the system
	 */
	Date findOldestPatientsAnswerDate();

	/**
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return list of answers for haq and time window
	 */
	List<PatientQuestionAnswer> find(Long haqId, Date startDate, Date endDate);

	/**
	 * Removes one time questions patient's answers
	 * @param patientId
	 * @param diseaseId 
	 */
	void removeAllOneTimeQuestionsAnswers(Long patientId, Long diseaseId);
	
	/**
	 * Removes one time questions patient's answers
	 * @param patientId
	 * @param questionId 
	 */
	void removeOneTimeQuestionAnswers(Long patientId, Long questionId);
	
	/**
	 * Finds all patient's single text answers for given disease and time window
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param startDate 
	 * @param endDate 
	 * @return all patient's single text answers for given disease and time window
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> 
	 *             <li>diseaseId == null</li>
	 *             <li>startDate == null</li>
	 *             <li>endDate == null</li>
	 */
	List<PatientQuestionAnswer> findAnswersForSingleAnswerEntryQuestions(Long patientId, Long diseaseId,
			Date startDate, Date endDate) throws IllegalArgumentException;
	
	/**
	 * Finds patient's first symptom date and diagnosis date for given disease
	 * 
	 * @param patientId
	 * @param diseaseId 
	 * @return patient's first symptom date and diagnosis date for given disease
	 * @throws IllegalArgumentException
	 *             if 
	 *             <li>patientId == null</li> 
	 *             <li>diseaseId == null</li>
	 */
	Pair<Date, Date> findFirstSymptomeAndDiagnosisDate(Long patientId, Long diseaseId) throws IllegalArgumentException;

	/**
	 * Deletes all answers to given question
	 * @param questionId
	 */
	void deleteAnswers(Long questionId);
	
	/**
	 * Finds patients answers for given time window and question
	 * @param userId 
	 * @param questionId - optional
	 * @param startDate
	 * @param endDate
	 * @return patient answer data export
	 */
	List<PatientQuestionAnswer> findAll(Long userId, Long questionId, Date startDate, Date endDate);

	/**
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return true exists patient's answer for one time question for given disease
	 */
	boolean existsAnswerToOneTimeQuesion(Long patientId, Long diseaseId);
}
