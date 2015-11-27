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
 * DoctorServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService;
import com.mobileman.projecth.business.patient.PatientKPIValidationService;
import com.mobileman.projecth.business.questionary.QuestionService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.data.Gender;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.questionary.NoAnswer;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.domain.questionary.QuestionType.Type;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
public class DoctorServiceTest extends TestCaseBase {
	
	@Autowired
	DoctorService doctorService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	UserService userService;
		
	@Autowired
	QuestionTypeService questionTypeService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	HaqService haqService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	private PatientKPIValidationService patientKPIValidationService;
	
	@Autowired
	private KeyPerformanceIndicatorTypeService keyPerformanceIndicatorTypeService;
	
	@Autowired
	private UserConnectionService userConnectionService;
	
	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * @throws Exception
	 */
	@Test
	public void addCustomQuestion() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Doctor doctor = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		
		List<QuestionType> qts = questionTypeService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		List<Question> questionsOld = questionService.findAll();
		
		Long id = doctorService.addCustomQuestion(doctor.getId(), patient.getId(), diseases.get(0).getId(), "pq", "expl", qts.get(0).getId());
		assertNotNull(id);
		
		List<Question> questionsNew = questionService.findAll();
		assertEquals(questionsOld.size() + 1, questionsNew.size());
		
		CustomQuestion customQuestion = (CustomQuestion) questionsNew.get(questionsNew.size() - 1);
		assertEquals("pq", customQuestion.getText());
		assertEquals("expl", customQuestion.getExplanation());
		assertEquals(patient, customQuestion.getUser());
		assertEquals(doctor, customQuestion.getCreator());
		assertEquals(CustomQuestionType.NEW, customQuestion.getCustomQuestionType());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void addCustomQuestion_QuestionType() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		Doctor doctor = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		
		List<Disease> diseases = diseaseService.findAll();
		
		List<Question> questionsOld = questionService.findAll();
		
		QuestionType questionType = new QuestionType();
		questionType.setAnswerDataType(AnswerDataType.BOOLEAN);
		questionType.setAnswers(new ArrayList<Answer>());
		questionType.setType(Type.SINGLE_CHOICE_LIST);
		NoAnswer answer1 = new NoAnswer();
		answer1.setActive(false);
		answer1.setAnswer("test");
		answer1.setQuestionType(questionType);
		answer1.setSortOrder(0);
		questionType.getAnswers().add(answer1);
		
		Answer answer2 = new Answer();
		answer2.setActive(true);
		answer2.setAnswer("test");
		answer2.setQuestionType(questionType);
		answer2.setSortOrder(1);
		questionType.getAnswers().add(answer2);
		
		Long id = doctorService.addCustomQuestion(doctor.getId(), patient.getId(), diseases.get(0).getId(), "pq", "expl", questionType);
		assertNotNull(id);
		flushSession();
		
		List<Question> questionsNew = questionService.findAll();
		assertEquals(questionsOld.size() + 1, questionsNew.size());
		
