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
 * Disease.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 26.11.2010
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
 * Represents disease.
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "disease", schema = "public" )
@Indexed
public class Disease extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DiseaseSubgroup diseaseSubgroup;
	private String code;
	private String name;
	private String imageName;
	private Set<Haq> haqs = new HashSet<Haq>(0);
	
	/**
	 * 
	 */
	public Disease() {
		super();
	}

	/**
	 * @param id
	 * @param code
	 * @param name
	 */
	public Disease(Long id, String code, String name) {
		super(id);
		this.code = code;
		this.name = name;
	}

	/**
	 * @return entity id
	 */
	@Override
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return super.getId();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disease_subgroup_id", nullable = false)
	public DiseaseSubgroup getDiseaseSubgroup() {
		return this.diseaseSubgroup;
	}

	public void setDiseaseSubgroup(DiseaseSubgroup diseaseSubgroup) {
		this.diseaseSubgroup = diseaseSubgroup;
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

	@Column(name = "image_name")
	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "disease")
	public Set<Haq> getHaqs() {
		return this.haqs;
	}

	public void setHaqs(Set<Haq> haqs) {
		this.haqs = haqs;
	}
}
