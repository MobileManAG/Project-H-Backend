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
 * QuestionDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 7.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.questionary;

import java.util.List;

import com.mobileman.projecth.domain.questionary.Answer;
import com.mobileman.projecth.domain.questionary.CustomQuestion;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link Question} entity
 * @author mobileman
 *
 */
public interface QuestionDao extends Dao<Question> {

	/**
	 * @param answerId
	 * @return answer found by its id
	 */
	Answer findAnswerById(Long answerId);
	
	/**
	 * Finds all custom questions defined for given user and disease
	 * 
	 * @param userId
	 * @param diseaseId
	 * @return all custom questions defined for given patient and disease
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> 
	 *             <li>diseaseId == null</li>
	 */
	List<CustomQuestion> findCustomQuestions(Long userId, Long diseaseId) throws IllegalArgumentException;
}
