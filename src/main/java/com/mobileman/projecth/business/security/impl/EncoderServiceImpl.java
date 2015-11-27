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
 * EncoderImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 11.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.security.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.security.EncoderService;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.ENCODER_SERVICE)
public class EncoderServiceImpl extends AESEncoderDecoderImpl implements EncoderService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(EncoderServiceImpl.class);

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.EncoderService#encode(java.lang.String)
	 */
	@Override
	public String encode(String value) {
		if (log.isDebugEnabled()) {
			log.debug("encode(" + value + ") - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("encode(String) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		String returnString = AESEncoderDecoder.encode(value);
		if (log.isDebugEnabled()) {
			log.debug("encode(String) - returns"); //$NON-NLS-1$
		}
		return returnString;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.EncoderService#encode(java.lang.Integer)
	 */
	@Override
	public String encode(Integer value) {
		if (log.isDebugEnabled()) {
			log.debug("encode(Integer) - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("encode(Integer) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		String returnString = AESEncoderDecoder.encode(String.valueOf(value));
		if (log.isDebugEnabled()) {
			log.debug("encode(Integer) - returns"); //$NON-NLS-1$
		}
		return returnString;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.EncoderService#encode(java.lang.Long)
	 */
	@Override
	public String encode(Long value) {
		if (log.isDebugEnabled()) {
			log.debug("encode(Long) - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("encode(Long) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		String returnString = AESEncoderDecoder.encode(String.valueOf(value));
		if (log.isDebugEnabled()) {
			log.debug("encode(Long) - returns"); //$NON-NLS-1$
		}
		return returnString;
	}
}
