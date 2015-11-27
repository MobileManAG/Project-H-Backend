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
 * BusinessServiceImpl.java
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


import com.mobileman.projecth.business.EntityService;
import com.mobileman.projecth.business.SearchService;
import com.mobileman.projecth.persistence.Dao;


/**
 * @author mobileman
 * @param <T> 
 *
 */
public abstract class BusinessServiceImpl<T> implements EntityService<T>, SearchService<T> {

	private Dao<T> dao;
	
	private final Logger log = Logger.getLogger(getClass());
	
	/**
	 * Setter for dao
	 *
	 * @param dao new value of dao
	 */
	public void setDao(Dao<T> dao) {
		this.dao = dao;
	}
	
	/**
	 * Gets a dao
	 *
	 * @return dao
	 */
	public Dao<T> getDao() {
		return this.dao;
	}
	
	protected void flush() {
		if (log.isDebugEnabled()) {
			log.debug("flush() - start"); //$NON-NLS-1$
		}

		this.dao.flush();

		if (log.isDebugEnabled()) {
			log.debug("flush() - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * Gets a log
	 *
	 * @return log
	 */
	public Logger getLog() {
		return this.log;
	}
	
	/**
	 * @param entity
	 * @return id
	 */
	@Override
	public Long save(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("save(T) - start"); //$NON-NLS-1$
		}

		Long returnLong = dao.save(entity);
		if (log.isDebugEnabled()) {
			log.debug("save(T) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}
	
	/**
	 * @param entity
	 */
	@Override
	public void update(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("update(T) - start"); //$NON-NLS-1$
		}

		dao.update(entity);

		if (log.isDebugEnabled()) {
			log.debug("update(T) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param entity
	 */
	@Override
	public void delete(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("delete(T) - start"); //$NON-NLS-1$
		}

		dao.delete(entity);
		
		if (log.isDebugEnabled()) {
			log.debug("delete(T) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param id
	 * @return T
	 */
	@Override
	public T findById(Long id) {
		if (log.isDebugEnabled()) {
			log.debug("findById(" + id + ") - start"); //$NON-NLS-1$
		}

		T returnT = dao.findById(id);
		if (log.isDebugEnabled()) {
			log.debug("findById(Long) - returns"); //$NON-NLS-1$
		}
		return returnT;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.SearchService#findAll()
	 */
	@Override
	public List<T> findAll() {
		if (log.isDebugEnabled()) {
			log.debug("findAll() - start"); //$NON-NLS-1$
		}

		List<T> returnList = dao.findAll();
		if (log.isDebugEnabled()) {
			log.debug("findAll() - returns"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.SearchService#findAll(long, long)
	 */
	@Override
	public List<T> findAll(long first, long count) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(long, long) - start"); //$NON-NLS-1$
		}

		List<T> returnList = dao.findAll(first, count);
		if (log.isDebugEnabled()) {
			log.debug("findAll(long, long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}
}
