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
package com.mobileman.projecth.business;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.domain.dto.patient.AnswerFrequency;
import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientInitialPost;
import com.mobileman.projecth.util.Pair;

/**
 * Declares business layer for the {@link Patient}
 * 
 * @author MobileMan
 * 
 */
public interface PatientService extends SearchService<Patient> {
	
	/**
	 * @return all patients with account (not deleted)
	 */
	List<Patient> findAllPatientsWithAccounts();

	/**
	 * @param posts
	 * @throws IllegalArgumentException
	 */
	void processPatientDailyPost(List<ProjectHPatientDailyPost> posts) throws IllegalArgumentException;

	/**
	 * @param data
	 * @throws IllegalArgumentException
	 */
	void processPatientInitialPost(ProjectHPatientInitialPost data) throws IllegalArgumentException;

	/**
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param logDate
	 * @return true exists record for given day
	 */
	boolean patientAnswerExists(Long patientId, Long diseaseId, Date logDate);
	
	/**
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return true exists patient's answer for one time question for given disease
	 */
	boolean existsAnswerToOneTimeQuesion(Long patientId, Long diseaseId);

	/**
	 * @return the oldest answer date of all patients in the system
	 */
	Date findOldestPatientsAnswerDate();

	/**
	 * 
	 * @param patientId
	 * @return the oldest patient's answer date
	 */
	Date findOldestPatientAnswerDate(Long patientId);

	/**
	 * 
	 * @param doctorId
	 * @return UserConnections list of a doctor
	 */
	List<UserConnection> findAllByDoctor(Long doctorId);

	/**
	 * 
	 * @param patientId
	 * @return Info about given patient
	 */
	Patient getPatientInfo(Long patientId);

	/**
	 * Computes questionary answers statistics for given patient
	 * 
	 * @see {@link #computeQuestionaryAnswersReport}
	 * @param patientId
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return computetd statistics for a patient
	 */

	/**
	 * Computes questionary answers time statistics for given patient
	 * 
	 * @see {@link #computePositiveAnswerFrequencyReport}
	 * @param patientId
	 * @param haqId
	 * @param startDate
	 * @param endDate
	 * @return computed statistics for a patient - [Date, count]
	 */

	/**
	 * <code>UC5 Medication</code> Creates an association between the usage of
	 * medication and given patient associated disease
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param medicationId
	 * @param standarUnitsTaken
	 * @param consumptionDate 
	 * @param comment
	 * @throws IllegalArgumentException
	 *             <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             medicationId == null</li> <li>standarUnitsTaken less or equal
	 *             zero</li>
	 */
	void addConsumedMedication(Long patientId, Long diseaseId, Long medicationId, double standarUnitsTaken,
			Date consumptionDate, String comment) throws IllegalArgumentException;

	/**
	 * Finds all medications consumed by given patient (consumed medication
	 * history)
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return all medications consumed by given patient (consumed medication
	 *         history)
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	List<Medication> findConsumedMedications(Long patientId, Long diseaseId) throws IllegalArgumentException;

	/**
	 * Dynamicaly computes list of patient's CDAI for each day in given time
	 * interval.
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return list of patient's CDAI for each day in given time interval.
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>startDate == null</li> <li>
	 *             endDate == null</li>
	 * @deprecated use
	 *             {@link PatientKeyPerformanceIndicatorValidation#computeKPITimeline(Long, Long, Date, Date)}
	 */

	/**
	 * <code>UC3050</code> Patient creates custom question for himself
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param text
	 * @param explanation
	 * @param questionTypeId
	 * @return id of new custom questions
	 * @throws IllegalArgumentException
	 *             <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             questionText == null</li> <li>questionTypeId == null</li>
	 */
	Long addCustomQuestion(Long patientId, Long diseaseId, String text, String explanation, Long questionTypeId)
			throws IllegalArgumentException;

	/**
	 * <code>UC3050</code> Patient creates custom question for himself with
	 * custom question type (custom answers)
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param text
	 * @param explanation
	 * @param questionType
	 * @return id of new custom questions
	 * @throws IllegalArgumentException
	 *             <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             questionText == null</li> <li>questionType == null</li> <li>
	 *             questionType.id == null</li>
	 */
	Long addCustomQuestion(Long patientId, Long diseaseId, String text, String explanation, QuestionType questionType)
			throws IllegalArgumentException;

	/**
	 * Computes time based statistics for given patient, chart and time window
	 * 
	 * @param patientId
	 *            patient for whom
	 * @param haqChartId
	 *            id of a {@link HaqChart}
	 * @param startDate
	 * @param endDate
	 * @return computed statistics for a patient - [Date, count]
	 */
	List<AnswerFrequency> computePositiveAnswerFrequencyReport(Long patientId, Long haqChartId, Date startDate,
			Date endDate);

	/**
	 * Computes questionary answers statistics for given patient and chart for
	 * all possible answers 
	 * 
	 * @param patientId
	 * @param haqChartId
	 * @param startDate
	 * @param endDate
	 * @return questionary statistics for given patient for all possible answers
	 */
	List<Object[]> computeAllAnswersFrequencyReport(Long patientId, Long haqChartId, Date startDate, Date endDate);
	
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
	 * Computes questionary answers time statistics for given patient and chart
	 * 
	 * @param patientId
	 * @param haqChartId
	 * @param startDate
	 * @param endDate
	 * @return questionary answers time statistics for given patient
	 */
	List<PatientQuestionaryAnswerStatistic> computeQuestionaryAnswersReport(Long patientId, Long haqChartId,
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
	 * Finds all custom questions defined for given patient and disease
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return all custom questions defined for given patient and disease
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	List<CustomQuestion> findCustomQuestions(Long patientId, Long diseaseId) throws IllegalArgumentException;

	/**
	 * Finds all patient's single text answers for given disease and time window
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @param startDate
	 * @param endDate
	 * @return all patient's single text answers for given disease and time
	 *         window
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	List<PatientQuestionAnswer> findAnswersForSingleAnswerEntryQuestions(Long patientId, Long diseaseId,
			Date startDate, Date endDate) throws IllegalArgumentException;

	/**
	 * Finds patient's initial symptom date and diagnosis date for given disease
	 * 
	 * @param patientId
	 * @param diseaseId
	 * @return patient's first symptom date and diagnosis date for given disease
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>diseaseId == null</li>
	 */
	Pair<Date, Date> findFirstSymptomeAndDiagnosisDate(Long patientId, Long diseaseId) throws IllegalArgumentException;
}
