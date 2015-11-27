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

package com.mobileman.projecth.persistence.index;

import java.util.List;

import com.mobileman.projecth.domain.index.Index;
import com.mobileman.projecth.persistence.Dao;

/**
 * DAO for all index entities
 * @author mobileman
 * @param <T> Index type
 *
 */
public interface IndexDao<T extends Index> extends Dao<T> {

	/**
	 * @param code
	 * @param type 
	 * @return index with given <code>code</code>
	 * @throws IllegalArgumentException
	 */
	T findByCode(String code, Class<T> type) throws IllegalArgumentException;
	
	/**
	 * @param type 
	 * @return all indexes
	 * @throws IllegalArgumentException
	 */
	List<T> findAll(Class<T> type) throws IllegalArgumentException;
	
	/**
	 * @param id
	 * @param type 
	 * @return index with given <code>code</code>
	 * @throws IllegalArgumentException
	 */
	T findById(Class<T> type, Long id) throws IllegalArgumentException;
}
