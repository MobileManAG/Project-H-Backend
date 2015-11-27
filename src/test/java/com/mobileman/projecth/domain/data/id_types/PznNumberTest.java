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
 * PznNumberTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 27.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data.id_types;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author mobileman
 *
 */
public class PznNumberTest {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void format() throws Exception {
		PznBarcode number = new PznBarcode();
		number.setNumber("1234567");
		assertEquals("PZN-1234567", number.format());
	}
}
