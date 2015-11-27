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
 * MedicationDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.impl;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.persistence.MedicationDao;

/**
 * DAO implementation for {@link MedicationDao}
 * @author mobileman
 *
 */
@Repository("medicationDao")
public class MedicationDaoImpl extends DaoImpl<Medication> implements MedicationDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(MedicationDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.MedicationDao#findByPzn(java.lang.String, Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Medication findByPzn(String pzn, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByPzn(String, Locale) - start"); //$NON-NLS-1$
		}

		if (pzn == null || pzn.trim().length() == 0) {
			throw new IllegalArgumentException("Name must not be empty");
		}
		
		if (locale == null) {
			throw new IllegalArgumentException("locale must not be null");
		}
		
		List<Medication> result = (List)getHibernateTemplate().find(
				"select m from Medication m " +
				"where lower(m.pzn.number)=? and m.locale=?", 
				new Object[]{pzn.toLowerCase(), locale});
		Medication returnMedication = result.isEmpty() ? null : result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findByPzn(String, Locale) - returns"); //$NON-NLS-1$
		}
		return returnMedication;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.MedicationDao#findByName(java.lang.String, Locale)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Medication> findByName(String name, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - start"); //$NON-NLS-1$
		}

		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Name must not be empty");
		}
		
		if (locale == null) {
			throw new IllegalArgumentException("locale must not be null");
		}
		
		name = name.trim();
		if (name.charAt(name.length() - 1) != '%') {
			name += "%";
		}
		
		List<Medication> result = (List)getHibernateTemplate().find(
				"select m from Medication m " +
				"where lower(m.name) like ? and m.locale=?", 
				new Object[]{name.toLowerCase(), locale});

		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.MedicationDao#findAllByNameOrPzn(java.lang.String, java.util.Locale)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Medication> findAllByNameOrPzn(String filter, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - start"); //$NON-NLS-1$
		}

		if (filter == null || filter.trim().length() == 0) {
			throw new IllegalArgumentException("Name must not be empty");
		}
		
		if (locale == null) {
			throw new IllegalArgumentException("locale must not be null");
		}
		
		filter = filter.trim();
		if (filter.charAt(filter.length() - 1) != '%') {
			filter += "%";
		}
		
		String lowerCaseFilter = filter.toLowerCase();
		List<Medication> result = (List)getHibernateTemplate().find(
				"select m from Medication m " +
				"where (lower(m.name) like ? or lower(m.pzn.number) like ?) and m.locale=?", 
				new Object[]{ lowerCaseFilter, lowerCaseFilter, locale});

		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
