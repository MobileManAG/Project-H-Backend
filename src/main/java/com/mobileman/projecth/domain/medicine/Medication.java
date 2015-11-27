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
package com.mobileman.projecth.domain.medicine;

import java.util.Date;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.mobileman.projecth.domain.data.id_types.PznBarcode;

/**
 * Represents a medication 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "medicine_class", schema = "public")
public class Medication extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private PznBarcode pznBarcode;
	private Date expireddate;
	private Date createdatetime;
	private String standardUnitSize;
	private Locale locale;
	
	/**
	 * 
	 */
	public Medication() {
	}
	
	/**
	 * @param id
	 */
	public Medication(Long id) {
		setId(id);
	}

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

	@Column(name = "name", nullable = false)
	@Field(index=org.hibernate.search.annotations.Index.TOKENIZED, store=Store.NO)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expireddate", length = 29)
	public Date getExpireddate() {
		return this.expireddate;
	}

	public void setExpireddate(Date expireddate) {
		this.expireddate = expireddate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdatetime", nullable = false, length = 29)
	public Date getCreatedatetime() {
		return this.createdatetime;
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}
	
	/**
	 * @return pznNumber
	 */
	@IndexedEmbedded
	@Embedded()
	@AttributeOverrides(value={
	       @AttributeOverride(name="number", column=@Column(name="pzn", length = 7, nullable = true, unique = true))
	   })
	public PznBarcode getPzn() {
		return this.pznBarcode;
	}
	
	/**
	 * @param pznBarcode new value of pzn
	 */
	public void setPzn(PznBarcode pznBarcode) {
		this.pznBarcode = pznBarcode;
	}
	
	/**
	 * @return the standard unit size of the medication
	 */
	@Column(name = "standard_unit_size", nullable = true)
	public String getStandardUnitSize() {
		return this.standardUnitSize;
	}
	
	/**
	 * @param standardUnitSize the standard unit size of the medication
	 */
	public void setStandardUnitSize(String standardUnitSize) {
		this.standardUnitSize = standardUnitSize;
	}
	
	/**
	 * Gets a locale
	 *
	 * @return locale
	 */
	@Column(name = "locale", nullable = false)
	public Locale getLocale() {
		return this.locale;
	}
	
	/**
	 * Setter for locale
	 *
	 * @param locale new value of locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
