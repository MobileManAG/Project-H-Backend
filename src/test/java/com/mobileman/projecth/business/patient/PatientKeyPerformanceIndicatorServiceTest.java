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
 * PatientKeyPerformanceIndicatorServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.HaqService;
import com.mobileman.projecth.business.MedicationService;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.chart.HaqChartService;
import com.mobileman.projecth.business.index.kpi.KeyPerformanceIndicatorTypeService;
import com.mobileman.projecth.business.questionary.QuestionService;
import com.mobileman.projecth.business.questionary.QuestionTypeService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.dto.patient.kpi.KeyPerformanceIndicatorStatistics;
import com.mobileman.projecth.domain.index.kpi.KeyPerformanceIndicatorType;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.persistence.patient.PatientMedicationDao;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
public class PatientKeyPerformanceIndicatorServiceTest extends TestCaseBase {

	@Autowired
	private PatientKPIService patientKPIService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MedicationService medicationService;
	
	@Autowired
	private PatientMedicationDao patientMedicationDao;
	
	@Autowired
	private KeyPerformanceIndicatorTypeService keyPerformanceIndicatorTypeService;
	
	@Autowired
	private QuestionTypeService questionTypeService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private HaqService haqService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	@Autowired
	private HaqChartService haqChartService;
	
	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKPI_CDAI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease rheuma = diseaseService.findByCode("M79.0");
		assertNotNull(rheuma);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.CDAI, rheuma.getId());
				
		BigDecimal result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(10.33d, result.doubleValue(), 0.01);
		
		result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("3.11.2010"), dateFormat.parse("3.11.2010"));
		assertEquals(13.00d, result.doubleValue(), 0.01);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computePatientKpiTimeline_CDAI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease rheuma = diseaseService.findByCode("M79.0");
		assertNotNull(rheuma);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.CDAI, rheuma.getId());
				
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPITimeline(
				patient.getId(), kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(3.00d, result.get(0).getValue().doubleValue(), 0.01);
		assertEquals(2.00d, result.get(1).getValue().doubleValue(), 0.01);
		assertEquals(3.00d, result.get(2).getValue().doubleValue(), 0.01);
		
		result = patientKPIService.computeKPITimeline(
				patient.getId(), kpi.getId(), dateFormat.parse("3.11.2010"), dateFormat.parse("3.11.2010"));
		assertEquals(3.00d, result.get(0).getValue().doubleValue(), 0.01);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKpiTimeline_CDAI() throws Exception {
				
		Disease rheuma = diseaseService.findByCode("M79.0");
		assertNotNull(rheuma);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.CDAI, rheuma.getId());
				
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPIAverageTimeline(
				kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertEquals(3.00d, result.get(0).getValue().doubleValue(), 0.01);
		assertEquals(2.00d, result.get(1).getValue().doubleValue(), 0.01);
		assertEquals(3.00d, result.get(2).getValue().doubleValue(), 0.01);
		
		result = patientKPIService.computeKPIAverageTimeline(
				kpi.getId(), dateFormat.parse("3.11.2010"), dateFormat.parse("3.11.2010"));
		assertEquals(3.00d, result.get(0).getValue().doubleValue(), 0.01);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computePatientKpiTimeline_PASI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease psoriasis = diseaseService.findByCode(DiseaseCodes.PSORIASIS_CODE);
		assertNotNull(psoriasis);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.PASI, psoriasis.getId());
				
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPITimeline(
				patient.getId(), kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2011"));
		assertEquals(0, result.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKpiTimeline_PASI() throws Exception {
				
		Disease psoriasis = diseaseService.findByCode(DiseaseCodes.PSORIASIS_CODE);
		assertNotNull(psoriasis);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.PASI, psoriasis.getId());
				
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPIAverageTimeline(
				kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2011"));
		assertEquals(0, result.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computePatientKpiTimeline_BASDAI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease morbusBechterew = diseaseService.findByCode("M45");
		assertNotNull(morbusBechterew);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.BASDAI, morbusBechterew.getId());
		
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPITimeline(
				patient.getId(), kpi.getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(2, result.size());
		assertEquals(6.1d, result.get(0).getValue().doubleValue(), 0.001);
		assertEquals(5.5d, result.get(1).getValue().doubleValue(), 0.001);
		
		result = patientKPIService.computeKPITimeline(
				patient.getId(), kpi.getId(), dateFormat.parse("2.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(5.5d, result.get(0).getValue().doubleValue(), 0.001);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKpiTimeline_BASDAI() throws Exception {
				
		Disease morbusBechterew = diseaseService.findByCode("M45");
		assertNotNull(morbusBechterew);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.BASDAI, morbusBechterew.getId());
		
		List<KeyPerformanceIndicatorStatistics> result = patientKPIService.computeKPIAverageTimeline(
				kpi.getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(2, result.size());
		assertEquals(6.1d, result.get(0).getValue().doubleValue(), 0.001);
		assertEquals(5.5d, result.get(1).getValue().doubleValue(), 0.001);
		
		result = patientKPIService.computeKPIAverageTimeline(
				kpi.getId(), dateFormat.parse("2.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(5.5d, result.get(0).getValue().doubleValue(), 0.001);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKpi_BASDAI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease morbusBechterew = diseaseService.findByCode("M45");
		assertNotNull(morbusBechterew);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.BASDAI, morbusBechterew.getId());
		
		BigDecimal result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("1.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(5.8d, result.doubleValue(), 0.001);
				
		result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("2.11.2010"), dateFormat.parse("2.11.2010"));
		assertEquals(5.5d, result.doubleValue(), 0.001);
		
		result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("3.11.2010"), dateFormat.parse("3.11.2010"));
		assertEquals(0.0d, result.doubleValue(), 0.001);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void computeKPI_BASDAI() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease morbusBechterew = diseaseService.findByCode("M45");
		assertNotNull(morbusBechterew);
		
		KeyPerformanceIndicatorType kpi = keyPerformanceIndicatorTypeService.find(KeyPerformanceIndicatorType.Type.BASDAI, morbusBechterew.getId());
				
		BigDecimal result = patientKPIService.computeKPI(
				patient.getId(), kpi.getId(), dateFormat.parse("1.2.2010"), dateFormat.parse("1.12.2010"));
		assertNotNull(result);
	}
}
