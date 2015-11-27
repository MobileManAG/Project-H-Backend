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
// default package
// Generated Oct 29, 2010 1:35:35 PM by Hibernate Tools 3.4.0.Beta1

package com.mobileman.projecth.domain.index.kpi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.index.Index;

/**
 * KPI key performance indicator 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "key_performance_indicator_type", schema = "public")
@Indexed
public class KeyPerformanceIndicatorType extends Index {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeration of possible KPI types
	 * @author mobileman
	 *
	 */
	public enum Type {
		
		/**
		 * <b>(0)</b> CDAI
		 */
		CDAI,
		
		/**
		 * <b>(1)</b> DAS-28 - Disease Activity Score
		 */
		DAS28,
		
		/**
		 * <b>(2)</b> BASDAI - Bath Ankylosing Spondylitis Disease Activity Index
		 */
		BASDAI,
		
		/**
		 * <b>(3)</b> PASI - Psoriasis Area Severity Index
		 */
		PASI
	}
	
	private String description;
	private Disease disease;
	private Type type;
	
	
	/**
	 * 
	 */
	public KeyPerformanceIndicatorType() {
		super();
	}

	
	/**
	 * @param id
	 * @param code
	 * @param description
	 */
	public KeyPerformanceIndicatorType(Long id, String code, String description) {
		super(id, code);
		setDescription(description);
	}


	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.domain.Entity#getId()
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)	
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * @return name KPI type
	 */
	@Override
	@Column(name = "code", nullable = false, length = 255)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getCode() {
		return super.getCode();
	}
	
	/**
	 * @return type type of KPI
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type", unique = false, nullable = false)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
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
	 * @return description of KPI type
	 */
	@Column(name = "description", length=255)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return disease disease to which given key perf. index belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "disease_id", nullable = false)
	@IndexedEmbedded
	public Disease getDisease() {
		return this.disease;
	}
	
	/**
	 * @param disease disease to which given key perf. index belongs
	 */
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
}
