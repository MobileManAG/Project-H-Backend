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
 * projecthPatientInitialPost.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 24.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.services.ws.mobile;


/**
 * Holder of the user's initialy transmitted data 
 * @author mobileman
 *
 */
public class ProjectHPatientInitialPost extends ProjectHPatientPost {
	
	private Integer birthday;
	private Integer sex;
		
	/**
	 * Gets a birthday
	 *
	 * @return birthday
	 */
	public Integer getBirthday() {
		return this.birthday;
	}
	/**
	 * Setter for birthday
	 *
	 * @param birthday new value of birthday
	 */
	public void setBirthday(Integer birthday) {
		this.birthday = birthday;
	}
	/**
	 * Gets a sex
	 *
	 * @return sex
	 */
	public Integer getSex() {
		return this.sex;
	}
	/**
	 * Setter for sex
	 *
	 * @param sex new value of gender
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}	
}
