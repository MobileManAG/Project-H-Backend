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
 * DiseaseTest
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 05.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.DiseaseGroup;
import com.mobileman.projecth.domain.disease.DiseaseSubgroup;

/**
 * @author MobileMan GmbH
 *
 */
public class DiseaseTest extends TestCaseBase {
	
	@Autowired
	private DiseaseService diseaseService;
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void save() throws Exception {
		
		diseaseService = (DiseaseService)applicationContext.getBean(ComponentNames.DISEASE_SERVICE);
		assertNotNull(diseaseService);
		
		DiseaseGroup diseaseGroup = new DiseaseGroup();
		diseaseGroup.setCode("code");
		diseaseGroup.setName("name");
		
		Long diseaseGroupId = diseaseService.saveGroup(diseaseGroup);
		assertNotNull(diseaseGroupId);
		assertTrue(!diseaseGroupId.equals(0L));
		
		DiseaseSubgroup diseaseSubgroup = new DiseaseSubgroup();
		diseaseSubgroup.setCode("code");
		diseaseSubgroup.setName("name");
		diseaseSubgroup.setDiseaseGroup(diseaseGroup);
		
		Long diseaseSubgroupId = diseaseService.saveSubgroup(diseaseSubgroup);
		assertNotNull(diseaseSubgroupId);
		assertTrue(!diseaseSubgroupId.equals(0L));
		
		Disease disease = new Disease();
		disease.setCode("code");
		disease.setName("name");
		disease.setImageName("imageName");
		disease.setDiseaseSubgroup(diseaseSubgroup);
		
		List<Disease> diseases = diseaseService.findAll();
		int oldSize = diseases.size();
		
		Long diseaseId = diseaseService.save(disease);
		assertNotNull(diseaseId);
		assertTrue(!diseaseId.equals(0L));
		
		diseases = diseaseService.findAll();
		assertTrue(diseases.size() == oldSize +1);
	}
	
}
