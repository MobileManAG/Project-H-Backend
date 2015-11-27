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

package com.mobileman.projecth.business.index.impl;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.index.IndexService;
import com.mobileman.projecth.domain.index.Index;
import com.mobileman.projecth.persistence.index.IndexDao;

/**
 * @author mobileman
 * @param <T> Index type
 *
 */
public class IndexServiceImpl<T extends Index> extends BusinessServiceImpl<T> implements IndexService<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(IndexServiceImpl.class);

	private Class<T> indexType;
	
	private IndexDao<T> indexDao;
	
	/**
	 * @param indexType
	 */
	public IndexServiceImpl(Class<T> indexType) {
		this.indexType = indexType;
	}
	
	/**
	 * @return indexType
	 */
	public Class<T> getIndexType() {
		return this.indexType;
	}
	
	/**
	 * @param indexDao 
	 */
	@Autowired
	public void setIndexDao(IndexDao<T> indexDao) {
		if (log.isDebugEnabled()) {
			log.debug("setIndexDao(IndexDao<T>) - start"); //$NON-NLS-1$
		}

		this.indexDao = indexDao;
		super.setDao(indexDao);

		if (log.isDebugEnabled()) {
			log.debug("setIndexDao(IndexDao<T>) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * Vrati indexDao
	 *
	 * @return indexDao
	 */
	public IndexDao<T> getIndexDao() {
		return this.indexDao;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.index.IndexService#findByCode(String)
	 */
	@Override
	public T findByCode(String code) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - start"); //$NON-NLS-1$
		}

		T returnT = getIndexDao().findByCode(code, getIndexType());
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - returns"); //$NON-NLS-1$
		}
		return returnT;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#findById(java.lang.Long)
	 */
	@Override
	public T findById(Long id) {
		if (log.isDebugEnabled()) {
			log.debug("findById(" + id + ") - start"); //$NON-NLS-1$
		}

		T result  = getIndexDao().findById(getIndexType(), id);
		
		if (log.isDebugEnabled()) {
			log.debug("findById(Long) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#findAll()
	 */
	@Override
	public List<T> findAll() {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - start"); //$NON-NLS-1$
		}

		List<T> result = getIndexDao().findAll(getIndexType());
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

}
