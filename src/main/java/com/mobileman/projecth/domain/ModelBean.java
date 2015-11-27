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
 * ModelBean.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain;

import java.util.HashMap;
import java.util.Map;

import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * @author mobileman
 *
 */
public abstract class ModelBean {

	private Map<String, Object> decryptedPropertiesValue;
	
	/**
	 * @param <T>
	 * @param property
	 * @return encrypted property value
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getDecryptedValue(String property) {
		if (this.decryptedPropertiesValue == null) {
			return null;
		}
		
		return (T) this.decryptedPropertiesValue.get(property);
	}
	
	/**
	 * @param property
	 * @return encrypted property value
	 */
	protected String getDecryptedValue(String property, String cryptedValue) {
		if (this.decryptedPropertiesValue == null) {
			this.decryptedPropertiesValue = new HashMap<String, Object>();
		}
		
		String result = null;
		if (!this.decryptedPropertiesValue.containsKey(property)) {
			result = AESEncoderDecoder.decode(cryptedValue);
			this.decryptedPropertiesValue.put(property, result);
		} else {
			result = (String)this.decryptedPropertiesValue.get(property);
		}
				
		return result;
	}
	
	/**
	 * @param <T>
	 * @param property
	 * 
	 */
	protected <T> void setDecryptedValue(String property, String value) {
		if (this.decryptedPropertiesValue == null) {
			this.decryptedPropertiesValue = new HashMap<String, Object>();
		}
		
		this.decryptedPropertiesValue.put(property, value);
	}
}
