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
 * PatientQuestionAnswerHolder.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.patient.questionary;

/**
 * @author mobileman
 *
 */
public class PatientQuestionAnswerHolder {

	private Long haqId; 
	private Long questionId; 
	private Long answerId; 
	private String customAnswer;
	
	
	
	/**
	 * @param haqId
	 * @param questionId
	 * @param answerId
	 * @param customAnswer
	 */
	public PatientQuestionAnswerHolder(Long haqId, Long questionId, Long answerId, String customAnswer) {
		super();
		this.haqId = haqId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.customAnswer = customAnswer;
	}
	
	
	
	/**
	 * 
	 */
	public PatientQuestionAnswerHolder() {
		super();
	}



	/**
	 * @return questionId
	 */
	public Long getQuestionId() {
		return this.questionId;
	}
	/**
	 * @param questionId new value of questionId
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	/**
	 * @return answerId
	 */
	public Long getAnswerId() {
		return this.answerId;
	}
	/**
	 * @param answerId new value of answerId
	 */
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	/**
	 * @return customAnswer
	 */
	public String getCustomAnswer() {
		return this.customAnswer;
	}
	/**
	 * @param customAnswer new value of customAnswer
	 */
	public void setCustomAnswer(String customAnswer) {
		this.customAnswer = customAnswer;
	}
	/**
	 * @return haqId
	 */
	public Long getHaqId() {
		return this.haqId;
	}
	/**
	 * @param haqId new value of haqId
	 */
	public void setHaqId(Long haqId) {
		this.haqId = haqId;
	}
	
	
}
