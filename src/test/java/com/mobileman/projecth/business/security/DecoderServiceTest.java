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
 * DecoderServiceTest.java
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
public class DecoderServiceTest extends TestCaseBase {
	
	@Autowired
	DecoderService decoderService;

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void decodeString() throws Exception {
		assertNull(decoderService.decode(null));
		
		String result = decoderService.decode("pd47Fp8X+bf1YKsPXzo7VA==");
		assertEquals("test", result);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void decodeInteger() throws Exception {
		
		
		assertNull(decoderService.decodeInteger(null));
		
		Integer result = decoderService.decodeInteger("w2gNG97Ppv1UhM3an6+KQA==");
		assertEquals(100, result.intValue());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void decodeLong() throws Exception {
		
		
		assertNull(decoderService.decodeLong(null));
		
		Long result = decoderService.decodeLong("w2gNG97Ppv1UhM3an6+KQA==");
		assertEquals(100, result.longValue());
	}
}
