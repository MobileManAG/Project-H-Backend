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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.data.ImageData;

/**
 * Represents answer to the question.
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "question_type_answer", schema = "public")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Answer extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of supported answer kinds
	 * @author mobileman
	 *
	 */
	public enum Kind {
		
		/**
		 * Default (0)
		 */
		DEFAULT,
		
		/**
		 * Custom (1)
		 */
		SCALE,
		
		/**
		 * No answer (2)
		 */
		NO_ANSWER;
	}
	
	/**
	 * 
	 */
	public Answer() {
		this(Kind.DEFAULT);
	}

	/**
	 * @param kind
	 */
	protected Answer(Kind kind) {
		super();
		this.kind = kind;
	}

	/**
	 * @param id
	 */
	public Answer(Long id) {
		super(id);
	}

	/**
	 * Answer text
	 */
	private String answer;
	
	/**
	 * Answer explanation
	 */
	private String explanation;
	
	/**
	 * Back ref to owner
	 */
	private QuestionType questionType;
	
	/**
	 * Sort order of the answer
	 */
	private int sortOrder;
	
	/**
	 * Answer's image data - users select image as an answer
	 */
	private ImageData answerImage;
	
	/**
	 * Defines id this answer is active (positiv) - used in statistics for counting
	 */
	private boolean active;
	
	/**
	 * Kind of the answer
	 */
	private Kind kind;
	
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
	 * @return Kind of the answer
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "kind", insertable=false, updatable=false)
	public Kind getKind() {
		return this.kind;
	}
	
	/**
	 * @param kind Kind of the answer
	 */
	public void setKind(Kind kind) {
		this.kind = kind;
	}

	/**
	 * @return questionType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_type_id", nullable = false)
	@ForeignKey(name = "fk_answer_question_type_id")
	public QuestionType getQuestionType() {
		return this.questionType;
	}



	/**
	 * @param questionType new value of questionType
	 */
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}



	/**
	 * @return answer text
	 */
	@Column(name = "answer", unique = false, nullable = false)
	public String getAnswer() {
		return this.answer;
	}

	/**
	 * @param answer answer text
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return order answer sort order
	 */
	@Column(name = "sort_order", unique = false, nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	/**
	 * @param sortOrder answer sort order
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return answerImage
	 */
	@Embedded
	public ImageData getAnswerImage() {
		return this.answerImage;
	}
	
	/**
	 * @param answerImage new value of answerImage
	 */
	public void setAnswerImage(ImageData answerImage) {
		this.answerImage = answerImage;
	}



	/**
	 * @return flag which defines if this answer is active (positiv) - used in statistics for counting
	 */
	@Column(name = "active", nullable = false)
	public boolean isActive() {
		return this.active;
	}



	/**
	 * @param active defines if this answer is active (positiv) - used in statistics for counting
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return Answer explanation
	 */
	@Column(name = "explanation", nullable = true)
	public String getExplanation() {
		return this.explanation;
	}

	/**
	 * @param explanation Answer explanation
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	
}
