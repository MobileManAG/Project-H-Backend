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
 * UserUtil.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 6.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.user;

import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.util.data.NameUtil;

/**
 * @author mobileman
 *
 */
public final class UserUtil {

	private UserUtil(){}
	
	/**
	 * @param user
	 * @return formatted name
	 */
	public static String fmtName(User user) {
		String fmt = "";
		if (user.getTitle() != null) {
			fmt += user.getTitle() + " ";
		}
		
		fmt += NameUtil.fmtName(user.getName());
		return fmt.trim();
	}
	
	/**
	 * @param user
	 * @return formatted user's gender
	 */
	public static String fmtGender(User user) {
		String result = "";
		if (user.getSex() != null) {
			result = user.getSex().intValue() == 0 ? "Frau" : "Mann";
		}
		
		return result;
	}
}
