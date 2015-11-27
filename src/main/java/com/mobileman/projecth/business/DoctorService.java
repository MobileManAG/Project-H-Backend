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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.data.Gender;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.questionary.QuestionType;

/**
 * 
 * @author MobileMan
 * 
 */
public interface DoctorService extends SearchService<Doctor> {

	/**
	 * <code>UC4050</code> Doctor creates custom question for a patient
	 * 
	 * @param doctorId
	 *            creator of a question
	 * @param patientId
	 *            patient for which is question created
	 * @param diseaseId
	 *            disease assoc. to which question belongs
	 * @param questionText
	 *            text of a question
	 * @param explanation 
	 * @param questionTypeId
	 *            type of a question
	 * @return id of new custom questions
	 * @throws IllegalArgumentException
	 *             <li>doctorId == null</li> <li>patientId == null</li> <li>
	 *             diseaseId == null</li> <li>questionText == null</li> <li>
	 *             questionTypeId == null</li>
	 */
	Long addCustomQuestion(Long doctorId, Long patientId, Long diseaseId, String questionText, String explanation, Long questionTypeId)
			throws IllegalArgumentException;
	
	/**
	 * <code>UC4050</code> Doctor creates custom question for a patient
	 * 
	 * @param doctorId
	 *            creator of a question
	 * @param patientId
	 *            patient for which is question created
	 * @param diseaseId
	 *            disease assoc. to which question belongs
	 * @param questionText
	 *            text of a question
	 * @param explanation 
	 * @param questionType
	 *            type of a question
	 * @return id of new custom questions
	 * @throws IllegalArgumentException
	 *             <li>doctorId == null</li> <li>patientId == null</li> <li>
	 *             diseaseId == null</li> <li>questionText == null</li> <li>
	 *             questionTypeId == null</li>
	 */
	Long addCustomQuestion(Long doctorId, Long patientId, Long diseaseId, String questionText, String explanation, QuestionType questionType)
			throws IllegalArgumentException;

	/**
	 * Saves KPI value manualy entered/validated by doctor
	 * 
	 * @param doctorId
	 * @param patientId
	 * @param date
	 * @param kpi
	 * @param validationValue
	 * @throws IllegalArgumentException
	 */
	void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue) throws IllegalArgumentException;

	/**
	 * Saves KPI value manualy entered/validated by doctor with data
	 * 
	 * @param doctorId
	 * @param patientId
	 * @param date
	 * @param kpi
	 * @param validationValue
	 * @param data
	 *            additional data used for computation of given validated kpi
	 *            value
	 * @throws IllegalArgumentException
	 */
	void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue, List<BigDecimal> data) throws IllegalArgumentException;

	/**
	 * 
	 * @param doctorId
	 * @return Listing of number of patents by disease 
	 * 			<li>count</li>
	 *         <li> {@link Disease} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByDisease(Long doctorId) throws IllegalArgumentException;

	/**
	 * 
	 * @param doctorId
	 * @return Listing of averages of gender by disease/all his patients 
	 *         <li>count</li>
	 *         <li> {@link Gender} </li>
	 *         <li> {@link Disease} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByGenderByDisease(Long doctorId) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param doctorId
	 * @return Listing of averages, broken down by 7 groups (age) by disease/all doctor's patients 
	 * 			<li>count</li>
	 *         <li> age group (int) </li>
	 *         <li> {@link Disease} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByAgeByDisease(Long doctorId) throws IllegalArgumentException;

	/**
	 * 
	 * @param doctorId
	 * @param startDate 
	 * @param endDate 
	 * @return Listing of average points by disease/all doctor's patients for given time window
	 * 			<li> {@link Date} </li>
	 *          <li> {@link KeyPerformanceIndicatorType} </li>
	 *          <li> {@link Double} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 *              <li>startDate == null</li>
	 *             <li>endDate == null</li>
	 */	
	List<KeyPerformanceIndicatorStatistics> findPatientsKpiAverageScoreTimelineByDisease(Long doctorId, Date startDate, Date endDate);
	
	/**
	 * 
	 * @param doctorId
	 * @return Listing of average scores by disease/all doctor's patients
	 *          <li> {@link KeyPerformanceIndicatorType} </li>
	 *          <li> {@link Double} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */	
	List<Object[]> findPatientsKpiAverageScoreByDisease(Long doctorId);
	
	/**
	 * @return all doctors with valid accounts
	 */
	public List<Doctor> findAllDoctorsWithAccounts();
}
