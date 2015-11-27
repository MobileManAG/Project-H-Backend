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
 * EntityService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import com.mobileman.projecth.domain.Entity;

/**
 * Declares common business operations for service interfaces of types derived
 * from the {@link Entity} type.
 * 
 * @author mobileman
 * @param <T>
 * 
 */
public interface EntityService<T> {

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

}
