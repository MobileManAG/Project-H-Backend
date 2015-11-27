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

import com.mobileman.projecth.business.EntityService;
import com.mobileman.projecth.business.SearchService;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;

/**
 * Service processing KPI values validated by a doctor
 * 
 * @author mobileman
 * 
 */
public interface PatientKPIValidationService extends EntityService<PatientKeyPerformanceIndicatorValidation>, SearchService<PatientKeyPerformanceIndicatorValidation> {
	
	/**
	 * Saves KPI value manualy entered/validated by doctor 
	 * 
	 * @param doctorId
	 * @param patientId
	 * @param date
	 * @param kpi
	 * @param validationValue
	 * @throws IllegalArgumentException 
	 */
	void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue) throws IllegalArgumentException;
	
	/**
	 * Finds all validates values entered by a doctor for given patient and given validation type ID
	 * @param patientId
	 * @param kpiTypeId id of {@link KeyPerformanceIndicatorType} 
	 * @param startDate 
	 * @param endDate 
	 * @return list of {@link KeyPerformanceIndicatorType}
	 */
	List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long patientId, Long kpiTypeId, Date startDate, Date endDate);
	
	/**
	 * Finds all validates values entered by a given doctor for given patient and given validation type ID
	 * @param doctorId 
	 * @param patientId
	 * @param kpiTypeId id of {@link KeyPerformanceIndicatorType} 
	 * @param startDate 
	 * @param endDate 
	 * @return list of {@link KeyPerformanceIndicatorType}
	 */
	List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long doctorId, Long patientId, Long kpiTypeId, Date startDate, Date endDate);

	/**
	 * Saves KPI value manualy entered/validated by doctor with data
	 * 
	 * @param doctorId
	 * @param patientId
	 * @param date
	 * @param kpi
	 * @param validationValue
	 * @param data additional data used for computation of given validated kpi value
	 * @throws IllegalArgumentException 
	 */
	void saveValidatedKpiValue(Long doctorId, Long patientId, Date date, KeyPerformanceIndicatorType kpi,
			BigDecimal validationValue, List<BigDecimal> data) throws IllegalArgumentException;
}
