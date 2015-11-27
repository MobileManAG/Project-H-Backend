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
 * HaqChartServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.chart.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.chart.HaqChartService;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.persistence.chart.HaqChartDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.HAQ_CHART_SERVICE)
public class HaqChartServiceImpl extends BusinessServiceImpl<HaqChart> implements HaqChartService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HaqChartServiceImpl.class);

	private HaqChartDao haqChartDao;
	
	/**
	 * @param haqChartDao new value of haqChartDao
	 */
	@Autowired
	public void setHaqChartDao(HaqChartDao haqChartDao) {
		if (log.isDebugEnabled()) {
			log.debug("setHaqChartDao(HaqChartDao) - start"); //$NON-NLS-1$
		}

		this.haqChartDao = haqChartDao;
		super.setDao(haqChartDao);

		if (log.isDebugEnabled()) {
			log.debug("setHaqChartDao(HaqChartDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.chart.HaqChartService#findByHaq(java.lang.Long)
	 */
	@Override
	public List<HaqChart> findByHaq(Long haqId) {
		if (log.isDebugEnabled()) {
			log.debug("findByHaq(Long) - start"); //$NON-NLS-1$
		}
		

		List<HaqChart> returnList = this.haqChartDao.find(haqId);
		if (log.isDebugEnabled()) {
			log.debug("findByHaq(Long) - returns"); //$NON-NLS-1$
		}
		return returnList;
	}

}
