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
package com.mobileman.projecth.business;

import java.util.List;

import org.springframework.mail.MailException;

import com.mobileman.projecth.business.exception.LoginException;
import com.mobileman.projecth.business.exception.UserRegistrationException;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserState;

/**
 * 
 * @author MobileMan
 *
 */
public interface UserService extends EntityService<User>, SearchService<User> {
	
	/**
	 * @param entity
	 * @return id of added User
	 */
	@Override
	Long save(User entity);
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @return User object in case of successful login
	 * @throws IllegalStateException if
	 * <li>user state is not <code>UserState.A</code> (ACTIVE)</li>
	 * @throws LoginException 
	 */
	User login(String login, String password) throws IllegalStateException, LoginException;
	
	/**
	 * Changes user password 
	 * @param userId id of the user whose password will be changed
	 * @param oldPassword
	 * @param newPassword
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>in case of invalid old password</li>
	 * @throws LoginException 
	 */
	void changePassword(Long userId, String oldPassword, String newPassword)  throws IllegalArgumentException, LoginException;
	
	/**
	 * Deletes the user account (but leaves user data unchanged)
	 * @param userId id of the user whose account will be deleted
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>user account is already deleted</li>
	 */
	void deleteUserAccount(Long userId) throws IllegalArgumentException;
	
	/**
	 * @param activationCode the activation code - part of the activation url link sent to the email of a user who made sign-up)
	 * @return user object after successful activation
	 * @throws IllegalArgumentException if
	 * <li>activationCode == null</li>
	 * <li>activationCode is blank</li>
	 * 
	 * @throws IllegalStateException if
	 * <li>inactive user does not exists</li>
	 */
	User activateUserAccount(String activationCode)
			throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Creates assciation of an user to disease.
	 * @param userId
	 * @param diseases - collection of diseases
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>diseases == null</li>
	 * <li>diseases is empty</li>
	 * <li>diseases contains null element</li>
	 */
	void addDiseasesToUser(Long userId, List<Disease> diseases);
	
	/**
	 * Removes assciation of an user from disease
	 * @param userId
	 * @param diseases - collection of diseases
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>diseases == null</li>
	 * <li>diseases is empty</li>
	 * <li>diseases contains null element</li>
	 */
	void removeDiseasesFromUser(Long userId, List<Disease> diseases);
	
	/**
	 * User account credential resend - in case of forgoten credentials
	 * @param email
	 * @param serverDnsName dns name of server from which user is registered
	 * @throws IllegalArgumentException if
	 * <li>email == null</li>
	 * <li>email is blank</li>
	 * @throws IllegalStateException if
	 * <li>no user account is associated with the email address</li>
	 * @throws MailException if
	 * <li>reset credentials email sending failed</li>
	 */
	void resetCredientials(String email, final String serverDnsName) throws IllegalArgumentException, IllegalStateException, MailException;
	
	/**
	 * Returns user with given activationCode if any exists
	 * @param activationCode
	 * @return user with given activationCode if any exists
	 * @throws IllegalArgumentException if
	 * <li>activationCode == null</li>
	 * <li>activationCode is blank</li>
	 * @throws IllegalStateException if
	 */
	User findUserByActivationUID(String activationCode) throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * @param login
	 * @return User with given login
	 * @throws LoginException 
	 */
	User findUserByLogin(String login) throws LoginException;
	
	/**
	 * @param email
	 * @return User with given email
	 * @throws IllegalArgumentException if
	 * <li>email == null or email is empty string</li>
	 */
	User findUserByEmail(String email) throws IllegalArgumentException;
	
	/**
	 * Enables/disables invitation blocking mechanism for a given user
	 * @param userId 
	 * @param blocked if true user will have blocked invitation requests sended by other users
	 * @throws IllegalArgumentException if
	 * 	<li>userId == null</li>
	 */
	void setConnectionRequestsBlocked(Long userId, boolean blocked) throws IllegalArgumentException;
	
	/**
	 * Changes user password 
	 * @param userId id of the user whose password will be changed
	 * @param newPassword
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>newPassword == null</li>
	 */
	void changePassword(Long userId, String newPassword)  throws IllegalArgumentException;
	
	/**
	 * Increments user's logins count attribute value 
	 * @param userId id of the user whose logins count attribute value will be incremented
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li>
	 * <li>User with userId does not exists</li>
	 */
	void incrementNumberOfLogins(Long userId)  throws IllegalArgumentException;
	
	/**
	 * @param userId
	 * @param aboutMe
	 * @throws IllegalArgumentException if
	 * <li>userId == null</li> 
	 */
	void updateAboutMe(Long userId, String aboutMe) throws IllegalArgumentException;
	
	/**
	 * Changes user's activation state to UNVERIFIED - {@link UserState#U}
	 * @param id user's id
	 */
	void unverifyUser(Long id);
	
	/**
	 * Changes user's activation state to VERIFIED - {@link UserState#V}
	 * @param id user's id
	 */
	void verifyUser(Long id);
	
	/**
	 * Changes user's activation state to BLOCKED - {@link UserState#B}
	 * @param id user's id
	 */
	void blockUser(Long id);
	
	/**
	 * Changes user's activation state to UNVERIFIED - {@link UserState#U}
	 * @param id user's id
	 */
	void unblockUser(Long id);
	
	/**
	 * Register of an Patient in the system 
	 * @param patient
	 * @param login
	 * @param password
	 * @param email 
	 * @param diseases
	 * @param serverDnsName dns name of server from which user is registered
	 * 
	 * @return id of saved patient
	 * @throws IllegalArgumentException if
	 * <li>name == null</li>
	 * <li>name is blank</li>
	 * <li>login == null</li>
	 * <li>login is blank</li>
	 * <li>password == null</li>
	 * <li>password is blank</li>
	 * <li>userType == null</li>
	 * <li>email == null</li>
	 * <li>email is blank</li>
	 * <li>user with given email already exists</li>
	 * <li>diseases == null</li>
	 * <li>diseases is empty</li>
	 * <li>diseases contains null element</li>
	 * @throws MailException if
	 * <li>activation email sending failed</li>
	 * @throws UserRegistrationException 
	 */
	Long register(Patient patient, String login, String password, String email, List<Disease> diseases, final String serverDnsName)
			throws IllegalArgumentException, MailException, UserRegistrationException;
	
	/**
	 * Register of an Patient in the system 
	 * @param doctor
	 * @param login
	 * @param password
	 * @param email 
	 * @param diseases
	 * @param serverDnsName dns name of server from which user is registered
	 * 
	 * @return id of saved doctor
	 * @throws IllegalArgumentException if
	 * <li>name == null</li>
	 * <li>name is blank</li>
	 * <li>login == null</li>
	 * <li>login is blank</li>
	 * <li>password == null</li>
	 * <li>password is blank</li>
	 * <li>userType == null</li>
	 * <li>email == null</li>
	 * <li>email is blank</li>
	 * <li>user with given email already exists</li>
	 * <li>diseases == null</li>
	 * <li>diseases is empty</li>
	 * <li>diseases contains null element</li>
	 * @throws MailException if
	 * <li>activation email sending failed</li>
	 * @throws UserRegistrationException 
	 */
	Long register(Doctor doctor, String login, String password, String email, List<Disease> diseases, final String serverDnsName)
			throws IllegalArgumentException, MailException, UserRegistrationException;
	
	/**
	 * @param filter
	 * @return finds users with given login, place or place
	 */
	List<User> findUsersByLoginNamePlace(String filter);

}
