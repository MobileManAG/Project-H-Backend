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
 * HaqChartDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 8.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.chart.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.chart.HaqChart;
import com.mobileman.projecth.persistence.chart.HaqChartDao;
import com.mobileman.projecth.persistence.impl.DaoImpl;

/**
 * 
 * @author mobileman
 *
 */
@Repository("haqChartDao")
public class HaqChartDaoImpl extends DaoImpl<HaqChart> implements HaqChartDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HaqChartDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.chart.HaqChartDao#find(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")	
	public List<HaqChart> find(Long haqId) {
		if (log.isDebugEnabled()) {
			log.debug("find(Long) - start"); //$NON-NLS-1$
		}

		List<HaqChart> result = (List)getHibernateTemplate().find(
				"select ch " +
				"from HaqChart ch " +
				"where ch.haq.id = ? " +
				"order by ch.sortOrder", 
				new Object[]{ haqId });

		if (log.isDebugEnabled()) {
			log.debug("find(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

}
