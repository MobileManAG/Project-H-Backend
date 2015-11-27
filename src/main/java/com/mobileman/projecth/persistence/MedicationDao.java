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
 * MedicationDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence;

import java.util.List;
import java.util.Locale;

import com.mobileman.projecth.domain.medicine.Medication;

/**
 * Represents DAO for the {@link Medication} entity.
 * @author mobileman
 *
 */
public interface MedicationDao extends Dao<Medication> {

	/**
	 * @param pzn
	 * @param locale 
	 * @return Medication found by pzn (pharmazentralnummer)
	 * @throws IllegalArgumentException 
	 */
	Medication findByPzn(String pzn, Locale locale) throws IllegalArgumentException;
	
	/**
	 * @param name
	 * @param locale 
	 * @return Medication found by medication name (amr)
	 * @throws IllegalArgumentException 
	 */
	List<Medication> findByName(String name, Locale locale) throws IllegalArgumentException;
	
	/**
	 * @param filter
	 * @param locale 
	 * @return list of medications whose name or pzn starts with given name or empty list
	 * @throws IllegalArgumentException if 
	 * <li>filter == null or filter is blank</li> 
	 * <li>locale == null</li> 
	 */
	List<Medication> findAllByNameOrPzn(String filter, Locale locale) throws IllegalArgumentException;
}
