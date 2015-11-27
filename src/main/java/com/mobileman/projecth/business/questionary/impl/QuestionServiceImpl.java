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
 * QuestionDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.questionary.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.questionary.QuestionService;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.CustomQuestion.CustomQuestionType;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao;
import com.mobileman.projecth.persistence.questionary.QuestionDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.QUESTION_SERVICE)
public class QuestionServiceImpl extends BusinessServiceImpl<Question> implements QuestionService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(QuestionServiceImpl.class);

	private QuestionDao questionDao;
	
	@Autowired
	private PatientQuestionAnswerDao patientQuestionAnswerDao;

	/**
	 * @param questionDao
	 */
	@Autowired
	public void setQuestionDao(QuestionDao questionDao) {
		if (log.isDebugEnabled()) {
			log.debug("setQuestionDao(QuestionDao) - start"); //$NON-NLS-1$
		}

		this.questionDao = questionDao;
		setDao(questionDao);

		if (log.isDebugEnabled()) {
			log.debug("setQuestionDao(QuestionDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#save(java.lang.Object)
	 */
	@Override
	public Long save(Question entity) {
		if (log.isDebugEnabled()) {
			log.debug("save(Question) - start"); //$NON-NLS-1$
		}

		if (CustomQuestion.class.isInstance(entity)) {
			CustomQuestion cq = CustomQuestion.class.cast(entity);
			
			if (cq.getCustomQuestionType() == null) {
				throw new IllegalArgumentException("CustomQuestionType must not be null");
			}
			
			if (cq.getCreator() == null) {
				throw new IllegalArgumentException("Creator must not be null");
			}
			
			if (cq.getUser() == null) {
				throw new IllegalArgumentException("User must not be null");
			}
			
			if (cq.getDisease() == null) {
				throw new IllegalArgumentException("Disease must not be null");
			}
			
			if (cq.getCustomQuestionType().equals(CustomQuestionType.NEW)) {
				if (cq.getTargetQuestion() != null) {
					throw new IllegalArgumentException("TargetQuestion must be null");
				}
			} else {
				if (cq.getTargetQuestion() == null) {
					throw new IllegalArgumentException("TargetQuestion must not be null");
				}
			}
			
		} else {
			if (entity.getHaq() == null) {
				throw new IllegalArgumentException("Haq must not be null");
			}
		}
		

		Long returnLong = super.save(entity);
		if (log.isDebugEnabled()) {
			log.debug("save(Question) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#delete(java.lang.Object)
	 */
	@Override
	public void delete(Question entity) {
		super.delete(entity);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.questionary.QuestionService#deleteCustomQuestion(java.lang.Long)
	 */
	@Override
	public void deleteCustomQuestion(Long questionId) {
		Question question = questionDao.findById(questionId);
		if (!question.getKind().equals(Question.Kind.CUSTOM)) {
			throw new IllegalArgumentException("Question with id=" + questionId + " is not of type CustomQuestion");
		}
		
		patientQuestionAnswerDao.deleteAnswers(questionId);
		super.delete(question);
	}
}
