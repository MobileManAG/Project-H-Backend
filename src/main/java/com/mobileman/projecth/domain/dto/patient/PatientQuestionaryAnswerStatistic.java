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
 * PatientAnswerStatistic.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 7.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.dto.patient;


/**
 * @author mobileman
 *
 */
public class PatientQuestionaryAnswerStatistic {

	private String haqDescription;
	private String questionText;
	private String answerText;
	private long count;
	private final Long haqId;
	private final Long questionId;
	private final Long answerId;
		
	/**
	 * @param haqDescription
	 * @param haqId 
	 * @param questionText
	 * @param questionId 
	 * @param answerText
	 * @param answerId 
	 * @param count 
	 */
	public PatientQuestionaryAnswerStatistic(String haqDescription, Long haqId,
			String questionText, Long questionId, String answerText, Long answerId, long count) {
		super();
		this.haqDescription = haqDescription;
		this.haqId = haqId;
		this.questionText = questionText;
		this.questionId = questionId;
		this.answerText = answerText;
		this.answerId = answerId;
		this.count = count;
	}
	/**
	 * Gets a haqDescription
	 *
	 * @return haqDescription
	 */
	public String getHaqDescription() {
		return this.haqDescription;
	}
	/**
	 * Setter for haqDescription
	 *
	 * @param haqDescription new value of haqDescription
	 */
	public void setHaqDescription(String haqDescription) {
		this.haqDescription = haqDescription;
	}
	/**
	 * Gets a questionText
	 *
	 * @return questionText
	 */
	public String getQuestionText() {
		return this.questionText;
	}
	/**
	 * Setter for questionText
	 *
	 * @param questionText new value of questionText
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	/**
	 * Gets a answerText
	 *
	 * @return answerText
	 */
	public String getAnswerText() {
		return this.answerText;
	}
	/**
	 * Setter for answerText
	 *
	 * @param answerText new value of answerText
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	/**
	 * Gets a answerId
	 *
	 * @return answerId
	 */
	public Long getAnswerId() {
		return this.answerId;
	}
	
	/**
	 * Gets a haqId
	 *
	 * @return haqId
	 */
	public Long getHaqId() {
		return this.haqId;
	}
	
	/**
	 * Gets a questionId
	 *
	 * @return questionId
	 */
	public Long getQuestionId() {
		return this.questionId;
	}
	
	/**
	 * Gets a count
	 *
	 * @return count
	 */
	public long getCount() {
		return this.count;
	}
}
