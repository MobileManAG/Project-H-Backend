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
 * PatientKeyPerformanceIndicatorService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mobileman.projecth.business.SearchService;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;

/**
 * Service performing various KPI computations for a patient
 * 
 * @author mobileman
 * 
 */
public interface PatientKPIService extends SearchService<PatientKeyPerformanceIndicator> {

	/**
	 * @param patientId
	 * @param kpiTypeId
	 * @param startDate
	 * @param endDate
	 * @return computed KPI value based on given type of
	 *         {@link KeyPerformanceIndicatorType} in given time window for a
	 *         patient
	 * @throws IllegalArgumentException
	 *             if <li>patientId == null</li> <li>kpiTypeId == null</li> <li>
	 *             startDate == null</li> <li>endDate == null</li>
	 */
	BigDecimal computeKPI(Long patientId, Long kpiTypeId, Date startDate, Date endDate) throws IllegalArgumentException;
	
	/**
	 * Computes KPI values for giben type of {@link KeyPerformanceIndicatorType} in given time window for a
	 * patient
	 * @param patientId
	 * @param kpiTypeId
	 * @param startDate
	 * @param endDate
	 * @return computed list of computed KPI values for giben type of
	 *         {@link KeyPerformanceIndicatorType} in given time window for a
	 *         patient
	 * @throws IllegalArgumentException if 
	 *             <li>patientId == null</li> 
	 *             <li>kpiTypeId == null</li> 
	 *             <li>startDate == null</li> 
	 *             <li>endDate == null</li>
	 */
	List<KeyPerformanceIndicatorStatistics> computeKPITimeline(Long patientId, Long kpiTypeId, Date startDate,
			Date endDate) throws IllegalArgumentException;
	
	/**
	 * Computes KPI average values for giben type of {@link KeyPerformanceIndicatorType} in given time window 
	 * @param kpiTypeId
	 * @param startDate
	 * @param endDate
	 * @return computed list of computed KPI average values for giben type of
	 *         {@link KeyPerformanceIndicatorType} in given time window
	 * @throws IllegalArgumentException if 
	 *             <li>kpiTypeId == null</li> 
	 *             <li>startDate == null</li> 
	 *             <li>endDate == null</li>
	 */
	List<KeyPerformanceIndicatorStatistics> computeKPIAverageTimeline(Long kpiTypeId, Date startDate,
			Date endDate) throws IllegalArgumentException;
}
