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
 * PatientTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.DoctorService;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.business.MedicationService;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.chart.HaqChartService;
import com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService;
import com.mobileman.projecth.business.questionary.QuestionService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.dto.patient.AnswerFrequency;
import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.questionary.NoAnswer;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.domain.questionary.QuestionType.Type;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;
import com.mobileman.projecth.persistence.patient.PatientMedicationDao;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;
import com.mobileman.projecth.util.Pair;
import com.mobileman.projecth.util.disease.DiseaseCodes;


/**
 * @author mobileman
 *
 */
public class PatientServiceTest extends TestCaseBase {
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MedicationService medicationService;
	
	@Autowired
	PatientMedicationDao patientMedicationDao;
	
	@Autowired
	private PatientKPIDao patientKPIDao;
	
	@Autowired
	QuestionTypeService questionTypeService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	HaqService haqService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	HaqChartService haqChartService;
	
	@Autowired
	private PatientQuestionAnswerService patientQuestionAnswerService;
	
	@Autowired
	private PatientKPIService patientKPIService;
	
	@Autowired
	private KeyPerformanceIndicatorTypeService keyPerformanceIndicatorTypeService;
	
	@Autowired
	DoctorService doctorService;
	
	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void processPatientDailyPost() throws Exception {
		
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		patient1 = patientService.findById(patient1.getId());
		patient1 = patientService.findById(patient1.getId());		
		assertNotNull(patient1.getPatientAnswerOverrides());
					
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		assertFalse(patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date()));
		
		List<ProjectHPatientDailyPost> posts = new ArrayList<ProjectHPatientDailyPost>();
		ProjectHPatientDailyPost post = new ProjectHPatientDailyPost();
		post.setDiseaseCode("M79.0");
		post.setActivationCode("1234");
		post.setPatientId(patient1.getId());
		post.setLogDate(new Date());
		post.setHaqId(haqs.get(0).getId());
		post.setQuestionsId(new Long[]{ 1001L, 1002L });
		post.setAnswersId(new Long[]{ 2L, 3L });
		posts.add(post);
		
		post = new ProjectHPatientDailyPost();
		post.setDiseaseCode("M79.0");
		post.setActivationCode("1234");
		post.setPatientId(patient1.getId());
		post.setLogDate(new Date());
		post.setHaqId(haqs.get(1).getId());
		post.setQuestionsId(new Long[]{ 1051L });
		post.setAnswersId(new Long[]{ 2L });
		posts.add(post);
		
		post = new ProjectHPatientDailyPost();
		post.setDiseaseCode("M79.0");
		post.setActivationCode("1234");
		post.setPatientId(patient1.getId());
		post.setLogDate(new Date());
		post.setHaqId(haqs.get(3).getId());
		post.setQuestionsId(new Long[]{ 1115L });
		post.setAnswersId(new Long[]{ 11L });
		posts.add(post);
		
		int answersCount = patientQuestionAnswerService.findAll().size();
		
		patientService.processPatientDailyPost(posts);
		
		assertEquals(answersCount + 4, patientQuestionAnswerService.findAll().size());
		
