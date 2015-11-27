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
 * MedicalInstitutionUtil.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 6.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.data;

import com.mobileman.projecth.domain.data.MedicalInstitution;

/**
 * @author mobileman
 *
 */
public final class MedicalInstitutionUtil {

	private MedicalInstitutionUtil() {}
	
	/**
	 * @param institution
	 * @return fmt
	 */
	public static String fmtMedicalInstitution(MedicalInstitution institution) {
		if (institution == null) {
			return "";
		}
		
		String fmt = "";
		
		if (institution.getAddress() != null) {
			if (institution.getAddress().getAddress() != null) {
				fmt += institution.getAddress().getAddress();
			}
			
			if (institution.getAddress().getNumber() != null) {
				fmt += " " + institution.getAddress().getNumber();
			}
			
			fmt += ", ";
			
			if (institution.getAddress().getPostalCode() != null) {
				fmt += institution.getAddress().getPostalCode().format();
			}
			
			if (institution.getAddress().getPlace() != null) {
				fmt += " " + institution.getAddress().getPlace();
			}
		}
		
		return fmt.trim();
	}
}
