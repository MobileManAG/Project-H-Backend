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
 * DaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.impl;

import org.apache.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.persistence.Dao;
import com.mobileman.projecth.util.CustomHibernateDaoSupport;

/**
 * @author mobileman
 * @param <T> 
 *
 */
public abstract class DaoImpl<T> extends CustomHibernateDaoSupport implements Dao<T> {
	/**
	 * Logger for this class
	 */
	private final Logger log = Logger.getLogger(getClass());
	
	private final Class<T> entityClass;
	
	/**
	 * 
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public DaoImpl() {
		java.lang.reflect.Type type = ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		if (java.lang.reflect.TypeVariable.class.isInstance(type)) {			
			TypeVariable typeVariable = TypeVariable.class.cast(type);
			this.entityClass = (Class<T>)typeVariable.getBounds()[0];
		} else {
			this.entityClass = (Class<T>)type;
		}
		
		if (this.entityClass == null) {
			throw new NullPointerException("null entity class !");
		}
	}
	
	/**
	 * Gets a entityClass
	 *
	 * @return entityClass
	 */
	public Class<T> getEntityClass() {
		return this.entityClass;
	}
	
	/**
	 * @param numbers
	 * @return double value from queryResult first item 
	 */
	protected double getDoubleValue(List<Number> numbers) {
		if (log.isDebugEnabled()) {
			log.debug("getDoubleValue(" + numbers + ") - start"); //$NON-NLS-1$
		}

		final double result;
		if (numbers == null || numbers.isEmpty()) {
			result = 0.0d;
		} else if (numbers.get(0) != null) {
			result = numbers.get(0).doubleValue();
		} else {
			result = 0.0d;
		}
		

		if (log.isDebugEnabled()) {
			log.debug("getDoubleValue(List<Number>) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.util.CustomHibernateDaoSupport#anyMethodName(org.hibernate.SessionFactory)
	 */
	@Override
	@Autowired
	public void anyMethodName(SessionFactory sessionFactory) {
		if (log.isDebugEnabled()) {
			log.debug("anyMethodName(SessionFactory) - start"); //$NON-NLS-1$
		}

		setSessionFactory(sessionFactory);

		if (log.isDebugEnabled()) {
			log.debug("anyMethodName(SessionFactory) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#save(java.lang.Object)
	 */
	@Override
	public Long save(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("save(T) - start"); //$NON-NLS-1$
		}

		Long returnLong = (Long) getHibernateTemplate().save(entity);
		if (log.isDebugEnabled()) {
			log.debug("save(T) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#update(java.lang.Object)
	 */
	@Override
	public void update(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("update(T) - start"); //$NON-NLS-1$
		}

		getHibernateTemplate().update(entity);

		if (log.isDebugEnabled()) {
			log.debug("update(T) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#evict(java.lang.Object)
	 */
	@Override
	public void evict(T entity) {
		getSession().evict(entity);
		
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		if (log.isDebugEnabled()) {
			log.debug("delete(T) - start"); //$NON-NLS-1$
		}

		getHibernateTemplate().delete(entity);

		if (log.isDebugEnabled()) {
			log.debug("delete(T) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#findById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findById(Long id) {
		if (log.isDebugEnabled()) {
			log.debug("findById(" + id + ") - start"); //$NON-NLS-1$
		}
		
		T entity = (T) getHibernateTemplate().load(getEntityClass(), id);

		if (log.isDebugEnabled()) {
			log.debug("findById(Long) - returns"); //$NON-NLS-1$
		}
		return entity;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#findAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		if (log.isDebugEnabled()) {
			log.debug("findAll() - start"); //$NON-NLS-1$
		}

		String query = "select e from " + getEntityClass().getSimpleName() 
			+ " e order by e.id";
		List<T> returnList = (List)getHibernateTemplate().find(query);
		if (log.isDebugEnabled()) {
			log.debug("findAll() - returns"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#findAll(long, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(long first, long count) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(long, long) - start"); //$NON-NLS-1$
		}

		String qs = "select e from " + getEntityClass().getSimpleName() 
			+ " e order by e.id";
		Query query = getSession().createQuery(qs);
		query.setFirstResult((int)first);
		query.setMaxResults((int)count);
		List<T> returnList = query.list();
		if (log.isDebugEnabled()) {
			log.debug("findAll(long, long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.Dao#flush()
	 */
	@Override
	public void flush() {
		if (log.isDebugEnabled()) {
			log.debug("flush() - start"); //$NON-NLS-1$
		}

		getHibernateTemplate().flush();

		if (log.isDebugEnabled()) {
			log.debug("flush() - returns"); //$NON-NLS-1$
		}
	}
}
