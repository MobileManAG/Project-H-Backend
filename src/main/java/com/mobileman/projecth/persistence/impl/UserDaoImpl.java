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
package com.mobileman.projecth.persistence.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.business.security.DecoderService;
import com.mobileman.projecth.business.security.EncoderService;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserAccount;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.persistence.UserDao;

/**
 * 
 */
@Repository("userDao")
public class UserDaoImpl extends DaoImpl<User> implements UserDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	@Autowired
	private EncoderService encoderService;
	
	@Autowired
	private DecoderService decoderService;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.impl.DaoImpl#update(java.lang.Object)
	 */
	@Override
	public void update(User entity) {
		if (log.isDebugEnabled()) {
			log.debug("update(User) - start"); //$NON-NLS-1$
		}

		super.update(entity);

		if (log.isDebugEnabled()) {
			log.debug("update(User) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#findByLogin(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User findByLogin(String login) {
		if (log.isDebugEnabled()) {
			log.debug("findByLogin(" + login +") - start"); //$NON-NLS-1$
		}

		if (login == null) {
			throw new IllegalArgumentException("Login must not be null");
		}
		
		if (login.trim().length() == 0) {
			throw new IllegalArgumentException("Login must not be empty string");
		}
		
		String encodedLogin = encoderService.encode(login).trim();	
		List<User> users = (List)getHibernateTemplate().find(
				"select u " +
				"from User u join u.userAccount ua where ua.loginEncoded=?", encodedLogin);
		

		User returnUser = users.isEmpty() ? null : users.get(0);
		if (log.isDebugEnabled()) {
			log.debug("findByLogin(String) - returns"); //$NON-NLS-1$
		}
		return returnUser;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#saveAccount(com.mobileman.projecth.domain.user.UserAccount)
	 */
	@Override
	public Long saveAccount(UserAccount userAccount) {
		if (log.isDebugEnabled()) {
			log.debug("saveAccount(UserAccount) - start"); //$NON-NLS-1$
		}
		
		if (userAccount.getUser().getUserType().equals(UserType.A)) {
			throw new IllegalArgumentException("Admin account can not be deleted");
		}
		
		Long result = (Long)getHibernateTemplate().save(userAccount);
		flush();
		
		if (log.isDebugEnabled()) {
			log.debug("saveAccount(UserAccount) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#deleteUserAccount(com.mobileman.projecth.domain.user.UserAccount)
	 */
	@Override
	public void deleteUserAccount(UserAccount account) {
		if (log.isDebugEnabled()) {
			log.debug("deleteUserAccount(UserAccount) - start"); //$NON-NLS-1$
		}

		if (account.getUser().getUserType().equals(UserType.A)) {
			throw new IllegalArgumentException("Admin account can not be deleted");
		}
		
		getHibernateTemplate().delete(account);

		if (log.isDebugEnabled()) {
			log.debug("deleteUserAccount(UserAccount) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#findByActivationCode(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User findByActivationCode(String activationCode)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByActivationCode(String) - start"); //$NON-NLS-1$
		}

		if (activationCode == null || activationCode.trim().length() == 0) {
			throw new IllegalArgumentException("activation code must not be null or empty");
		}
		
		List<User> users = (List)getHibernateTemplate().find(
				"select u " +
				"from User u " +
				"where u.activationUid=?", activationCode);
		User result = null;
		if (!users.isEmpty()) {
			result = users.get(0);
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findByActivationCode(String) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#findByEmail(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User findByEmail(String email) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findByEmail(" + email + ") - start"); //$NON-NLS-1$
		}

		User result = null;
		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("email code must not be null or empty");
		}
		
		String encodedLogin = encoderService.encode(email).trim();
		List<User> users = (List)getHibernateTemplate().find(
				"select u " +
				"from User u " +
				"where u.userAccount.emailEncoded=?", encodedLogin);
		if (!users.isEmpty()) {
			result = users.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("findByEmail(String) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#updateAboutMe(java.lang.Long, java.lang.String)
	 */
	@Override
	public void updateAboutMe(Long userId, String aboutMe) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("updateAboutMe(Long, String) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("user id must not be null");
		}
		User user = findById(userId);
		user.setAboutMe(aboutMe);
		update(user);

		if (log.isDebugEnabled()) {
			log.debug("updateAboutMe(Long, String) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.impl.DaoImpl#save(java.lang.Object)
	 */
	@Override
	public Long save(User entity) {
		if (log.isDebugEnabled()) {
			log.debug("save(User) - start"); //$NON-NLS-1$
		}

		if (UserType.A.equals(entity.getUserType())) {
			throw new IllegalArgumentException("User of type ADMIN must not be persisted");
		}
		

		Long returnLong = super.save(entity);
		if (log.isDebugEnabled()) {
			log.debug("save(User) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#existUserWithEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean existUserWithEmail(String email) {
		if (log.isDebugEnabled()) {
			log.debug("existUserWithEmail(" + email + ") - start"); //$NON-NLS-1$
		}
		
		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("email code must not be null or empty");
		}
		
		String encodedLogin = encoderService.encode(email).trim();
		List<Object> counts = (List)getHibernateTemplate().find(
				"select count(u.id) " +
				"from User u " +
				"where u.userAccount.emailEncoded=?", encodedLogin);
		
		if (log.isDebugEnabled()) {
			log.debug("existUserWithEmail(String) - returns"); //$NON-NLS-1$
		}
		return !counts.isEmpty() && Number.class.cast(counts.get(0)).intValue() > 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserDao#findUsersByLoginNamePlace(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUsersByLoginNamePlace(String filter) {
		if (log.isDebugEnabled()) {
			log.debug("findUsersByLoginNamePlace(" + filter +") - start"); //$NON-NLS-1$
		}

		if (filter == null) {
			throw new IllegalArgumentException("Login must not be null");
		}
		
		if (filter.trim().length() == 0) {
			throw new IllegalArgumentException("Login must not be empty string");
		}
		
		String encodedFilter = encoderService.encode(filter).trim();
		Query query = getSession().createQuery("select u " +
				"from User u join u.userAccount ua " +
				"left join u.medicalInstitution.country country " +
				"where u.userAccount.id is not null " +
				"and (ua.loginEncoded=:_filt or u.name.nameEncoded=:_filt or u.name.surnameEncoded=:_filt " +
				"or u.medicalInstitution.nameEncoded=:_filt " +
				"or u.medicalInstitution.address.placeEncoded=:_filt " +
				"or u.medicalInstitution.address.numberEncoded=:_filt " +
				"or u.medicalInstitution.address.addressEncoded=:_filt " +
				"or u.medicalInstitution.address.postalCode.codeEncoded=:_filt " +
				// Location
				"or country.name=:_filtdec " +
				"or country.code=:_filtdec) ");
		query.setParameter("_filt", encodedFilter);
		query.setParameter("_filtdec", filter);
		
		List<User> users = query.list();
		
		if (log.isDebugEnabled()) {
			log.debug("findUsersByLoginNamePlace(String) - returns"); //$NON-NLS-1$
		}
		return users;
	}
}
