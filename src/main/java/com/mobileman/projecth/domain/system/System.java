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
 * projecth.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.system;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represents the projecth - system entity
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "system", schema = "public" )
public class System extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ModelVersion version;
	
	/**
	 * @return entity id
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * Gets a version
	 *
	 * @return version
	 */
	@Embedded
	public ModelVersion getVersion() {
		return this.version;
	}
	
	/**
	 * Setter for version
	 *
	 * @param version new value of version
	 */
	public void setVersion(ModelVersion version) {
		this.version = version;
	}
}
