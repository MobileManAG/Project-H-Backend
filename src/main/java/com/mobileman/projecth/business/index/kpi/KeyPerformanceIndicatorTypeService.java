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
 * KeyPerformanceIndicatorTypeService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.index.kpi;

import java.util.List;

import com.mobileman.projecth.business.index.IndexService;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;

/**
 * Declares service For {@link KeyPerformanceIndicatorType}
 * 
 * @author mobileman
 *
 */
public interface KeyPerformanceIndicatorTypeService extends IndexService<KeyPerformanceIndicatorType> {

	/**
	 * @param code
	 * @param diseaseId 
	 * @return finds KeyPerformanceIndicatorType for given code and disease 
	 */
	KeyPerformanceIndicatorType find(String code, Long diseaseId);
	
	/**
	 * @param type
	 * @param diseaseId 
	 * @return finds KeyPerformanceIndicatorType for given code and disease 
	 */
	KeyPerformanceIndicatorType find(KeyPerformanceIndicatorType.Type type, Long diseaseId);
	
	/**
	 * @param diseaseId 
	 * @return finds KeyPerformanceIndicatorType's for given disease 
	 */
	List<KeyPerformanceIndicatorType> findAll(Long diseaseId);
}
