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

package com.mobileman.projecth.persistence.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type;
import com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao;

/**
 * @author mobileman
 *
 */
@Repository(value = "keyPerformanceIndicatorTypeDao")
public class KeyPerformanceIndicatorTypeDaoImpl extends DaoImpl<KeyPerformanceIndicatorType> implements KeyPerformanceIndicatorTypeDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(KeyPerformanceIndicatorTypeDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao#find(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")	
	public KeyPerformanceIndicatorType find(String code) {
		if (log.isDebugEnabled()) {
			log.debug("find(String) - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorType> result = (List)getHibernateTemplate().find(
				"select kpit from KeyPerformanceIndicatorType kpit where lower(kpit.code) = ?", 
				new Object[]{ code.toLowerCase() });
		

		KeyPerformanceIndicatorType returnKeyPerformanceIndicatorType = result.isEmpty() ? null : result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("find(String) - returns"); //$NON-NLS-1$
		}
		return returnKeyPerformanceIndicatorType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao#find(java.lang.String, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public KeyPerformanceIndicatorType find(String name, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("find(String, Long) - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorType> result = (List)getHibernateTemplate().find(
				"from KeyPerformanceIndicatorType kpit " +
				"where lower(kpit.code)=? and kpit.disease.id = ?", 
				new Object[]{name.toLowerCase(), diseaseId});
		if (result.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("find(String, Long) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		KeyPerformanceIndicatorType returnKeyPerformanceIndicatorType = result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("find(String, Long) - returns"); //$NON-NLS-1$
		}
		return returnKeyPerformanceIndicatorType;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao#find(com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType.Type, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public KeyPerformanceIndicatorType find(Type type, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("find(Type, Long) - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorType> result = (List)getHibernateTemplate().find(
				"from KeyPerformanceIndicatorType kpit " +
				"where kpit.type=? and kpit.disease.id = ?", 
				new Object[]{ type, diseaseId });
		if (result.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("find(Type, Long) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		KeyPerformanceIndicatorType returnKeyPerformanceIndicatorType = result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("find(Type, Long) - returns"); //$NON-NLS-1$
		}
		return returnKeyPerformanceIndicatorType;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao#findAll(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorType> findAll(Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(" + diseaseId + ") - start"); //$NON-NLS-1$
		}

		List<KeyPerformanceIndicatorType> result = (List)getHibernateTemplate().find(
				"select kpit from KeyPerformanceIndicatorType kpit where kpit.disease.id = ?", 
				new Object[]{ diseaseId });
		
		if (log.isDebugEnabled()) {
			log.debug("findAll(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}
}
