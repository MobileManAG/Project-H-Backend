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
 * AESEncoderDecoder.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 1.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.util.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.ws.commons.util.Base64;

/**
 * @author mobileman
 *
 */
public final class AESEncoderDecoder {
	
	private AESEncoderDecoder(){}

	/**
	 * 
	 */
	private static final Cipher ecipher;
	
	/**
	 * 
	 */
	private static final Cipher dcipher;
	
	private final static String CHARSET = "UTF-8";
	
	private static final String ALGORITHM = "AES";
	
	static {
		try {
			final byte[] salt = {
				    (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
				    (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
				};
			
			KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);		    
		    String stringSalt = new String(salt, Charset.forName(CHARSET));
		    byte[] key = (stringSalt + "mOBIleMAN").getBytes(CHARSET);		    
		    key = MessageDigest.getInstance("SHA-1").digest(key);
		    key = Arrays.copyOf(key, 32); // use only first 256 bit
		    
		    SecretKeySpec skeySpec = new SecretKeySpec(key, kgen.getAlgorithm());
			ecipher = Cipher.getInstance("AES");
			dcipher = Cipher.getInstance("AES");
			dcipher.init(Cipher.DECRYPT_MODE, skeySpec);
			ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		      
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}

	/**
	 * @param value
	 * @return decoded string
	 */
	public static String decode(String value) {
		if (value == null) {
			return null;
		}
		
		String result = null;
	
		try {
	
			byte[] dec = Base64.decode(value);
			byte[] utf8 = dcipher.doFinal(dec);
			result = new String(utf8, CHARSET);
	
		} catch (Exception e) {	
			result = "";
		}
		
		return result;
	}

	/**
	 * @param value
	 * @return ecoded string
	 */
	public static String encode(String value) {
		if (value == null) {
			return null;
		}
		
		String result = null;
		try {
	
			byte[] utf8 = value.getBytes(CHARSET);
			byte[] enc = ecipher.doFinal(utf8);
			result = Base64.encode(enc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
		return result != null ? result.trim() : null;
	}
}
