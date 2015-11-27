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
 * UserConnectionServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.user.connection.impl;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.UserConnectionService;
import com.mobileman.projecth.business.exception.UserConnectionException;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserActivationState;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;
import com.mobileman.projecth.persistence.UserConnectionDao;
import com.mobileman.projecth.persistence.UserDao;

/**
 * @author MobileMan GmbH
 *
 */
@Service(ComponentNames.USER_CONNECTION_SERVICE)
public class UserConnectionServiceImpl extends BusinessServiceImpl<UserConnection> implements UserConnectionService {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(UserConnectionServiceImpl.class);

	private UserConnectionDao userConnectionDao;

	@Autowired
	private UserDao userDao;
	
	/**
	 * @param userConnectionDao
	 */
	@Autowired
	public void setUserConnectionDao(UserConnectionDao userConnectionDao) {
		if (log.isDebugEnabled()) {
			log.debug("setUserConnectionDao(UserConnectionDao) - start"); //$NON-NLS-1$
		}

		this.userConnectionDao = userConnectionDao;
		setDao(userConnectionDao);

		if (log.isDebugEnabled()) {
			log.debug("setUserConnectionDao(UserConnectionDao) - returns"); //$NON-NLS-1$
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#findConfirmedConnections(java.lang.Long)
	 */
	@Override
	public List<UserConnection> findConfirmedConnections(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("findConfirmedConnections(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		List<UserConnection> result = userConnectionDao.findConnections(userId, UserConnectionState.A);

		if (log.isDebugEnabled()) {
			log.debug("findConfirmedConnections(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#findPendingConnections(java.lang.Long)
	 */
	@Override
	public List<UserConnection> findPendingConnections(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("findPendingConnections(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		List<UserConnection> result = userConnectionDao.findConnections(userId, UserConnectionState.P);

		if (log.isDebugEnabled()) {
			log.debug("findPendingConnections(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#acceptInvitation(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void acceptInvitation(Long userId, Long ownerId)
			throws IllegalArgumentException, IllegalStateException {
		if (log.isDebugEnabled()) {
			log.debug("acceptInvitation(Long, Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner id must not be null");
		}
		
		UserConnection connection = userConnectionDao.findConnection(userId, ownerId);
		if(connection == null){
			throw new IllegalStateException("Connection not exists");
		}
		
		if(!connection.getState().equals(UserConnectionState.P)){
			throw new IllegalStateException("Connection wrong state, it isn't pending");
		}
		
		connection.setState(UserConnectionState.A);
		userConnectionDao.update(connection);

		if (log.isDebugEnabled()) {
			log.debug("acceptInvitation(Long, Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#findAllNotCanceledConnections(java.lang.Long)
	 */
	@Override
	public List<UserConnection> findAllNotCanceledConnections(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("findAllNotCanceledConnections(Long) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		List<UserConnection> result = userConnectionDao.findAllNotCanceledConnections(userId);
				

		if (log.isDebugEnabled()) {
			log.debug("findAllNotCanceledConnections(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#cancelInvitation(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void cancelInvitation(Long ownerId, Long invitedUserId)
			throws IllegalArgumentException, IllegalStateException {
		if (log.isDebugEnabled()) {
			log.debug("cancelInvitation(Long, Long) - start"); //$NON-NLS-1$
		}

		if (ownerId == null) {
			throw new IllegalArgumentException("Owner id must not be null");
		}
		if (invitedUserId == null) {
			throw new IllegalArgumentException("Invited user id must not be null");
		}
		
		UserConnection connection = userConnectionDao.findConnection(invitedUserId, ownerId);
		if(connection == null){
			throw new IllegalStateException("Connection does not exists");
		}
		
		switch (connection.getState()) {
		case C:
			throw new IllegalStateException("Connection is in wrong state, it isn't pending or accepted");
		default:
			break;
		}
		
		connection.setState(UserConnectionState.C);
		userConnectionDao.update(connection);

		if (log.isDebugEnabled()) {
			log.debug("cancelInvitation(Long, Long) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#invite(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long invite(Long ownerId, Long invitedUserId)
			throws IllegalArgumentException, IllegalStateException {
		return invite(ownerId, invitedUserId, true);
	}

	/**
	 * @param owner
	 * @param invitedUser
	 */
	private void checkActivationState(User owner, User invitedUser) throws UserConnectionException {
		if (log.isDebugEnabled()) {
			log.debug("checkActivationState(" + owner + ", " + invitedUser + ") - start"); //$NON-NLS-1$
		}

		if (owner.getUserType().equals(UserType.D)) {
			if (!owner.getActivationState().equals(UserActivationState.VERIFIED)) {
				throw new UserConnectionException(
						UserConnectionException.Reason.INVITING_USER_IS_NOT_VERIFIED);
			}
		}
		
		if (invitedUser.getUserType().equals(UserType.D)) {
			if (!invitedUser.getActivationState().equals(UserActivationState.VERIFIED)) {
				throw new UserConnectionException(
						UserConnectionException.Reason.INVITED_USER_IS_NOT_VERIFIED);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("checkActivationState(User, User) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#existsAcceptedConnection(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean existsAcceptedConnection(Long userId1, Long userId2) {
		if (log.isDebugEnabled()) {
			log.debug("existsAcceptedConnection(Long, Long) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = userConnectionDao.existsAcceptedConnection(userId1, userId2);
		if (log.isDebugEnabled()) {
			log.debug("existsAcceptedConnection(Long, Long) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#notCanceledConnectionExists(java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean notCanceledConnectionExists(Long userId, String user2Login) {
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(Long, String) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		if (user2Login == null) {
			throw new IllegalArgumentException("User 2 login must not be null");
		}
		

		boolean returnboolean = userConnectionDao.notCanceledConnectionExists(userId, user2Login);
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(Long, String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#invite(java.lang.Long, java.lang.Long, boolean)
	 */
	@Override
	public Long invite(Long ownerId, Long invitedUserId, boolean checkActivationState) throws IllegalArgumentException,
			IllegalStateException, UserConnectionException {
		if (log.isDebugEnabled()) {
			log.debug("invite(" + ownerId + ", " + invitedUserId + ", " + checkActivationState + ") - start"); //$NON-NLS-1$
		}

		if (ownerId == null) {
			throw new IllegalArgumentException("Owner id must not be null");
		}
		if (invitedUserId == null) {
			throw new IllegalArgumentException("Invited user id must not be null");
		}
		
		User owner = userDao.findById(ownerId);
		User invitedUser = userDao.findById(invitedUserId);
		if (owner.getUserAccount() == null) {
			throw new UserConnectionException(
					UserConnectionException.Reason.INVITING_USER_IS_DELETED);
		}
		
		if (invitedUser.getUserAccount() == null) {
			throw new UserConnectionException(
					UserConnectionException.Reason.INVITED_USER_IS_DELETED);
		}
		
		final Long connectionId;
		UserConnection connection = userConnectionDao.findConnection(invitedUserId, ownerId);
		if(connection == null){
			
			if (checkActivationState) {
				checkActivationState(owner, invitedUser);
			}
			
			connection = new UserConnection();
			connection.setState(UserConnectionState.P);
			connection.setCreated(new Date());			
			connection.setOwner(owner);
			connection.setUser(invitedUser);
			userConnectionDao.save(connection);
			connectionId = connection.getId();
			
		} else {
			if (checkActivationState) {
				checkActivationState(owner, invitedUser);
			}			
			
			if(!connection.getState().equals(UserConnectionState.C)){
				throw new UserConnectionException(
						UserConnectionException.Reason.CONNECTION_IS_NOT_CANCELED);
			}
			
			connection.setState(UserConnectionState.P);
			connection.setOwner(owner);
			connection.setUser(invitedUser);
			userConnectionDao.update(connection);
			connectionId = connection.getId();
		}
		

		if (log.isDebugEnabled()) {
			log.debug("invite(Long, Long, boolean) - returns"); //$NON-NLS-1$
		}
		return connectionId;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#findInvitableUsers(String, Long, Collection)
	 */
	@Override
	public List<User> findInvitableUsers(String filter, Long invitingUserId, Collection<UserType> includeUserTypes) {		
		return userConnectionDao.findInvitableUsers(filter, invitingUserId, includeUserTypes);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#findInvitableUsers(java.lang.String, java.lang.Long)
	 */
	@Override
	public List<User> findInvitableUsers(String filter, Long invitingUserId) {
		return userConnectionDao.findInvitableUsers(filter, invitingUserId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.UserConnectionService#notCanceledConnectionExists(java.lang.Long)
	 */
	@Override
	public boolean notCanceledConnectionExists(Long userId) {
		return userConnectionDao.notCanceledConnectionExists(userId);
	}	
}
