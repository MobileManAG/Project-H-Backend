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
 * UserConnectionTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 5.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;

/**
 * @author mobileman
 *
 */
public class UserConnectionTest extends TestCaseBase {
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserConnectionService userConnectionService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findConfirmedConnections_NullId() throws Exception {
		userConnectionService.findConfirmedConnections(null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findConfirmedConnections() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserConnection> users =userConnectionService.findConfirmedConnections(doctor.getId());
		assertEquals(1, users.size());
		assertEquals(UserConnectionState.A, users.get(0).getState());
		assertEquals(doctor, users.get(0).getOwner());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findPendingConnections_NullId() throws Exception {
		userConnectionService.findPendingConnections(null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findPendingConnections() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserConnection> users =userConnectionService.findPendingConnections(doctor.getId());
		assertEquals(1, users.size());
		assertEquals(UserConnectionState.P, users.get(0).getState());
		assertEquals(doctor, users.get(0).getOwner());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void notCanceledConnectionExists() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserConnection> users =userConnectionService.findPendingConnections(doctor.getId());
		assertEquals(1, users.size());
		assertEquals(UserConnectionState.P, users.get(0).getState());		
		assertEquals(doctor, users.get(0).getOwner());
		
		assertTrue(userConnectionService.notCanceledConnectionExists(doctor.getId(), users.get(0).getUser().getUserAccount().getLogin()));
		
		assertTrue(userConnectionService.notCanceledConnectionExists(doctor.getId()));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAllNotCanceledConnections() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserConnection> users =userConnectionService.findAllNotCanceledConnections(doctor.getId());
		assertEquals(2, users.size());
		assertEquals(UserConnectionState.P, users.get(0).getState());
		assertEquals(doctor, users.get(0).getOwner());
		assertEquals(UserConnectionState.A, users.get(1).getState());
		assertEquals(doctor, users.get(1).getOwner());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void existsAcceptedConnection() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserConnection> users =userConnectionService.findAllNotCanceledConnections(doctor.getId());
		assertEquals(2, users.size());
		assertEquals(UserConnectionState.P, users.get(0).getState());
		assertEquals(UserConnectionState.A, users.get(1).getState());
		
		assertFalse(userConnectionService.existsAcceptedConnection(doctor.getId(), users.get(0).getUser().getId()));
		assertTrue(userConnectionService.existsAcceptedConnection(doctor.getId(), users.get(1).getUser().getId()));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findInvitableUsersUserType() throws Exception {
		User doctor = userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		User user = userService.findUserByLogin("sysuser1");
		assertNotNull(user);
		
		userConnectionService.cancelInvitation(doctor.getId(), user.getId());
		flushSession();
		
		String filter = user.getUserAccount().getLogin();
		List<User> users = userConnectionService.findInvitableUsers(filter, doctor.getId(), Arrays.asList(UserType.P));
		assertEquals(1, users.size());
		
		users = userConnectionService.findInvitableUsers(filter, doctor.getId(), Arrays.asList(UserType.D));
		assertEquals(0, users.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findInvitableUsers() throws Exception {
		User doctor = userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		User user = userService.findUserByLogin("sysuser1");
		assertNotNull(user);
		
		userConnectionService.cancelInvitation(doctor.getId(), user.getId());
		flushSession();
		
		String filter = user.getUserAccount().getLogin();
		List<User> users = userConnectionService.findInvitableUsers(filter, doctor.getId());
		assertEquals(1, users.size());
		
		users = userConnectionService.findInvitableUsers("sysuser3", doctor.getId());
		assertEquals(0, users.size());
	}
}
