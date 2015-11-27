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
 * MedicineTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.domain.medicine.Medication;

/**
 * @author mobileman
 *
 */
public class MedicationServiceTest extends TestCaseBase {

	@Autowired
	MedicationService medicationService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByPznNullPzn() throws Exception {		
		medicationService.findByPzn(null, Locale.getDefault());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByPznBlankPzn() throws Exception {		
		medicationService.findByPzn(" ", Locale.getDefault());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findByPzn() throws Exception {		
		Medication medication = medicationService.findByPzn("2758099", Locale.getDefault());
		assertNull(medication);
		
		medication = medicationService.findByPzn("2758099", Locale.GERMANY);
		assertNotNull(medication);
		assertNotNull(medication.getId());
		assertEquals("2758099", medication.getPzn().getNumber());
		assertEquals("PZN-2758099", medication.getPzn().format());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByNameNullName() throws Exception {		
		medicationService.findByName(null, Locale.getDefault());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByNameBlankName() throws Exception {		
		medicationService.findByName(" ", Locale.getDefault());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findByName() throws Exception {		
		List<Medication> medications = medicationService.findByName("Kort 1", Locale.getDefault());
		assertTrue(medications.isEmpty());
		
		medications = medicationService.findByName("Kort 1", Locale.GERMANY);
		assertEquals(1, medications.size());
		assertEquals("Kort 1", medications.get(0).getName());
		
		medications = medicationService.findByName("Kort", Locale.GERMANY);
		assertEquals(4, medications.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findByNameOrPzn() throws Exception {		
		List<Medication> medications = medicationService.findAllByNameOrPzn("Kort 1", Locale.getDefault());
		assertTrue(medications.isEmpty());
		
		medications = medicationService.findAllByNameOrPzn("Kort 1", Locale.GERMANY);
		assertEquals(1, medications.size());
		assertEquals("Kort 1", medications.get(0).getName());
		
		medications = medicationService.findAllByNameOrPzn("Kort", Locale.GERMANY);
		assertEquals(4, medications.size());
		
		medications = medicationService.findAllByNameOrPzn("2758099", Locale.GERMANY);
		assertEquals(1, medications.size());
	}
}
