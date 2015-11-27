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
 * HaqAnswersPostServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 15.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.services.ws.mobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.patient.PatientQuestionAnswerService;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.PatientQuestionAnswer;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.persistence.patient.PatientKPIDao;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialResponse;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostResponse;

/**
 * @author mobileman
 *
 */
public class HaqAnswersPostServiceTest extends TestCaseBase {

	@Autowired
	private HaqAnswersPostService haqAnswersPostService;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	@Autowired
	private PatientKPIDao patientKPIDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private PatientQuestionAnswerService patientQuestionAnswerService;
	
	/**
	 * @throws Exception
	 */
	@Test
	public void processUserInitialPost() throws Exception {
		
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		User doctor = userService.findUserByLogin("sysuser3");		
		assertNotNull(patient);
		
		ProjectHPostInitialRequest data = new ProjectHPostInitialRequest();
		data.setUserName("sysuser1");
				
		ProjectHPostInitialResponse result = haqAnswersPostService.processUserPost(data);
		assertNotNull(result);
		assertTrue(result.isResult());
		
		data.setInitialDiagnosisDate(new BigDecimal(dateFormat.parse("4.11.2010").getTime()/1000));
		data.setInitialSymptomDate(new BigDecimal(dateFormat.parse("4.11.2009").getTime()/1000));
		data.setMedicine("processUserInitialPost");
		data.setMedicineClass("0;1;-1;0");
		data.setBirthday(new BigInteger("1988"));
		data.setGender(new BigInteger("0"));
		
		List<PatientQuestionAnswer> oneTimeAnswers = patientQuestionAnswerService.findAll();
		assertTrue(patientService.existsAnswerToOneTimeQuesion(patient.getId(), oneTimeAnswers.get(0).getQuestion().getHaq().getDisease().getId()));
		assertFalse(patientService.existsAnswerToOneTimeQuesion(doctor.getId(), oneTimeAnswers.get(0).getQuestion().getHaq().getDisease().getId()));
		
		int oldcnt = oneTimeAnswers.size();
		result = haqAnswersPostService.processUserPost(data);
		assertNotNull(result);
		assertTrue(result.isResult());
		
		oneTimeAnswers = patientQuestionAnswerService.findAll();
		assertEquals(oldcnt, patientQuestionAnswerService.findAll().size());
		
		
		
		assertEquals("No", oneTimeAnswers.get(oneTimeAnswers.size() - 7).getAnswer().getAnswer());
		assertEquals("Yes", oneTimeAnswers.get(oneTimeAnswers.size() - 6).getAnswer().getAnswer());
		assertEquals("Kaine Angabe", oneTimeAnswers.get(oneTimeAnswers.size() - 5).getAnswer().getAnswer());
		assertEquals("No", oneTimeAnswers.get(oneTimeAnswers.size() - 4).getAnswer().getAnswer());
		assertEquals("processUserInitialPost", oneTimeAnswers.get(oneTimeAnswers.size() - 3).getCustomAnswer());
		assertEquals(dateFormat.parse("4.11.2009"), new Date(Long.valueOf(oneTimeAnswers.get(oneTimeAnswers.size() - 2).getCustomAnswer())));
		assertEquals(dateFormat.parse("4.11.2010"), new Date(Long.valueOf(oneTimeAnswers.get(oneTimeAnswers.size() - 1).getCustomAnswer())));
	}
	
	
	/**
	 * @throws Exception
	 */
	@Test
	public void processUserDailyPost() throws Exception {
		ProjectHPostRequest data = new ProjectHPostRequest();
		data.setUserName("sysuser1");
		data.setLogDate(new BigDecimal(dateFormat.parse("1.11.2010").getTime()/1000));
		
		ProjectHPostResponse result = haqAnswersPostService.processUserPost(data);
		assertNotNull(result);
		assertFalse(result.isResult());
		
		data.setLogDate(new BigDecimal(dateFormat.parse("4.11.2010").getTime()/1000));
		data.setHAQ1("1001;1002;1003;1004;1005;1006;1007;1008;1009;1010;1011;1012;1013;1014;1021;1022;1023;1024;1025;1026;1027;1028;1029;1030;1031;1032;1033;1034");
		data.setHAQ1Value("0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0");
		
		data.setHAQ2("1051;1052;1053;1054;1055;1056;1057;1058;1059;1060;1061;1062;1063;1064;1071;1072;1073;1074;1075;1076;1077;1078;1079;1080;1081;1082;1083;1084");
		data.setHAQ2Value("0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0");
		
		data.setHAQ3("1100");
		data.setHAQ3Value("-1");
		
		data.setHAQ3SUB("1102");
		data.setHAQ3SUBValue("0");
		
		data.setHAQ4("1115");
		data.setHAQ4Value("1");
		
		data.setHAQ5("1127;1128;1129;1130;1131;1132;1133;1134;1135");
		data.setHAQ5Value("-1;-1;-1;-1;-1;-1;-1;-1;-1");
		
		data.setHAQ6("1136;1137");
		data.setHAQ6Value("-1;-1");
		
		data.setHAQ7("1138;1139;1140");
		data.setHAQ7Value("-1;0;1");
		
		int count = patientKPIDao.findAll().size();
		result = haqAnswersPostService.processUserPost(data);
		assertNotNull(result);
		assertTrue(result.isResult());
		
		assertEquals(count + 1, patientKPIDao.findAll().size());
	}
}
