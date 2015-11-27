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
 * HaqChartDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.chart;

import java.util.List;

import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link HaqChart} entity
 * 
 * @author mobileman
 *
 */
public interface HaqChartDao extends Dao<HaqChart> {

	/**
	 * Finds all charts defined for a given HAQ 
	 * @param haqId
	 * @return all charts defined  for a given HAQ 
	 */
	List<HaqChart> find(Long haqId);

}
