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
 * MedicationServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.impl;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.MedicationService;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.persistence.MedicationDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.MEDICATION_SERVICE)
public class MedicationServiceImpl extends BusinessServiceImpl<Medication> implements MedicationService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(MedicationServiceImpl.class);

	private MedicationDao medicationDao;
	
	/**
	 * @param medicationDao
	 */
	@Autowired
	public void setMedicationDao(MedicationDao medicationDao) {
		if (log.isDebugEnabled()) {
			log.debug("setMedicationDao(MedicationDao) - start"); //$NON-NLS-1$
		}

		this.medicationDao = medicationDao;
		setDao(medicationDao);

		if (log.isDebugEnabled()) {
			log.debug("setMedicationDao(MedicationDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MedicationService#findByPzn(java.lang.String, Locale)
	 */
	@Override
	public Medication findByPzn(String pzn, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByPzn(String, Locale) - start"); //$NON-NLS-1$
		}

		Medication returnMedication = this.medicationDao.findByPzn(pzn, locale);
		if (log.isDebugEnabled()) {
			log.debug("findByPzn(String, Locale) - returns"); //$NON-NLS-1$
		}
		return returnMedication;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MedicationService#findByName(java.lang.String, Locale)
	 */
	@Override
	public List<Medication> findByName(String name, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - start"); //$NON-NLS-1$
		}

		List<Medication> returnList = this.medicationDao.findByName(name, locale);
		if (log.isDebugEnabled()) {
			log.debug("findByName(String, Locale) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MedicationService#findAllByNameOrPzn(String, Locale)
	 */
	@Override
	public List<Medication> findAllByNameOrPzn(String filter, Locale locale) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAllByNameOrPzn(" + filter + ", " + locale + ") - start"); //$NON-NLS-1$
		}

		List<Medication> result  = this.medicationDao.findAllByNameOrPzn(filter, locale);
		if (log.isDebugEnabled()) {
			log.debug("findAllByNameOrPzn(String, Locale) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

}
