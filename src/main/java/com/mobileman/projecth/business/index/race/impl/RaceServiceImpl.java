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
 * FamilySituationServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.index.race.impl;

import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.index.impl.IndexServiceImpl;
import com.mobileman.projecth.business.index.race.RaceService;
import com.mobileman.projecth.domain.index.race.Race;

/**
 * @author mobileman
 * 
 */
@Service("raceService")
public class RaceServiceImpl extends IndexServiceImpl<Race> implements RaceService {

	/**
	 */
	public RaceServiceImpl() {
		super(Race.class);
	}

}
