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
package com.mobileman.projecth.persistence;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.DiseaseGroup;
import com.mobileman.projecth.domain.disease.DiseaseSubgroup;

/**
 * Represents DAO for the {@link Disease} entity.
 * @author MobileMan GmbH
 *
 */
public interface DiseaseDao extends Dao<Disease>{
	
	/**
	 * 
	 * @param diseaseGroup
	 * @return DiseaseGroup id
	 */
	Long saveGroup(DiseaseGroup diseaseGroup);
	
	/**
	 * 
	 * @param diseaseSubgroup
	 * @return DiseaseSubgroup id
	 */
	Long saveSubgroup(DiseaseSubgroup diseaseSubgroup);

	/**
	 * @param code
	 * @return disease by given code (null if such disease does not exists)
	 */
	Disease findByCode(String code);
}
