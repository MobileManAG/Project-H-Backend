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
 * @date 1t.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.index.family;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.index.Index;

/**
 * Represent family situation
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "family_situation", schema = "public")
@Indexed
public class FamilySituation extends Index {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Family situation name
	 */
	private String name;

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
	 * @return name Family situation code
	 */
	@Override
	@Column(name = "code", nullable = false, length = 255, unique=true)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @return Family situation name
	 */
	@Column(name = "name", nullable = false, length = 255, unique=true)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getName() {
		return this.name;
	}

	/**
	 * @param name Family situation name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
