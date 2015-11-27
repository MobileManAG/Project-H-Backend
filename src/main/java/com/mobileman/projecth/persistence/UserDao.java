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
package com.mobileman.projecth.persistence;

import java.util.List;

import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserAccount;

/**
 * Represents DAO for the {@link User} entity.
 * 
 * @author mobileman
 * 
 */
public interface UserDao extends Dao<User> {

	/**
	 * @param login
	 * @return user with login
	 */
	User findByLogin(String login);

	/**
	 * 
	 * @param userAccount
	 * @return id of saved account
	 */
	Long saveAccount(UserAccount userAccount);

	/**
	 * @param activationCode
	 * @return {@link User} with given activation code or <code>null</code> if
	 *         user with given activationCode does not exists
	 * @throws IllegalArgumentException
	 *             if <li>activationCode == null</li> <li>activationCode is
	 *             blank</li>
	 */
	User findByActivationCode(String activationCode) throws IllegalArgumentException;

	/**
	 * 
	 * @param email
	 * @return {@link User} with given email or <code>null</code> if user with
	 *         given email does not exists
	 * @throws IllegalArgumentException
	 *             if <li>email == null</li> <li>email is blank</li>
	 */
	User findByEmail(String email) throws IllegalArgumentException;

	/**
	 * @param account
	 */
	void deleteUserAccount(UserAccount account);

	/**
	 * @param userId
	 * @param aboutMe
	 * @throws IllegalArgumentException
	 *             if <li>userId == null</li>
	 */
	void updateAboutMe(Long userId, String aboutMe) throws IllegalArgumentException;

	/**
	 * @param email
	 * @return true if exists user with given email
	 */
	boolean existUserWithEmail(String email);

	/**
	 * @param filter
	 * @return finds users with given login, place or place
	 */
	List<User> findUsersByLoginNamePlace(String filter);
}
