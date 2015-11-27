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
 * QuestionTypeDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.questionary.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.domain.questionary.QuestionType.Type;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.questionary.QuestionTypeDao;

/**
 * @author mobileman
 *
 */
@Repository(value = "questionTypeDao")
public class QuestionTypeDaoImpl extends DaoImpl<QuestionType> implements QuestionTypeDao {

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.questionary.QuestionTypeDao#find(com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public QuestionType find(Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}
		
		if (answerDataType == null) {
			throw new IllegalArgumentException("answerDataType must not be null");
		}
		
		List<QuestionType> queryResult = (List)getHibernateTemplate().find(
				"select qt from QuestionType qt " +
				"where qt.type=? and qt.answerDataType=?", 
				new Object[]{ type, answerDataType } );
		
		return queryResult.isEmpty() ? null : queryResult.get(0);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.questionary.QuestionTypeDao#find(java.lang.Long, com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QuestionType find(Long userId, Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}
		
		if (answerDataType == null) {
			throw new IllegalArgumentException("answerDataType must not be null");
		}
		
		List<QuestionType> queryResult = (List)getHibernateTemplate().find(
				"select qt from QuestionType qt " +
				"where qt.user.id=? and qt.type=? and qt.answerDataType=?", 
				new Object[]{ userId, type, answerDataType } );
		
		return queryResult.isEmpty() ? null : queryResult.get(0);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.questionary.QuestionTypeDao#findAll(com.mobileman.projecth.domain.questionary.QuestionType.Type, com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<QuestionType> findAll(Type type, AnswerDataType answerDataType) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}
		
		if (answerDataType == null) {
			throw new IllegalArgumentException("answerDataType must not be null");
		}
		
		List<QuestionType> queryResult = (List)getHibernateTemplate().find(
				"select qt from QuestionType qt " +
				"where qt.type=? and qt.answerDataType=?", 
				new Object[]{ type, answerDataType } );
		
		return queryResult;
	}

}
