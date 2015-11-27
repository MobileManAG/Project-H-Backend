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
package com.mobileman.projecth.business.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.DiseaseGroup;
import com.mobileman.projecth.domain.disease.DiseaseSubgroup;
import com.mobileman.projecth.persistence.DiseaseDao;

/**
 *
 */
@Service(ComponentNames.DISEASE_SERVICE)
public class DiseaseServiceImpl extends BusinessServiceImpl<Disease> implements DiseaseService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(DiseaseServiceImpl.class);
	
	private DiseaseDao diseaseDao;

	/**
	 * @param diseaseDao
	 */
	@Autowired
	public void setDoctorDao(DiseaseDao diseaseDao) {
		if (log.isDebugEnabled()) {
			log.debug("setDoctorDao(DiseaseDao) - start"); //$NON-NLS-1$
		}

		this.diseaseDao = diseaseDao;
		setDao(diseaseDao);

		if (log.isDebugEnabled()) {
			log.debug("setDoctorDao(DiseaseDao) - returns"); //$NON-NLS-1$
		}
	}

	@Override
	public Long saveGroup(DiseaseGroup diseaseGroup) {
		if (log.isDebugEnabled()) {
			log.debug("saveGroup(DiseaseGroup) - start"); //$NON-NLS-1$
		}

		Long result = null;
		result = diseaseDao.saveGroup(diseaseGroup);

		if (log.isDebugEnabled()) {
			log.debug("saveGroup(DiseaseGroup) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	@Override
	public Long saveSubgroup(DiseaseSubgroup diseaseSubgroup) {
		if (log.isDebugEnabled()) {
			log.debug("saveSubgroup(DiseaseSubgroup) - start"); //$NON-NLS-1$
		}

		Long result = null;
		result = diseaseDao.saveSubgroup(diseaseSubgroup);

		if (log.isDebugEnabled()) {
			log.debug("saveSubgroup(DiseaseSubgroup) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.DiseaseService#findByCode(java.lang.String)
	 */
	@Override
	public Disease findByCode(String code) {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - start"); //$NON-NLS-1$
		}

		Disease returnDisease = this.diseaseDao.findByCode(code);
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - returns"); //$NON-NLS-1$
		}
		return returnDisease;
	}

}
