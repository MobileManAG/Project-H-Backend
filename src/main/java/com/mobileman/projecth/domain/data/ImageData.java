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
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Represent a image (with data)
 * @author mobileman
 *
 */
@Embeddable
public class ImageData {

	/**
	 * Answer image name (path)
	 */
	private String imageName;
	
	/**
	 * Answer image data
	 */
	private String imageData;
	
	/**
	 * @return imageName
	 */
	@Column(name = "image_name", unique = false, nullable = true, length = 4000)
	public String getImageName() {
		return this.imageName;
	}
	/**
	 * @param imageName new value of imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * @return imageData
	 */
	@Column(name = "image_data", unique = false, nullable = true, length = 4000)
	public String getImageData() {
		return this.imageData;
	}
	/**
	 * @param imageData new value of imageData
	 */
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
	
	
}
