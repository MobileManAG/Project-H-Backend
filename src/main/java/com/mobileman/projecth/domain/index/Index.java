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

package com.mobileman.projecth.domain.index;

import javax.persistence.Column;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.Entity;

/**
 * Base type for all index entities.
 * 
 * @author mobileman
 *
 */
public abstract class Index extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Index's code - unique value
	 */
	private String code;
	
	
	
	/**
	 * 
	 */
	public Index() {
		super();
	}

	/**
	 * @param id
	 */
	protected Index(Long id) {
		super(id);
	}
	
	/**
	 * @param id
	 */
	protected Index(Long id, String code) {
		super(id);
		setCode(code);
	}

	/**
	 * @return name of given chart type
	 */
	@Column(name = "code", nullable = false, length = 255)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getCode() {
		return this.code;
	}

	/**
	 * @param name name of given chart type
	 */
	public void setCode(String name) {
		this.code = name;
	}

}
