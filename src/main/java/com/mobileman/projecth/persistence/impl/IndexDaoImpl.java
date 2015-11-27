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

import com.mobileman.projecth.domain.index.Index;
import com.mobileman.projecth.persistence.index.IndexDao;

/**
 * @author mobileman
 * @param <T> Index type
 *
 */
@Repository("indexDao")
public class IndexDaoImpl<T extends Index> extends DaoImpl<T> implements IndexDao<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(IndexDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.index.IndexDao#findByCode(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findByCode(String code, Class<T> type) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String, Class<T>) - start"); //$NON-NLS-1$
		}

		if (code == null) {
			throw new IllegalArgumentException("code must not be null");
		}
		
		String query = "select e from " + type.getSimpleName() + " e where e.code=? order by e.id";
		List<T> result = (List)getHibernateTemplate().find(query, code);
		T returnT = result.isEmpty() ? null : result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String, Class<T>) - returns"); //$NON-NLS-1$
		}
		return returnT;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.index.IndexDao#findById(java.lang.Class, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findById(Class<T> type, Long id) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findById(Class<T>, Long) - start"); //$NON-NLS-1$
		}

		T returnT = (T) getHibernateTemplate().load(type, id);
		if (log.isDebugEnabled()) {
			log.debug("findById(Class<T>, Long) - returns"); //$NON-NLS-1$
		}
		return returnT;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.index.IndexDao#findAll(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> type) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String, Class<T>) - start"); //$NON-NLS-1$
		}

		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}
		
		String query = "select e from " + type.getSimpleName() + " e order by e.id";
		List<T> result = (List)getHibernateTemplate().find(query);
		
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String, Class<T>) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
