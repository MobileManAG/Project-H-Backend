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
 * IndexServiceTest.java
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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.index.country.CountryService;

/**
 * @author mobileman
 *
 */
public class IndexServiceTest extends TestCaseBase {
	
	@Autowired
	private CountryService countryService;

	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void findByCode() throws Exception {
		countryService.findByCode(null);
	}
}
