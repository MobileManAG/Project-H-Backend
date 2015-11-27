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
package com.mobileman.projecth.business.patient.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.PatientMedicationService;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.patient.PatientQuestionAnswerService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.dto.patient.AnswerFrequency;
import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.util.patient.questionary.PatientQuestionAnswerHolder;
import com.mobileman.projecth.persistence.UserConnectionDao;
import com.mobileman.projecth.persistence.chart.HaqChartDao;
import com.mobileman.projecth.persistence.patient.PatientDao;
import com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao;
import com.mobileman.projecth.persistence.questionary.QuestionDao;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientInitialPost;
import com.mobileman.projecth.util.Pair;

/**
 * 
 */ 
@Service(ComponentNames.PATIENT_SERVICE)
public class PatientServiceImpl extends BusinessServiceImpl<Patient> implements PatientService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientServiceImpl.class);
	
	private PatientDao patientDao;
	
	@Autowired
	private PatientMedicationService patientMedicationService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	private UserConnectionDao userConnectionDao;
	
	@Autowired
	private QuestionTypeService questionTypeService;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private HaqChartDao haqChartDao;
		
	@Autowired
	private PatientQuestionAnswerDao patientQuestionAnswerDao;
	
	@Autowired
	private PatientQuestionAnswerService patientQuestionAnswerService;
	
	/**
	 * @param patientDao
	 */
	@Autowired
	public void setPatientDao(PatientDao patientDao) {
		if (log.isDebugEnabled()) {
			log.debug("setPatientDao(PatientDao) - start"); //$NON-NLS-1$
		}

		this.patientDao = patientDao;
		setDao(patientDao);

		if (log.isDebugEnabled()) {
			log.debug("setPatientDao(PatientDao) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#patientAnswerExists(java.lang.Long, java.lang.Long, java.util.Date)
	 */
	@Override
	public boolean patientAnswerExists(Long patientId, Long diseaseId, Date logDate) {
		if (log.isDebugEnabled()) {
			log.debug("patientAnswerExists(Long, Long, Date) - start"); //$NON-NLS-1$
		}

		boolean result = patientQuestionAnswerDao.answerExists(patientId, diseaseId, logDate);

		if (log.isDebugEnabled()) {
			log.debug("patientAnswerExists(Long, Long, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findOldestPatientAnswerDate(java.lang.Long)
	 */
	@Override
	public Date findOldestPatientAnswerDate(Long patientId) {
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientAnswerDate(Long) - start"); //$NON-NLS-1$
		}

		Date result = patientQuestionAnswerDao.findOldestPatientAnswerDate(patientId);

		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientAnswerDate(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findOldestPatientsAnswerDate()
	 */
	@Override
	public Date findOldestPatientsAnswerDate() {
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientsAnswerDate() - start"); //$NON-NLS-1$
		}

		Date result = patientQuestionAnswerDao.findOldestPatientsAnswerDate();

		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientsAnswerDate() - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#processPatientDailyPost(java.util.List)
	 */
	@Override
	public void processPatientDailyPost(List<ProjectHPatientDailyPost> posts)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("processPatientDailyPost(List<projecthPatientDailyPost>) - start"); //$NON-NLS-1$
		}
		
		List<PatientQuestionAnswerHolder> answers = new ArrayList<PatientQuestionAnswerHolder>();
		Disease disease = null;
		Patient patient = null;
		Date logDate = null;
		for (ProjectHPatientDailyPost post : posts) {
			if (logDate == null) {
				logDate = post.getLogDate();
			}
			
			if (disease == null) {
				if (posts.get(0).getDiseaseId() != null) {
					disease = diseaseService.findById(posts.get(0).getDiseaseId());
				} else {
					disease = diseaseService.findByCode(posts.get(0).getDiseaseCode());
				}
			}
			
			if (patient == null) {
				patient = findById(post.getPatientId());
			}
			
			Long[] questionsId = post.getQuestionsId();
			Long[] answersId = post.getAnswersId();
			String[] custAnswers = post.getCustomAnswers();
						
			for (int i = 0; i < questionsId.length; i++) {
				Long  questionaryId = questionsId[i];
				String custAnswer = custAnswers != null ? custAnswers[i] : null;
				answers.add(new PatientQuestionAnswerHolder(post.getHaqId(), questionaryId, answersId[i], custAnswer));
			}
		}
		
		if (disease != null && patient != null) {
			patientQuestionAnswerService.saveAnswers(patient.getId(), disease.getId(), logDate, answers);
		}

		if (log.isDebugEnabled()) {
			log.debug("processPatientDailyPost(List<projecthPatientDailyPost>) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#processPatientInitialPost(com.mobileman.projecth.services.ws.mobile.projecthPatientInitialPost)
	 */
	@Override
	public void processPatientInitialPost(ProjectHPatientInitialPost data)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("processPatientInitialPost(projecthPatientInitialPost) - start"); //$NON-NLS-1$
		}

		if (data.getDiseaseCode() == null 
				&& data.getDiseaseId() == null) {
			throw new IllegalArgumentException("Disease code or ID must not by null");
		}
		
		Patient patient = findById(data.getPatientId());
		
		final Disease disease;
		if (data.getDiseaseCode() != null) {
			disease = diseaseService.findByCode(data.getDiseaseCode());
		} else {
			disease = diseaseService.findById(data.getDiseaseId());
		}
		
		patientQuestionAnswerDao.removeAllOneTimeQuestionsAnswers(data.getPatientId(), disease.getId());
		
		for (int i = 0; i < data.getQuestionsId().length; i++) {
			patientQuestionAnswerService.saveAnswer(
					data.getPatientId(), 
					data.getQuestionsId()[i], 
					data.getAnswersId()[i], 
					data.getCustomAnswers()[i], new Date());
		}
		
		patient.setSex(data.getSex());
		patient.setBirthday(data.getBirthday());
		
		update(patient);

		if (log.isDebugEnabled()) {
			log.debug("processPatientInitialPost(projecthPatientInitialPost) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findAllByDoctor(java.lang.Long)
	 */
	@Override
	public List<UserConnection> findAllByDoctor(Long doctorId) {
		if (log.isDebugEnabled()) {
			log.debug("findAllByDoctor(Long) - start"); //$NON-NLS-1$
		}
		
		List<UserConnection> result = userConnectionDao.findAllByDoctor(doctorId);
			

		if (log.isDebugEnabled()) {
			log.debug("findAllByDoctor(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#getPatientInfo(java.lang.Long)
	 */
	@Override
	public Patient getPatientInfo(Long patientId) {
		if (log.isDebugEnabled()) {
			log.debug("getPatientInfo(Long) - start"); //$NON-NLS-1$
		}

		Patient returnPatient = findById(patientId);
		if (log.isDebugEnabled()) {
			log.debug("getPatientInfo(Long) - returns"); //$NON-NLS-1$
		}
		return returnPatient;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#addConsumedMedication(Long, Long, Long, double, Date, String)
	 */
	@Override
	public void addConsumedMedication(Long patientId, Long diseaseId,
			Long medicationId, double standarUnitsTaken, Date consumptionDate, String comment)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(Long, Long, Long, double) - start"); //$NON-NLS-1$
		}

		this.patientMedicationService.addConsumedMedication(patientId, diseaseId, medicationId, standarUnitsTaken, consumptionDate, comment);

		if (log.isDebugEnabled()) {
			log.debug("addConsumedMedication(Long, Long, Long, double) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findConsumedMedications(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Medication> findConsumedMedications(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedications(Long, Long) - start"); //$NON-NLS-1$
		}

		List<Medication> returnList = this.patientMedicationService.findAllConsumedMedications(patientId, diseaseId);
		if (log.isDebugEnabled()) {
			log.debug("findConsumedMedications(Long, Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#addCustomQuestion(Long, Long, String, String, Long)
	 */
	@Override
	public Long addCustomQuestion(Long patientId, Long diseaseId, String questionText, String explanation, Long questionTypeId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(" + patientId + ", " + diseaseId + ", " + questionText + ", " 
					+ explanation + ", " + questionTypeId + ") - start"); //$NON-NLS-1$
		}

		Patient patient = findById(patientId);
		Disease disease = diseaseService.findById(diseaseId);
		QuestionType questionType = questionTypeService.findById(questionTypeId);
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText(questionText);
		customQuestion.setDisease(disease);
		customQuestion.setUser(patient);
		customQuestion.setCreator(patient);
		customQuestion.setQuestionType(questionType);
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setSortOrder(0);
		customQuestion.setExplanation(explanation);
		
		Long id = questionDao.save(customQuestion);

		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(Long, Long, String, String, Long) - returns"); //$NON-NLS-1$
		}
		return id;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computePositiveAnswerFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<AnswerFrequency> computePositiveAnswerFrequencyReport(Long patientId, Long haqChartId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computePositiveAnswerFrequencyReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (haqChartId == null) {
			throw new IllegalArgumentException("haqChartId can not be null");
		}
		
		List<AnswerFrequency> result;
		HaqChart haqChart = this.haqChartDao.findById(haqChartId);
		if (haqChart.getQuestions() != null && !haqChart.getQuestions().isEmpty()) {
			// not implemented yet 
			for (com.mobileman.projecth.domain.questionary.Question question : haqChart.getQuestions()) {
				
			}
			// 
			result = new ArrayList<AnswerFrequency>();
		} else {
			
		}
		
		result = patientQuestionAnswerDao.computePositiveAnswerFrequencyReport(patientId, haqChart.getHaq().getId(), startDate, endDate);
		

		if (log.isDebugEnabled()) {
			log.debug("computePositiveAnswerFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeAllAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Object[]> computeAllAnswersFrequencyReport(Long patientId, Long haqChartId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeAllAnswersFrequencyReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (haqChartId == null) {
			throw new IllegalArgumentException("haqChartId can not be null");
		}
		
		HaqChart haqChart = this.haqChartDao.findById(haqChartId);
		

		List<Object[]> returnList = patientQuestionAnswerDao.computeAllAnswersFrequencyReport(patientId, haqChart.getHaq().getId(), startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("computeAllAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeQuestionAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Object[]> computeQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate,
			Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersFrequencyReport(" + patientId + ", " + questionId + ", " + startDate  + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (questionId == null) {
			throw new IllegalArgumentException("questionId can not be null");
		}
		
		List<Object[]> returnList = patientQuestionAnswerDao.computeQuestionAnswersFrequencyReport(patientId, questionId, startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeQuestionaryAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeQuestionaryAnswersReport(Long patientId, Long haqChartId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (haqChartId == null) {
			throw new IllegalArgumentException("haqChartId can not be null");
		}
		
		HaqChart haqChart = this.haqChartDao.findById(haqChartId);
		

		List<PatientQuestionaryAnswerStatistic> returnList = patientQuestionAnswerDao.computeQuestionaryAnswersReport(patientId, haqChart.getHaq().getId(), startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findCustomQuestions(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<CustomQuestion> findCustomQuestions(Long patientId, Long diseaseId) throws IllegalArgumentException {
		return questionDao.findCustomQuestions(patientId, diseaseId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#addCustomQuestion(java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, com.mobileman.projecth.domain.questionary.QuestionType)
	 */
	@Override
	public Long addCustomQuestion(Long patientId, Long diseaseId, String text, String explanation,
			QuestionType questionType) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(" + patientId + ", " + diseaseId + ", " + text + ", " 
					+ explanation + ", " + questionType + ") - start"); //$NON-NLS-1$
		}
		
		if (questionType == null) {
			throw new IllegalArgumentException("QuestionType is undefined");
		}

		Patient patient = findById(patientId);
		Disease disease = diseaseService.findById(diseaseId);
		
		questionTypeService.save(patientId, questionType);
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText(text);
		customQuestion.setDisease(disease);
		customQuestion.setUser(patient);
		customQuestion.setCreator(patient);
		customQuestion.setQuestionType(questionType);
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setSortOrder(0);
		customQuestion.setExplanation(explanation);
		
		Long id = questionDao.save(customQuestion);

		if (log.isDebugEnabled()) {
			log.debug("addCustomQuestion(Long, Long, String, String, QuestionType) - returns"); //$NON-NLS-1$
		}
		return id;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findAnswersForSingleAnswerEntryQuestions(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionAnswer> findAnswersForSingleAnswerEntryQuestions(Long patientId, Long diseaseId,
			Date startDate, Date endDate) throws IllegalArgumentException {
		
		return patientQuestionAnswerDao.findAnswersForSingleAnswerEntryQuestions(patientId, diseaseId, startDate, endDate);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findFirstSymptomeAndDiagnosisDate(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Pair<Date, Date> findFirstSymptomeAndDiagnosisDate(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		return patientQuestionAnswerDao.findFirstSymptomeAndDiagnosisDate(patientId, diseaseId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#existsAnswerToOneTimeQuesion(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean existsAnswerToOneTimeQuesion(Long patientId, Long diseaseId) {
		return patientQuestionAnswerDao.existsAnswerToOneTimeQuesion(patientId, diseaseId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeQuestionAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersReport(" + patientId+ ", " + questionId+ ", " + startDate+ ", " + endDate+ ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (questionId == null) {
			throw new IllegalArgumentException("questionId can not be null");
		}
		
		List<PatientQuestionaryAnswerStatistic> result = patientQuestionAnswerDao.computeQuestionAnswersReport(
				patientId, questionId, startDate, endDate);

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeCustomQuestionAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Object[]> computeCustomQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate,
			Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersFrequencyReport(" + patientId + ", " + questionId + ", " + startDate  + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (questionId == null) {
			throw new IllegalArgumentException("questionId can not be null");
		}
		
		List<Object[]> returnList = patientQuestionAnswerDao.computeCustomQuestionAnswersFrequencyReport(patientId, questionId, startDate, endDate);
		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#computeCustomQuestionAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeCustomQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersReport(" + patientId+ ", " + questionId+ ", " + startDate+ ", " + endDate+ ") - start"); //$NON-NLS-1$
		}

		if (patientId == null) {
			throw new IllegalArgumentException("patientId can not be null");
		}
		
		if (questionId == null) {
			throw new IllegalArgumentException("questionId can not be null");
		}
		
		List<PatientQuestionaryAnswerStatistic> result = patientQuestionAnswerDao.computeCustomQuestionAnswersReport(
				patientId, questionId, startDate, endDate);

		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.PatientService#findAllPatientsWithAccounts()
	 */
	@Override
	public List<Patient> findAllPatientsWithAccounts() {
		return patientDao.findAllPatientsWithAccounts();
	}
}
