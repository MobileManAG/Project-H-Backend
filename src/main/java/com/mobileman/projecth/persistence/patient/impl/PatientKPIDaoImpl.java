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

package com.mobileman.projecth.persistence.patient.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.kpi.PatientKeyPerformanceIndicator;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;

/**
 * @author mobileman
 *
 */
@Repository("patientKPIDao")
public class PatientKPIDaoImpl extends DaoImpl<PatientKeyPerformanceIndicator> implements PatientKPIDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientKPIDaoImpl.class);

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientKPIDao#findPatientsKpiAverageScoreTimelineByDisease(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> findPatientsKpiAverageScoreTimelineByDisease(Long doctorId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreTimelineByDisease(Long, Date, Date) - start"); //$NON-NLS-1$
		}

		final String sqlQuery = 
			"select cast(pkpi.log_date as date), " +
			"kpit.id as kpitid, kpit.code as kpitcode, kpit.description as kpitdesc, " +
			"disease2_.id as did, disease2_.code as dcode, disease2_.name as dname, " +
			"avg(pkpi.kpi_value) " +
			"from patient_key_performance_indicator pkpi " +
			"inner join key_performance_indicator_type kpit on kpit.id=pkpi.validation_type_id " +
			"inner join disease disease2_ on disease2_.id=kpit.disease_id " +
			"where pkpi.patient_id in (" +
			"	select (case when userconnec3_.user_id=? then userconnec3_.owner_id else userconnec3_.user_id end) " +
			"	from user_connection userconnec3_ " +
			"	where userconnec3_.state='A' and (userconnec3_.owner_id=? or userconnec3_.user_id=?)" +
			") " +
			"and cast(pkpi.log_date as date) between cast(? as date) and cast(? as date) " +
			"group by cast(pkpi.log_date as date), " +
			"kpit.id, kpit.code, kpit.description," +
			"disease2_.id, disease2_.code, disease2_.name " +
			"order by 1, 2";
    	SQLQuery query = getSession().createSQLQuery(sqlQuery);
    	query.setParameter(0, doctorId);
    	query.setParameter(1, doctorId);
    	query.setParameter(2, doctorId);
    	query.setParameter(3, startDate);
    	query.setParameter(4, endDate);
    	
    	List<Object[]> dataList = query.list();
    	Map<Long, KeyPerformanceIndicatorType> kpiCache = new HashMap<Long, KeyPerformanceIndicatorType>();
    	Map<Long, Disease> diseaseCache = new HashMap<Long, Disease>();
    	
    	List<KeyPerformanceIndicatorStatistics> result = new ArrayList<KeyPerformanceIndicatorStatistics>(dataList.size());
    	for (int i = 0; i < dataList.size(); i++) {
    		Object[] data = dataList.get(i);
    		
    		Disease disease = findDiseasee(data, diseaseCache);
    		KeyPerformanceIndicatorType kpit = findKpiType(data, kpiCache);
    		if (kpit.getDisease() == null) {
    			kpit.setDisease(disease);
    		}   		
    		
    		kpit.setDisease(disease);
    		double kpiAvg = Number.class.cast(data[7]).doubleValue();
    		KeyPerformanceIndicatorStatistics item = new KeyPerformanceIndicatorStatistics(new BigDecimal(kpiAvg), (Date)data[0], kpit);
    		result.add(item);
    	}
    

		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreTimelineByDisease(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
    	return result;
	}

	/**
	 * 
	 */
	private Disease findDiseasee(Object[] data, Map<Long, Disease> cache) {
		Validate.notNull(data);
		Validate.notNull(cache);
		Long id = Number.class.cast(data[4]).longValue();
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		
		Disease disease = new Disease(id, (String)data[5], (String)data[6]);
		cache.put(disease.getId(), disease);
		
		return disease;
	}

	/**
	 * 
	 */
	private KeyPerformanceIndicatorType findKpiType(Object[] data, Map<Long, KeyPerformanceIndicatorType> cache) {
		Validate.notNull(data);
		Validate.notNull(cache);
		Long id = Number.class.cast(data[1]).longValue();
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		
		KeyPerformanceIndicatorType kpit = new KeyPerformanceIndicatorType(id, (String)data[2], (String)data[3]);
		cache.put(kpit.getId(), kpit);
		
		return kpit;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientKPIDao#findPatientsKpiAverageScoreByDisease(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findPatientsKpiAverageScoreByDisease(Long doctorId) {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreByDisease(Long) - start"); //$NON-NLS-1$
		}

		final String sqlQuery = 
			"select kpit.id as kpitid, kpit.code as kpitcode, kpit.description as kpitdesc, " +
			"disease2_.id as did, disease2_.code as dcode, disease2_.name as dname, " +
			"avg(pkpi.kpi_value) " +
			"from patient_key_performance_indicator pkpi " +
			"inner join key_performance_indicator_type kpit on kpit.id=pkpi.validation_type_id " +
			"inner join disease disease2_ on disease2_.id=kpit.disease_id " +
			"where pkpi.patient_id in (" +
			"	select (case when userconnec3_.user_id=? then userconnec3_.owner_id else userconnec3_.user_id end) " +
			"	from user_connection userconnec3_ " +
			"	where userconnec3_.state='A' and (userconnec3_.owner_id=? or userconnec3_.user_id=?)" +
			") " +
			"group by kpit.id, kpit.code, kpit.description," +
			"disease2_.id, disease2_.code, disease2_.name " +
			"order by 1, 2";
    	SQLQuery query = getSession().createSQLQuery(sqlQuery);
    	query.setParameter(0, doctorId);
    	query.setParameter(1, doctorId);
    	query.setParameter(2, doctorId);
    	
    	List<Object[]> dataList = query.list();
    	
    	List<Object[]> result = new ArrayList<Object[]>(dataList.size());
    	for (int i = 0; i < dataList.size(); i++) {
    		Object[] data = dataList.get(i);
    		int index = 0;
    		KeyPerformanceIndicatorType kpit = new KeyPerformanceIndicatorType(
    				Number.class.cast(data[index++]).longValue(), (String)data[index++], (String)data[index++]);
    		Disease disease = new Disease(Number.class.cast(data[index++]).longValue(), (String)data[index++], (String)data[index++]);
    		kpit.setDisease(disease);
    		double kpiAvg = Number.class.cast(data[index++]).doubleValue();
    		result.add(new Object[]{ data[0], kpit, Double.valueOf(kpiAvg) });
    	}
    

		if (log.isDebugEnabled()) {
			log.debug("findPatientsKpiAverageScoreByDisease(Long) - returns"); //$NON-NLS-1$
		}
    	return result;
	}
}
