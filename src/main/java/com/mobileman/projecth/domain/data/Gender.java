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
 * Gender.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 21.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data;

/**
 * @author mobileman
 *
 */
public enum Gender {

	/**
	 * <b>0</b> Women
	 */
	FEMALE,
	
	/**
	 * <b>1</b> Man
	 */
	MALE,
	
	/**
	 * <b>2</b> Women
	 */
	UNKNOWN,;
	
	/**
	 * @param ordinal
	 * @return gender
	 */
	public static Gender getGender(int ordinal) {
		switch (ordinal) {
		case 0:
			return FEMALE;
		case 1:
			return MALE;
		default:
			return UNKNOWN;
		}
	}
}
