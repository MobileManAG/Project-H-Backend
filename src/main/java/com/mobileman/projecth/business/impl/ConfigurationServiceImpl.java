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
 * ConfigurationServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.impl;

import com.mobileman.projecth.business.ConfigurationService;

/**
 * @author mobileman
 *
 */
public class ConfigurationServiceImpl implements ConfigurationService {
	
	private String imagesRootDirectoryPath;
	
	private String messageCenterSenderEmail;

	private String publicContactReceiverEmail;

	private int minPasswordLength;

	private int maxPasswordLength;
	
	private int minLoginLength;
	
	private int maxLoginLength;
	
	/**
	 * @param imagesRootDirectoryPath new value of imagesRootDirectoryPath
	 */
	public void setImagesRootDirectoryPath(String imagesRootDirectoryPath) {
		this.imagesRootDirectoryPath = imagesRootDirectoryPath;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getImagesRootDirectoryPath()
	 */
	@Override
	public String getImagesRootDirectoryPath() {
		return imagesRootDirectoryPath;
	}
	
	/**
	 * @param messageCenterSenderEmail new value of messageCenterSenderEmail
	 */
	public void setMessageCenterSenderEmail(String messageCenterSenderEmail) {
		this.messageCenterSenderEmail = messageCenterSenderEmail;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getMessageCenterSenderEmail()
	 */
	@Override
	public String getMessageCenterSenderEmail() {
		return messageCenterSenderEmail;
	}
	
	/**
	 * @param publicContactReceiverEmail new value of publicContactReceiverEmail
	 */
	public void setPublicContactReceiverEmail(String publicContactReceiverEmail) {
		this.publicContactReceiverEmail = publicContactReceiverEmail;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getPublicContactReceiverEmail()
	 */
	@Override
	public String getPublicContactReceiverEmail() {
		return publicContactReceiverEmail;
	}
	
	/**
	 * @param minPasswordLength new value of minPasswordLength
	 */
	public void setMinPasswordLength(int minPasswordLength) {
		this.minPasswordLength = minPasswordLength;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getMinPasswordLength()
	 */
	@Override
	public int getMinPasswordLength() {
		return minPasswordLength;
	}

	/**
	 * @param maxPasswordLength new value of maxPasswordLength
	 */
	public void setMaxPasswordLength(int maxPasswordLength) {
		this.maxPasswordLength = maxPasswordLength;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getMaxPasswordLength()
	 */
	@Override
	public int getMaxPasswordLength() {
		return maxPasswordLength;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getMinLoginLength()
	 */
	@Override
	public int getMinLoginLength() {
		return this.minLoginLength;
	}

	/**
	 * @param minLoginLength new value of minLoginLength
	 */
	public void setMinLoginLength(int minLoginLength) {
		this.minLoginLength = minLoginLength;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ConfigurationService#getMaxLoginLength()
	 */
	@Override
	public int getMaxLoginLength() {
		return this.maxLoginLength;
	}

	/**
	 * @param maxLoginLength new value of maxLoginLength
	 */
	public void setMaxLoginLength(int maxLoginLength) {
		this.maxLoginLength = maxLoginLength;
	}

	
}
