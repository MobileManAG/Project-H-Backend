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
 * HaqService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import java.util.List;

import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.questionary.OneTimeHaq;

/**
 * Declares service For {@link Haq}
 * 
 * @author mobileman
 *
 */
public interface HaqService extends EntityService<Haq>, SearchService<Haq> {
	
	/**
	 * 
	 * @param diseaseId
	 * @return list of haq by disease
	 * @throws IllegalArgumentException if
	 * <li>diseaseId == <code>null</code>
	 */
	List<Haq> findByDisease(Long diseaseId) throws IllegalArgumentException;

	/**
	 * @param diseaseId
	 * @return one time questionary for given disease (if exists)
	 */
	OneTimeHaq findOneTimeHaqByDisease(Long diseaseId);
}
