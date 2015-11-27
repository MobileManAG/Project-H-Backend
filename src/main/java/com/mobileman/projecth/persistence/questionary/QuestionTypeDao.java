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
 * QuestionTypeDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.questionary;

import java.util.List;

import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link QuestionType} entity
 * 
 * @author mobileman
 *
 */
public interface QuestionTypeDao extends Dao<QuestionType> {

	/**
	 * Finds a question type for given type and given answer data type
	 * 
	 * @param type
	 * @param answerDataType
	 * @return question type for given type and given answer data type
	 * @throws IllegalArgumentException if
	 * <li>type == null</li>
	 * <li>answerDataType == null</li>
	 */
	QuestionType find(QuestionType.Type type, QuestionType.AnswerDataType answerDataType)
			throws IllegalArgumentException;
	
	/**
	 * Finds a question type for given type and given answer data type created by given user
	 * @param userId 
	 * 
	 * @param type
	 * @param answerDataType
	 * @return question type for given type and given answer data type created by given user
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>type == null</li>
	 * <li>answerDataType == null</li>
	 */
	QuestionType find(Long userId, QuestionType.Type type, QuestionType.AnswerDataType answerDataType)
			throws IllegalArgumentException;
	
	/**
	 * Finds all question types for given type and given answer data type
	 * 
	 * @param type
	 * @param answerDataType
	 * @return question type for given type and given answer data type
	 * @throws IllegalArgumentException if
	 * <li>type == null</li>
	 * <li>answerDataType == null</li>
	 */
	List<QuestionType> findAll(QuestionType.Type type, QuestionType.AnswerDataType answerDataType)
			throws IllegalArgumentException;
}
