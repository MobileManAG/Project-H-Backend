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
 * HaqChartServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.chart;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.domain.disease.Haq;

/**
 * @author mobileman
 *
 */
public class HaqChartServiceTest extends TestCaseBase {

	@Autowired
	private HaqChartService haqChartService;
	
	@Autowired
	HaqService haqService;
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findByHaq() throws Exception {
		
		Haq haq = haqService.findAll().get(2);		
		List<HaqChart> charts = haqChartService.findByHaq(haq.getId());
		assertEquals(2, charts.size());
	}
}
