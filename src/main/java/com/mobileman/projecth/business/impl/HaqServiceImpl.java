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
 * QuestionDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.questionary.OneTimeHaq;
import com.mobileman.projecth.persistence.HaqDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.HAQ_SERVICE)
public class HaqServiceImpl extends BusinessServiceImpl<Haq> implements HaqService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HaqServiceImpl.class);

	HaqDao haqDao;

	/**
	 * @param haqDao
	 */
	@Autowired
	public void setHaqDao(HaqDao haqDao) {
		if (log.isDebugEnabled()) {
			log.debug("setHaqDao(HaqDao) - start"); //$NON-NLS-1$
		}

		this.haqDao = haqDao;
		setDao(haqDao);

		if (log.isDebugEnabled()) {
			log.debug("setHaqDao(HaqDao) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.HaqService#findByDisease(java.lang.Long)
	 */
	@Override
	public List<Haq> findByDisease(Long diseaseId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByDisease(Long) - start"); //$NON-NLS-1$
		}

		List<Haq> result = haqDao.findAllByDisease(diseaseId);		

		if (log.isDebugEnabled()) {
			log.debug("findByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.HaqService#findOneTimeHaqByDisease(java.lang.Long)
	 */
	@Override
	public OneTimeHaq findOneTimeHaqByDisease(Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("findOneTimeHaqByDisease(Long) - start"); //$NON-NLS-1$
		}

		OneTimeHaq result = haqDao.findOneTimeHaqByDisease(diseaseId);		

		if (log.isDebugEnabled()) {
			log.debug("findOneTimeHaqByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}
}
