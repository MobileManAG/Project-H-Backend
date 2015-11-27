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
package com.mobileman.projecth.persistence.doctor.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.data.Gender;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.persistence.doctor.DoctorDao;
import com.mobileman.projecth.persistence.impl.DaoImpl;

/**
 * @author mobileman
 *
 */
@Repository("doctorDao")
public class DoctorDaoImpl extends DaoImpl<Doctor> implements DoctorDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(DoctorDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.doctor.DoctorDao#findPatientsCountByDisease(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPatientsCountByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByDisease(Long) - start"); //$NON-NLS-1$
		}
		
		List<Object[]> dataList = (List)getHibernateTemplate().find(
				"select count(p.id), d.id, d.code, d.name " +
				"from Patient p " +
				"join p.diseases d " +
				"where p.id in (" +
				"	select (case when uc.user.id=? then uc.owner.id else uc.user.id end) " +
				"	from UserConnection uc " +
				"	where uc.state='A' and (uc.owner.id=? or uc.user.id=?)" +
				") " +
				"group by d.id, d.code, d.name " +
				"order by d.id", new Object[]{ doctorId, doctorId, doctorId });
		
		List<Object[]> result = new ArrayList<Object[]>(dataList.size());
		for (int i = 0; i < dataList.size(); i++) {
			Object[] data = dataList.get(i);
			Disease disease = new Disease((Long)data[1], (String)data[2], (String)data[3]);			
			result.add(new Object[]{ data[0], disease });
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.doctor.DoctorDao#findPatientsCountByGenderByDisease(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPatientsCountByGenderByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByGenderByDisease(Long) - start"); //$NON-NLS-1$
		}
		
		List<Object[]> dataList = (List)getHibernateTemplate().find(
				"select count(p.id), p.sex, d.id, d.code, d.name " +
				"from Patient p " +
				"join p.diseases d " +
				"where p.id in (" +
				"	select (case when uc.user.id=? then uc.owner.id else uc.user.id end) " +
				"	from UserConnection uc " +
				"	where uc.state='A' and (uc.owner.id=? or uc.user.id=?)" +
				") " +
				"group by p.sex, d.id, d.code, d.name " +
				"order by p.sex, d.id", new Object[]{ doctorId, doctorId, doctorId });
		
		List<Object[]> result = new ArrayList<Object[]>(dataList.size());
		for (int i = 0; i < dataList.size(); i++) {
			Object[] data = dataList.get(i);
			Disease disease = new Disease((Long)data[2], (String)data[3], (String)data[4]);
			Gender gender = null;
			if (data[1] != null) {
				gender = Gender.getGender(Number.class.cast(data[1]).intValue());
			}
			
			result.add(new Object[]{ data[0], gender, disease });
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByGenderByDisease(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.doctor.DoctorDao#findPatientsCountByAgeByDisease(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findPatientsCountByAgeByDisease(Long doctorId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByAgeByDisease(Long) - start"); //$NON-NLS-1$
		}
		
		final String sqlQuery = "select count(patient0_.id) as col_0_0_,  " +
    		"case " +
    		"	when patient0_.birthday is null then 0 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) <= interval '19 year' then 1 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) between interval '20 year' and interval '29 year' then 2 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) between interval '30 year' and interval '39 year' then 3 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) between interval '40 year' and interval '49 year' then 4 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) between interval '50 year' and interval '59 year' then 5 " +
    		"	when age(to_date(to_char(patient0_.birthday, '9999'), 'YYYY')) between interval '60 year' and interval '69 year' then 6 " +
    		"	else 7 " +
    		"end, " + 
    		"disease2_.id as col_2_0_, disease2_.code as col_3_0_, disease2_.name as col_4_0_  " +
    		"from \"user\" patient0_  " +
    		"inner join user_disease diseases1_ on patient0_.id=diseases1_.user_id  " +
    		"inner join disease disease2_ on diseases1_.disease_id=disease2_.id  " +
    		"where patient0_.user_type='P'  " +
    		"and patient0_.id in ( " +
    		"	select (case when userconnec3_.user_id=? then userconnec3_.owner_id else userconnec3_.user_id end)  " +
    		"	from user_connection userconnec3_  " +
    		"	where userconnec3_.state='A' and (userconnec3_.owner_id=? " +
    		"	or userconnec3_.user_id=?) " +
    		") " +
    		"group by patient0_.birthday, disease2_.id, disease2_.code, disease2_.name " +
    		"order by patient0_.birthday, disease2_.id";
    	SQLQuery query = getSession().createSQLQuery(sqlQuery);
    	query.setParameter(0, doctorId);
    	query.setParameter(1, doctorId);
    	query.setParameter(2, doctorId);
    	
    	List<Object[]> dataList = query.list();
    	
    	List<Object[]> result = new ArrayList<Object[]>(dataList.size());
    	for (int i = 0; i < dataList.size(); i++) {
    		Object[] data = dataList.get(i);
    		Disease disease = new Disease(Number.class.cast(data[2]).longValue(), (String)data[3], (String)data[4]);
    		int group = Number.class.cast(data[1]).intValue();
    		result.add(new Object[]{ Number.class.cast(data[0]).longValue(), new Integer(group), disease });
    	}
	

		if (log.isDebugEnabled()) {
			log.debug("findPatientsCountByAgeByDisease(Long) - returns"); //$NON-NLS-1$
		}
    	return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.doctor.DoctorDao#findAllDoctorsWithAccounts()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Doctor> findAllDoctorsWithAccounts() {
		if (log.isDebugEnabled()) {
			log.debug("findAllDoctorsWithAccounts() - start"); //$NON-NLS-1$
		}
		
		List<Doctor> dataList = (List)getHibernateTemplate().find(
				"select d from Doctor d where d.userAccount.id is not null order by d.created desc");

		if (log.isDebugEnabled()) {
			log.debug("findAllDoctorsWithAccounts() - returns"); //$NON-NLS-1$
		}
		
		return dataList;
	}	
}
