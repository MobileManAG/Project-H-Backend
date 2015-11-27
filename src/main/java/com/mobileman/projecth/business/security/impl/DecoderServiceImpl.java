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
 * DecoderImpl.java
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
import com.mobileman.projecth.business.security.DecoderService;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.DECODER_SERVICE)
public class DecoderServiceImpl extends AESEncoderDecoderImpl implements DecoderService {
	/**
	 * Logger for this class
	 */
	private final Logger log = Logger.getLogger(getClass());

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.DecoderService#decode(java.lang.String)
	 */
	@Override
	public String decode(String value) {
		if (log.isDebugEnabled()) {
			log.debug("decode(" + value + ") - start"); //$NON-NLS-1$
		}

		if (value == null) {
			return null;
		}		

		String returnString = AESEncoderDecoder.decode(value);
		if (log.isDebugEnabled()) {
			log.debug("decode(String) - returns"); //$NON-NLS-1$
		}
		
		return returnString == null ? null : returnString.trim();
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.DecoderService#decodeInteger(java.lang.String)
	 */
	@Override
	public Integer decodeInteger(String value) {
		if (log.isDebugEnabled()) {
			log.debug("decodeInteger(String) - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("decodeInteger(String) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		Integer returnInteger = Integer.valueOf(AESEncoderDecoder.decode(value));
		if (log.isDebugEnabled()) {
			log.debug("decodeInteger(String) - returns"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.security.DecoderService#decodeLong(java.lang.String)
	 */
	@Override
	public Long decodeLong(String value) {
		if (log.isDebugEnabled()) {
			log.debug("decodeLong(String) - start"); //$NON-NLS-1$
		}

		if (value == null) {
			if (log.isDebugEnabled()) {
				log.debug("decodeLong(String) - returns"); //$NON-NLS-1$
			}
			return null;
		}
		

		Long returnLong = Long.valueOf(AESEncoderDecoder.decode(value));
		if (log.isDebugEnabled()) {
			log.debug("decodeLong(String) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}

}
