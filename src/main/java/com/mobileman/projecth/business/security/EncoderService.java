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
 * Encoder.java
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

/**
 * Declares methods for system encoder
 * 
 * @author mobileman
 *
 */
public interface EncoderService {

	/**
	 * Encodes string value
	 * @param value
	 * @return encoded string value
	 */
	public String encode(final String value);
	
	/**
	 * Encodes int value
	 * @param value
	 * @return encoded int value
	 */
	public String encode(final Integer value);
	
	/**
	 * Encodes long value
	 * @param value
	 * @return encoded long value
	 */
	public String encode(final Long value);

	/**
	 * @param value
	 * @return true if given value is already encoded
	 */
	public boolean isEncoded(String value);
}
