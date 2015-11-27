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
 * CustomQuestion.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 7.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.questionary;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.user.User;

/**
 * Represents custom question (defined by user).
 * 
 * @author mobileman
 *
 */
@Entity
@DiscriminatorValue("1")
public class CustomQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of supported question types
	 * @author mobileman
	 *
	 */
	public enum CustomQuestionType {
		
		/**
		 * <b>(0)</b> Represents a new custom question - it is added to list of questions defined for a disease
		 */
		NEW,
		
		/**
		 * <b>(1)</b> Overides (replaces) an existing question
		 */
		OVERRIDE,
		
		/**
		 * <b>(2)</b> Hides an existing question
		 */
		HIDE;
	}
	
	/**
	 * Type of a custom question
	 */
	private CustomQuestionType customQuestionType;
	
	/**
	 * Disease to which this custom question belongs
	 */
	private Disease disease;
	
	/**
	 * User who created custom question
	 */
	private User creator;
	
	/**
	 * User for which this question was created
	 */
	private User user;
	
	/**
	 * Target question (filled in case of {@link CustomQuestionType#OVERRIDE} or {@link CustomQuestionType#HIDE})
	 */
	private Question targetQuestion;
	
	/**
	 * 
	 */
	public CustomQuestion() {
		super(Kind.CUSTOM);
	}

	/**
	 * @return User for which this question was created
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	@ForeignKey(name = "fk_question_user_id")
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user User for which this question was created
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return disease to which this custom question belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disease_id", nullable = true)
	@ForeignKey(name = "fk_question_disease_id")
	public Disease getDisease() {
		return this.disease;
	}

	/**
	 * @param disease disease to which this custom question belongs
	 */
	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	/**
	 * @return creator user who created custom question
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = true)
	@ForeignKey(name = "fk_question_creator_id")
	public User getCreator() {
		return this.creator;
	}

	/**
	 * @param creator new value of creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return Type of a custom question
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "custom_question_type", nullable = true)
	public CustomQuestionType getCustomQuestionType() {
		return this.customQuestionType;
	}

	/**
	 * @param customQuestionType Type of a custom question
	 */
	public void setCustomQuestionType(CustomQuestionType customQuestionType) {
		this.customQuestionType = customQuestionType;
	}

	/**
	 * @return Target question (filled in case of {@link CustomQuestionType#OVERRIDE} or {@link CustomQuestionType#HIDE})
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_question_id", nullable = true)
	@ForeignKey(name = "fk_question_target_question_id")
	public Question getTargetQuestion() {
		return this.targetQuestion;
	}

	/**
	 * @param targetQuestion Target question (filled in case of {@link CustomQuestionType#OVERRIDE} or {@link CustomQuestionType#HIDE})
	 */
	public void setTargetQuestion(Question targetQuestion) {
		this.targetQuestion = targetQuestion;
	}
	
	
}
