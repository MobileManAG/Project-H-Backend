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

package com.mobileman.projecth.persistence.patient;

import java.util.Date;
import java.util.List;

import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link PatientKeyPerformanceIndicator} entity.
 * 
 * @author mobileman
 *
 */
public interface PatientKPIDao extends Dao<PatientKeyPerformanceIndicator> {

	/**
	 * 
	 * @param doctorId
	 * @param startDate 
	 * @param endDate 
	 * @return Listing of average points by disease/all his patients for given time window
	 * 			<li> {@link Date} </li>
	 *          <li> {@link KeyPerformanceIndicatorType} </li>
	 *          <li> {@link Double} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 *             <li>startDate == null</li>
	 *             <li>endDate == null</li>
	 */
	List<KeyPerformanceIndicatorStatistics> findPatientsKpiAverageScoreTimelineByDisease(Long doctorId, Date startDate, Date endDate);
	
	/**
	 * 
	 * @param doctorId
	 * @return Listing of average scores by disease/all doctor's patients
	 *          <li> {@link KeyPerformanceIndicatorType} </li>
	 *          <li> {@link Double} </li>
	 * @throws IllegalArgumentException
	 *             if <li>doctorId == null</li>
	 */	
	List<Object[]> findPatientsKpiAverageScoreByDisease(Long doctorId);
}
