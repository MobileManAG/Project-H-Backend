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
 * UserDataEncryptionDecryptionService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 11.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.hibernate.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.security.DecoderService;
import com.mobileman.projecth.business.security.EncoderService;
import com.mobileman.projecth.domain.data.Address;
import com.mobileman.projecth.domain.data.MedicalInstitution;
import com.mobileman.projecth.domain.data.Name;
import com.mobileman.projecth.domain.data.PhoneNumber;
import com.mobileman.projecth.domain.data.id_types.PostalCode;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserAccount;

/**
 * @author mobileman
 *
 */
@Service("userDataEncryptionDecryptionService")
public class UserDataEncryptionDecryptionService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(UserDataEncryptionDecryptionService.class);
	
	@Autowired
	private EncoderService encoderService;
	
	@Autowired
	private DecoderService decoderService;
	
	/**
	 * @param phoneNumber
	 * @return encoded phoneNumber
	 */
	private PhoneNumber encode(PhoneNumber phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		
		phoneNumber.setCountryCode(encoderService.encode(phoneNumber.getCountryCode()));
		phoneNumber.setNumber(encoderService.encode(phoneNumber.getNumber()));
		return phoneNumber;
	}
	
	/**
	 * @param phoneNumber
	 * @return encoded phoneNumber
	 */
	private PhoneNumber decode(PhoneNumber phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		
		phoneNumber.setCountryCode(decoderService.decode(phoneNumber.getCountryCode()));
		phoneNumber.setNumber(decoderService.decode(phoneNumber.getNumber()));
		return phoneNumber;
	}
	
	/**
	 * @param name
	 * @return encoded Name
	 */
	private Name encode(Name name) {
		if (name == null) {
			return null;
		}
		
		name.setName(encoderService.encode(name.getName()));
		name.setSurname(encoderService.encode(name.getSurname()));
		return name;
	}

	/**
	 * @param name
	 * @return encoded Name
	 */
	private Name decode(Name name) {
		if (name == null) {
			return null;
		}
		
		name.setName(decoderService.decode(name.getName()));
		name.setSurname(decoderService.decode(name.getSurname()));
		return name;
	}

	/**
	 * @param encrypt 
	 * @param user
	 * @param state
	 * @param propertyNames
	 */
	public void process(boolean encrypt, User user, Object[] state, String[] propertyNames) {
		if (log.isDebugEnabled()) {
			log.debug("process(" + encrypt + ", User, Object[], String[]) - start"); //$NON-NLS-1$
		}
		
		process(encrypt, user);
		
		for (int i = 0; i < state.length; i++) {
			if ("userAccount".equals(propertyNames[i])) {
				process(encrypt, UserAccount.class.cast(state[i]));
			} else if ("name".equals(propertyNames[i])) {
				process(encrypt, (Name)state[i]);
			} else if ("title".equals(propertyNames[i])) {
				if (encrypt) {
					state[i] = encoderService.encode((String) state[i]);
				} else {
					state[i] = decoderService.decode((String) state[i]);
				}
			} else if ("medicalInstitution".equals(propertyNames[i])) {
				process(encrypt, (MedicalInstitution)state[i]);
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("process(boolean, User, Object[], String[]) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * @param encrypt
	 * @param account
	 * @param state
	 * @param propertyNames
	 */
	public void process(boolean encrypt, UserAccount account, Object[] state, String[] propertyNames) {
		if (log.isDebugEnabled()) {
			log.debug("process(boolean, UserAccount, Object[], String[]) - start"); //$NON-NLS-1$
		}

		for (int i = 0; i < state.length; i++) {
			if ("login".equals(propertyNames[i])) {
				String login = (String) state[i];
				
				if (encrypt && !encoderService.isEncoded(login)) {
					state[i] = encoderService.encode(login).trim();
					account.setLogin(encoderService.encode(login).trim());
				} else {
					//
					if (encoderService.isEncoded(login)) {
						state[i] = decoderService.decode(login);
						account.setLogin(decoderService.decode(login));
					}					
				}
			} else if ("email".equals(propertyNames[i])) {
				String email = (String) state[i];				
				if (encrypt && !encoderService.isEncoded(email)) {
					state[i] = encoderService.encode(email).trim();
					account.setEmail(encoderService.encode(email).trim());
				} else if(!encrypt && encoderService.isEncoded(email)) {
					state[i] = decoderService.decode(email);
					account.setEmail(decoderService.decode(email));
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("process(boolean, UserAccount, Object[], String[]) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * @param encrypt
	 * @param account
	 */
	public void process(boolean encrypt, UserAccount account) {
		if (account == null) {
			return;
		}
		
		String login = account.getLogin();
		String email = account.getEmail();
		
		if (encrypt && !encoderService.isEncoded(login)) {
			//
			account.setLogin(encoderService.encode(login).trim());
			account.setEmail(encoderService.encode(email).trim());
		} else if(!encrypt && encoderService.isEncoded(login)) {
			//
			account.setLogin(decoderService.decode(login));
			account.setEmail(decoderService.decode(email));
		}
	}
	
	/**
	 * @param encrypt
	 * @param nameComp
	 */
	private void process(boolean encrypt, Name nameComp) {
		if (nameComp == null) {
			return;
		}
		
		String name = nameComp.getName();
		String surname = nameComp.getSurname();
		
		if (encrypt) {
			nameComp.setName(encoderService.encode(name));
			nameComp.setSurname(encoderService.encode(surname));
		} else {
			nameComp.setName(decoderService.decode(name));
			nameComp.setSurname(decoderService.decode(surname));
		}
	}
	
	/**
	 * @param encrypt
	 * @param user
	 */
	public void process(boolean encrypt, User user) {
		if (user == null) {
			return;
		}
		
		process(encrypt, user.getUserAccount());
		if (Doctor.class.isInstance(user)) {
			Doctor doctor = Doctor.class.cast(user);
			process(encrypt, doctor.getMedicalInstitution());
		}
		
		if (encrypt) {
			encode(user.getName());
			user.setTitle(encoderService.encode(user.getTitle()));
		} else {
			decode(user.getName());
			user.setTitle(decoderService.decode(user.getTitle()));
		}
		
	}
	
	/**
	 * @param postalCode
	 */
	private void process(boolean encode, PostalCode postalCode) {
		if (postalCode == null) {
			return;
		}
		if (encode) {
			postalCode.setCode(encoderService.encode(postalCode.getCode()));
		} else {
			postalCode.setCode(decoderService.decode(postalCode.getCode()));
		}
	}
	
	/**
	 * @param address
	 */
	private void process(boolean encode, Address address) {
		if (address == null) {
			return;
		}
		
		process(encode, address.getPostalCode());
		
		if (encode) {
			address.setAddress(encoderService.encode(address.getAddress()));
			address.setNumber(encoderService.encode(address.getNumber()));
			address.setPlace(encoderService.encode(address.getPlace()));
		} else {
			address.setAddress(decoderService.decode(address.getAddress()));
			address.setNumber(decoderService.decode(address.getNumber()));
			address.setPlace(decoderService.decode(address.getPlace()));
		}
	}

	/**
	 * @param mi
	 */
	private void process(boolean encode, MedicalInstitution mi) {
		if (mi == null) {
			return;
		}
		
		process(encode, mi.getAddress());
		
		if (encode) {
			mi.setName(encoderService.encode(mi.getName()));
			mi.setHomePageUrl(encoderService.encode(mi.getHomePageUrl()));			
			mi.setFaxNumber(encode(mi.getFaxNumber()));
			mi.setPhoneNumber(encode(mi.getPhoneNumber()));
		} else {
			mi.setName(decoderService.decode(mi.getName()));
			mi.setHomePageUrl(decoderService.decode(mi.getHomePageUrl()));			
			mi.setFaxNumber(decode(mi.getFaxNumber()));
			mi.setPhoneNumber(decode(mi.getPhoneNumber()));
		}		
	}

	/**
	 * @param user
	 * @param currentState
	 * @param previousState
	 * @param propertyNames
	 */
	public void encode(User user, Object[] currentState, Object[] previousState, String[] propertyNames) {
		for (int i = 0; i < currentState.length; i++) {
			if ("userAccount".equals(propertyNames[i])) {
				process(true, UserAccount.class.cast(currentState[i]));
			} else if ("name".equals(propertyNames[i])) {
				process(true, (Name)currentState[i]);
			} else if ("title".equals(propertyNames[i])) {
				currentState[i] = encoderService.encode((String) currentState[i]);
			} else if ("medicalInstitution".equals(propertyNames[i])) {
				process(true, (MedicalInstitution)currentState[i]);
			}
		}
	}
}
