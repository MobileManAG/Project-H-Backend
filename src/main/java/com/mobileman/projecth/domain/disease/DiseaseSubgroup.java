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
 * DiseaseSubgroup.java
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 5.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.domain.disease;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.search.annotations.Indexed;

/**
 * Represents disease subgroup.
 * 
 * @author MobileMan GmbH
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "disease_subgroup", schema = "public")
@Indexed
public class DiseaseSubgroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3254113581101059789L;
	
	private long id;
	private DiseaseGroup diseaseGroup;
	private String code;
	private String name;
	private Set<Disease> diseases = new HashSet<Disease>(0);

	public DiseaseSubgroup() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disease_group_id", nullable = false)
	public DiseaseGroup getDiseaseGroup() {
		return this.diseaseGroup;
	}

	public void setDiseaseGroup(DiseaseGroup diseaseGroup) {
		this.diseaseGroup = diseaseGroup;
	}

	@Column(name = "code", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "diseaseSubgroup")
	public Set<Disease> getDiseases() {
		return this.diseases;
	}

	public void setDiseases(Set<Disease> diseases) {
		this.diseases = diseases;
	}

}
