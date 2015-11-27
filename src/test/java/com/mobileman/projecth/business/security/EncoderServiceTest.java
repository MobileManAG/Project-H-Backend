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
 * EncoderServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 11.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.security;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;

/**
 * @author mobileman
 *
 */
public class EncoderServiceTest extends TestCaseBase {

	@Autowired
	EncoderService encoderService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void encodeString() throws Exception {
		assertNull(encoderService.encode((String)null));
		
		String result = encoderService.encode("test");
		assertEquals("pd47Fp8X+bf1YKsPXzo7VA==", result.trim());
		
		String[] logs= {
				"projecth Testpatient"
		};
		
		String[] ems= {
				"kontakt@test.com",

		};
		System.out.println("\n");
		for (int i = 0; i < logs.length; i++) {
			String encd = encoderService.encode(logs[i]);
			String eml = encoderService.encode(ems[i]);
			String sql = "UPDATE user_account SET \"login\"= '" + encd + "', email='" + eml + "' WHERE \"login\"='" + logs[i] + "'";
			System.out.println(sql);
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void encodeInteger() throws Exception {
		assertNull(encoderService.encode((Integer)null));
		
		String result = encoderService.encode(100);
		assertEquals("w2gNG97Ppv1UhM3an6+KQA==", result.trim());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void encodeLong() throws Exception {
		assertNull(encoderService.encode((Long)null));
		
		String result = encoderService.encode(100L);
		assertEquals("w2gNG97Ppv1UhM3an6+KQA==", result.trim());
	}
}
