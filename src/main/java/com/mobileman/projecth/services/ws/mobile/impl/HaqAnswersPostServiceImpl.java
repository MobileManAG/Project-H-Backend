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
 * HaqAnswersPostServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 15.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.services.ws.mobile.impl;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.exception.LoginException;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientDailyPost;
import com.mobileman.projecth.services.ws.mobile.ProjectHPatientInitialPost;
import com.mobileman.projecth.services.ws.mobile.HaqAnswersPostService;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialResponse;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostResponse;
import com.mobileman.projecth.services.ws.mobile.model.ObjectFactory;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
@Service("haqAnswersPostService")
@Transactional
public class HaqAnswersPostServiceImpl implements HaqAnswersPostService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HaqAnswersPostServiceImpl.class);
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	private static final Map<AnswerKey, Long> newAnswersMap;
	
	static {
		newAnswersMap = new HashMap<AnswerKey, Long>();
		newAnswersMap.put(new AnswerKey(1001L, 1), 1L);
		String[] haqIds = "1001;1002;1003;1004;1005;1006;1007;1008;1009;1010;1011;1012;1013;1014;1021;1022;1023;1024;1025;1026;1027;1028;1029;1030;1031;1032;1033;1034".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 1L);
			newAnswersMap.put(new AnswerKey(id, 1), 2L);
			newAnswersMap.put(new AnswerKey(id, 0), 3L);
		}
		
		haqIds = "1051;1052;1053;1054;1055;1056;1057;1058;1059;1060;1061;1062;1063;1064;1071;1072;1073;1074;1075;1076;1077;1078;1079;1080;1081;1082;1083;1084".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 1L);
			newAnswersMap.put(new AnswerKey(id, 1), 2L);
			newAnswersMap.put(new AnswerKey(id, 0), 3L);
		}
		
		haqIds = "1100".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 1L);
			newAnswersMap.put(new AnswerKey(id, 1), 2L);
			newAnswersMap.put(new AnswerKey(id, 0), 3L);
		}
		
		newAnswersMap.put(new AnswerKey(1102L, -1), 16L);
		newAnswersMap.put(new AnswerKey(1102L, 0), 17L);
		newAnswersMap.put(new AnswerKey(1102L, 1), 18L);
		newAnswersMap.put(new AnswerKey(1102L, 2), 19L);
		newAnswersMap.put(new AnswerKey(1102L, 3), 20L);
		newAnswersMap.put(new AnswerKey(1102L, 4), 21L);
		newAnswersMap.put(new AnswerKey(1102L, 5), 22L);
		
		newAnswersMap.put(new AnswerKey(1115L, -1), 4L);
		newAnswersMap.put(new AnswerKey(1115L, 0), 5L);
		newAnswersMap.put(new AnswerKey(1115L, 1), 6L);
		newAnswersMap.put(new AnswerKey(1115L, 2), 7L);
		newAnswersMap.put(new AnswerKey(1115L, 3), 8L);
		newAnswersMap.put(new AnswerKey(1115L, 4), 9L);
		newAnswersMap.put(new AnswerKey(1115L, 5), 10L);
		newAnswersMap.put(new AnswerKey(1115L, 6), 11L);
		newAnswersMap.put(new AnswerKey(1115L, 7), 12L);
		newAnswersMap.put(new AnswerKey(1115L, 8), 13L);
		newAnswersMap.put(new AnswerKey(1115L, 9), 14L);
		newAnswersMap.put(new AnswerKey(1115L, 10), 15L);
		
		haqIds = "1127;1128;1129;1130;1131;1132;1133;1134;1135".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 1L);
			newAnswersMap.put(new AnswerKey(id, 1), 2L);
			newAnswersMap.put(new AnswerKey(id, 0), 3L);
		}
		
		haqIds = "1136;1137;1138;1139".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 23L);
			newAnswersMap.put(new AnswerKey(id, 0), 24L);
			newAnswersMap.put(new AnswerKey(id, 1), 25L);
			newAnswersMap.put(new AnswerKey(id, 2), 26L);
			newAnswersMap.put(new AnswerKey(id, 3), 27L);
		}
		
		haqIds = "1140".split("[;]");
		for (int i = 0; i < haqIds.length; i++) {
			Long id = Long.parseLong(haqIds[i]);
			newAnswersMap.put(new AnswerKey(id, -1), 1L);
			newAnswersMap.put(new AnswerKey(id, 1), 2L);
			newAnswersMap.put(new AnswerKey(id, 0), 3L);
		}
	}
	
	/**
	 * Answer key for old model
	 * @author mobileman
	 *
	 */
	public static class AnswerKey {
		/** 
		 * {@inheritDoc}
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.index == null) ? 0 : this.index.hashCode());
			result = prime * result + ((this.questionId == null) ? 0 : this.questionId.hashCode());
			return result;
		}
		/** 
		 * {@inheritDoc}
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AnswerKey other = (AnswerKey) obj;
			if (this.index == null) {
				if (other.index != null)
					return false;
			} else if (!this.index.equals(other.index))
				return false;
			if (this.questionId == null) {
				if (other.questionId != null)
					return false;
			} else if (!this.questionId.equals(other.questionId))
				return false;
			return true;
		}
		private final Long questionId;
		private final Integer index;
				
		/**
		 * @param questionId
		 * @param index
		 */
		public AnswerKey(Long questionId, Integer index) {
			super();
			this.questionId = questionId;
			this.index = index;
		}
		/**
		 * @return questionId
		 */
		public Long getQuestionId() {
			return this.questionId;
		}
		/**
		 * @return index
		 */
		public Integer getIndex() {
			return this.index;
		}
		
	}

	/**
	 * @param ids
	 * @return ids of long
	 */
	protected Long[] convertToLong(String[] ids) {
		if (log.isDebugEnabled()) {
			log.debug("convertToLong(String[]) - start"); //$NON-NLS-1$
		}

		Long[] result = new Long[ids.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Long.valueOf(ids[i]);
		}
		

		if (log.isDebugEnabled()) {
			log.debug("convertToLong(String[]) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * @param epochTimeInSecond
	 * @return Date from time epoch in second
	 */
	public static Date getDateFromTimeEpochInSecond(BigDecimal epochTimeInSecond) {
		if (log.isDebugEnabled()) {
			log.debug("getDateFromTimeEpochInSecond(BigDecimal) - start"); //$NON-NLS-1$
		}

		Date date = epochTimeInSecond != null ? new Date(epochTimeInSecond.longValue() * 1000) : null;

		if (log.isDebugEnabled()) {
			log.debug("getDateFromTimeEpochInSecond(BigDecimal) - returns"); //$NON-NLS-1$
		}
		return date;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.services.ws.mobile.HaqAnswersPostService#processUserPost(com.mobileman.projecth.services.ws.mobile.model.projecthPostRequest)
	 */
	@Override
	public ProjectHPostResponse processUserPost(ProjectHPostRequest data) {
		if (log.isDebugEnabled()) {
			log.debug("processUserPost(ProjectHPostRequest) - start"); //$NON-NLS-1$
		}

		ProjectHPostResponse result = new ObjectFactory().createProjectHPostResponse();
		final Long patientId;
		if (data.getUserName() != null 
				&& data.getUserName().trim().length() != 0 
				&& !data.getUserName().equals("-1")) {
			Patient user;
			try {
				user = (Patient)userService.findUserByLogin(data.getUserName());
				patientId = user.getId();
			} catch (LoginException e) {
				log.error("processUserPost(projecthPostRequest)", e); //$NON-NLS-1$

				result.setMessage("Unknown user name");
				result.setResult(false);

				if (log.isDebugEnabled()) {
					log.debug("processUserPost(projecthPostRequest) - returns"); //$NON-NLS-1$
				}
				return result;
			}
			
		} else {
			result.setMessage("Unknown user name");
			result.setResult(false);

			if (log.isDebugEnabled()) {
				log.debug("processUserPost(projecthPostRequest) - returns"); //$NON-NLS-1$
			}
			return result;
		}
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		final Date logDate = getDateFromTimeEpochInSecond(data.getLogDate());
		
		if (patientService.patientAnswerExists(patientId, disease.getId(), logDate)) {
			result.setResult(false);

			if (log.isDebugEnabled()) {
				log.debug("processUserPost(projecthPostRequest) - returns"); //$NON-NLS-1$
			}
			return result;
		}
		
		final String[][] response = {
				{ data.getHAQ1(), data.getHAQ1Value() },
				{ data.getHAQ2(), data.getHAQ2Value() },
				{ data.getHAQ3(), data.getHAQ3Value() },
				{ data.getHAQ3SUB(), data.getHAQ3SUBValue() },
				{ data.getHAQ4(), data.getHAQ4Value() },
				{ data.getHAQ5(), data.getHAQ5Value() },
				{ data.getHAQ6(), data.getHAQ6Value() },
				{ data.getHAQ7(), data.getHAQ7Value() },
		};
		
		final Long[] haqsId = {
				1L,
				2L,
				3L,
				3L,
				4L,
				5L,
				6L,
				7L,
		};
		
		List<ProjectHPatientDailyPost> posts = new ArrayList<ProjectHPatientDailyPost>();
		
		for (int i = 0; i < response.length; i++) {
			
			Long[] questionariesId = convertToLong(response[i][0].split("[;]"));
			String[] values = response[i][1].split("[;]");
			Long[] answersId = new Long[values.length];
			for (int j = 0; j < values.length; j++) {
				answersId[j] = translateToAnswerId(questionariesId[j], Integer.valueOf(values[j]));
			}
			
			ProjectHPatientDailyPost post = new ProjectHPatientDailyPost();
			post.setDiseaseId(disease.getId());
			post.setLogDate(logDate);
			post.setPatientId(patientId);
			post.setQuestionsId(questionariesId);
			post.setAnswersId(answersId);
			post.setHaqId(haqsId[i]);			
			posts.add(post);
		}
		
		patientService.processPatientDailyPost(posts);
		
		result.setResult(true);

		if (log.isDebugEnabled()) {
			log.debug("processUserPost(projecthPostRequest) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.services.ws.mobile.HaqAnswersPostService#processUserPost(com.mobileman.projecth.services.ws.mobile.model.projecthPostInitialRequest)
	 */
	@Override
	public ProjectHPostInitialResponse processUserPost(ProjectHPostInitialRequest data) {
		if (log.isDebugEnabled()) {
			log.debug("processUserPost(projecthPostInitialRequest) - start"); //$NON-NLS-1$
		}

		final ProjectHPostInitialResponse result = new ObjectFactory().createProjectHPostInitialResponse();
		
		final Long patientId;
		if (data.getUserName() != null 
				&& data.getUserName().trim().length() != 0 
				&& !data.getUserName().equals("-1")) {
			Patient user;
			try {
				user = (Patient)userService.findUserByLogin(data.getUserName());
				patientId = user.getId();
			} catch (LoginException e) {
				log.error("processUserPost(projecthPostInitialRequest)", e); //$NON-NLS-1$

				result.setMessage("Unknown user name");
				result.setResult(false);

				if (log.isDebugEnabled()) {
					log.debug("processUserPost(projecthPostInitialRequest) - returns"); //$NON-NLS-1$
				}
				return result;
			}
			
		} else {
			result.setMessage("Unknown user name");
			result.setResult(false);

			if (log.isDebugEnabled()) {
				log.debug("processUserPost(projecthPostInitialRequest) - returns"); //$NON-NLS-1$
			}
			return result;
		}
		
		Date initialDiagnosisDate = getDateFromTimeEpochInSecond(data.getInitialDiagnosisDate());
		Date initialSymptomDate = getDateFromTimeEpochInSecond(data.getInitialSymptomDate());
		
		Integer sex = data.getGender() != null ? data.getGender().intValue() : null;
		Integer birthday = data.getBirthday() != null ? data.getBirthday().intValue() : null;
		
		ProjectHPatientInitialPost post = new ProjectHPatientInitialPost();
		post.setDiseaseCode(DiseaseCodes.RHEUMA_CODE);
		post.setPatientId(patientId);
		post.setActivationCode(data.getActivationCode());
		post.setBirthday(birthday);
		post.setSex(sex);
		post.setDiseaseCode(DiseaseCodes.RHEUMA_CODE);
		
		String[] medecines = data.getMedicineClass() == null ? new String[]{ "-1", "-1", "-1", "-1" } : data.getMedicineClass().split("[;]");
		
		Long[] questionsId = new Long[]{
				2070L,
				2071L,
				2072L,
				2073L,
				2074L,
				2075L,
				2076L
		};
		
		Long[] answersId = new Long[]{
				getMedecineAnswerid(medecines[0]),
				getMedecineAnswerid(medecines[1]),
				getMedecineAnswerid(medecines[2]),
				getMedecineAnswerid(medecines[3]),
				data.getMedicine() == null ? 32L : 33L,
				initialSymptomDate == null ? 66L : 67L,
				initialDiagnosisDate == null ? 66L : 67L,
		};
		
		String[] customAnswers = new String[]{
				null,
				null,
				null,
				null,
				data.getMedicine(),
				initialSymptomDate == null ? null : String.valueOf(initialSymptomDate.getTime()),
				initialDiagnosisDate == null ? null : String.valueOf(initialDiagnosisDate.getTime()),
		};
		
		post.setAnswersId(answersId);
		post.setQuestionsId(questionsId);
		post.setCustomAnswers(customAnswers);
		
		try {
			patientService.processPatientInitialPost(post);
			result.setResult(true);
		} catch (IllegalArgumentException e) {
			log.error("processUserPost(projecthPostInitialRequest)", e); //$NON-NLS-1$

			result.setMessage(e.getMessage());
			result.setResult(false);
		}
		

		if (log.isDebugEnabled()) {
			log.debug("processUserPost(projecthPostInitialRequest) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * @param value
	 */
	private Long getMedecineAnswerid(String value) {
		if (log.isDebugEnabled()) {
			log.debug("getMedecineAnswerid(String) - start"); //$NON-NLS-1$
		}

		if (value == null || value.equals("-1")) {
			if (log.isDebugEnabled()) {
				log.debug("getMedecineAnswerid(String) - returns"); //$NON-NLS-1$
			}
			return 1L;
		} else if (value.equals("0")) {
			if (log.isDebugEnabled()) {
				log.debug("getMedecineAnswerid(String) - returns"); //$NON-NLS-1$
			}
			return 3L;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("getMedecineAnswerid(String) - returns"); //$NON-NLS-1$
			}
			return 2L;
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.services.ws.mobile.HaqAnswersPostService#translateToAnswerId(java.lang.Long, Integer)
	 */
	@Override
	public Long translateToAnswerId(Long questionId, Integer oldAnswerId) {
		if (log.isDebugEnabled()) {
			log.debug("translateToAnswerId(Long, Integer) - start"); //$NON-NLS-1$
		}

		final AnswerKey key = new AnswerKey(questionId, oldAnswerId);
		Long id = newAnswersMap.get(key);

		if (log.isDebugEnabled()) {
			log.debug("translateToAnswerId(Long, Integer) - returns"); //$NON-NLS-1$
		}
		return id;
	}
}

