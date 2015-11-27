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
 * ImageServiceImpl.java
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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ConfigurationService;
import com.mobileman.projecth.business.ImageService;

/**
 * @author mobileman
 *
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private ConfigurationService configurationService;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.ImageService#copyImage(java.lang.String, java.lang.String)
	 */
	@Override
	public String copyImage(String sourceFileFullPath, String relativePath) {
		if (sourceFileFullPath == null) {
			throw new IllegalArgumentException("sourceFileFullPath must not be null");
		}
		
		if (sourceFileFullPath.trim().length() == 0) {
			throw new IllegalArgumentException("sourceFileFullPath must not be empty");
		}
		
		if (relativePath == null) {
			throw new IllegalArgumentException("relativePath must not be null");
		}
		
		if (sourceFileFullPath.trim().length() == 0) {
			throw new IllegalArgumentException("relativePath must not be empty");
		}
		
		DateFormat dirNameDateFormat = new SimpleDateFormat("yyyy_MM");
		DateFormat fileNameDateFormat = new SimpleDateFormat("hh_mm_ss");
		
		String result = "";
		try {
			File sourceFile = new File(sourceFileFullPath);
			String[] fileNameComponents = sourceFile.getName().split("[.]");
			String postfix = fileNameComponents[fileNameComponents.length - 1];
			
			String targetFileDir = configurationService.getImagesRootDirectoryPath() + File.separator + relativePath + File.separator + dirNameDateFormat.format(new Date());
			File dir = new File(targetFileDir);
			dir.mkdirs();
			if (!dir.exists()) {
				throw new RuntimeException("could not create directories: " + dir.getAbsolutePath());
			}
			
			File newFile = new File(targetFileDir + File.separator + fileNameDateFormat.format(new Date()) + "." + postfix);
			try {
				FileUtils.moveFile(sourceFile, newFile);
				result = relativePath + File.separator + dirNameDateFormat.format(new Date()) + File.separator + newFile.getName();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
						
		} finally {
			
		}
		
		return result;
	}

}