		boolean exist = patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date());
		assertTrue(exist);
		
		List<PatientKeyPerformanceIndicator> pkpis = patientKPIDao.findAll();
		assertEquals(7, pkpis.size());
		assertEquals(10L, pkpis.get(6).getValue().longValue());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAllByDoctor() throws Exception {
				
		DoctorService doctorBo = (DoctorService)applicationContext.getBean(ComponentNames.DOCTOR_SERVICE);
		assertNotNull(doctorBo);
		
		Doctor doctor1 = doctorBo.findAll().get(0);
		assertNotNull(doctor1);
		
		List<UserConnection> patients = patientService.findAllByDoctor(doctor1.getId());
		assertNotNull(patients);
		assertEquals(2, patients.size());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getPatientInfo() throws Exception {
		
		List<Patient> patients = patientService.findAll();
		assertNotNull(patients);
		assertEquals(3, patients.size());
		
		Long patientId = patients.get(0).getId();
		
		Patient patient = patientService.getPatientInfo(patientId);
		assertNotNull(patient);
		assertNotNull(patient.getUserAccount());
		assertNotNull(patient.getPatientAnswerOverrides());
		assertNotNull(patient.getMedicines());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findById() throws Exception {
		
		List<Patient> patients = patientService.findAll();
		assertNotNull(patients);
		assertEquals(3, patients.size());
		
		Long patientId = patients.get(0).getId();
		
		Patient patient = patientService.findById(patientId);
		assertNotNull(patient);
		assertNotNull(patient);
		assertNotNull(patient.getUserAccount());
		assertNotNull(patient.getPatientAnswerOverrides());
		assertNotNull(patient.getMedicines());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAllPatientsWithAccounts() throws Exception {
		
		List<Patient> patients = patientService.findAllPatientsWithAccounts();
		assertTrue(patients.size() > 0);
		for (Patient patient : patients) {
			assertNotNull(patient.getUserAccount());
			assertEquals(UserType.P, patient.getUserType());
		}
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void patientAnswerExists() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		assertFalse(patientService.patientAnswerExists(patient.getId(), disease.getId(), new Date()));
		
		assertTrue(patientService.patientAnswerExists(patient.getId(), disease.getId(), dateFormat.parse("1.11.2010")));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findOldestPatientAnswerDate() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		Date date = patientService.findOldestPatientAnswerDate(patient.getId());
		assertEquals("01.11.2010", dateFormat.format(date));
		assertNull(patientService.findOldestPatientAnswerDate(0L));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findOldestPatientsAnswerDate() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		Date date = patientService.findOldestPatientsAnswerDate();
		assertEquals("01.11.2010", dateFormat.format(date));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void addCustomQuestion() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		List<QuestionType> qts = questionTypeService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		List<Question> questionsOld = questionService.findAll();
		
		Long id = patientService.addCustomQuestion(patient.getId(), diseases.get(0).getId(), "pq", "expl", qts.get(0).getId());
		assertNotNull(id);
		
		List<Question> questionsNew = questionService.findAll();
		assertEquals(questionsOld.size() + 1, questionsNew.size());
		
		CustomQuestion customQuestion = (CustomQuestion) questionsNew.get(questionsNew.size() - 1);
		assertEquals("pq", customQuestion.getText());
		assertEquals("expl", customQuestion.getExplanation());
		assertEquals(patient, customQuestion.getUser());
		assertEquals(patient, customQuestion.getCreator());
		assertEquals(CustomQuestionType.NEW, customQuestion.getCustomQuestionType());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void addCustomQuestionQuestionType() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
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
		
		Long id = patientService.addCustomQuestion(patient.getId(), diseases.get(0).getId(), "pq", "expl", questionType);
		assertNotNull(id);
		flushSession();
		
		List<Question> questionsNew = questionService.findAll();
		assertEquals(questionsOld.size() + 1, questionsNew.size());
		
		CustomQuestion customQuestion = (CustomQuestion) questionsNew.get(questionsNew.size() - 1);
		assertEquals("pq", customQuestion.getText());
		assertEquals(patient, customQuestion.getUser());
		assertEquals(patient, customQuestion.getCreator());
		assertEquals(CustomQuestionType.NEW, customQuestion.getCustomQuestionType());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computePositiveAnswerFrequencyReport() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		List<HaqChart> charts = haqChartService.findByHaq(haqs.get(0).getId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		List<AnswerFrequency> stats = patientService.computePositiveAnswerFrequencyReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		
		assertEquals(3, stats.size());
		assertEquals(3, stats.get(0).getCount().intValue());
		assertEquals(2, stats.get(1).getCount().intValue());
		assertEquals(3, stats.get(2).getCount().intValue());
		
		stats = patientService.computePositiveAnswerFrequencyReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("1.11.2010"));
		
		assertEquals(1, stats.size());
		assertEquals(3, stats.get(0).getCount().intValue());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeAllAnswersFrequencyReport() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		List<HaqChart> charts = haqChartService.findByHaq(haqs.get(0).getId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		List<Object[]> stats = patientService.computeAllAnswersFrequencyReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		
		assertEquals(12, stats.size());
				
		stats = patientService.computeAllAnswersFrequencyReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("1.11.2010"));
		
		assertEquals(4, stats.size());
		assertEquals("Druckempfindlich rechte Hand Daumen hinten", stats.get(0)[2]);
		assertEquals("No", stats.get(0)[4]);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeQuestionAnswersFrequencyReport() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Question question = null;
		for (Question q : haqs.get(0).getQuestions()) {
			if ("Geschwollen rechte Hand Daumen vorne".equals(q.getText())) {
				question = q;
				break;
			}
		}
		
		List<Object[]> stats = patientService.computeQuestionAnswersFrequencyReport(
				patient.getId(), question.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		
		assertEquals(3, stats.size());
				
		stats = patientService.computeQuestionAnswersFrequencyReport(
				patient.getId(), question.getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("1.11.2010"));
		
		assertEquals(1, stats.size());
		assertEquals("Geschwollen rechte Hand Daumen vorne", stats.get(0)[2]);
		assertEquals("Yes", stats.get(0)[4]);
		
		stats = patientService.computeCustomQuestionAnswersFrequencyReport(
				patient.getId(), question.getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("1.11.2010"));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeQuestionaryAnswersReport() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		List<HaqChart> charts = haqChartService.findByHaq(haqs.get(0).getId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		List<PatientQuestionaryAnswerStatistic> stats = patientService.computeQuestionaryAnswersReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(427, stats.size());
		
		for (PatientQuestionaryAnswerStatistic stat : stats) {
			if ("Geschwollen rechte Hand Daumen hinten".equals(stat.getQuestionText())
					&& "No".equals(stat.getAnswerText())) {
				assertEquals(2, stat.getCount());
			}
		}
		
		stats = patientService.computeQuestionaryAnswersReport(
				patient.getId(), charts.get(0).getId(), dateFormat.parse("13.12.2010"), dateFormat.parse("13.12.2010"));
		
		for (PatientQuestionaryAnswerStatistic stat : stats) {
			if ("Geschwollen rechte Hand Daumen hinten".equals(stat.getQuestionText())
					&& "No".equals(stat.getAnswerText())) {
				assertEquals(0, stat.getCount());
			}
		}
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeQuestionAnswersReport() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Question question = null;
		for (Question tmp : haqs.get(0).getQuestions()) {
			if ("Geschwollen rechte Hand Daumen vorne".equals(tmp.getText())) {
				question = tmp;
				break;
			}
		}
		
		List<PatientQuestionaryAnswerStatistic> stats = patientService.computeQuestionAnswersReport(
				patient.getId(), question.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(3, stats.size());
		
		boolean found = false;
		for (PatientQuestionaryAnswerStatistic stat : stats) {
			if ("Geschwollen rechte Hand Daumen vorne".equals(stat.getQuestionText())
					&& "Yes".equals(stat.getAnswerText())) {
				assertEquals(3, stat.getCount());
				found = true;
			}
		}
		
		assertTrue(found);
		found = false;
		stats = patientService.computeQuestionAnswersReport(
				patient.getId(), question.getId(), dateFormat.parse("13.12.2010"), dateFormat.parse("13.12.2010"));
		
		for (PatientQuestionaryAnswerStatistic stat : stats) {
			if ("Geschwollen rechte Hand Daumen vorne".equals(stat.getQuestionText())
					&& "Yes".equals(stat.getAnswerText())) {
				assertEquals(0, stat.getCount());
				found = true;
			}
		}
		assertTrue(found);
		
		stats = patientService.computeCustomQuestionAnswersReport(
				patient.getId(), haqs.get(0).getQuestions().get(0).getId(), dateFormat.parse("13.12.2010"), dateFormat.parse("13.12.2010"));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void processPatientDailyPost_Psoriasis() throws Exception {
		
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		patient1 = patientService.findById(patient1.getId());
					
		Disease disease = diseaseService.findByCode(DiseaseCodes.PSORIASIS_CODE);
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		assertFalse(patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date()));
		
		Haq haqPasiWerte = haqs.get(1);
		
		Date logDate = new Date();
		List<ProjectHPatientDailyPost> posts = new ArrayList<ProjectHPatientDailyPost>();
		ProjectHPatientDailyPost post = new ProjectHPatientDailyPost();
		post.setDiseaseId(disease.getId());
		post.setPatientId(patient1.getId());
		post.setLogDate(logDate);
		post.setHaqId(haqs.get(0).getId());
		post.setQuestionsId(new Long[]{ 
				haqPasiWerte.getQuestions().get(0).getId(), 
				haqPasiWerte.getQuestions().get(1).getId(),
				haqPasiWerte.getQuestions().get(2).getId(),
				haqPasiWerte.getQuestions().get(3).getId(),
				haqPasiWerte.getQuestions().get(4).getId(),
				haqPasiWerte.getQuestions().get(5).getId(),
				haqPasiWerte.getQuestions().get(6).getId(),
				haqPasiWerte.getQuestions().get(7).getId(),
				haqPasiWerte.getQuestions().get(8).getId(),
				haqPasiWerte.getQuestions().get(9).getId(),
				haqPasiWerte.getQuestions().get(10).getId(),
				haqPasiWerte.getQuestions().get(11).getId(),
				haqPasiWerte.getQuestions().get(12).getId(),
				haqPasiWerte.getQuestions().get(13).getId(),
				haqPasiWerte.getQuestions().get(14).getId(),
				haqPasiWerte.getQuestions().get(15).getId()
				
				});
		
		post.setAnswersId(new Long[]{ 
				haqPasiWerte.getQuestions().get(0).getQuestionType().getAnswers().get(1).getId(),
				haqPasiWerte.getQuestions().get(1).getQuestionType().getAnswers().get(2).getId(),
				haqPasiWerte.getQuestions().get(2).getQuestionType().getAnswers().get(3).getId(),
				haqPasiWerte.getQuestions().get(3).getQuestionType().getAnswers().get(4).getId(),
				
				haqPasiWerte.getQuestions().get(4).getQuestionType().getAnswers().get(1).getId(),
				haqPasiWerte.getQuestions().get(5).getQuestionType().getAnswers().get(2).getId(),
				haqPasiWerte.getQuestions().get(6).getQuestionType().getAnswers().get(3).getId(),
				haqPasiWerte.getQuestions().get(7).getQuestionType().getAnswers().get(4).getId(),
				
				haqPasiWerte.getQuestions().get(8).getQuestionType().getAnswers().get(1).getId(),
				haqPasiWerte.getQuestions().get(9).getQuestionType().getAnswers().get(2).getId(),
				haqPasiWerte.getQuestions().get(10).getQuestionType().getAnswers().get(3).getId(),
				haqPasiWerte.getQuestions().get(11).getQuestionType().getAnswers().get(4).getId(),
				
				haqPasiWerte.getQuestions().get(12).getQuestionType().getAnswers().get(1).getId(),
				haqPasiWerte.getQuestions().get(13).getQuestionType().getAnswers().get(2).getId(),
				haqPasiWerte.getQuestions().get(14).getQuestionType().getAnswers().get(3).getId(),
				haqPasiWerte.getQuestions().get(15).getQuestionType().getAnswers().get(4).getId(),
				});
		
		post.setCustomAnswers(new String[]{
				
				"22",
				null,
				null,
				null,
				"33",
				null,
				null,
				null,
				"11",
				null,
				null,
				null,
				"18",
				null,
				null,
				null,
		});
		
		posts.add(post);
				
		int answersCount = patientQuestionAnswerService.findAll().size();
		
		patientService.processPatientDailyPost(posts);
		
		assertEquals(answersCount + 16, patientQuestionAnswerService.findAll().size());
		
		boolean exist = patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date());
		assertTrue(exist);
		
		List<PatientKeyPerformanceIndicator> pkpis = patientKPIDao.findAll();
		assertEquals(7, pkpis.size());
		assertEquals(7.74d, pkpis.get(6).getValue().doubleValue(), 0.01);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.PASI, disease.getId());
		
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPITimeline(patient1.getId(), kpi.getId(), logDate, logDate);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new BigDecimal(13.8d).doubleValue(), result.get(0).getValue().doubleValue(), 0.01);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void processPatientDailyPost_MorbusBechterew() throws Exception {
		
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
					
		Disease disease = diseaseService.findByCode(DiseaseCodes.MORBUS_BECHTEREW_CODE);
		List<Haq> haqs = haqService.findByDisease(disease.getId());
		assertFalse(patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date()));
		
		
		Date logDate = new Date();
		List<ProjectHPatientDailyPost> posts = new ArrayList<ProjectHPatientDailyPost>();
		
		for (int i = 0; i < 4; i++) {
			ProjectHPatientDailyPost post = new ProjectHPatientDailyPost();
			post.setDiseaseId(disease.getId());
			post.setPatientId(patient1.getId());
			post.setLogDate(logDate);
			post.setHaqId(haqs.get(i).getId());
			post.setQuestionsId(new Long[]{ haqs.get(i).getQuestions().get(0).getId()});		
			post.setAnswersId(new Long[]{ haqs.get(i).getQuestions().get(0).getQuestionType().getAnswers().get(i + 2).getId() });
			posts.add(post);
		}
		
		ProjectHPatientDailyPost post = new ProjectHPatientDailyPost();
		post.setDiseaseId(disease.getId());
		post.setPatientId(patient1.getId());
		post.setLogDate(logDate);
		post.setHaqId(haqs.get(4).getId());
		post.setQuestionsId(new Long[]{ haqs.get(4).getQuestions().get(0).getId()});		
		post.setAnswersId(new Long[]{ haqs.get(4).getQuestions().get(0).getQuestionType().getAnswers().get(4).getId() });				
		posts.add(post);
		
		post = new ProjectHPatientDailyPost();
		post.setDiseaseId(disease.getId());
		post.setPatientId(patient1.getId());
		post.setLogDate(logDate);
		post.setHaqId(haqs.get(4).getId());
		post.setQuestionsId(new Long[]{ haqs.get(4).getQuestions().get(1).getId()});		
		post.setAnswersId(new Long[]{ haqs.get(4).getQuestions().get(1).getQuestionType().getAnswers().get(6).getId() });				
		posts.add(post);
				
		int answersCount = patientQuestionAnswerService.findAll().size();
		
		patientService.processPatientDailyPost(posts);
		
		assertEquals(answersCount + 6, patientQuestionAnswerService.findAll().size());
		
		boolean exist = patientService.patientAnswerExists(patient1.getId(), disease.getId(), new Date());
		assertTrue(exist);
		
		List<PatientKeyPerformanceIndicator> pkpis = patientKPIDao.findAll();
		assertEquals(7, pkpis.size());
		assertEquals(4.80d, pkpis.get(6).getValue().doubleValue(), 0.01);
		
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void findCustomQuestionsNullPatientId() throws Exception {
		patientService.findCustomQuestions(null, 1L);
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void findCustomQuestionsNullDiseaseId() throws Exception {
		patientService.findCustomQuestions(1L, null);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findCustomQuestions() throws Exception {
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		Doctor doctor = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		assertNotNull(disease);
		
		QuestionType yesNoQuestionType = questionTypeService.findById(1L);
		
		List<CustomQuestion> customQuestions = patientService.findCustomQuestions(patient1.getId(), disease.getId());
		assertNotNull(customQuestions);
		assertEquals(0, customQuestions.size());
		
		patientService.addCustomQuestion(patient1.getId(), disease.getId(), "Question defined by patient", "", yesNoQuestionType.getId());
		customQuestions = patientService.findCustomQuestions(patient1.getId(), disease.getId());
		assertEquals(1, customQuestions.size());
		
		doctorService.addCustomQuestion(doctor.getId(), patient1.getId(), disease.getId(), "Question defined by doctor", "", yesNoQuestionType.getId());
		customQuestions = patientService.findCustomQuestions(patient1.getId(), disease.getId());
		assertEquals(2, customQuestions.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAnswersForSingleAnswerEntryQuestions() throws Exception {
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		assertNotNull(disease);
		Date startDate = dateFormat.parse("1.10.2010");
		Date endDate = new Date();
		
		List<PatientQuestionAnswer> answers = 
			patientService.findAnswersForSingleAnswerEntryQuestions(patient1.getId(), disease.getId(), startDate, endDate);
		assertTrue(answers.isEmpty());
		
		Question saq = null;
		for (Question question : questionService.findAll()) {
			if (question.getHaq().getDisease().equals(disease) 
					&& question.getQuestionType().getType().equals(QuestionType.Type.SINGLE_ANSWER_ENTER)) {
				saq = question;
				break;
			}
		}
		assertNotNull(saq);
		
		Long id = patientQuestionAnswerService.saveAnswer(
				patient1.getId(), saq.getId(), saq.getQuestionType().getAnswers().get(0).getId(), null, dateFormat.parse("1.11.2010"));
		assertNotNull(id);
		answers = 
			patientService.findAnswersForSingleAnswerEntryQuestions(patient1.getId(), disease.getId(), startDate, endDate);
		assertEquals(1, answers.size());
		assertEquals(saq.getQuestionType().getAnswers().get(0), answers.get(0).getAnswer());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findFirstSymptomeAndDiagnosisDate() throws Exception {
		Patient patient1 = (Patient)userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		assertNotNull(disease);
		
		List<Haq> haqs = haqService.findByDisease(disease.getId());
				
		Pair<Date, Date> dates = 
			patientService.findFirstSymptomeAndDiagnosisDate(patient1.getId(), disease.getId());
		assertNotNull(dates);
		assertNull(dates.getFirst());
		assertNull(dates.getSecond());
		
		List<Question> questions = new ArrayList<Question>();
		for (Haq haq : haqs) {
			if (haq.getKind().equals(Haq.Kind.ONE_TIME)) {
				questions = haq.getQuestions();
				break;
			}
		}
		
		Question fsd = null;
		Question fdd = null;
		for (Question question : questions) {
			if (question.getQuestionType().getType().equals(QuestionType.Type.SINGLE_ANSWER_ENTER)
					&& question.getQuestionType().getAnswerDataType().equals(QuestionType.AnswerDataType.DATE)) {
				if (question.getSortOrder() == 5) {
					fsd = question;
				}
				
				if (question.getSortOrder() == 6) {
					fdd = question;
				}
			}
		}
		assertNotNull(fsd);
		assertNotNull(fdd);
		
		Date firstSymptomDate = dateFormat.parse("1.11.2009");
		Date firstDiagnosisDate = dateFormat.parse("1.11.2010");
		
		Long id = patientQuestionAnswerService.saveAnswer(
				patient1.getId(), fsd.getId(), fsd.getQuestionType().getAnswers().get(1).getId(), 
				"" + firstSymptomDate.getTime(), dateFormat.parse("1.11.2010"));
		assertNotNull(id);
		id = patientQuestionAnswerService.saveAnswer(
				patient1.getId(), fdd.getId(), fdd.getQuestionType().getAnswers().get(1).getId(), 
				"" + firstDiagnosisDate.getTime(), dateFormat.parse("1.11.2010"));
		assertNotNull(id);
		
		dates = patientService.findFirstSymptomeAndDiagnosisDate(patient1.getId(), disease.getId());
		assertNotNull(dates);
		assertEquals(firstSymptomDate, dates.getFirst());
		assertEquals(firstDiagnosisDate, dates.getSecond());
	}
}
