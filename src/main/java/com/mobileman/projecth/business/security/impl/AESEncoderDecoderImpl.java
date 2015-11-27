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
 * AESEncoderDecoderImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 11.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.security.impl;

import org.apache.log4j.Logger;

/**
 * @author mobileman
 *
 */
public abstract class AESEncoderDecoderImpl {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(AESEncoderDecoderImpl.class);
	
	/**
	 * 
	 */
	public AESEncoderDecoderImpl() {
		super();
	}
	
	/**
	 * @param value
	 * @return isEncoded
	 */
	public boolean isEncoded(String value) {
		return isValueEncoded(value);
	}
	
	/**
	 * @param value
	 * @return boolean
	 */
	protected static boolean isValueEncoded(String value) {
		if (log.isDebugEnabled()) {
			log.debug("isEncoded(String) - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("isEncoded(String) - returns"); //$NON-NLS-1$
			}
			return false;
		}
		
		value = value.trim();
		boolean returnboolean = value.length() > 2 && "==".equals(value.substring(value.length() - 2));
		if (log.isDebugEnabled()) {
			log.debug("isEncoded(String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
		
}
