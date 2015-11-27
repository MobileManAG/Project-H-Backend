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
 * UserConnectionService.java
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import java.util.Collection;
import java.util.List;

import com.mobileman.projecth.business.exception.UserConnectionException;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;

/**
 * @author MobileMan GmbH
 *
 */
public interface UserConnectionService extends EntityService<UserConnection>, SearchService<UserConnection>  {

	/**
	 * Finds all confirmed/accepted connections of a given user - connections
	 * which are in state {@link UserConnectionState#A}
	 * 
	 * @param userId
	 * @return finds all user's confirmed connections
	 * @throws IllegalArgumentException
	 *             if <li>userId == null</li>
	 */
	List<UserConnection> findConfirmedConnections(Long userId);

	/**
	 * Finds all pending connections of a given user - connections which are in
	 * state {@link UserConnectionState#P}
	 * 
	 * @param userId
	 * @return finds all user's pending connections
	 * @throws IllegalArgumentException
	 *             if <li>userId == null</li>
	 */
	List<UserConnection> findPendingConnections(Long userId);
	
	/**
	 * 
	 * @param userId
	 * @param ownerId
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>ownerId == null</li>
	 * @throws IllegalStateException if
	 * <li>connection not exists</li>
	 * <li>userConnection isn't in pending state</li>
	 */
	void acceptInvitation(Long userId, Long ownerId) throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Cancels existing invitation
	 * @param ownerId
	 * @param invitedUserId
	 * @throws IllegalArgumentException if
	 * <li>ownerId == null</li>
	 * <li>invitedUserId == null</li>
	 * @throws IllegalStateException if
	 * <li>accepted connection does not exists</li>
	 */
	void cancelInvitation(Long ownerId, Long invitedUserId)
			throws IllegalArgumentException, IllegalStateException;

	/**
	 * @param ownerId
	 * @param invitedUserId
	 * @return id of a connection
	 * @throws IllegalArgumentException if
	 * <li>ownerId == null</li>
	 * <li>invitedUserId == null</li>
	 * @throws IllegalStateException if
	 * <li>pending or accepted connection already exists</li>
	 * @throws UserConnectionException 
	 * 
	 */
	Long invite(Long ownerId, Long invitedUserId)
			throws IllegalArgumentException, IllegalStateException, UserConnectionException;
	
	/**
	 * @param ownerId
	 * @param invitedUserId
	 * @param checkActivationState 
	 * @return id of a connection
	 * @throws IllegalArgumentException if
	 * <li>ownerId == null</li>
	 * <li>invitedUserId == null</li>
	 * @throws IllegalStateException if
	 * <li>pending or accepted connection already exists</li>
	 * @throws UserConnectionException 
	 * 
	 */
	Long invite(Long ownerId, Long invitedUserId, boolean checkActivationState)
			throws IllegalArgumentException, IllegalStateException, UserConnectionException;
	
	/**
	 * Finds all connections of a given user - connections which are not in
	 * state {@link UserConnectionState#C}
	 * 
	 * @param userId
	 * @return finds all user's pending connections
	 * @throws IllegalArgumentException
	 *             if <li>userId == null</li>
	 */
	List<UserConnection> findAllNotCanceledConnections(Long userId);
	
	/**
	 * @param userId1
	 * @param userId2
	 * @return true if exists accepted connection between given two users
	 */
	boolean existsAcceptedConnection(Long userId1, Long userId2);
	
	/**
	 * @param userId
	 * @param user2Login
	 * @return true if exists not canceled connection between given two users
	 */
	boolean notCanceledConnectionExists(Long userId, String user2Login);
	
	/**
	 * @param filter
	 * @param invitingUserId
	 * @return finds users with given filter which can be invited by given user
	 */
	List<User> findInvitableUsers(String filter, Long invitingUserId);
	
	/**
	 * @param filter
	 * @param invitingUserId
	 * @param includeUserTypes
	 * @return finds users with given filter which can be invited by given user
	 */
	List<User> findInvitableUsers(String filter, Long invitingUserId, Collection<UserType> includeUserTypes);
	
	/**
	 * @param userId
	 * @return true if exists not canceled connection for given user
	 */
	boolean notCanceledConnectionExists(Long userId);
}
