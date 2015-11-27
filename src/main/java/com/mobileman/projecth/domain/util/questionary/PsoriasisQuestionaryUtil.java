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
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.questionary;

/**
 * @author mobileman
 *
 */
public final class PsoriasisQuestionaryUtil {

	private PsoriasisQuestionaryUtil(){}
	
	/**
	 * 
	 * @return id 
	 */
	public static Long[] getUmfangIds() {
		// Kopf , Korper, Arme, Beine
		return new Long[]{ 2101L, 2105L, 2109L, 2113L };
	}
	
	/**
	 * @return id 
	 */
	public static Long[] getErythemaIds() {
		return new Long[]{ 2102L, 2106L, 2110L, 2114L };
	}
	
	/**
	 * @return id 
	 */
	public static Long[] getIndurationIds() {
		return new Long[]{ 2103L, 2107L, 2111L, 2115L };
	}
	
	/**
	 * @return id 
	 */
	public static Long[] getDesquamationIds() {
		return new Long[]{ 2104L, 2108L, 2112L, 2116L };
	}
	
	/**
	 * @param percentualArea
	 * @return grade of percentual skin area
	 */
	public static int transformSkinAreaToGrade(float percentualArea) {
		if (percentualArea == 0) {
			return 0;
		} else if (percentualArea < 10) {
			return 1;
		} else if (percentualArea >= 10 && percentualArea <= 29) {
			return 2;
		} else if (percentualArea >= 30 && percentualArea <= 49) {
			return 3;
		} else if (percentualArea == 50 && percentualArea <= 69) {
			return 4;
		} else if (percentualArea == 70 && percentualArea <= 89) {
			return 5;
		} else {
			return 6;
		}
	}
}
