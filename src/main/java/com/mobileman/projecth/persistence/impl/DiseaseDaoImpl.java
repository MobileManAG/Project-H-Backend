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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.DiseaseGroup;
import com.mobileman.projecth.domain.disease.DiseaseSubgroup;
import com.mobileman.projecth.persistence.DiseaseDao;

/**
 * @author MobileMan GmbH
 *
 */
@Repository("diseaseDao")
public class DiseaseDaoImpl extends DaoImpl<Disease> implements DiseaseDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(DiseaseDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.DiseaseDao#saveGroup(com.mobileman.projecth.domain.disease.DiseaseGroup)
	 */
	@Override
	public Long saveGroup(DiseaseGroup diseaseGroup) {
		if (log.isDebugEnabled()) {
			log.debug("saveGroup(DiseaseGroup) - start"); //$NON-NLS-1$
		}

		Long result = null;
		result = (Long)getHibernateTemplate().save(diseaseGroup);

		if (log.isDebugEnabled()) {
			log.debug("saveGroup(DiseaseGroup) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.DiseaseDao#saveSubgroup(com.mobileman.projecth.domain.disease.DiseaseSubgroup)
	 */
	@Override
	public Long saveSubgroup(DiseaseSubgroup diseaseSubgroup) {
		if (log.isDebugEnabled()) {
			log.debug("saveSubgroup(DiseaseSubgroup) - start"); //$NON-NLS-1$
		}

		Long result = null;
		result = (Long)getHibernateTemplate().save(diseaseSubgroup);

		if (log.isDebugEnabled()) {
			log.debug("saveSubgroup(DiseaseSubgroup) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.DiseaseDao#findByCode(java.lang.String)
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public Disease findByCode(String code) {
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - start"); //$NON-NLS-1$
		}

		List<Disease> result = (List)getHibernateTemplate().find(
				"from Disease d where lower(d.code)=?", 
				new Object[]{code.toLowerCase()});
		if (result.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("findByCode(String) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		Disease returnDisease = result.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findByCode(String) - returns"); //$NON-NLS-1$
		}
		return returnDisease;
	}
}
