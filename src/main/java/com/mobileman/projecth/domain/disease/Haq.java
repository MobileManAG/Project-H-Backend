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
 * Haq.java
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 26.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.domain.disease;

import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

/**
 * Represents haq.
 * 
 * @author MobileMan GmbH
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "haq", schema = "public")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@Indexed
public class Haq implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of supported haq types
	 * @author mobileman
	 *
	 */
	public enum Kind {
		
		/**
		 * Default haq (0)
		 */
		DEFAULT,
		
		/**
		 * One time haq (1)
		 */
		ONE_TIME,
	}
	
	private Long id;
	private Kind kind;
	private Disease disease;
	private int sortorder;
	private String haqQuestion;
	private String explanation;
	private List<com.mobileman.projecth.domain.questionary.Question> questions;
	
	private String tag;

	public Haq() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return kind
	 */
	@Column(name = "kind", insertable=false, updatable=false)
	public Kind getKind() {
		return this.kind;
	}
	
	/**
	 * @param kind new value of kind
	 */
	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disease_id")
	public Disease getDisease() {
		return this.disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
	@Column(name = "sortorder", nullable = false)
	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	@Column(name = "\"HAQ-Question\"", nullable = false)
	public String getHaqQuestion() {
		return this.haqQuestion;
	}

	public void setHaqQuestion(String haqQuestion) {
		this.haqQuestion = haqQuestion;
	}

	@Column(name = "explanation", nullable = false, length = 4000)
	public String getExplanation() {
		return this.explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return questions
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "haq", cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderBy("sortOrder")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<com.mobileman.projecth.domain.questionary.Question> getQuestions() {
		return this.questions;
	}

	/**
	 * @param questionaries
	 */
	public void setQuestions(List<com.mobileman.projecth.domain.questionary.Question> questionaries) {
		this.questions = questionaries;
	}

	/**
	 * @return tag HAQ tag
	 */
	@Column(name = "tag", nullable = true)
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
