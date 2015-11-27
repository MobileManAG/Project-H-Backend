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
 * HibernateEntityInterceptor.java
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

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserAccount;

/**
 * Hibernate interceptor 
 * @author mobileman
 *
 */
@Service("hibernateEntityInterceptor")
public class HibernateEntityInterceptor extends EmptyInterceptor {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(HibernateEntityInterceptor.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//@Autowired
	//private UserDataEncryptionDecryptionService userDataEncryptionDecryptionService;
	
	/** 
	 * {@inheritDoc}
	 * @see org.hibernate.EmptyInterceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (log.isDebugEnabled()) {
			log.debug("onLoad(Object, Serializable, Object[], String[], Type[]) - start"); //$NON-NLS-1$
		}

		if (UserAccount.class.isInstance(entity)) {
			//userDataEncryptionDecryptionService.process(false, UserAccount.class.cast(entity), state, propertyNames);
		} else if (User.class.isInstance(entity)) {
			//userDataEncryptionDecryptionService.process(false, User.class.cast(entity), state, propertyNames);
		}
		

		boolean returnboolean = super.onLoad(entity, id, state, propertyNames, types);
		if (log.isDebugEnabled()) {
			log.debug("onLoad(Object, Serializable, Object[], String[], Type[]) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (log.isDebugEnabled()) {
			log.debug("onSave(Object, Serializable, Object[], String[], Type[]) - start"); //$NON-NLS-1$
		}

		if (UserAccount.class.isInstance(entity)) {
			//userDataEncryptionDecryptionService.process(true, UserAccount.class.cast(entity), state, propertyNames);
		} else if (User.class.isInstance(entity)) {
			//userDataEncryptionDecryptionService.process(true, User.class.cast(entity), state, propertyNames);
		}
		
		boolean returnboolean = super.onSave(entity, id, state, propertyNames, types);
		if (log.isDebugEnabled()) {
			log.debug("onSave(Object, Serializable, Object[], String[], Type[]) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		if (log.isDebugEnabled()) {
			log.debug("onFlushDirty(Object, Serializable, Object[], Object[], String[], Type[]) - start"); //$NON-NLS-1$
		}

		if (User.class.isInstance(entity)) {
			//User user = User.class.cast(entity);
			//userDataEncryptionDecryptionService.process(true, user, currentState, propertyNames);
		}
		

		boolean returnboolean = super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
		if (log.isDebugEnabled()) {
			log.debug("onFlushDirty(Object, Serializable, Object[], Object[], String[], Type[]) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see org.hibernate.EmptyInterceptor#preFlush(java.util.Iterator)
	 */
	@Override
	public void preFlush(Iterator entities) {
		if (log.isDebugEnabled()) {
			log.debug("preFlush(Iterator) - start"); //$NON-NLS-1$
		}

		super.preFlush(entities);

		if (log.isDebugEnabled()) {
			log.debug("preFlush(Iterator) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see org.hibernate.EmptyInterceptor#instantiate(java.lang.String, org.hibernate.EntityMode, java.io.Serializable)
	 */
	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
		if (log.isDebugEnabled()) {
			log.debug("instantiate(String, EntityMode, Serializable) - start"); //$NON-NLS-1$
		}

		Object result = super.instantiate(entityName, entityMode, id);
		if (UserAccount.class.isInstance(result)) {
			//userDataEncryptionDecryptionService.process(false, UserAccount.class.cast(result));
		}
		

		if (log.isDebugEnabled()) {
			log.debug("instantiate(String, EntityMode, Serializable) - returns"); //$NON-NLS-1$
		}
		return result;
	}
}