		CustomQuestion customQuestion = (CustomQuestion) questionsNew.get(questionsNew.size() - 1);
		assertEquals("pq", customQuestion.getText());
		assertEquals(patient, customQuestion.getUser());
		assertEquals(doctor, customQuestion.getCreator());
		assertEquals(CustomQuestionType.NEW, customQuestion.getCustomQuestionType());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void saveManualKpiValue() throws Exception {
		
		Doctor doctor = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		
		List<PatientKeyPerformanceIndicatorValidation> validations = patientKPIValidationService.findAll();
		assertNotNull(validations);
		assertEquals(0, validations.size());
		
		Patient patient = patientService.findAll().get(0);
		assertNotNull(patient);
		assertNotNull(patient.getId());
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		KeyPerformanceIndicatorType questionaryValidationType = keyPerformanceIndicatorTypeService.find("CDAI", disease.getId());
		doctorService.saveValidatedKpiValue(doctor.getId(), patient.getId(), new Date(), questionaryValidationType, new BigDecimal("15"));
		
		validations = patientKPIValidationService.findAll();
		assertNotNull(validations);
		assertEquals(1, validations.size());
		
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date start = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date end = calendar.getTime();
		List<PatientKeyPerformanceIndicatorValidation> values = patientKPIValidationService.findValidatedValues(
				patient.getId(), questionaryValidationType.getId(), start, end);
		assertEquals(1, values.size());
		assertEquals(15, values.get(0).getValue().intValue());
		
		values = patientKPIValidationService.findValidatedValues(
				doctor.getId(), patient.getId(), questionaryValidationType.getId(), start, end);
		assertEquals(1, values.size());
		assertEquals(15, values.get(0).getValue().intValue());
		
		values = patientKPIValidationService.findValidatedValues(
				patient.getId(), patient.getId(), questionaryValidationType.getId(), start, end);
		assertEquals(0, values.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void saveManualKpiValue_WithData() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor1);
		
		List<PatientKeyPerformanceIndicatorValidation> validations = patientKPIValidationService.findAll();
		assertNotNull(validations);
		assertEquals(0, validations.size());
		
		Patient patient = patientService.findAll().get(0);
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		KeyPerformanceIndicatorType questionaryValidationType = keyPerformanceIndicatorTypeService.find("CDAI", disease.getId());
		
		List<BigDecimal> data = new ArrayList<BigDecimal>();
		data.add(new BigDecimal("12.3"));
		data.add(new BigDecimal("123"));
		
		doctorService.saveValidatedKpiValue(doctor1.getId(), patient.getId(), new Date(), questionaryValidationType, new BigDecimal("15"),
				data);
		
		validations = patientKPIValidationService.findAll();
		assertNotNull(validations);
		assertEquals(1, validations.size());
		
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date start = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date end = calendar.getTime();
		List<PatientKeyPerformanceIndicatorValidation> values = patientKPIValidationService.findValidatedValues(
				patient.getId(), questionaryValidationType.getId(), start, end);
		assertEquals(1, values.size());
		assertEquals(15, values.get(0).getValue().intValue());		
		assertEquals(2, values.get(0).getData().size());
		assertEquals(12.3d, values.get(0).getData().get(0).doubleValue(), 0.01);
		assertEquals(123.0d, values.get(0).getData().get(1).doubleValue(), 0.01);
		
		values.get(0).getData().clear();
		values.get(0).getData().add(new BigDecimal("0.70"));
		patientKPIValidationService.update(values.get(0));
		
		values = patientKPIValidationService.findValidatedValues(
				patient.getId(), questionaryValidationType.getId(), start, end);
		assertEquals(0.7d, values.get(0).getData().get(0).doubleValue(), 0.01);
		
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPatientsCountByDisease() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		Patient pat1 = (Patient)userService.findUserByLogin("sysuser1");
		Patient pat2 = (Patient)userService.findUserByLogin("sysuser2");
		
		Disease rheuma = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		Disease psoriasis = diseaseService.findByCode(DiseaseCodes.PSORIASIS_CODE);
		
		List<Object[]> props = doctorService.findPatientsCountByDisease(doctor1.getId());
		assertEquals(0, props.size());
		
		userService.addDiseasesToUser(pat1.getId(), Arrays.asList(rheuma));
		userService.addDiseasesToUser(pat2.getId(), Arrays.asList(rheuma));
		
		props = doctorService.findPatientsCountByDisease(doctor1.getId());
		assertEquals(1, props.size());
		assertEquals(1L, props.get(0)[0]);
		
		userConnectionService.acceptInvitation(doctor1.getId(), pat2.getId());
		flushSession();
		
		props = doctorService.findPatientsCountByDisease(doctor1.getId());
		assertEquals(1, props.size());
		assertEquals(2L, props.get(0)[0]);
		Disease disease = Disease.class.cast(props.get(0)[1]);
		assertEquals(rheuma, disease);
		assertEquals(rheuma.getCode(), disease.getCode());
		assertEquals(rheuma.getName(), disease.getName());
		
		userService.removeDiseasesFromUser(pat2.getId(), Arrays.asList(rheuma));
		userService.addDiseasesToUser(pat2.getId(), Arrays.asList(psoriasis));
		
		props = doctorService.findPatientsCountByDisease(doctor1.getId());
		assertEquals(2, props.size());
		assertEquals(1L, props.get(0)[0]);
		assertEquals(1L, props.get(1)[0]);
		assertEquals(rheuma, Disease.class.cast(props.get(0)[1]));
		assertEquals(rheuma.getCode(), Disease.class.cast(props.get(0)[1]).getCode());
		assertEquals(rheuma.getName(), Disease.class.cast(props.get(0)[1]).getName());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPatientsCountByGenderByDisease() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		Patient pat1 = (Patient)userService.findUserByLogin("sysuser1");
		Patient pat2 = (Patient)userService.findUserByLogin("sysuser2");
		
		Disease rheuma = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		
		List<Object[]> props = doctorService.findPatientsCountByGenderByDisease(doctor1.getId());
		assertEquals(0, props.size());
		
		userService.addDiseasesToUser(pat1.getId(), Arrays.asList(rheuma));
		userService.addDiseasesToUser(pat2.getId(), Arrays.asList(rheuma));
		
		props = doctorService.findPatientsCountByGenderByDisease(doctor1.getId());
		assertEquals(1, props.size());
		assertEquals(1L, props.get(0)[0]);
		
		userConnectionService.acceptInvitation(doctor1.getId(), pat2.getId());
		flushSession();
		props = doctorService.findPatientsCountByGenderByDisease(doctor1.getId());
		assertEquals(1, props.size());
		assertEquals(2L, props.get(0)[0]);
		
		assertEquals(Gender.MALE, props.get(0)[1]);
		Disease disease = Disease.class.cast(props.get(0)[2]);
		assertEquals(rheuma, disease);
		assertEquals(rheuma.getCode(), disease.getCode());
		assertEquals(rheuma.getName(), disease.getName());
		
		pat2.setSex(0);
		userService.update(pat2);
		
		props = doctorService.findPatientsCountByGenderByDisease(doctor1.getId());
		
		props = doctorService.findPatientsCountByGenderByDisease(doctor1.getId());
		assertEquals(2, props.size());
		assertEquals(1L, props.get(0)[0]);
		assertEquals(1L, props.get(1)[0]);
		assertEquals(Gender.FEMALE, props.get(0)[1]);
		assertEquals(Gender.MALE, props.get(1)[1]);
		assertEquals(rheuma, Disease.class.cast(props.get(0)[2]));
		assertEquals(rheuma.getCode(), Disease.class.cast(props.get(0)[2]).getCode());
		assertEquals(rheuma.getName(), Disease.class.cast(props.get(0)[2]).getName());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPatientsCountByAgeByDisease() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		Patient pat1 = (Patient)userService.findUserByLogin("sysuser1");
		Patient pat2 = (Patient)userService.findUserByLogin("sysuser2");
		
		Disease rheuma = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		
		List<Object[]> props = doctorService.findPatientsCountByAgeByDisease(doctor1.getId());
		assertEquals(0, props.size());
		
		userService.addDiseasesToUser(pat1.getId(), Arrays.asList(rheuma));
		userService.addDiseasesToUser(pat2.getId(), Arrays.asList(rheuma));
		
		props = doctorService.findPatientsCountByAgeByDisease(doctor1.getId());
		assertEquals(0, props.size());
		
		pat1.setDateOfBirth(dateFormat.parse("1.1.1974"));
		userService.update(pat1);
		pat1 = (Patient)userService.findUserByLogin("sysuser1");
		
		props = doctorService.findPatientsCountByAgeByDisease(doctor1.getId());
		assertEquals(1, props.size());
		
		userConnectionService.acceptInvitation(doctor1.getId(), pat2.getId());
		flushSession();
		props = doctorService.findPatientsCountByAgeByDisease(doctor1.getId());
		assertEquals(2, props.size());
		
		assertEquals(1L, props.get(0)[0]);
		assertEquals(1L, props.get(1)[0]);
		assertEquals(4, props.get(0)[1]);
		assertEquals(3, props.get(1)[1]);
		
		Disease disease = Disease.class.cast(props.get(0)[2]);
		assertEquals(rheuma, disease);
		assertEquals(rheuma.getCode(), disease.getCode());
		assertEquals(rheuma.getName(), disease.getName());
		
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPatientsKpiAverageScoreTimelineByDisease() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		Patient pat1 = (Patient)userService.findUserByLogin("sysuser1");
		
		List<KeyPerformanceIndicatorStatistics> props = doctorService.findPatientsKpiAverageScoreTimelineByDisease(
				doctor1.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(6, props.size());
		assertEquals(2.0d, props.get(0).getValue().doubleValue(), 0.1d);
		assertEquals(2.0d, props.get(1).getValue().doubleValue(), 0.1d);
		assertEquals(1.0d, props.get(2).getValue().doubleValue(), 0.1d);
		
		userConnectionService.cancelInvitation(doctor1.getId(), pat1.getId());
		flushSession();
		
		props = doctorService.findPatientsKpiAverageScoreTimelineByDisease(
				doctor1.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(0, props.size());
		
		userConnectionService.invite(doctor1.getId(), pat1.getId());
		userConnectionService.acceptInvitation(doctor1.getId(), pat1.getId());
		flushSession();
		
		props = doctorService.findPatientsKpiAverageScoreTimelineByDisease(
				doctor1.getId(), dateFormat.parse("2.2.2010"), dateFormat.parse("2.2.2010"));
		assertEquals(1, props.size());
		KeyPerformanceIndicatorType kpit = props.get(0).getKeyPerformanceIndicatorType();
		assertEquals("CDAI", kpit.getCode());
		assertEquals("M79.0", kpit.getDisease().getCode());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPatientsKpiAverageScoreByDisease() throws Exception {
		
		Doctor doctor1 = (Doctor)userService.findUserByLogin("sysuser3");
		Patient pat1 = (Patient)userService.findUserByLogin("sysuser1");
		
		List<Object[]> props = doctorService.findPatientsKpiAverageScoreByDisease(doctor1.getId());
		assertEquals(1, props.size());
		assertEquals(1.33d, (Double)props.get(0)[2], 0.01);
		KeyPerformanceIndicatorType kpit = (KeyPerformanceIndicatorType) props.get(0)[1];
		assertEquals("CDAI", kpit.getCode());
		assertEquals("M79.0", kpit.getDisease().getCode());
		
		userConnectionService.cancelInvitation(doctor1.getId(), pat1.getId());
		flushSession();
		
		props = doctorService.findPatientsKpiAverageScoreByDisease(doctor1.getId());
		assertEquals(0, props.size());
		
		userConnectionService.invite(doctor1.getId(), pat1.getId());
		userConnectionService.acceptInvitation(doctor1.getId(), pat1.getId());
		flushSession();
		
		props = doctorService.findPatientsKpiAverageScoreByDisease(doctor1.getId());
		assertEquals(1, props.size());
		
	}
}
