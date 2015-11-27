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
 * HaqAnswersPostService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 15.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.services.ws.mobile;

import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostInitialResponse;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostRequest;
import com.mobileman.projecth.services.ws.mobile.model.ProjectHPostResponse;

/**
 * Represents service for processing/transforming a mobile client daily and initail posts.
 * Transforms data posted via web-service to system entities data.
 * 
 * @author mobileman
 *
 */
public interface HaqAnswersPostService {

	/**
	 * @param data
	 * @return response containing result of post processing
	 */
	ProjectHPostResponse processUserPost(ProjectHPostRequest data);
	
	/**
	 * @param data
	 * @return response containing result of initial post processing
	 */
	ProjectHPostInitialResponse processUserPost(ProjectHPostInitialRequest data);
	
	/**
	 * @param questionId
	 * @param oldAnswerId
	 * @return new answer id
	 */
	Long translateToAnswerId(Long questionId, Integer oldAnswerId);
}
