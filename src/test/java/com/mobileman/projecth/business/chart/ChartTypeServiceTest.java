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
 * ChartTypeServiceTest.java
 * 
 * Projekt: MAIS
 * 
 * @author mobileman
 * @date 2.1.2011
 * @version 1.0
 * 
 * (c) 2005 DUPRES-Consulting s.r.o., Slovak republic
 */

package com.mobileman.projecth.business.chart;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.chart.ChartTypeService;
import com.mobileman.projecth.domain.chart.ChartType;

/**
 * Test for {@link ChartTypeService}
 * 
 * @author mobileman
 *
 */
public class ChartTypeServiceTest extends TestCaseBase {

	@Autowired
	private ChartTypeService chartTypeService;
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findByCode() throws Exception {
		
		List<ChartType> chartTypes = chartTypeService.findAll();
		assertEquals(6, chartTypes.size());
	}
	
}
