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
package com.mobileman.projecth.persistence.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.questionary.OneTimeHaq;
import com.mobileman.projecth.persistence.HaqDao;

/**
 * @author mobileman
 *
 */
@Repository("haqDao")
public class HaqDaoImpl extends DaoImpl<Haq> implements HaqDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HaqDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.HaqDao#findAllByDisease(java.lang.Long)
	 */	
	@Override
	@SuppressWarnings("unchecked")
	public List<Haq> findAllByDisease(Long diseaseId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAllByDisease(" + diseaseId + ") - start"); //$NON-NLS-1$
		}

		List<Haq> result = (List)getHibernateTemplate().find(
				"select h from Haq h where h.disease.id=? order by h.class, h.sortorder asc", 
				diseaseId);

		if (log.isDebugEnabled()) {
			log.debug("findAllByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.HaqDao#findOneTimeHaqByDisease(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public OneTimeHaq findOneTimeHaqByDisease(Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("findOneTimeHaqByDisease(" + diseaseId + ") - start"); //$NON-NLS-1$
		}

		List<OneTimeHaq> result = (List)getHibernateTemplate().find(
				"select h from Haq h where h.disease.id=? and h.kind=? order by h.sortorder asc", 
				new Object[]{ diseaseId, Haq.Kind.ONE_TIME });
		OneTimeHaq returnOneTimeHaq = result.isEmpty() ? null : result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findOneTimeHaqByDisease(Long) - returns"); //$NON-NLS-1$
		}
		
		return returnOneTimeHaq;
	}
	
}
