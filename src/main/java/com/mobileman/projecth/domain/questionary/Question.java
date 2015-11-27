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
 * Question.java
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
import com.mobileman.projecth.domain.disease.Haq;

/**
 * Represents the question.
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "question", schema = "public")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Question extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of supported question types
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
		CUSTOM,
	}
	
	/**
	 * Enumeration of supported extended question types
	 * @author mobileman
	 *
	 */
	public enum ExtendedType {
		
		/**
		 * Initial symptom date
		 */
		INITIAL_SYMPTOM_DATE,
		
		/**
		 * Diagnosis date
		 */
		DIAGNOSIS_DATE,
	}
	
	protected Question(Kind kind) {
		this.kind = kind;
	}
	
	/**
	 * 
	 */
	public Question() {
		this(Kind.DEFAULT);
	}
	
	/**
	 * Questionary to which this question belongs
	 */
	private Haq haq;

	/**
	 * Text of the question
	 */
	private String text;
	
	/**
	 * Tag of the question
	 */
	private String tag;
	
	/**
	 * Type of the question
	 */
	private QuestionType questionType;
	
	private int sortOrder;
	
	/**
	 * Question's image data
	 */
	private ImageData imageData;
	
	/**
	 * Explanation of the question
	 */
	private String explanation;
	
	private String groupText;
	
	/**
	 * Kind of the question
	 */
	private Kind kind;
	
	/**
	 * Extended question type
	 */
	private ExtendedType extendedType;
	
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
	 * @return haq Questionary to which this question belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "haq_id", nullable = true)
	@ForeignKey(name = "fk_question_haq_id")
	public Haq getHaq() {
		return this.haq;
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
	 * @param haq Questionary to which this question belongs
	 */
	public void setHaq(Haq haq) {
		this.haq = haq;
	}

	/**
	 * @return text text of the question
	 */
	@Column(name = "text", unique = false, nullable = false, length=2500)
	public String getText() {
		return this.text;
	}

	/**
	 * @param text text of the question
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Type of the question 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_type_id", nullable = false)
	@ForeignKey(name = "fk_question_question_type_id")
	public QuestionType getQuestionType() {
		return this.questionType;
	}

	/**
	 * @param questionType Type of the question
	 */
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	/**
	 * @return sortOrder order of a question in list of all haq's questions
	 */
	@Column(name = "sort_order", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	/**
	 * @param sortOrder order of a question in list of all haq's questions
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return imageData
	 */
	@Embedded
	public ImageData getImageData() {
		return this.imageData;
	}

	/**
	 * @param imageData new value of imageData
	 */
	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

	/**
	 * @return explanation
	 */
	@Column(name = "explanation", nullable = true)
	public String getExplanation() {
		return this.explanation;
	}

	/**
	 * @param explanation new value of explanation
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return groupText
	 */
	@Column(name = "group_text", nullable = true, length=255)
	public String getGroupText() {
		return this.groupText;
	}

	/**
	 * @param groupText new value of groupText
	 */
	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	/**
	 * @return Extended question type
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "extended_type", insertable=false, updatable=false)
	public ExtendedType getExtendedType() {
		return this.extendedType;
	}

	/**
	 * @param extendedType Extended question type
	 */
	public void setExtendedType(ExtendedType extendedType) {
		this.extendedType = extendedType;
	}
	
	/**
	 * @return tag
	 */
	@Column(name = "tag", nullable = true, length=255)
	public String getTag() {
		return this.tag;
	}

	/**
	 * @param tag new value of tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
}
