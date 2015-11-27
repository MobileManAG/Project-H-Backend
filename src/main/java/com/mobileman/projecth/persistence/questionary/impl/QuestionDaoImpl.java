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
 * @date 7.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.questionary.impl;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.questionary.QuestionDao;

/**
 * @author mobileman
 *
 */
@Repository(value = "questionDao")
public class QuestionDaoImpl extends DaoImpl<Question> implements QuestionDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(QuestionDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.questionary.QuestionDao#findAnswerById(java.lang.Long)
	 */
	@Override
	public Answer findAnswerById(Long answerId) {
		if (log.isDebugEnabled()) {
			log.debug("findAnswerById(Long) - start"); //$NON-NLS-1$
		}

		Answer answer = (Answer) getHibernateTemplate().load(Answer.class, answerId);		

		if (log.isDebugEnabled()) {
			log.debug("findAnswerById(Long) - returns"); //$NON-NLS-1$
		}
		return answer;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.questionary.QuestionDao#findCustomQuestions(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomQuestion> findCustomQuestions(Long userId, Long diseaseId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersStatistics(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		if (userId == null) {
			throw new IllegalArgumentException("user id must not be null");
		}
		
		if (diseaseId == null) {
			throw new IllegalArgumentException("disease id must not be null");
		}

		List<CustomQuestion> queryResult = (List)getHibernateTemplate().find(
				"select cq from CustomQuestion cq " +
				"join cq.disease d " +
				"join cq.user u " +
				"where d.id=? " +
				"and u.id=? " +
				"order by cq.sortOrder ", 
				new Object[]{ diseaseId, userId } );
		

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersStatistics(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return queryResult;
	}

}
