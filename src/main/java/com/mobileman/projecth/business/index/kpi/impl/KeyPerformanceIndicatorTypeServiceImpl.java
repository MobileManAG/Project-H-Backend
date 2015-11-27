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
 * KeyPerformanceIndicatorTypeServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.index.kpi.impl;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.index.impl.IndexServiceImpl;
import com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type;
import com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.KPI_TYPE_SERVICE)
public class KeyPerformanceIndicatorTypeServiceImpl extends IndexServiceImpl<KeyPerformanceIndicatorType> implements
		KeyPerformanceIndicatorTypeService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(KeyPerformanceIndicatorTypeServiceImpl.class);
	
	private KeyPerformanceIndicatorTypeDao keyPerformanceIndicatorTypeDao;
	
	/**
	 * @param keyPerformanceIndicatorTypeDao new value of keyPerformanceIndicatorTypeDao
	 */
	@Autowired
	public void setKeyPerformanceIndicatorTypeDao(KeyPerformanceIndicatorTypeDao keyPerformanceIndicatorTypeDao) {
		if (log.isDebugEnabled()) {
			log.debug("setKeyPerformanceIndicatorTypeDao(KeyPerformanceIndicatorTypeDao) - start"); //$NON-NLS-1$
		}

		this.keyPerformanceIndicatorTypeDao = keyPerformanceIndicatorTypeDao;
		super.setDao(keyPerformanceIndicatorTypeDao);

		if (log.isDebugEnabled()) {
			log.debug("setKeyPerformanceIndicatorTypeDao(KeyPerformanceIndicatorTypeDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 */
	public KeyPerformanceIndicatorTypeServiceImpl() {
		super(KeyPerformanceIndicatorType.class);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService#find(java.lang.String, java.lang.Long)
	 */
	@Override
	public KeyPerformanceIndicatorType find(String code, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("find(String, Long) - start"); //$NON-NLS-1$
		}

		KeyPerformanceIndicatorType returnKeyPerformanceIndicatorType = keyPerformanceIndicatorTypeDao.find(code, diseaseId);
		if (log.isDebugEnabled()) {
			log.debug("find(String, Long) - returns"); //$NON-NLS-1$
		}
		return returnKeyPerformanceIndicatorType;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService#find(com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type, java.lang.Long)
	 */
	@Override
	public KeyPerformanceIndicatorType find(Type type, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("find(Type, Long) - start"); //$NON-NLS-1$
		}

		KeyPerformanceIndicatorType returnKeyPerformanceIndicatorType = keyPerformanceIndicatorTypeDao.find(type, diseaseId);
		if (log.isDebugEnabled()) {
			log.debug("find(Type, Long) - returns"); //$NON-NLS-1$
		}
		return returnKeyPerformanceIndicatorType;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService#findAll(java.lang.Long)
	 */
	@Override
	public List<KeyPerformanceIndicatorType> findAll(Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(" + diseaseId + ") - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorType> result = keyPerformanceIndicatorTypeDao.findAll(diseaseId);
		if (log.isDebugEnabled()) {
			log.debug("findAll(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
