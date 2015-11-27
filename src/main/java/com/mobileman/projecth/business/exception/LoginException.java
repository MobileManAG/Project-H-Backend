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

import com.mobileman.projecth.domain.user.UserActivationState;

/**
 * Represents errors in user's sign-in/sign-up logic
 * 
 * @author mobileman
 *
 */
public class LoginException extends RuntimeException {

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
		 * User is not active
		 */
		USER_IS_NOT_ACTIVE,
		
		/**
		 * User entered wrong user name
		 */
		USER_DOES_NOT_EXISTS,
		
		/**
		 * User entered wrong password/login
		 */
		INVALID_CREDENTIALS,
		
		/**
		 * User is in UNVERIFIED state - {@link UserActivationState#UNVERIFIED}
		 */
		UNVERIFIED,
		
		/**
		 * User is in BLOCKED state - {@link UserActivationState#BLOCKED}
		 */
		BLOCKED,
		
		/**
		 * Password is too short
		 */
		PASSWORD_TOO_SHORT,
		
		/**
		 * Password is too long
		 */
		PASSWORD_TOO_LONG,
		
		/**
		 * LOGIN is too short
		 */
		LOGIN_TOO_SHORT,
		
		/**
		 * LOGIN is too long
		 */
		LOGIN_TOO_LONG;
	}
	
	private final Reason reason;
	
	/**
	 * Number of unsuccessful user's logins
	 */
	private int unsuccessfulLoginsCount;

	/**
	 * @param reason
	 */
	public LoginException(Reason reason) {
		super();
		this.reason = reason;
	}
	
	/**
	 * @param reason
	 * @param unsuccessfulLoginsCount 
	 */
	public LoginException(Reason reason, int unsuccessfulLoginsCount) {
		this(reason);
		this.unsuccessfulLoginsCount = unsuccessfulLoginsCount;
	}

	/**
	 * @return reason
	 */
	public Reason getReason() {
		return this.reason;
	}
	
	/**
	 * @return unsuccessfulLoginsCount
	 */
	public int getUnsuccessfulLoginsCount() {
		return this.unsuccessfulLoginsCount;
	}
}
