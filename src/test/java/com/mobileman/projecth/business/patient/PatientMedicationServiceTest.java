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
 * PatientMedicationServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.patient;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.DiseaseService;
import com.mobileman.projecth.business.MedicationService;
import com.mobileman.projecth.business.PatientMedicationService;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.medicine.Medication;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.patient.medication.PatientMedication;
import com.mobileman.projecth.domain.util.medication.MedicationFrequency;
import com.mobileman.projecth.persistence.patient.PatientMedicationDao;
import com.mobileman.projecth.util.disease.DiseaseCodes;

/**
 * @author mobileman
 *
 */
public class PatientMedicationServiceTest extends TestCaseBase {

	@Autowired
	PatientMedicationService patientMedicationService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	MedicationService medicationService;
	
	@Autowired
	PatientMedicationDao patientMedicationDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private DiseaseService diseaseService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addConsumedMedication() throws Exception {
		
		int oldSize = patientMedicationDao.findAll().size();
				
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		Medication medication = medicationService.findByName("Kort 2", Locale.GERMANY).get(0);
		assertNotNull(medication);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		patientMedicationService.addConsumedMedication(patient.getId(), disease.getId(), medication.getId(), 2.0d, new Date(), null);		
		assertEquals(oldSize + 1, patientMedicationDao.findAll().size());
		
		
		patientMedicationService.addConsumedMedication(patient.getId(), disease.getId(), medication.getId(), 2.0d, new Date(), "test");		
		assertEquals(oldSize + 2, patientMedicationDao.findAll().size());
		
		List<Medication> result = patientMedicationService.findAllConsumedMedications(patient.getId(), disease.getId());
		assertEquals(4, result.size());
		assertFalse(result.get(0).equals(result.get(1)));
		
		result = patientMedicationService.findAllConsumedMedications(patient.getId(), 0L);
		assertEquals(0, result.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addConsumedMedicationWeekly() throws Exception {
		
		int oldSize = patientMedicationDao.findAll().size();
				
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		Medication medication = medicationService.findByName("Kort 2", Locale.GERMANY).get(0);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		assertNotNull(disease);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date startDate = dateFormat.parse("1.11.2010");
		Date endDate = dateFormat.parse("28.11.2010");
		
		patientMedicationService.addConsumedMedication(
				patient.getId(), disease.getId(), medication.getId(), 2.0d, 
				MedicationFrequency.WEEKLY,
				startDate,
				endDate, null);		
		assertEquals(oldSize + 4, patientMedicationDao.findAll().size());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addConsumedMedicationBiWeekly() throws Exception {
		
		int oldSize = patientMedicationDao.findAll().size();
				
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		Medication medication = medicationService.findByName("Kort 2", Locale.GERMANY).get(0);
		
		Disease disease = diseaseService.findByCode(DiseaseCodes.RHEUMA_CODE);
		assertNotNull(disease);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date startDate = dateFormat.parse("1.11.2010");
		Date endDate = dateFormat.parse("28.11.2010");
		
		patientMedicationService.addConsumedMedication(
				patient.getId(), disease.getId(), medication.getId(), 2.0d, 
				MedicationFrequency.BI_WEEKLY,
				startDate,
				endDate, null);		
		assertEquals(oldSize + 2, patientMedicationDao.findAll().size());
		
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findConsumedMedications() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		List<Medication> result = patientMedicationService.findAllConsumedMedications(patient.getId(), disease.getId());
		assertEquals(4, result.size());
		Medication medication = result.get(0);
		assertNotNull(medication);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findAllForDisease() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		List<PatientMedication> result = patientMedicationService.findAllForDisease(patient.getId(), disease.getId());
		assertEquals(6, result.size());
		PatientMedication medication = result.get(0);
		assertNotNull(medication);
	}
	
	/**
	 * {@link PatientMedicationService#findConsumedMedicationsDiary(Long, Long, java.util.Date, java.util.Date)}
	 * @throws Exception
	 */
	@Test
	public void findPatientConsumedMedicationsDiary() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date startDate = dateFormat.parse("1.11.2010");
		Date endDate = dateFormat.parse("3.11.2010");
		List<Object[]> result = patientMedicationService.findConsumedMedicationsDiary(patient.getId(), disease.getId(), startDate, endDate);
		
		assertNotNull(result);
		assertEquals(5, result.size());
		assertEquals(2, Number.class.cast(result.get(2)[2]).intValue());
		assertEquals(2, Number.class.cast(result.get(3)[2]).intValue());
		
		for (Object[] row : result) {
			assertTrue(Medication.class.isInstance(row[1]));
		}	
	}
	
	/**
	 * {@link PatientMedicationService#findConsumedMedicationsDiary(Long, java.util.Date, java.util.Date)}
	 * @throws Exception
	 */
	@Test
	public void findPatientConsumedMedicationsDiary2() throws Exception {
		Patient patient = (Patient)userService.findUserByLogin("sysuser1");		
		assertNotNull(patient);
		
		Disease disease = diseaseService.findByCode("M79.0");
		assertNotNull(disease);
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date startDate = dateFormat.parse("1.11.2010");
		Date endDate = dateFormat.parse("3.11.2010");
		List<Object[]> result = patientMedicationService.findConsumedMedicationsDiary(patient.getId(), startDate, endDate);
		
		assertNotNull(result);
		assertEquals(5, result.size());
		assertEquals(2, Number.class.cast(result.get(2)[2]).intValue());
		assertEquals(2, Number.class.cast(result.get(3)[2]).intValue());
		
		for (Object[] row : result) {
			assertTrue(Medication.class.isInstance(row[1]));
		}	
	}
}
