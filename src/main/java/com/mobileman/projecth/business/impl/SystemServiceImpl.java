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
 * SystemServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.impl;

import org.apache.log4j.Logger;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.MailManager;
import com.mobileman.projecth.business.SystemService;
import com.mobileman.projecth.domain.user.UserType;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.SYSTEM_SERVICE)
public class SystemServiceImpl implements SystemService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(SystemServiceImpl.class);

	@Autowired
	private MailManager mailManager;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.SystemService#requestNewDiseaseGroup(java.lang.String, java.lang.String, com.mobileman.projecth.domain.user.UserType)
	 */
	@Override
	public void requestNewDiseaseGroup(String diseaseName, String emailAddress,
			UserType userType) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("requestNewDiseaseGroup(" + diseaseName + ", " + emailAddress + ", " + userType + ") - start"); //$NON-NLS-1$
		}
		
		mailManager.sendNewDiseaseGroupRequestEmail(diseaseName, emailAddress, userType);

		if (log.isDebugEnabled()) {
			log.debug("requestNewDiseaseGroup(String, String, UserType) - returns"); //$NON-NLS-1$
		}
	}

}
