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
 * AllTests.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.mobileman.projecth.business.DiseaseTest;
import com.mobileman.projecth.business.DoctorServiceTest;
import com.mobileman.projecth.business.HaqTest;
import com.mobileman.projecth.business.MailManagerTest;
import com.mobileman.projecth.business.MedicationServiceTest;
import com.mobileman.projecth.business.SystemServiceTest;
import com.mobileman.projecth.business.UserConnectionTest;
import com.mobileman.projecth.business.UserServiceTest;
import com.mobileman.projecth.business.chart.ChartTypeServiceTest;
import com.mobileman.projecth.business.chart.HaqChartServiceTest;
import com.mobileman.projecth.business.index.CountryServiceTest;
import com.mobileman.projecth.business.index.IndexServiceTest;
import com.mobileman.projecth.business.messages.MessageServiceTest;
import com.mobileman.projecth.business.patient.PatientKeyPerformanceIndicatorServiceTest;
import com.mobileman.projecth.business.patient.PatientMedicationServiceTest;
import com.mobileman.projecth.business.patient.PatientQuestionAnswerServiceTest;
import com.mobileman.projecth.business.patient.PatientServiceTest;
import com.mobileman.projecth.business.questionary.QuestionServiceTest;
import com.mobileman.projecth.business.questionary.QuestionTypeServiceTest;
import com.mobileman.projecth.domain.data.id_types.PznNumberTest;
import com.mobileman.projecth.persistence.UserDaoTest;
import com.mobileman.projecth.services.ws.mobile.HaqAnswersPostServiceTest;

/**
 * Test suite.
 * 
 * @author MobileMan GmbH
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({
	InitDbTest.class,
	CountryServiceTest.class,
	IndexServiceTest.class,
	UserDaoTest.class,
	MailManagerTest.class,
	PznNumberTest.class,
	ChartTypeServiceTest.class,
	DiseaseTest.class,
	HaqTest.class,
	QuestionTypeServiceTest.class,
	QuestionServiceTest.class,	
	HaqChartServiceTest.class,
	UserServiceTest.class,
	MessageServiceTest.class,
	MedicationServiceTest.class,
	PatientServiceTest.class,
	DoctorServiceTest.class,
	PatientMedicationServiceTest.class,
	PatientQuestionAnswerServiceTest.class,
	PatientKeyPerformanceIndicatorServiceTest.class,
	UserConnectionTest.class,
	SystemServiceTest.class,
	HaqAnswersPostServiceTest.class
	})
@ContextConfiguration(locations = {"/spring/config/BeanLocations.xml"})
@TransactionConfiguration(defaultRollback=true)
public class AllTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// InitSchema.createSchema();
		junit.textui.TestRunner.run(new JUnit4TestAdapter(AllTests.class));
	}
}
