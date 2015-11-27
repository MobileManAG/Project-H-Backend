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
 * QuestionTypeServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.questionary.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.Answer.Kind;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.domain.questionary.QuestionType.Type;
import com.mobileman.projecth.persistence.UserDao;
import com.mobileman.projecth.persistence.questionary.QuestionTypeDao;

/**
 * @author mobileman
 *
 */
@Service(value = ComponentNames.QUESTION_TYPE_SERVICE)
public class QuestionTypeServiceImpl extends BusinessServiceImpl<QuestionType> implements QuestionTypeService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(QuestionTypeServiceImpl.class);

	private QuestionTypeDao questionTypeDao;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * @param questionTypeDao new value of questionTypeDao
	 */
	@Autowired
	public void setQuestionTypeDao(QuestionTypeDao questionTypeDao) {
		if (log.isDebugEnabled()) {
			log.debug("setQuestionTypeDao(QuestionTypeDao) - start"); //$NON-NLS-1$
		}

		this.questionTypeDao = questionTypeDao;
		setDao(questionTypeDao);

		if (log.isDebugEnabled()) {
			log.debug("setQuestionTypeDao(QuestionTypeDao) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.questionary.QuestionTypeService#save(java.lang.Long, com.mobileman.projecth.domain.questionary.QuestionType)
	 */
	@Override
	public void save(Long userId, QuestionType questionType) {
		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		if (questionType == null) {
			throw new IllegalArgumentException("questionType must not be null");
		}
		
		switch (questionType.getType()) {
		case MULTIPLE_CHOICE_LIST:
		case SINGLE_CHOICE_LIST:
			if (questionType.getAnswers() == null || questionType.getAnswers().size() < 2) {
				throw new IllegalArgumentException("Number of answers must be greather than 1");
			}
			break;
		case SCALE:
		case SINGLE_ANSWER_ENTER:
			if (questionType.getAnswers() == null || questionType.getAnswers().size() != 2) {
				throw new IllegalArgumentException("Number of answers must be equal to 2");
			}
			break;
		default:
			break;
		}
		
		boolean noAnswerFound = false;
		for (Answer answer : questionType.getAnswers()) {
			if (answer.getKind().equals(Kind.NO_ANSWER)) {
				noAnswerFound = true;
				break;
			}
		}
		
		if (!noAnswerFound) {
			throw new IllegalArgumentException("NoAnswer does not exists");
		}
		
		questionType.setUser(userDao.findById(userId));
		questionTypeDao.save(questionType);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.questionary.QuestionTypeService#find(com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@Override
	public QuestionType find(Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		return questionTypeDao.find(type, answerDataType);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.questionary.QuestionTypeService#find(java.lang.Long, com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@Override
	public QuestionType find(Long userId, Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		return questionTypeDao.find(userId, type, answerDataType);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.questionary.QuestionTypeService#findAll(com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@Override
	public List<QuestionType> findAll(Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		return questionTypeDao.findAll(type, answerDataType);
	}
}
