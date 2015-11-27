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
 * PatientQuestionAnswerDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.patient.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.dto.patient.AnswerFrequency;
import com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.questionary.Question;
import com.mobileman.projecth.domain.questionary.QuestionType;
import com.mobileman.projecth.domain.questionary.QuestionType.AnswerDataType;
import com.mobileman.projecth.persistence.KeyPerformanceIndicatorTypeDao;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao;
import com.mobileman.projecth.util.Pair;

/**
 * @author mobileman
 *
 */
@Repository("patientQuestionAnswerDao")
public class PatientQuestionAnswerDaoImpl extends DaoImpl<PatientQuestionAnswer> implements PatientQuestionAnswerDao {
	
	@Autowired
	private KeyPerformanceIndicatorTypeDao keyPerformanceIndicatorTypeDao;
	
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(PatientQuestionAnswerDaoImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computePositiveAnswerFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AnswerFrequency> computePositiveAnswerFrequencyReport(Long patientId, Long haqId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computePositiveAnswerFrequencyReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		List<AnswerFrequency> result = (List)getHibernateTemplate().find(
				"select " +
				"new com.mobileman.projecth.domain.dto.patient.AnswerFrequency(qa.logDate, count(qa.answer.id)) " +
				"from PatientQuestionAnswer qa " +
				"where qa.patient.id=? " +
				"and qa.question.haq.id=? " +
				"and qa.answer.questionType.answerDataType=? " +
				"and qa.answer.active is true " +
				"and date(qa.logDate) between date(?) and date(?) " +
				"group by qa.logDate " +
				"order by 1 ", 
				new Object[]{ patientId, haqId, AnswerDataType.BOOLEAN, startDate, endDate });

		if (log.isDebugEnabled()) {
			log.debug("computePositiveAnswerFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeAllAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> computeAllAnswersFrequencyReport(Long patientId, Long haqId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeAllAnswersFrequencyReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = (List)getHibernateTemplate().find(
				"select " +
				"qa.logDate, q.id, q.text, a.id, (case when qa.customAnswer is not null then qa.customAnswer else a.answer end), a.kind " +
				"from PatientQuestionAnswer qa " +
				"join qa.patient p " +
				"join qa.question q " +
				"join qa.answer a " +
				"join q.haq h " +
				"where p.id=? and h.id=? and date(qa.logDate) between date(?) and date(?) " +
				"group by qa.logDate, q.id, q.text, a.id, a.answer, a.kind, qa.customAnswer " +
				"order by 1, 3, 5 ", 
				new Object[]{ patientId, haqId, startDate, endDate });

		if (log.isDebugEnabled()) {
			log.debug("computeAllAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeQuestionaryAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeQuestionaryAnswersReport(Long patientId, Long haqId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		List<PatientQuestionaryAnswerStatistic> result = (List)getHibernateTemplate().find(
				"select new com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic(" +
				"	h.haqQuestion, h.id, q.text, q.id, qta.answer, qta.id, " +
				"	(" +
				"	select count(pqa.id) from PatientQuestionAnswer pqa " +
				"	where pqa.patient.id=? and pqa.question.id=q.id and pqa.answer.id = qta.id " +
				"	and date(pqa.logDate) between date(?) and date(?)" +
				"	)" +
				") " +
				"from Question q " +
				"join q.questionType qt " +
				"join qt.answers qta " +
				"join q.haq h " +
				"where h.id=? " +
				"group by h.haqQuestion, h.id, q.text, q.id, qta.answer, qta.id " +
				"order by 2, 3, 6 ", 
				new Object[]{ patientId, startDate, endDate, haqId } );
		

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeQuestionAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersReport(" + patientId + ", " + questionId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<PatientQuestionaryAnswerStatistic> result = (List)getHibernateTemplate().find(
				"select new com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic(" +
				"	h.haqQuestion, h.id, q.text, q.id, qta.answer, qta.id, " +
				"	(" +
				"	select count(pqa.id) from PatientQuestionAnswer pqa " +
				"	where pqa.patient.id=? and pqa.question.id=q.id and pqa.answer.id = qta.id " +
				"	and date(pqa.logDate) between date(?) and date(?)" +
				"	)" +
				") " +
				"from Question q " +
				"join q.questionType qt " +
				"join qt.answers qta " +
				"join q.haq h " +
				"where q.id=? " +
				"group by h.haqQuestion, h.id, q.text, q.id, qta.answer, qta.id " +
				"order by 2, 3, 6 ", 
				new Object[]{ patientId, startDate, endDate, questionId } );
		

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCDAI(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal computeCDAI(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCDAI(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}

		final String queryString = 
			"select count(distinct cast(pqa.logdate as date)) " +
			"from patient_question_answer pqa " +
			"join question q on q.id = pqa.question_id " +
			"join haq h on h.id = q.haq_id " +
			"where h.disease_id = ? and pqa.patient_id=? and (date(pqa.logdate) between date(?) and date(?))";
		
		final Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, diseaseId);
		query.setParameter(1, patientId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		
		List<Number> queryResult = query.list();
		int measures = queryResult.isEmpty() ? 0 : (queryResult.get(0) != null ? queryResult.get(0).intValue() : 0 );
		
		final double result;
		if (measures == 0) {
			result = 0.0;
		} else {
			queryResult = (List)getHibernateTemplate().find(
					"select count(p.answer.id) from PatientQuestionAnswer p " +
					"join p.question q " +
					"join p.answer a " +
					"join q.haq h " +
					"where p.patient.id=? and date(p.logDate) between date(?) and date(?) and h.id = 1 and a.active is true", 
					new Object[]{ patientId, startDate, endDate } );
			double sumhaq1 = getDoubleValue(queryResult);
			
			queryResult = (List)getHibernateTemplate().find(
					"select count(p.answer.id) from PatientQuestionAnswer p " +
					"join p.question q " +
					"join p.answer a " +
					"join q.haq h " +
					"where p.patient.id=? and date(p.logDate) between date(?) and date(?) and h.id = 2 and a.active is true", 
					new Object[]{ patientId, startDate, endDate } );
			double sumhaq2 = getDoubleValue(queryResult);
			
			queryResult = (List)getHibernateTemplate().find(
					"select sum(case when qt.type=? then cast(p.customAnswer as double) else cast(a.answer as double) end) from PatientQuestionAnswer p " +
					"join p.question q " +
					"join q.questionType qt " +
					"join p.answer a " +
					"join q.haq h " +
					"where p.patient.id=? and date(p.logDate) between date(?) and date(?) and h.id = 4 and a.active is true " +
					"group by qt.type", 
					new Object[]{ QuestionType.Type.SCALE, patientId, startDate, endDate } );
			double sumhaq4 = getDoubleValue(queryResult);
			
			result = sumhaq1/measures + sumhaq2/measures + sumhaq4/measures;
		}		
		


		BigDecimal returnBigDecimal = new BigDecimal(result);
		if (log.isDebugEnabled()) {
			log.debug("computeCDAI(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnBigDecimal;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCDAITimeline(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computeCDAITimeline(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select cast(patientans0_.logdate as date) as cnt, 1 as date, " +
			"(select count(patientans2_.answer_id) from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.patient_id=? and patientans2_.logdate = patientans0_.logdate and q.haq_id=1 and a.active is true) as cnt1, " +
			"(select count(patientans2_.answer_id) from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.patient_id=? and patientans2_.logdate = patientans0_.logdate and q.haq_id=2 and a.active is true) as cnt2, " +
			"(select case when sum(cast(patientans2_.custom_answer as decimal)) is null then 0 else sum(cast(patientans2_.custom_answer as decimal)) end from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.patient_id=? and patientans2_.logdate = patientans0_.logdate and q.haq_id=4 and a.active is true) as sum1 " +
			"from patient_question_answer patientans0_  " +
			"join question q on q.id = patientans0_.question_id " +
			"join haq h on h.id = q.haq_id " +
			"where patientans0_.patient_id=? " +
			"and (date(patientans0_.logdate) between date(?) and date(?)) " +
			"and h.id in (1, 2, 4) " +
			"and h.disease_id=? " +
			"group by patientans0_.logdate order by 1";
		final Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, patientId);
		query.setParameter(1, patientId);
		query.setParameter(2, patientId);
		query.setParameter(3, patientId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		query.setParameter(6, diseaseId);
		
		KeyPerformanceIndicatorType cdai = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.CDAI, diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
						
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
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
				data = new KeyPerformanceIndicatorStatistics(new BigDecimal(cnt1 + cnt2 + sum), date, cdai);
				marker.put(date, data);
				results.add(data);
			} else {
				BigDecimal newValue = data.getValue().add(new BigDecimal((cnt1 + cnt2 + sum)));
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCDAIAverageTimeline(Long, Date, Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computeCDAIAverageTimeline( Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select patientans0_.logdate as log_date, 1 as date, " +
			"avg(	" +
			"	(select case when count(patientans2_.answer_id) is null then 0 else count(patientans2_.answer_id) end from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.logdate = patientans0_.logdate and q.haq_id=1 and a.active is true) + " +
			"	(select case when count(patientans2_.answer_id) is null then 0 else count(patientans2_.answer_id) end from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.logdate = patientans0_.logdate and q.haq_id=2 and a.active is true) + " +
			"	(select case when sum(cast(patientans2_.custom_answer as decimal)) is null then 0 else sum(cast(patientans2_.custom_answer as decimal)) end from patient_question_answer patientans2_ join question q on q.id = patientans2_.question_id join question_type_answer a on a.id = patientans2_.answer_id where patientans2_.logdate = patientans0_.logdate and q.haq_id=4 and a.active is true) " +
			")" +
			"from patient_question_answer patientans0_  " +
			"join question q on q.id = patientans0_.question_id " +
			"join haq h on h.id = q.haq_id " +
			"where (date(patientans0_.logdate) between date(?) and date(?)) " +
			"and h.id in (1, 2, 4) " +
			"and h.disease_id=? " +
			"group by patientans0_.logdate order by 1";
		final Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, startDate);
		query.setParameter(1, endDate);
		query.setParameter(2, diseaseId);
		
		KeyPerformanceIndicatorType cdai = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.CDAI, diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
						
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
			double cdaiValue = 0.0d;
			if (arrayValues[2] != null) {
				cdaiValue = Number.class.cast(arrayValues[2]).longValue();
			}
			
			if (data == null) {
				data = new KeyPerformanceIndicatorStatistics(new BigDecimal(cdaiValue), date, cdai);
				marker.put(date, data);
				results.add(data);
			} else {
				BigDecimal newValue = data.getValue().add(new BigDecimal(cdaiValue));
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeCDAITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCDAI(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal computeBASDAI(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeBASDAI(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		/*
		final String queryString = 
			"select avg( (distinct (" +
			"	(" +
			"		(" +
			"			(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2004 and a.active is true) + " +
			"			(select case when cast(a.answer as double) is null then 0.0 else cast(a.answer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2005 and a.active is true) " +
			"		) / 2.0 " +
			"	+ " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2000 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2001 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2002 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2003 and a.active is true) " +
			"	) / 5.0" +
			"	)" +
			")) as avg_val " +
			"from PatientQuestionAnswer pqa  " +
			"join pqa.patient p " +
			"join pqa.question q " +
			"join q.haq h " +
			"where p.id=:_pid " +
			"and (date(pqa.logDate) between date(:_ds) and date(:_de)) " +
			"and h.disease.id=:_did ";
		final Query query = getSession().createQuery(queryString);
		query.setParameter("_pid", patientId);
		query.setParameter("_ds", startDate);
		query.setParameter("_de", endDate);
		query.setParameter("_did", diseaseId);
				
		List<Object[]> result = query.list();
		BigDecimal returnBigDecimal;
		if (result.isEmpty() || result.get(0) == null) {
			returnBigDecimal = new BigDecimal(0.0d);
		} else {
			returnBigDecimal = new BigDecimal(Number.class.cast(result.get(0)).doubleValue());
		}
		*/
		List<KeyPerformanceIndicatorStatistics> stats = computeBASDAITimeline(patientId, diseaseId, startDate, endDate);
		int count = 0;
		double val = 0.0d;
		for (KeyPerformanceIndicatorStatistics stat : stats) {
			double tmp = stat.getValue().doubleValue();
			if (tmp > 0.0d) {
				val +=tmp;
				++count;
			}
		}
		
		if (count > 0) {
			val /= count;
		}
		
		BigDecimal returnBigDecimal = new BigDecimal(val);
		if (log.isDebugEnabled()) {
			log.debug("computeBASDAI(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return returnBigDecimal;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeBASDAITimeline(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computeBASDAITimeline(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeBASDAITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select cast(pqa.logDate as date) as cnt, " +
			"(" +
			"	(" +
			"		(" +
			"			(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2004 and a.active is true) + " +
			"			(select case when cast(a.answer as double) is null then 0.0 else cast(a.answer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2005 and a.active is true) " +
			"		) / 2.0 " +
			"	+ " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2000 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2001 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2002 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a join pqa1.patient p where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2003 and a.active is true) " +
			"	) / 5.0" +
			") as r " +
			"from PatientQuestionAnswer pqa  " +
			"join pqa.patient p " +
			"join pqa.question q " +
			"join q.haq h " +
			"where p.id=:_pid " +
			"and (date(pqa.logDate) between date(:_ds) and date(:_de)) " +
			"and h.disease.id=:_did " +
			"group by pqa.logDate order by 1";
		final Query query = getSession().createQuery(queryString);
		query.setParameter("_pid", patientId);
		query.setParameter("_ds", startDate);
		query.setParameter("_de", endDate);
		query.setParameter("_did", diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		KeyPerformanceIndicatorType basdai = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.BASDAI, diseaseId);
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
			
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
			BigDecimal value = null;
			if (arrayValues[1] != null) {
				value = new BigDecimal(Number.class.cast(arrayValues[1]).doubleValue());
			}
			
			if (data == null) {
				data = new KeyPerformanceIndicatorStatistics(value, date, basdai);
				marker.put(date, data);
				results.add(data);
			} else if (value != null) {
				BigDecimal newValue = data.getValue().add(value);
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeBASDAITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeBASDAIAverageTimeline(Long, Date, Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computeBASDAIAverageTimeline(Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeBASDAITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select pqa.logDate as log_date, " +
			"avg(" +
			"	(" +
			"		(" +
			"			(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2004 and a.active is true) + " +
			"			(select case when cast(a.answer as double) is null then 0.0 else cast(a.answer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2005 and a.active is true) " +
			"		) / 2 " +
			"	+ " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2000 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2001 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2002 and a.active is true) + " +
			"		(select case when cast(pqa1.customAnswer as double) is null then 0 else cast(pqa1.customAnswer as double) end from PatientQuestionAnswer pqa1 join pqa1.question q1 join pqa1.answer a where pqa1.logDate = pqa.logDate and q1.id=2003 and a.active is true) " +
			"	) / 5" +
			") as r " +
			"from PatientQuestionAnswer pqa  " +
			"join pqa.question q " +
			"join q.haq h " +
			"where (date(pqa.logDate) between date(:_ds) and date(:_de)) " +
			"and h.disease.id=:_did " +
			"group by pqa.logDate order by 1";
		final Query query = getSession().createQuery(queryString);
		query.setParameter("_ds", startDate);
		query.setParameter("_de", endDate);
		query.setParameter("_did", diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		KeyPerformanceIndicatorType basdai = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.BASDAI, diseaseId);
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
			
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
			BigDecimal value = null;
			if (arrayValues[1] != null) {
				value = new BigDecimal(Number.class.cast(arrayValues[1]).doubleValue());
			}
			
			if (data == null) {
				data = new KeyPerformanceIndicatorStatistics(value, date, basdai);
				marker.put(date, data);
				results.add(data);
			} else if (value != null) {
				BigDecimal newValue = data.getValue().add(value);
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computeBASDAITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#answerExists(java.lang.Long, java.lang.Long, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean answerExists(Long patientId, Long diseaseId, Date logDate) {
		if (log.isDebugEnabled()) {
			log.debug("answerExists(Long, Long, Date) - start"); //$NON-NLS-1$
		}

		List<Object> result = (List)getHibernateTemplate().find(
				"select count(pqa.id) from PatientQuestionAnswer pqa " +
				"join pqa.patient p " +
				"join pqa.question q " +
				"join q.haq h " +
				"where h.disease.id=? and p.id=? and date(pqa.logDate) = date(?) and h.kind=?", 
				new Object[]{ diseaseId, patientId, logDate, Haq.Kind.DEFAULT });
		boolean returnboolean = Number.class.cast(result.get(0)).intValue() > 0;
		if (log.isDebugEnabled()) {
			log.debug("answerExists(Long, Long, Date) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#findOldestPatientAnswerDate(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Date findOldestPatientAnswerDate(Long patientId) {
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientAnswerDate(Long) - start"); //$NON-NLS-1$
		}

		List<Date> queryResult = (List)getHibernateTemplate().find(
				"select min(pa.logDate) from PatientQuestionAnswer pa where pa.patient.id = ?", patientId);
		Date returnDate = queryResult.isEmpty() ? null : queryResult.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientAnswerDate(Long) - returns"); //$NON-NLS-1$
		}
		return returnDate;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#findOldestPatientsAnswerDate()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Date findOldestPatientsAnswerDate() {
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientsAnswerDate() - start"); //$NON-NLS-1$
		}

		List<Date> queryResult = (List)getHibernateTemplate().find(
				"select min(pa.logDate) from PatientQuestionAnswer pa");
		Date returnDate = queryResult.isEmpty() ? null : queryResult.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findOldestPatientsAnswerDate() - returns"); //$NON-NLS-1$
		}
		return returnDate;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#find(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientQuestionAnswer> find(Long haqId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("find(Long, Date, Date) - start"); //$NON-NLS-1$
		}

		final List<PatientQuestionAnswer> queryResult;
		if (haqId == null) {
			queryResult = (List)getHibernateTemplate().find(
					"select rs from PatientQuestionAnswer rs " +
					"where date(rs.logDate) between date(?) and date(?) " +
					"order by rs.logDate ",	new Object[]{ startDate, endDate } );
		} else {
			queryResult = (List)getHibernateTemplate().find(
					"select rs from PatientQuestionAnswer rs " +
					"where rs.question.haq.id=? and date(rs.logDate) between date(?) and date(?) " +
					"order by rs.logDate ",	new Object[]{ haqId, startDate, endDate } );
		}
		

		if (log.isDebugEnabled()) {
			log.debug("find(Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return queryResult;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#removeAllOneTimeQuestionsAnswers(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void removeAllOneTimeQuestionsAnswers(Long patientId, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("removeAllOneTimeQuestionsAnswers(" + patientId + ", " + diseaseId + ") - start"); //$NON-NLS-1$
		}

		getHibernateTemplate().bulkUpdate(
				"delete from PatientQuestionAnswer pqa where pqa.id in (" +
				"	select pqa1.id from PatientQuestionAnswer pqa1 " +
				"	join pqa1.patient p " +
				"	join pqa1.question q " +
				"	join q.haq h " +
				"	join h.disease d " +
				"	where p.id=? " +
				"	and d.id=? " +
				"	and h.kind=? " +
				")", 
				new Object[]{ patientId, diseaseId, Haq.Kind.ONE_TIME });

		if (log.isDebugEnabled()) {
			log.debug("removeAllOneTimeQuestionsAnswers(Long, Long) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#removeOneTimeQuestionAnswers(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void removeOneTimeQuestionAnswers(Long patientId, Long questionId) {
		if (log.isDebugEnabled()) {
			log.debug("removeOneTimeQuestionAnswers(" + patientId + ", " + questionId + ") - start"); //$NON-NLS-1$
		}

		getHibernateTemplate().bulkUpdate(
				"delete from PatientQuestionAnswer pqa where pqa.id in (" +
				"	select pqa1.id " +
				"	from PatientQuestionAnswer pqa1 " +
				"	join pqa1.patient p " +
				"	join pqa1.question q " +
				"	join q.haq h " +
				"	where p.id=? " +
				"	and q.id=? " +
				"	and h.kind=? " +
				")", 
				new Object[]{ patientId, questionId, Haq.Kind.ONE_TIME });

		if (log.isDebugEnabled()) {
			log.debug("removeOneTimeQuestionAnswers(Long, Long) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computePASITimeline(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computePASITimeline(Long patientId, Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computePASITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select cast(pqa.logDate as date) as cnt, " +
			"(" +
			"	0.1*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2102,2103,2104) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2101 and a.active is true)) " +
			"	 is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2102,2103,2104) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2101 and a.active is true)) " +
			"	end) + " +
			"	0.2*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2110,2111,2112) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2109 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2110,2111,2112) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2109 and a.active is true)) " +
			"	end) + " +
			"	0.3*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2106,2107,2108) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2105 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2106,2107,2108) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2105 and a.active is true)) " +
			"	end) + " +
			"	0.4*(" +
			"	case when ((select sum(cast(a.answer as double)) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2114,2115,2116) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2113 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(cast(a.answer as double)) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where p.id=:_pid and pqa1.logDate = pqa.logDate and q1.id in (2114,2115,2116) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.patient.id=:_pid and pqa1.logDate = pqa.logDate and q1.id=2113 and a.active is true)) " +
			"	end) " +
			") as r " +
			"from PatientQuestionAnswer pqa  " +
			"join pqa.patient p " +
			"join pqa.question q " +
			"join q.haq h " +
			"where p.id=:_pid " +
			"and (date(pqa.logDate) between date(:_ds) and date(:_de)) " +
			"and h.disease.id=:_did " +
			"group by pqa.logDate order by 1";
		final Query query = getSession().createQuery(queryString);
		query.setParameter("_pid", patientId);
		query.setParameter("_ds", startDate);
		query.setParameter("_de", endDate);
		query.setParameter("_did", diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		KeyPerformanceIndicatorType pasi = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.PASI, diseaseId);
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
			
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
			BigDecimal value = null;
			if (arrayValues[1] != null) {
				value = new BigDecimal(Number.class.cast(arrayValues[1]).doubleValue());
			}
			
			if (data == null) {
				data = new KeyPerformanceIndicatorStatistics(value, date, pasi);
				marker.put(date, data);
				results.add(data);
			} else if (value != null) {
				BigDecimal newValue = data.getValue().add(value);
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computePASITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computePASIAverageTimeline(Long, Date, Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KeyPerformanceIndicatorStatistics> computePASIAverageTimeline(Long diseaseId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computePASITimeline(Long, Long, Date, Date) - start"); //$NON-NLS-1$
		}
		
		final String queryString = 
			"select pqa.logDate as log_date, " +
			"avg(" +
			"	0.1*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2102,2103,2104) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2101 and a.active is true)) " +
			"	 is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2102,2103,2104) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2101 and a.active is true)) " +
			"	end) + " +
			"	0.2*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2110,2111,2112) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2109 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2110,2111,2112) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2109 and a.active is true)) " +
			"	end) + " +
			"	0.3*(" +
			"	case when ((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2106,2107,2108) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2105 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(case when a.answer is null then 0 else cast(a.answer as double) end) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2106,2107,2108) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2105 and a.active is true)) " +
			"	end) + " +
			"	0.4*(" +
			"	case when ((select sum(cast(a.answer as double)) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2114,2115,2116) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2113 and a.active is true)) " +
			"	is null then 0 else " +
			"	((select sum(cast(a.answer as double)) from PatientQuestionAnswer pqa1 " +
			"				join pqa1.question q1 " +
			"				join pqa1.answer a " +
			"				join pqa1.patient p " +
			"				where pqa1.logDate = pqa.logDate and q1.id in (2114,2115,2116) and a.active is true) * " +
			"		(select case when (pqa1.customAnswer is null or cast(pqa1.customAnswer as double) = 0.0) then 0 when (cast(pqa1.customAnswer as double) > 0.0 and cast(pqa1.customAnswer as double) < 10.0) then 1 when (cast(pqa1.customAnswer as double) between 10.0 and 29.0) then 2 when (cast(pqa1.customAnswer as double) between 30.0 and 49.0) then 3 when (cast(pqa1.customAnswer as double) between 50.0 and 69.0) then 4 when (cast(pqa1.customAnswer as double) between 70.0 and 89.0) then 5 else 6 end from PatientQuestionAnswer pqa1 " +
			"			join pqa1.question q1 " +
			"			join pqa1.answer a " +
			"			where pqa1.logDate = pqa.logDate and q1.id=2113 and a.active is true)) " +
			"	end) " +
			") as r " +
			"from PatientQuestionAnswer pqa  " +
			"join pqa.question q " +
			"join q.haq h " +
			"where (date(pqa.logDate) between date(:_ds) and date(:_de)) " +
			"and h.disease.id=:_did " +
			"group by pqa.logDate order by 1";
		
		final Query query = getSession().createQuery(queryString);
		query.setParameter("_ds", startDate);
		query.setParameter("_de", endDate);
		query.setParameter("_did", diseaseId);
				
		List<Object[]> result = query.list();
		List<KeyPerformanceIndicatorStatistics> results = new ArrayList<KeyPerformanceIndicatorStatistics>(result.size());
		Map<Date, KeyPerformanceIndicatorStatistics> marker = new HashMap<Date, KeyPerformanceIndicatorStatistics>();
		KeyPerformanceIndicatorType pasi = keyPerformanceIndicatorTypeDao.find(KeyPerformanceIndicatorType.Type.PASI, diseaseId);
		
		for (Object[] arrayValues : result) {
			Date date = (Date) arrayValues[0];
			
			KeyPerformanceIndicatorStatistics data = marker.get(date);
			
			BigDecimal value = null;
			if (arrayValues[1] != null) {
				value = new BigDecimal(Number.class.cast(arrayValues[1]).doubleValue());
			}
			
			if (data == null) {
				data = new KeyPerformanceIndicatorStatistics(value, date, pasi);
				marker.put(date, data);
				results.add(data);
			} else if (value != null) {
				BigDecimal newValue = data.getValue().add(value);
				data.setValue(newValue);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("computePASITimeline(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return results;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#findAnswersForSingleAnswerEntryQuestions(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PatientQuestionAnswer> findAnswersForSingleAnswerEntryQuestions(Long patientId, Long diseaseId,
			Date startDate, Date endDate) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findAnswersForSingleAnswerEntryQuestions(" + patientId + ", " + diseaseId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		startDate = DateUtils.round(startDate, Calendar.DAY_OF_MONTH);
		endDate = DateUtils.round(endDate, Calendar.DAY_OF_MONTH);
		
		final List<PatientQuestionAnswer> queryResult = (List)getHibernateTemplate().find(
				"select rs from PatientQuestionAnswer rs " +
				"join rs.patient p " +
				"join rs.question q " +
				"join q.questionType qt " +
				"join q.haq h " +
				"join h.disease hd " +
				"left join q.disease rsd " +
				"where p.id=? " +
				"and (hd.id=? or rsd.id=?) " +
				"and qt.type=? " +
				"and date(rs.logDate) between date(?) and date(?) " +
				"order by rs.logDate ",	
				new Object[]{ patientId, diseaseId, diseaseId, QuestionType.Type.SINGLE_ANSWER_ENTER, startDate, endDate } );

		if (log.isDebugEnabled()) {
			log.debug("findAnswersForSingleAnswerEntryQuestions(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return queryResult;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#findFirstSymptomeAndDiagnosisDate(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Pair<Date, Date> findFirstSymptomeAndDiagnosisDate(Long patientId, Long diseaseId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findFirstSymptomeAndDiagnosisDate(" + patientId + ", " + diseaseId + ") - start"); //$NON-NLS-1$
		}
		
		final List<PatientQuestionAnswer> queryResult = (List)getHibernateTemplate().find(
				"select rs from PatientQuestionAnswer rs " +
				"join rs.patient p " +
				"join rs.question q " +
				"join q.questionType qt " +
				"join q.haq h " +
				"join h.disease hd " +
				"left join q.disease rsd " +
				"where p.id=? " +
				"and hd.id=? " +
				"and h.kind=? " +
				"and qt.answerDataType=? " +
				"and rs.answer.active is true " +
				"and (q.extendedType=? or q.extendedType=?) " +
				"order by q.extendedType",	
				new Object[]{ patientId, diseaseId, Haq.Kind.ONE_TIME, 
						QuestionType.AnswerDataType.DATE, 
						Question.ExtendedType.DIAGNOSIS_DATE,
						Question.ExtendedType.INITIAL_SYMPTOM_DATE} );
		
		Pair<Date, Date> result = null;
		Date firstSymptomDate = null;
		Date firstDiagnosisDate = null;
		if (!queryResult.isEmpty()) {
			if (queryResult.size() == 1) {
				if (queryResult.get(0).getQuestion().getExtendedType().equals(Question.ExtendedType.INITIAL_SYMPTOM_DATE)) {
					firstSymptomDate = StringUtils.isBlank(queryResult.get(0).getCustomAnswer()) ? null : new Date(Long.valueOf(queryResult.get(0).getCustomAnswer()));
				} else {
					firstDiagnosisDate = StringUtils.isBlank(queryResult.get(0).getCustomAnswer()) ? null : new Date(Long.valueOf(queryResult.get(0).getCustomAnswer()));
				}
				
			} else {				
				firstSymptomDate = StringUtils.isBlank(queryResult.get(0).getCustomAnswer()) ? null : new Date(Long.valueOf(queryResult.get(0).getCustomAnswer()));
				firstDiagnosisDate = StringUtils.isBlank(queryResult.get(1).getCustomAnswer()) ? null : new Date(Long.valueOf(queryResult.get(1).getCustomAnswer()));
			}
		}
		
		result = Pair.create(firstSymptomDate, firstDiagnosisDate);

		if (log.isDebugEnabled()) {
			log.debug("findFirstSymptomeAndDiagnosisDate(Long, Long) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#deleteAnswers(java.lang.Long)
	 */
	@Override
	public void deleteAnswers(Long questionId) {
		if (questionId == null) {
			throw new IllegalArgumentException("questionId must not be null");
		}
		
		final String query = "delete from PatientQuestionAnswer pqa where pqa.question.id = ?";
		getHibernateTemplate().bulkUpdate(query, questionId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#findAll(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientQuestionAnswer> findAll(Long userId, Long questionId, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("findAll(" + userId + ", " + questionId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}

		final List<PatientQuestionAnswer> queryResult = (List)getHibernateTemplate().find(
				"select rs from PatientQuestionAnswer rs " +
				"where rs.patient.id=? and rs.question.id=? and date(rs.logDate) between date(?) and date(?) " +
				"order by rs.logDate ",	new Object[]{userId,  questionId, startDate, endDate } );

		if (log.isDebugEnabled()) {
			log.debug("findAll(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return queryResult;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#existsAnswerToOneTimeQuesion(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean existsAnswerToOneTimeQuesion(Long patientId, Long diseaseId) {
		if (log.isDebugEnabled()) {
			log.debug("answerExists(Long, Long, Date) - start"); //$NON-NLS-1$
		}

		List<Object> result = (List)getHibernateTemplate().find(
				"select count(pqa.id) from PatientQuestionAnswer pqa " +
				"join pqa.patient p " +
				"join pqa.question q " +
				"join q.haq h " +
				"where h.disease.id=? and p.id=? and h.kind=?", 
				new Object[]{ diseaseId, patientId, Haq.Kind.ONE_TIME });
		boolean returnboolean = Number.class.cast(result.get(0)).intValue() > 0;
		if (log.isDebugEnabled()) {
			log.debug("answerExists(Long, Long, Date) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeQuestionAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> computeQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate,
			Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersFrequencyReport(" + patientId + ", " + questionId + ", " + startDate  + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = (List)getHibernateTemplate().find(
				"select " +
				"qa.logDate, q.id, q.text, a.id, (case when qa.customAnswer is not null then qa.customAnswer else a.answer end), a.kind " +
				"from PatientQuestionAnswer qa " +
				"join qa.patient p " +
				"join qa.question q " +
				"join qa.answer a " +
				"where p.id=? and q.id=? and date(qa.logDate) between date(?) and date(?) " +
				"group by qa.logDate, q.id, q.text, a.id, a.answer, a.kind, qa.customAnswer " +
				"order by 1, 3, 5 ", 
				new Object[]{ patientId, questionId, startDate, endDate });

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCustomQuestionAnswersReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientQuestionaryAnswerStatistic> computeCustomQuestionAnswersReport(Long patientId, Long questionId,
			Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeQuestionAnswersReport(" + patientId + ", " + questionId + ", " + startDate + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<PatientQuestionaryAnswerStatistic> result = (List)getHibernateTemplate().find(
				"select new com.mobileman.projecth.domain.dto.patient.PatientQuestionaryAnswerStatistic(" +
				"	q.text, q.id, q.text, q.id, qta.answer, qta.id, " +
				"	(" +
				"	select count(pqa.id) from PatientQuestionAnswer pqa " +
				"	where pqa.patient.id=? and pqa.question.id=q.id and pqa.answer.id = qta.id " +
				"	and date(pqa.logDate) between date(?) and date(?)" +
				"	)" +
				") " +
				"from CustomQuestion q " +
				"join q.questionType qt " +
				"join qt.answers qta " +
				"where q.id=? " +
				"group by q.text, q.id, qta.answer, qta.id " +
				"order by 2, 3, 6 ", 
				new Object[]{ patientId, startDate, endDate, questionId } );
		

		if (log.isDebugEnabled()) {
			log.debug("computeQuestionaryAnswersReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.patient.PatientQuestionAnswerDao#computeCustomQuestionAnswersFrequencyReport(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> computeCustomQuestionAnswersFrequencyReport(Long patientId, Long questionId, Date startDate,
			Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersFrequencyReport(" + patientId + ", " + questionId + ", " + startDate  + ", " + endDate + ") - start"); //$NON-NLS-1$
		}
		
		List<Object[]> result = (List)getHibernateTemplate().find(
				"select " +
				"qa.logDate, q.id, q.text, a.id, (case when qa.customAnswer is not null then qa.customAnswer else a.answer end), a.kind " +
				"from PatientQuestionAnswer qa " +
				"join qa.patient p " +
				"join qa.question q " +
				"join qa.answer a " +
				"where p.id=? and q.id=? and date(qa.logDate) between date(?) and date(?) " +
				"group by qa.logDate, q.id, q.text, a.id, a.answer, a.kind, qa.customAnswer " +
				"order by 1, 3, 5 ", 
				new Object[]{ patientId, questionId, startDate, endDate });

		if (log.isDebugEnabled()) {
			log.debug("computeCustomQuestionAnswersFrequencyReport(Long, Long, Date, Date) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	
}
