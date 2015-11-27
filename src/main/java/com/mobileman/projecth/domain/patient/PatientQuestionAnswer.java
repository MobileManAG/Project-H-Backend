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
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.patient;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.Answer;


/**
 * Represents patient's answer to a question
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "patient_question_answer", schema = "public")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@Indexed
public class PatientQuestionAnswer extends com.mobileman.projecth.domain.Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * patient to which this answer belongs
	 */
	private Patient patient;
	
	/**
	 * Question to which a patient answers
	 */
	private Question question;
	
	/**
	 * Choosen patient's answer to the question
	 */
	private Answer answer;

	/**
	 * Patient's custom textual answer
	 */
	private String customAnswer;
	
	private Date logDate;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.domain.Entity#getId()
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * @return patient to which this answer belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	@ForeignKey(name = "fk_pat_q_answer_patient_id")
	public Patient getPatient() {
		return this.patient;
	}

	/**
	 * @param patient
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @return Question to which a patient answers
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	@ForeignKey(name = "fk_pat_q_answer_question_id")
	public Question getQuestion() {
		return this.question;
	}

	/**
	 * @param question Question to which a patient answers
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return log date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logdate", nullable = false, length = 29)
	public Date getLogDate() {
		return this.logDate;
	}

	/**
	 * @param logdate
	 */
	public void setLogDate(Date logdate) {
		this.logDate = logdate;
	}

	/**
	 * @return Choosen patient's answer to the question
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "answer_id", nullable = false)
	@ForeignKey(name = "fk_pat_q_answer_answer_id")
	public Answer getAnswer() {
		return this.answer;
	}

	/**
	 * @param answer Choosen patient's answer to the question
	 */
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	/**
	 * @return Patient's custom textual answer
	 */
	@Column(name = "custom_answer", nullable = true, length = 2500)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getCustomAnswer() {
		return this.customAnswer;
	}

	/**
	 * @param customAnswer Patient's custom textual answer
	 */
	public void setCustomAnswer(String customAnswer) {
		this.customAnswer = customAnswer;
	}
	
}
