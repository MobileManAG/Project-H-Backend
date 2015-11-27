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
 * @date 3.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.questionary;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.IndexColumn;

import com.mobileman.projecth.domain.user.User;

/**
 * Entity representing a type of an answer
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "question_type", schema = "public")
public class QuestionType extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of supported question types
	 * @author mobileman
	 *
	 */
	public enum Type {
		
		/**
		 * <b>(0)</b> Only one question's answer - user enters the answer manualt
		 */
		SINGLE_ANSWER_ENTER,
		
		/**
		 * <b>(1)</b> List of allowed answers - choice of multiple answers allowed
		 */
		MULTIPLE_CHOICE_LIST,
		
		/**
		 * <b>(2)</b> List of allowed answers - choice of single answer allowed
		 */
		SINGLE_CHOICE_LIST,
		
		/**
		 * <b>(3)</b> Scale number values with min/max and step
		 */
		SCALE;
	}
	
	/**
	 * Enumeration of supported answers's data types
	 * @author mobileman
	 *
	 */
	public enum AnswerDataType {
		
		/**
		 * <b>(0)</b> Answer value of {@link Boolean} type
		 */
		BOOLEAN,
		
		/**
		 * <b>(1)</b> Answer value of {@link Number} type
		 */
		NUMBER,
		
		/**
		 * <b>(2)</b> Answer value of {@link String} type
		 */
		TEXT,
		
		/**
		 * <b>(3)</b> Answer value of {@link Date} type
		 */
		DATE,
		
		/**
		 * <b>(4)</b> Answer value of {@link String} type - name of the image
		 */
		IMAGE;
	}
	
	private Type type;
	
	/**
	 * Answers's data type
	 */
	private AnswerDataType answerDataType;
	
	private String description;
	
	/**
	 * All possible answers of a question of given type
	 */
	private List<Answer> answers;
	
	private List<String> tags;
	
	private User user;
	
	/**
	 * @return entity id
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * @return description
	 */
	@Column(name = "description", unique = false, nullable = true)
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description new value of description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return answers all possible answers of a question of given type
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionType", cascade=CascadeType.ALL, orphanRemoval=true)
	@IndexColumn(name = "sort_order")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<Answer> getAnswers() {
		return this.answers;
	}
	
	/**
	 * @param answers all possible answers of a question of given type
	 */
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}


	/**
	 * @return type
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type", unique = false, nullable = false)
	public Type getType() {
		return this.type;
	}


	/**
	 * @param type new value of type
	 */
	public void setType(Type type) {
		this.type = type;
	}

	
	/**
	 * @return answerDataType
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "answer_data_type", unique = false, nullable = false)
	public AnswerDataType getAnswerDataType() {
		return this.answerDataType;
	}

	/**
	 * @param answerDataType new value of answerDataType
	 */
	public void setAnswerDataType(AnswerDataType answerDataType) {
		this.answerDataType = answerDataType;
	}

	/**
	 * @return tags
	 */
	@ElementCollection
	@CollectionTable(name="question_type_tag")
	@Column(name="tag", length=255, nullable = false)
	@JoinColumn(name="question_type_id", nullable=false)
	@ForeignKey(name = "fk_qtt_question_type_id")
	public List<String> getTags() {
		return this.tags;
	}

	/**
	 * @param tags new value of tags
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return user
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	@ForeignKey(name = "fk_question_type_user_id")
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user new value of user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
