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

import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicatorValidation;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link PatientKeyPerformanceIndicator} entity.
 * 
 * @author mobileman
 * 
 */
public interface PatientKPIValidationDao extends Dao<PatientKeyPerformanceIndicatorValidation> {

	/**
	 * Finds all validates values entered by a doctor for given patient and
	 * given validation type ID
	 * 
	 * @param patientId
	 * @param questionaryValidationTypeId
	 * @param startDate
	 * @param endDate
	 * @return list of {@link KeyPerformanceIndicatorType}
	 */
	List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long patientId,
			Long questionaryValidationTypeId, Date startDate, Date endDate);

	/**
	 * Finds all validates values entered by a given doctor for given patient
	 * and given validation type ID
	 * 
	 * @param doctorId
	 * @param patientId
	 * @param questionaryValidationTypeId
	 * @param startDate
	 * @param endDate
	 * @return list of {@link KeyPerformanceIndicatorType}
	 */
	List<PatientKeyPerformanceIndicatorValidation> findValidatedValues(Long doctorId, Long patientId,
			Long questionaryValidationTypeId, Date startDate, Date endDate);

}
