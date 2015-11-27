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
 * GrantedRight.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 19.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.user.rights;

/**
 * Defines rights which user can choose to grant access to other users
 * @author mobileman
 * 
 */
public enum GrantedRight {

	/**
	 * <b>0</b> Applicable for all users: All registered patients can see base data
	 */
	BASE_DATA_ALL, 
	
	/**
	 * <b>1</b> Applicable for all connections: All connected users can see base data
	 */
	BASE_DATA_CONNECTIONS, 
	
	/**
	 * <b>2</b> No one cam see base data
	 */
	BASE_DATA_NONE, 
	
	/**
	 * <b>3</b> Applicable for all users: All registered patients can see diseases data
	 */
	DISEASE_DATA_ALL, 
	
	/**
	 * <b>4</b> Applicable for all connections: All connected users can see diseases data
	 */
	DISEASE_DATA_CONNECTIONS, 
	
	/**
	 * <b>5</b> No one cam see diseases data
	 */
	DISEASE_DATA_NONE;
}
