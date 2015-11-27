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
 * CountryServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 23.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.index.country.CountryService;
import com.mobileman.projecth.domain.index.country.Country;

/**
 * @author mobileman
 *
 */
public class CountryServiceTest extends TestCaseBase {

	@Autowired
	private CountryService countryService;
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findByCode() throws Exception {
		Country country = countryService.findByCode("DE");
		assertNotNull(country);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findAll() throws Exception {
		List<Country> countries = countryService.findAll();
		assertNotNull(countries);
		assertEquals(4, countries.size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findById() throws Exception {
		List<Country> countries = countryService.findAll();
		assertFalse(countries.isEmpty());
		Country ctr = countryService.findById(countries.get(0).getId());
		assertEquals(countries.get(0), ctr);
	}
}
