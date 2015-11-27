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
 * LoginException.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 14.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.exception;


/**
 * Represents errors logic processing mail messages
 * 
 * @author mobileman
 *
 */
public class MailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reason of an exception
	 * 
	 * @author mobileman
	 *
	 */
	public enum Reason {
		
		/**
		 * 
		 */
		SENDER_NAME_MISSING,
		
		/**
		 * 
		 */
		SENDER_EMAIL_MISSING;
	}
	
	private final Reason reason;
	
	/**
	 * @param reason
	 */
	public MailException(Reason reason) {
		super();
		this.reason = reason;
	}

	/**
	 * @return reason
	 */
	public Reason getReason() {
		return this.reason;
	}
}
