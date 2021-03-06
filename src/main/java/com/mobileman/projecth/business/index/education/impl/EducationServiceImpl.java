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
 * EducationServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.index.education.impl;

import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.index.education.EducationService;
import com.mobileman.projecth.business.index.impl.IndexServiceImpl;
import com.mobileman.projecth.domain.index.education.Education;

/**
 * @author mobileman
 *
 */
@Service("educationService")
public class EducationServiceImpl extends IndexServiceImpl<Education> implements EducationService {

	/**
	 */
	public EducationServiceImpl() {
		super(Education.class);
	}

}
