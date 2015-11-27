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
package com.mobileman.projecth.persistence.patient.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.patient.PatientDao;

/**
 * 
 */
@Repository("patientDao")
public class PatientDaoImpl extends DaoImpl<Patient> implements PatientDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientDao#computeCDAITimeline(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public double computeCDAI(Long patientId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCDAI(Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select count(distinct cast(patientans0_.logdate as date)) as col_0_0_ " +
			"from public.patient_answer_result_set patientans0_ " +
			"where patientans0_.patientid=? and (date(patientans0_.logdate) between date(?) and date(?))";
		
		final Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, patientId);
		query.setParameter(1, startDate);
		query.setParameter(2, endDate);
		
		List<Number> queryResult = query.list();
		int measures = queryResult.isEmpty() ? 0 : (queryResult.get(0) != null ? queryResult.get(0).intValue() : 0 );
		
		final double result;
		if (measures == 0) {
			result = 0.0;
		} else {
			queryResult = (List)getHibernateTemplate().find(
					"select count(p.answerId) from PatientAnswerResultSet p " +
					"where p.patientid=? and date(p.logdate) between date(?) and date(?) and p.haqid = 1 and p.answerId = 1", 
					new Object[]{ patientId, startDate, endDate } );
			double sumhaq1 = getDoubleValue(queryResult);
			
			queryResult = (List)getHibernateTemplate().find(
					"select count(p.answerId) from PatientAnswerResultSet p " +
					"where p.patientid=? and date(p.logdate) between date(?) and date(?) and p.haqid = 2 and p.answerId = 1", 
					new Object[]{ patientId, startDate, endDate } );
			double sumhaq2 = getDoubleValue(queryResult);
			
			queryResult = (List)getHibernateTemplate().find(
					"select sum(cast(p.answertext as int)) from PatientAnswerResultSet p " +
					"where p.patientid=? and date(p.logdate) between date(?) and date(?) and p.haqid = 4 and p.answerId >= 0", 
					new Object[]{ patientId, startDate, endDate } );
			double sumhaq4 = getDoubleValue(queryResult);
			
			result = sumhaq1/measures + sumhaq2/measures + sumhaq4/measures;
		}		
		


		if (log.isDebugEnabled()) {
			log.debug("computeCDAI(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientDao#computeCDAITimeline(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> computeCDAITimeline(Long patientId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select cast(patientans0_.logdate as date) as col_0_0_, 1 as date, " +
			"(select count(patientans2_.answer_id) from public.patient_answer_result_set patientans2_ " +
			"	where patientans2_.patientid=? and patientans2_.logdate = patientans0_.logdate and patientans2_.haqid=1 and patientans2_.answer_id=1) as count1,  " +
			"(select count(patientans3_.answer_id) from public.patient_answer_result_set patientans3_ " +
			"	where patientans3_.patientid=? and patientans3_.logdate = patientans0_.logdate and patientans3_.haqid=2 and patientans3_.answer_id=1) as count2 ,  " +
			"(select sum(cast(patientans4_.answertext as int4)) from public.patient_answer_result_set patientans4_ " +
			"	where patientans4_.patientid=? and patientans4_.logdate = patientans0_.logdate and patientans4_.haqid=4 and patientans4_.answer_id>=0) as sum4  " +
			"from public.patient_answer_result_set patientans0_  " +
			"where patientans0_.patientid=? " +
			"and (date(patientans0_.logdate) between date(?) and date(?)) " +
			"and patientans0_.haqid in (1,2,4)" +
			"group by patientans0_.logdate order by 1";
		final Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, patientId);
		query.setParameter(1, patientId);
		query.setParameter(2, patientId);
		query.setParameter(3, patientId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		/*
		List<Object[]> data = (List)getHibernateTemplate().find(
				"select cast(p1.logdate as date), " +
				"1 as dummy, " +
				"(select count(distinct p.logdate) from PatientAnswerResultSet p where p.patientid=p1.patientid and date(p.logdate) >= ? and date(p.logdate) <= date(p1.logdate)) as logs_count, " +
				"(select count(p.answerId) from PatientAnswerResultSet p where p.patientid=p1.patientid and date(p.logdate) >= ? and date(p.logdate) <= date(p1.logdate) and p.haqid = 1 and p.answerId = 1) as logs_count1, " +
				"(select count(p.answerId) from PatientAnswerResultSet p where p.patientid=p1.patientid and date(p.logdate) >= ? and date(p.logdate) <= date(p1.logdate) and p.haqid = 2 and p.answerId = 1) as logs_count2, " +
				"(select sum(cast(p.answertext as int)) from PatientAnswerResultSet p where p.patientid=p1.patientid and date(p.logdate) >= ? and date(p.logdate) <= date(p1.logdate) and p.haqid = 4 and p.answerId >= 0) as logs_count3 " +
				
				"from PatientAnswerResultSet p1 where p1.patientid=? and date(p1.logdate) between date(?) and date(?) " +
				"group by p1.logdate, p1.patientid " +
				"order by 1", 
				new Object[]{ startDate, startDate, startDate, startDate, patientId, startDate, endDate } );
		*/
		
		List<Object[]> result = query.list();
		List<Object[]> results = new ArrayList<Object[]>(result.size());
		Map<Date, Object[]> marker = new HashMap<Date, Object[]>();
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
						
			Object[] data = marker.get(date);
			
			double cnt1 = 0.0d, cnt2 = 0.0d, sum = 0.0d;
			if (arrayValues[2] != null) {
				cnt1 = Number.class.cast(arrayValues[2]).longValue();
			}
			
			if (arrayValues[3] != null) {
				cnt2 = Number.class.cast(arrayValues[3]).longValue();
			}
			
			if (arrayValues[4] != null) {
				sum = Number.class.cast(arrayValues[4]).longValue();
			}
			
			if (data == null) {
				data = new Object[]{ date, Double.valueOf(cnt1 + cnt2 + sum) };
				marker.put(date, data);
				results.add(data);
			} else {
				data[1] = Double.class.cast(data[1]).doubleValue() + (cnt1 + cnt2 + sum);
			}
		}
		/*			
		List<Object[]> data = (List)getHibernateTemplate().find(
				"select cast(pki.logDate as date), cast(pki.value as double) " +
				"from PatientKeyPerformanceIndicator pki where pki.patient.id=? and date(pki.logDate) between date(?) and date(?) " +
				"order by 1", 
				new Object[]{ patientId, startDate, endDate } );
		*/

		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientDao#findAllPatientsWithAccounts()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Patient> findAllPatientsWithAccounts() {
		if (log.isDebugEnabled()) {
			log.debug("findAllPatientsWithAccounts() - start"); //$NON-NLS-1$
		}
		
		List<Patient> result = (List)getHibernateTemplate().find(
				"select u from Patient u where u.userAccount.id is not null order by u.created desc");

		if (log.isDebugEnabled()) {
			log.debug("findAllPatientsWithAccounts() - returns"); //$NON-NLS-1$
		}
		
		return result;
	}
}
