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
 * ConfigurationService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

/**
 * @author mobileman
 *
 */
public interface ConfigurationService {

	/**
	 * @return root dir path to directory containing projecth images
	 */
	String getImagesRootDirectoryPath();

	/**
	 * @return nachrichten@projecth.com 
	 */
	String getMessageCenterSenderEmail();
	
	/**
	 * @return kontakt@projecth.com
	 */
	String getPublicContactReceiverEmail();

	/**
	 * @return min length of user's password
	 */
	int getMinPasswordLength();
	
	/**
	 * @return max length of user's password
	 */
	int getMaxPasswordLength();
	
	/**
	 * @return min length of user's login
	 */
	int getMinLoginLength();
	
	/**
	 * @return max length of user's login
	 */
	int getMaxLoginLength();
}
