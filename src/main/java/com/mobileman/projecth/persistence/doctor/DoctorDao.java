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
package com.mobileman.projecth.persistence.doctor;

import java.util.List;

import com.mobileman.projecth.domain.data.Gender;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link Doctor} entity.
 * 
 * @author mobileman
 * 
 */
public interface DoctorDao extends Dao<Doctor> {

	/**
	 * 
	 * @param doctorId
	 * @return Listing of number of patents by disease
	 * 			<li>count</li>
	 *			<li> {@link Disease} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByDisease(Long doctorId) throws IllegalArgumentException;

	/**
	 * 
	 * @param doctorId
	 * @return Listing of averages of gender by disease/all doctor's patients 
	 * 			<li>count</li>
	 *         	<li> {@link Gender} </li>
	 *         	<li> {@link Disease} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByGenderByDisease(Long doctorId) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param doctorId
	 * @return Listing of averages, broken down by 7 groups (age) by disease/all doctor's patients 
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */
	List<Object[]> findPatientsCountByAgeByDisease(Long doctorId) throws IllegalArgumentException;

	/**
	 * @return all doctors with valid accounts
	 */
	List<Doctor> findAllDoctorsWithAccounts();
}
