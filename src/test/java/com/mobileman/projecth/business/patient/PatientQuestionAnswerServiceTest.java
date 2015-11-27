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
 * PatientQuestionAnswerServiceTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.impl.ConfigurationServiceImpl;
import com.mobileman.projecth.business.questionary.QuestionService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
public class PatientQuestionAnswerServiceTest extends TestCaseBase {
	
	@Autowired
	private PatientQuestionAnswerService patientQuestionAnswerService;
	
	@Autowired
	QuestionTypeService questionTypeService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	HaqService haqService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	private ConfigurationServiceImpl configurationService;
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveAnswer() throws Exception {
		
		Question question = questionService.findAll().get(0);
		Answer answer = question.getQuestionType().getAnswers().get(0);
		
		User patient = userService.findUserByLogin("sysuser1");
		assertNotNull(patient);
		
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date logDate = dateFormat.parse("1.1.2011");
		
		int count = patientQuestionAnswerService.findAll().size();
		
		patientQuestionAnswerService.saveAnswer(patient.getId(), question.getId(), answer.getId(), "custom", logDate);
		
		assertEquals(count + 1, patientQuestionAnswerService.findAll().size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveFileEntryAnswer() throws Exception {
		DateFormat dirNameDateFormat = new SimpleDateFormat("yyyy_MM");
		DateFormat fileNameDateFormat = new SimpleDateFormat("hh_mm_ss");
		
		Disease psoriasis = diseaseService.findByCode(DiseaseCodes.PSORIASIS_CODE);		
		List<Haq> haqs = haqService.findByDisease(psoriasis.getId());
		Haq haq1 = haqs.get(0);
		assertEquals(1, haq1.getQuestions().size());
		
		Question question = haq1.getQuestions().get(0);
		Answer noAnswer = question.getQuestionType().getAnswers().get(0);
		assertFalse(noAnswer.isActive());
		Answer fileAnswer = question.getQuestionType().getAnswers().get(1);
		assertTrue(fileAnswer.isActive());
		
		User patient = userService.findUserByLogin("sysuser1");
		assertNotNull(patient);
		
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date logDate = dateFormat.parse("1.1.2011");
		
		File tmpFile = File.createTempFile("projecth", ".test");
				
		configurationService.setImagesRootDirectoryPath(tmpFile.getParent());
		
		int count = patientQuestionAnswerService.findAll().size();
		
		patientQuestionAnswerService.saveAnswer(patient.getId(), question.getId(), noAnswer.getId(), tmpFile.getPath(), logDate);
		List<PatientQuestionAnswer> answers = patientQuestionAnswerService.findAll();
		assertEquals(count + 1, answers.size());
		assertEquals(null, answers.get(answers.size() - 1).getCustomAnswer());
		
		/////////////// POSITIVE
		patientQuestionAnswerService.saveAnswer(patient.getId(), question.getId(), fileAnswer.getId(), tmpFile.getPath(), logDate);
		
		answers = patientQuestionAnswerService.findAll();
		assertEquals(count + 2, answers.size());
		assertEquals(patient.getId() 
				+ File.separator + psoriasis.getId() 
				+ File.separator + dirNameDateFormat.format(new Date())
				+ File.separator + fileNameDateFormat.format(new Date()) + ".test", answers.get(answers.size() - 1).getCustomAnswer());
		
		
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void exportData() throws Exception {
		
		HaqService haqService = (HaqService)applicationContext.getBean(ComponentNames.HAQ_SERVICE);
		assertNotNull(haqService);
		
		List<Haq> haqs = haqService.findAll();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		List<PatientQuestionAnswer> result = 
			patientQuestionAnswerService.exportData(haqs.get(0).getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("1.11.2010"));
		assertEquals(4, result.size());
		
		result = patientQuestionAnswerService.exportData(haqs.get(0).getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("3.11.2010"));
		assertEquals(12, result.size());
		
		result = patientQuestionAnswerService.exportData(null, dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(27, result.size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findAll() throws Exception {
		
		User patient = userService.findUserByLogin("sysuser1");
		assertNotNull(patient);
		
		List<Haq> haqs = haqService.findAll();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Question question = null;
		for (Question q : haqs.get(0).getQuestions()) {
			if ("Geschwollen rechte Hand Daumen vorne".equals(q.getText())) {
				question = q;
				break;
			}
		}
		
		List<PatientQuestionAnswer> result = 
			patientQuestionAnswerService.findAll(patient.getId(), question.getId(), dateFormat.parse("1.1.2010"), dateFormat.parse("1.11.2010"));
		assertEquals(1, result.size());
		
	}
}

