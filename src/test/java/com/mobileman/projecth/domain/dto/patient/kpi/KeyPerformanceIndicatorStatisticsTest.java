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
 * KeyPerformanceIndicatorStatisticsTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.dto.patient.kpi;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

/**
 * @author mobileman
 *
 */
public class KeyPerformanceIndicatorStatisticsTest {

	/**
	 * @throws Exception
	 */
	@Test
	public void keyPerformanceIndicatorStatistics() throws Exception {
		KeyPerformanceIndicatorStatistics obj = new KeyPerformanceIndicatorStatistics(null, new Date(), null);
		assertEquals(new BigDecimal("0.0"), obj.getValue());
	}
}
