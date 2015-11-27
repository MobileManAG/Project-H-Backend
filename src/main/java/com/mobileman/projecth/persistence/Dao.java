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
 * Dao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence;

import java.util.List;


/**
 * Declares common operations for the entities DAOs
 * 
 * @author mobileman
 * @param <T> 
 *
 */
public interface Dao<T> {

	/**
	 * @param entity
	 * @return id
	 */
	Long save(T entity);
	
	/**
	 * @param entity
	 */
	void update(T entity);
	
	/**
	 * @param entity
	 */
	void delete(T entity);
	
	/**
	 * @param id
	 * @return T
	 */
	T findById(Long id);

	/**
	 * @return all entities
	 */
	List<T> findAll();
	
	/**
	 * @param first 
	 * @param count 
	 * @return all entities in range of given pagination
	 * 
	 */
	List<T> findAll(long first, long count);
	
	/**
	 * 
	 */
	void flush();

	/**
	 * Evicts entity from 1 lvl cache
	 * @param entity
	 */
	void evict(T entity);
}
