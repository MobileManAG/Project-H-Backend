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
 * QuestionTypeServicetext.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.questionary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.NoAnswer;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.domain.questionary.QuestionType.Type;
import com.mobileman.projecth.domain.user.User;

/**
 * Unit tests for {@link QuestionTypeService}
 * 
 * @author mobileman
 *
 */
public class QuestionTypeServiceTest extends TestCaseBase {

	@Autowired
	QuestionTypeService questionTypeService;
	
	@Autowired
	UserService userService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAll() throws Exception {
		List<QuestionType> questionTypes = questionTypeService.findAll();
		assertFalse(questionTypes.isEmpty());
		
		assertFalse(questionTypes.get(0).getAnswers().isEmpty());
		assertFalse(questionTypes.get(0).getTags().isEmpty());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void saveNullUserId() throws Exception {
		questionTypeService.save(null, new QuestionType());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void saveNullQuestionType() throws Exception {
		questionTypeService.save(1L, null);
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void save() throws Exception {
		List<QuestionType> questionTypesOld = questionTypeService.findAll();
		assertFalse(questionTypesOld.isEmpty());
		
		User patient1 = userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		
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
		
		questionTypeService.save(patient1.getId(), questionType);
		
		List<QuestionType> questionTypes = questionTypeService.findAll();
		assertEquals(questionTypesOld.size() + 1, questionTypes.size());
		
		assertEquals(questionType.getUser().getId(), questionTypes.get(questionTypes.size() - 1).getUser().getId());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void find() throws Exception {
		
		QuestionType qt = questionTypeService.find(QuestionType.Type.SINGLE_CHOICE_LIST, AnswerDataType.BOOLEAN);
		assertNotNull(qt);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findForUser() throws Exception {
		User patient1 = userService.findUserByLogin("sysuser1");
		assertNotNull(patient1);
		
		QuestionType qt = questionTypeService.find(patient1.getId(), QuestionType.Type.SINGLE_CHOICE_LIST, AnswerDataType.BOOLEAN);
		assertNotNull(qt);
	}
}
