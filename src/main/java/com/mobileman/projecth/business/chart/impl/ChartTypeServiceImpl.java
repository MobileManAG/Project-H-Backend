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

package com.mobileman.projecth.business.chart.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.chart.ChartTypeService;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.domain.chart.ChartType;
import com.mobileman.projecth.persistence.chart.ChartTypeDao;

/**
 * @author mobileman
 *
 */
@Service(value = "chartTypeService")
public class ChartTypeServiceImpl extends BusinessServiceImpl<ChartType> implements ChartTypeService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ChartTypeServiceImpl.class);
	
	/**
	 * 
	 * @param chartTypeDao
	 */
	@Autowired
	public void setChartTypeDao(ChartTypeDao chartTypeDao) {
		if (log.isDebugEnabled()) {
			log.debug("setChartTypeDao(ChartTypeDao) - start"); //$NON-NLS-1$
		}

		super.setDao(chartTypeDao);

		if (log.isDebugEnabled()) {
			log.debug("setChartTypeDao(ChartTypeDao) - returns"); //$NON-NLS-1$
		}
	}
}
