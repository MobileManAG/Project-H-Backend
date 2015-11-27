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
 * QuestionServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 7.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.questionary;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.OneTimeHaq;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.Haq;

/**
 * @author mobileman
 *
 */
public class QuestionServiceTest extends TestCaseBase {

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
	
	/**
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveCustomQuestion_NullUser() throws Exception {
		
		List<QuestionType> qts = questionTypeService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<Haq> haqs = haqService.findAll();
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setCreator(doctor);
		customQuestion.setDisease(diseases.get(0));
		
		questionService.save(customQuestion);
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveCustomQuestion_NullCreator() throws Exception {
		
		List<QuestionType> qts = questionTypeService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<Haq> haqs = haqService.findAll();
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setUser(doctor);
		customQuestion.setDisease(diseases.get(0));
		
		questionService.save(customQuestion);
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveCustomQuestion_NullDisease() throws Exception {
		
		List<QuestionType> qts = questionTypeService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<Haq> haqs = haqService.findAll();
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setUser(doctor);
		customQuestion.setCreator(doctor);
		
		questionService.save(customQuestion);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveCustomQuestion_NEW() throws Exception {
		
		int count = questionService.findAll().size();
		List<QuestionType> qts = questionTypeService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<Haq> haqs = haqService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setUser(doctor);
		customQuestion.setCreator(doctor);
		customQuestion.setDisease(diseases.get(0));
		customQuestion.setTag("testTag");
		
		questionService.save(customQuestion);
		
		List<Question> questions = questionService.findAll();
		assertEquals(count + 1, questions.size());
		
		Question question = questions.get(questions.size() - 1);
		assertEquals(customQuestion.getHaq(), question.getHaq());
		assertEquals(customQuestion.getQuestionType().getId(), question.getQuestionType().getId());
		assertEquals(customQuestion.getText(), question.getText());
		assertEquals(customQuestion.getUser(), CustomQuestion.class.cast(question).getUser());
		assertEquals("testTag", customQuestion.getTag());
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void deleteCustomQuestion_NotCustom() throws Exception {
		Question question = questionService.findAll().get(0);
		questionService.deleteCustomQuestion(question.getId());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void deleteCustomQuestion() throws Exception {
		
		int count = questionService.findAll().size();
		List<QuestionType> qts = questionTypeService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<Haq> haqs = haqService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setCustomQuestionType(CustomQuestionType.NEW);
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setUser(doctor);
		customQuestion.setCreator(doctor);
		customQuestion.setDisease(diseases.get(0));
		
		questionService.save(customQuestion);
		
		List<Question> questions = questionService.findAll();
		assertEquals(count + 1, questions.size());
		
		Question question = questions.get(questions.size() - 1);
		questionService.deleteCustomQuestion(question.getId());
		
		questions = questionService.findAll();
		assertEquals(count, questions.size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveCustomQuestion_HIDE() throws Exception {
		
		List<QuestionType> qts = questionTypeService.findAll();
		
		List<Haq> haqs = haqService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Question question = new Question();
		question.setText("qd");
		question.setHaq(haqs.get(0));
		question.setQuestionType(qts.get(0));
		questionService.save(question);
		List<Question> existingQuestions = questionService.findAll();
		question = existingQuestions.get(0);
		
		CustomQuestion customQuestion = new CustomQuestion();
		customQuestion.setCustomQuestionType(CustomQuestionType.HIDE);
		customQuestion.setText("q1");
		customQuestion.setHaq(haqs.get(0));
		customQuestion.setQuestionType(qts.get(0));
		customQuestion.setUser(doctor);
		customQuestion.setCreator(doctor);
		customQuestion.setDisease(diseases.get(0));
		
		try {
			questionService.save(customQuestion);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		customQuestion.setTargetQuestion(question);
		
		questionService.save(customQuestion);
		
		List<Question> questions = questionService.findAll();
		assertEquals(existingQuestions.size() + 1, questions.size());
		
		assertEquals(customQuestion.getHaq(), questions.get(questions.size() - 1).getHaq());
		assertEquals(customQuestion.getQuestionType(), questions.get(questions.size() - 1).getQuestionType());
		assertEquals(customQuestion.getText(), questions.get(questions.size() - 1).getText());
		assertEquals(customQuestion.getUser(), CustomQuestion.class.cast(questions.get(questions.size() - 1)).getUser());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveOneTimeQuestion() throws Exception {
		
		List<QuestionType> qts = questionTypeService.findAll();
		
		List<Haq> haqs = haqService.findAll();
		List<Disease> diseases = diseaseService.findAll();
		OneTimeHaq haq = null;
		for (Haq tmp : haqs) {
			if (tmp.getKind().equals(Haq.Kind.ONE_TIME)) {
				haq = (OneTimeHaq)tmp;
				break;
			}
		}
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Question question = new Question();
		question.setText("qd");
		question.setHaq(haq);
		question.setQuestionType(qts.get(0));
		question.setTag("aTag");
		questionService.save(question);
		
		List<Question> existingQuestions = questionService.findAll();
		question = existingQuestions.get(existingQuestions.size() - 1);
		
		assertEquals("aTag", question.getTag());
		haqs = haqService.findAll();
		for (Haq tmp : haqs) {
			if (tmp.getKind().equals(Haq.Kind.ONE_TIME)) {
				haq = (OneTimeHaq)tmp;
				break;
			}
		}
		
	}
}
