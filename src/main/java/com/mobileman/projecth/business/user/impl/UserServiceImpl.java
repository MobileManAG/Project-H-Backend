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
package com.mobileman.projecth.business.user.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.ConfigurationService;
import com.mobileman.projecth.business.MailManager;
import com.mobileman.projecth.business.UserConnectionService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.exception.LoginException;
import com.mobileman.projecth.business.exception.LoginException.Reason;
import com.mobileman.projecth.business.exception.UserRegistrationException;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.impl.user.UserLoginHandler;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserAccount;
import com.mobileman.projecth.domain.user.UserActivationState;
import com.mobileman.projecth.domain.user.UserState;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.persistence.UserConnectionDao;
import com.mobileman.projecth.persistence.UserDao;
import com.mobileman.projecth.util.HashUtil;

/**
 * 
 */
@Service(ComponentNames.USER_SERVICE)
public class UserServiceImpl extends BusinessServiceImpl<User> implements UserService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private MailManager mailManager;
	
	private UserDao userDao;
	
	@Autowired
	private UserConnectionDao userConnectionDao;
	
	@Autowired
	private UserLoginHandler userLoginHandler;
	
	@Autowired
	private UserConnectionService userConnectionService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	private static final String TEST_PATIENT_LOGIN = "projecth Testpatient";
		
	/**
	 * @param sysuserDao
	 */
	@Autowired
	public void setUserDao(UserDao sysuserDao) {
		if (log.isDebugEnabled()) {
			log.debug("setUserDao(UserDao) - start"); //$NON-NLS-1$
		}

		this.userDao = sysuserDao;
		setDao(sysuserDao);

		if (log.isDebugEnabled()) {
			log.debug("setUserDao(UserDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public Long save(User entity) {
		if (log.isDebugEnabled()) {
			log.debug("save(User) - start"); //$NON-NLS-1$
		}
		
		Long result;
		String plainPassword = entity.getUserAccount().getPassword();
						
		entity.getUserAccount().setPassword(HashUtil.getHashedPassword(plainPassword));
		userDao.saveAccount(entity.getUserAccount());
		entity.setLastUpdate(new Date());
		result = userDao.save(entity);
		flush();

		if (log.isDebugEnabled()) {
			log.debug("save(User) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public User login(String login, String plainPassword) {
		if (log.isDebugEnabled()) {
			log.debug("login(String, String) - start"); //$NON-NLS-1$
		}

		User result = null;
		
		String password = HashUtil.getHashedPassword(plainPassword);
		User user = userDao.findByLogin(login);
		if (user == null) {
			throw new LoginException(Reason.USER_DOES_NOT_EXISTS);
		}
		
		if(!user.getState().equals(UserState.A)){
			throw new LoginException(Reason.USER_IS_NOT_ACTIVE);
		}
				
		if(user.getActivationState().equals(UserActivationState.BLOCKED)){
			throw new LoginException(Reason.BLOCKED);
		}
		
		UserAccount userAccount = user.getUserAccount();
		if(userAccount == null){
			throw new LoginException(Reason.USER_DOES_NOT_EXISTS);
		}
		
		if (!userAccount.getPassword().equals(password)) {
			this.userLoginHandler.processUnsuccessfulLogin(user);
			throw new LoginException(Reason.INVALID_CREDENTIALS, user.getUnsuccessfulLoginsCount());
		}
		
		result = user;
		
		this.userLoginHandler.processSuccessfulLogin(result);
				

		if (log.isDebugEnabled()) {
			log.debug("login(String, String) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#changePassword(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void changePassword(Long userId, String plainOldPassword, String plainNewPassword) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("changePassword(Long, String, String) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		if (plainNewPassword.length() < configurationService.getMinPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_SHORT);
		}
		
		if (plainNewPassword.length() > configurationService.getMaxPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_LONG);
		}
		
		String oldPassword = null;
		if(plainOldPassword != null){
			oldPassword = HashUtil.getHashedPassword(plainOldPassword);
		}
		String newPassword = HashUtil.getHashedPassword(plainNewPassword);
		User user = userDao.findById(userId);
		if(user.getState().equals(UserState.A)){
			if(user.getUserAccount() != null){
				if (user.getUserAccount().getPassword().equals(oldPassword)) {
					user.getUserAccount().setPassword(newPassword);
					userDao.update(user);
				} else {
					throw new LoginException(Reason.INVALID_CREDENTIALS);
				}
			}
		} else if(user.getState().equals(UserState.P)) {
			if(user.getUserAccount() != null){
				user.getUserAccount().setPassword(newPassword);
				user.setState(UserState.A);
				user.setActivationUid(null);
				userDao.update(user);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("changePassword(Long, String, String) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#deleteUserAccount(Long)
	 */
	@Override
	@Transactional
	public void deleteUserAccount(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteUserAccount(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User ID must not be null");
		}
		
		final User user = getDao().findById(userId);
		final UserAccount account = user.getUserAccount();
		if (account == null) {
			throw new IllegalArgumentException("User account already deleted");
		}
		
		user.setUserAccount(null);
		getDao().update(user);
		
		userDao.deleteUserAccount(account);				
		userConnectionDao.deletaAllConnections(user);
		
		getDao().flush();

		if (log.isDebugEnabled()) {
			log.debug("deleteUserAccount(Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#activateUserAccount(java.lang.String)
	 */
	@Override
	@Transactional
	public User activateUserAccount(String activationCode)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("activateUserAccount(String) - start"); //$NON-NLS-1$
		}

		if (activationCode == null || activationCode.trim().length() == 0) {
			throw new IllegalArgumentException("activation code must not be null or empty");
		}
		
		final User user = this.userDao.findByActivationCode(activationCode);
		if (user == null) {
			throw new IllegalStateException("Inactive user does not exists");
		}
		
		user.setActivationUid(null);
		user.setState(UserState.A);
		user.setLastUpdate(new Date());
		getDao().update(user);
		flush();

		if (log.isDebugEnabled()) {
			log.debug("activateUserAccount(String) - returns"); //$NON-NLS-1$
		}
		return user;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#addDiseasesToUser(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void addDiseasesToUser(Long userId, List<Disease> diseases){
		if (log.isDebugEnabled()) {
			log.debug("addDiseasesToUser(Long, List<Disease>) - start"); //$NON-NLS-1$
		}
		
		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		if (diseases == null) {
			throw new IllegalArgumentException("diseases must not be null");
		}
		
		if (diseases.isEmpty()) {
			throw new IllegalArgumentException("diseases must not be empty");
		}
		
		if (diseases.contains(null)) {
			throw new IllegalArgumentException("diseases must not contains null element");
		}
		
		User user = findById(userId);
		List<Disease> userDiseases = user.getDiseases();
		
		if(userDiseases == null){
			userDiseases = new ArrayList<Disease>();
			user.setDiseases(userDiseases);
		}
		
		for(Disease disease : diseases){
			if(!user.getDiseases().contains(disease)){
				user.getDiseases().add(disease);
			}
		}
		
		user.setLastUpdate(new Date());
		update(user);
		

		if (log.isDebugEnabled()) {
			log.debug("addDiseasesToUser(Long, List<Disease>) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#removeDiseasesFromUser(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void removeDiseasesFromUser(Long userId, List<Disease> diseases){
		if (log.isDebugEnabled()) {
			log.debug("removeDiseasesFromUser(Long, List<Disease>) - start"); //$NON-NLS-1$
		}
		
		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		if (diseases == null) {
			throw new IllegalArgumentException("diseases must not be null");
		}
		
		if (diseases.isEmpty()) {
			throw new IllegalArgumentException("diseases must not be empty");
		}
		
		if (diseases.contains(null)) {
			throw new IllegalArgumentException("diseases must not contains null element");
		}
		
		User user = findById(userId);
		user.setLastUpdate(new Date());
		for(Disease disease : diseases){
			if(user.getDiseases().contains(disease)){
				user.getDiseases().remove(disease);
			}
		}
		update(user);

		if (log.isDebugEnabled()) {
			log.debug("removeDiseasesFromUser(Long, List<Disease>) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#resetCredientials(String, String)
	 */
	@Override
	@Transactional
	public void resetCredientials(String email, final String serverDnsName) {
		if (log.isDebugEnabled()) {
			log.debug("resetCredientials(" + email + ", " + serverDnsName + ") - start"); //$NON-NLS-1$
		}

		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("email must not be null or empty");
		}
		
		User user = userDao.findByEmail(email);
		if (user == null) {
			throw new IllegalArgumentException("user with give email does not exists");
		}
		
		user.setState(UserState.P);
		user.setActivationUid(HashUtil.getRandomString(30));
		
		save(user);
		flush();
		
		mailManager.sendResetCredientialsEmail(user, serverDnsName);

		if (log.isDebugEnabled()) {
			log.debug("resetCredientials(String) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#findUserByActivationUID(java.lang.String)
	 */
	@Override
	public User findUserByActivationUID(String activationCode)
			throws IllegalArgumentException, IllegalStateException {
		if (log.isDebugEnabled()) {
			log.debug("findUserByActivationUID(String) - start"); //$NON-NLS-1$
		}

		if (activationCode == null || activationCode.trim().length() == 0) {
			throw new IllegalArgumentException("email must not be null or empty");
		}
		
		final User user = this.userDao.findByActivationCode(activationCode);
		if (user == null) {
			throw new IllegalStateException("Inactive user does not exists");
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findUserByActivationUID(String) - returns"); //$NON-NLS-1$
		}
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#findUserByLogin(java.lang.String)
	 */
	@Override
	public User findUserByLogin(String login) {
		if (log.isDebugEnabled()) {
			log.debug("findUserByLogin(String) - start"); //$NON-NLS-1$
		}

		User user = userDao.findByLogin(login);
		if (user == null) {
			throw new LoginException(Reason.USER_DOES_NOT_EXISTS);			
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findUserByLogin(String) - returns"); //$NON-NLS-1$
		}
		return user;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#setConnectionRequestsBlocked(java.lang.Long, boolean)
	 */
	@Override
	@Transactional
	public void setConnectionRequestsBlocked(Long userId, boolean blocked)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("setConnectionRequestsBlocked(Long, boolean) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		User user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalStateException("User with id " + userId + " does not exists");
		}
		
		user.setConnectionRequestsBlocked(blocked);
		user.setLastUpdate(new Date());
		userDao.update(user);

		if (log.isDebugEnabled()) {
			log.debug("setConnectionRequestsBlocked(Long, boolean) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#changePassword(java.lang.Long, java.lang.String)
	 */
	@Override
	public void changePassword(Long userId, String plainNewPassword)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("changePassword(Long, String) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		if (plainNewPassword == null || plainNewPassword.trim().length() == 0) {
			throw new IllegalArgumentException("new password must not be null or empty");
		}
		
		if (plainNewPassword.length() < configurationService.getMinPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_SHORT);
		}
		
		if (plainNewPassword.length() > configurationService.getMaxPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_LONG);
		}
		
		String newPassword = HashUtil.getHashedPassword(plainNewPassword);
		User user = userDao.findById(userId);
		user.getUserAccount().setPassword(newPassword);
		userDao.update(user);

		if (log.isDebugEnabled()) {
			log.debug("changePassword(Long, String) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#findUserByEmail(java.lang.String)
	 */
	@Override
	public User findUserByEmail(String email) {
		if (log.isDebugEnabled()) {
			log.debug("findUserByEmail(" + email + ") - start"); //$NON-NLS-1$
		}

		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("email must not be null or empty");
		}
		
		

		User result = userDao.findByEmail(email);
		if (log.isDebugEnabled()) {
			log.debug("findUserByEmail(String) - returns"); //$NON-NLS-1$
		}
		
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#incrementNumberOfLogins(java.lang.Long)
	 */
	@Override
	public void incrementNumberOfLogins(Long userId) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("incrementNumberOfLogins(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		User user = userDao.findById(userId);
		this.userLoginHandler.processSuccessfulLogin(user);

		if (log.isDebugEnabled()) {
			log.debug("incrementNumberOfLogins(Long) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.impl.BusinessServiceImpl#update(java.lang.Object)
	 */
	@Override
	public void update(User entity) {
		if (log.isDebugEnabled()) {
			log.debug("update(" + entity + ") - start"); //$NON-NLS-1$
		}

		entity.setLastUpdate(new Date());
		super.update(entity);

		if (log.isDebugEnabled()) {
			log.debug("update(User) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#updateAboutMe(java.lang.Long, java.lang.String)
	 */
	@Override
	public void updateAboutMe(Long userId, String aboutMe) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("updateAboutMe(Long, String) - start"); //$NON-NLS-1$
		}

		userDao.updateAboutMe(userId, aboutMe);

		if (log.isDebugEnabled()) {
			log.debug("updateAboutMe(Long, String) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#unverifyUser(java.lang.Long)
	 */
	@Override
	public void unverifyUser(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("unverifyUser(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		User user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalStateException("User with id " + userId + " does not exists");
		}
		
		user.setActivationState(UserActivationState.UNVERIFIED);
		getDao().update(user);
		getDao().flush();

		if (log.isDebugEnabled()) {
			log.debug("unverifyUser(Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#verifyUser(java.lang.Long)
	 */
	@Override
	public void verifyUser(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("verifyUser(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		User user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalStateException("User with id " + userId + " does not exists");
		}
		
		user.setActivationState(UserActivationState.VERIFIED);
		getDao().update(user);
		getDao().flush();

		if (log.isDebugEnabled()) {
			log.debug("verifyUser(Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#blockUser(java.lang.Long)
	 */
	@Override
	public void blockUser(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("blockUser(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		User user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalStateException("User with id " + userId + " does not exists");
		}
		
		user.setActivationState(UserActivationState.BLOCKED);
		getDao().update(user);
		getDao().flush();
		
		if (log.isDebugEnabled()) {
			log.debug("blockUser(Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#unblockUser(java.lang.Long)
	 */
	@Override
	public void unblockUser(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("unblockUser(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		
		User user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalStateException("User with id " + userId + " does not exists");
		}
		
		user.setActivationState(UserActivationState.UNVERIFIED);
		getDao().update(user);
		getDao().flush();
		
		if (log.isDebugEnabled()) {
			log.debug("unblockUser(Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#register(Patient, String, String, String, List, String)
	 */
	@Override
	@Transactional
	public Long register(Patient patient, String login, String password, String email, List<Disease> diseases, final String serverDnsName)
			throws IllegalArgumentException, MailException {
		if (log.isDebugEnabled()) {
			log.debug("register(Patient, String, String, String, List<Disease>) - start"); //$NON-NLS-1$
		}

		Long returnLong = register((User) patient, login, password, email, diseases, serverDnsName);
		if (log.isDebugEnabled()) {
			log.debug("register(Patient, String, String, String, List<Disease>) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#register(Doctor, String, String, String, List, String)
	 */
	@Override
	@Transactional
	public Long register(Doctor doctor, String login, String password, String email, List<Disease> diseases, final String serverDnsName)
			throws IllegalArgumentException, MailException {
		if (log.isDebugEnabled()) {
			log.debug("register(Doctor, String, String, String, List<Disease>) - start"); //$NON-NLS-1$
		}

		Long returnLong = register((User) doctor, login, password, email, diseases, serverDnsName);
		if (log.isDebugEnabled()) {
			log.debug("register(Doctor, String, String, String, List<Disease>) - returns"); //$NON-NLS-1$
		}
		return returnLong;
	}
	
	/**
	 * @param user
	 * @param login
	 * @param password
	 * @param diseases
	 * @param email
	 * @return id of a new user
	 */
	private Long register(User user, String login, String password, String email, List<Disease> diseases, final String serverDnsName) {
		if (log.isDebugEnabled()) {
			log.debug("register(User, String, String, String, List<Disease>) - start"); //$NON-NLS-1$
		}

		Long result = null;
		if (user == null) {
			throw new IllegalArgumentException("user must not be null");
		}
		
		if (login == null || login.trim().length() == 0) {
			throw new IllegalArgumentException("login must not be null or empty");
		}
		
		if (login.length() < configurationService.getMinLoginLength()) {
			throw new LoginException(Reason.LOGIN_TOO_SHORT);
		}
		
		if (login.length() > configurationService.getMaxLoginLength()) {
			throw new LoginException(Reason.LOGIN_TOO_LONG);
		}
		
		if (password == null || password.trim().length() == 0) {
			throw new IllegalArgumentException("password must not be null or empty");
		}
		
		if (password.length() < configurationService.getMinPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_SHORT);
		}
		
		if (password.length() > configurationService.getMaxPasswordLength()) {
			throw new LoginException(Reason.PASSWORD_TOO_LONG);
		}
		
		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("email must not be null or empty");
		}
		
		if(userDao.existUserWithEmail(email)){
			throw new UserRegistrationException(
					UserRegistrationException.Reason.EMAIL_ALREADY_EXISTS);
		}
		
		UserAccount userAccount = new UserAccount();
		userAccount.setLogin(login);
		userAccount.setPassword(password);
		userAccount.setCreated(new Date());
		userAccount.setEmail(email);
		
		if (Doctor.class.isInstance(user)
				&& Patient.class.isInstance(user)) {
			throw new IllegalArgumentException("User must be Patient or Doctor");
		}
		
		userAccount.setUser(user);
		user.setState(UserState.R);
		user.setUserAccount(userAccount);
		user.setCreated(new Date());
		user.setLastLogin(new Date());
		user.setActivationUid(HashUtil.getRandomString(30));
		user.setActivationState(UserActivationState.VERIFIED);
		user.setDiseases(diseases);
		if (user.getUserType().equals(UserType.D)) {
			user.setActivationState(UserActivationState.UNVERIFIED);
		}
		
		result = save(user);
		
		if (user.getUserType().equals(UserType.D)) {
			Patient patient = (Patient)userDao.findByLogin(TEST_PATIENT_LOGIN);
			if (patient != null) {
				userConnectionService.invite(user.getId(), patient.getId(), false);
				userConnectionService.acceptInvitation(patient.getId(), user.getId());
			}
		}
		
		mailManager.sendActivationEmail(user, serverDnsName);
		
		flush();
		
		if (log.isDebugEnabled()) {
			log.debug("register(User, String, String, String, List<Disease>) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserService#findUsersByLoginNamePlace(java.lang.String)
	 */
	@Override
	public List<User> findUsersByLoginNamePlace(String filter) {
		return userDao.findUsersByLoginNamePlace(filter);
	}
}
