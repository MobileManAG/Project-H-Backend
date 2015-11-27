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
 * UserConnection
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.persistence;

import java.util.Collection;
import java.util.List;

import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;

/**
 * Represents DAO for the {@link UserConnection} entity.
 * 
 * @author MobileMan GmbH
 * 
 */
public interface UserConnectionDao extends Dao<UserConnection> {

	/**
	 * @param userId
	 * @param connectionState
	 * @return list of given user's connections in given state
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>connectionState == null</li>
	 */
	List<UserConnection> findConnections(Long userId,
			UserConnectionState connectionState)
			throws IllegalArgumentException;

	/**
	 * @param userId
	 * @param ownerId
	 * @return connection by user and owner
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>ownerId == null</li>
	 */
	UserConnection findConnection(Long userId, Long ownerId) throws IllegalArgumentException;

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
	 * @return true if exists accepted conenction between given two users
	 */
	boolean existsAcceptedConnection(Long userId1, Long userId2);

	/**
	 * @param userId
	 * @param user2Login
	 * @return true if exists not canceled connection between given two users
	 */
	boolean notCanceledConnectionExists(Long userId, String user2Login);

	/**
	 * Deletes all user's connections
	 * @param user 
	 * @throws IllegalArgumentException
	 *             if <li>user == null</li>
	 *             <li>user.id == null</li>
	 */
	void deletaAllConnections(User user) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param doctorId
	 * @return list of a UserConnections
	 */
	List<UserConnection> findAllByDoctor(Long doctorId);
	
	/**
	 * @param filter
	 * @param invitingUserId
	 * @return finds users with given filter which can be invited by given user
	 */
	List<User> findInvitableUsers(String filter, Long invitingUserId);
	
	/**
	 * @param filter
	 * @param invitintUserId
	 * @param includeUserTypes
	 * @return finds users with given filter which can be invited by given user
	 */
	List<User> findInvitableUsers(String filter, Long invitintUserId, Collection<UserType> includeUserTypes);
	
	/**
	 * @param userId
	 * @return true if exists not canceled connection for given user
	 */
	boolean notCanceledConnectionExists(Long userId);
}
