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
 * projecthPatientPost.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 23.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.services.ws.mobile;

import java.util.Date;

import com.mobileman.projecth.domain.disease.Disease;

/**
 * Holder of the user's transmitted data - base part
 * @author mobileman
 *
 */
public class ProjectHPatientPost {

	private Long patientId;
	
	private String activationCode;
	
	private Date logDate;
	
	private String diseaseCode;
	private Long diseaseId;
	
	private Long[] questionsId;
	private Long[] answersId;
	private String[] customAnswers;
	
	/**
	 * Gets a patientId
	 *
	 * @return patientId
	 */
	public Long getPatientId() {
		return this.patientId;
	}

	/**
	 * Setter for patientId
	 *
	 * @param patientId nova hodnota patientId
	 */
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	/**
	 * Gets a activationCode
	 *
	 * @return activationCode
	 */
	public String getActivationCode() {
		return this.activationCode;
	}

	/**
	 * Setter for activationCode
	 *
	 * @param activationCode nova hodnota activationCode
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * Gets a logDate
	 *
	 * @return logDate
	 */
	public Date getLogDate() {
		return this.logDate;
	}

	/**
	 * Setter for logDate
	 *
	 * @param logDate nova hodnota logDate
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	/**
	 * @return code of a disease ({@link Disease}) to which given haq answers belong
	 */
	public String getDiseaseCode() {
		return this.diseaseCode;
	}
	
	/**
	 * @param diseaseCode code of a disease ({@link Disease}) to which given haq answers belong
	 */
	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	
	/**
	 * @return diseaseId
	 */
	public Long getDiseaseId() {
		return this.diseaseId;
	}

	/**
	 * @param diseaseId new value of diseaseId
	 */
	public void setDiseaseId(Long diseaseId) {
		this.diseaseId = diseaseId;
	}

	/**
	 * @return questionsId
	 */
	public Long[] getQuestionsId() {
		return this.questionsId;
	}
	/**
	 * @param questionsId new value of questionsId
	 */
	public void setQuestionsId(Long[] questionsId) {
		this.questionsId = questionsId;
	}
	/**
	 * @return answersId
	 */
	public Long[] getAnswersId() {
		return this.answersId;
	}
	/**
	 * @param answersId new value of answersId
	 */
	public void setAnswersId(Long[] answersId) {
		this.answersId = answersId;
	}
	/**
	 * @return customAnswers
	 */
	public String[] getCustomAnswers() {
		return this.customAnswers;
	}
	/**
	 * @param customAnswers new value of customAnswers
	 */
	public void setCustomAnswers(String[] customAnswers) {
		this.customAnswers = customAnswers;
	}
}
