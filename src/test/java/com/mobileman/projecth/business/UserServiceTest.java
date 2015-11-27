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
 * SysuserTest
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 05.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.exception.LoginException;
import com.mobileman.projecth.business.exception.UserConnectionException;
import com.mobileman.projecth.business.exception.UserRegistrationException;
import com.mobileman.projecth.business.security.DecoderService;
import com.mobileman.projecth.business.security.EncoderService;
import com.mobileman.projecth.domain.data.MedicalInstitution;
import com.mobileman.projecth.domain.data.Name;
import com.mobileman.projecth.domain.data.PhoneNumber;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.disease.DiseaseGroup;
import com.mobileman.projecth.domain.disease.DiseaseSubgroup;
import com.mobileman.projecth.domain.doctor.Doctor;
import com.mobileman.projecth.domain.patient.Patient;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserState;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.UserWeight;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;
import com.mobileman.projecth.domain.user.rights.GrantedRight;

/**
 * @author MobileMan GmbH
 *
 */
public class UserServiceTest extends TestCaseBase {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	private UserConnectionService userConnectionService;
	
	@Autowired
	EncoderService encoderService;
	
	@Autowired
	DecoderService decoderService;
	
	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void login() throws Exception {
		
		UserService userService = (UserService)applicationContext.getBean(ComponentNames.USER_SERVICE);
		assertNotNull(userService);
		
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);	
		assertTrue(Doctor.class.isInstance(doctor));
		assertEquals(0, doctor.getUnsuccessfulLoginsCount());
		
		try {
			doctor = userService.login("sysuser3", "zleHeslo");
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.INVALID_CREDENTIALS, e.getReason());
			assertEquals(1, e.getUnsuccessfulLoginsCount());
			assertEquals(1, userService.findById(doctor.getId()).getUnsuccessfulLoginsCount());
		}
		
		try{
			doctor = userService.login("zlyLogin", "54321");
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.USER_DOES_NOT_EXISTS, e.getReason());
		}
		
		assertEquals(0, userService.login("sysuser3", "54321").getUnsuccessfulLoginsCount());
		
		User patient = userService.login("sysuser1", "12345");
		assertNotNull(patient);
		assertTrue(Patient.class.isInstance(patient));
		
		patient = userService.login("sysuser2", "67890");
		assertNotNull(patient);
		
		assertEquals(1, patient.getLoginsCount());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void changePassword() throws Exception {
		
		UserService userService = (UserService)applicationContext.getBean(ComponentNames.USER_SERVICE);
		assertNotNull(userService);
		
		String login = "sysuser1";
		String badPassword = "1234567890";
		String oldPassword = "12345";
		String newPassword = "54321";
		
		User patient = userService.login(login, oldPassword);
		assertNotNull(patient);
		assertTrue(Patient.class.isInstance(patient));
		
		try {
			userService.changePassword(patient.getId(), badPassword, newPassword);
			fail();
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.INVALID_CREDENTIALS, e.getReason());
		}
		
		try {
			userService.changePassword(patient.getId(), oldPassword, "a");
			fail();
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.PASSWORD_TOO_SHORT, e.getReason());
		}
		
		try {
			userService.changePassword(patient.getId(), oldPassword, "aqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
			fail();
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.PASSWORD_TOO_LONG, e.getReason());
		}
		
		userService.changePassword(patient.getId(), oldPassword, newPassword);
		
		patient = userService.login(login, newPassword);
		assertNotNull(patient);
		assertTrue(Patient.class.isInstance(patient));
		
		userService.changePassword(patient.getId(), newPassword, oldPassword);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteUser_NullId() throws Exception {
		userService.deleteUserAccount(null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteUser() throws Exception {
				
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		assertNotNull(doctor.getUserAccount());
		
		List<UserConnection> connections =userConnectionService.findConfirmedConnections(doctor.getId());
		assertEquals(1, connections.size());
		assertEquals(UserConnectionState.A, connections.get(0).getState());
		assertEquals(doctor, connections.get(0).getOwner());
		
		userService.deleteUserAccount(doctor.getId());
		
		doctor = (Doctor)userService.findById(doctor.getId());
		assertNotNull(doctor);
		assertNull(doctor.getUserAccount());
		
		connections =userConnectionService.findConfirmedConnections(doctor.getId());
		assertEquals(0, connections.size());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteUser_AlreadyDeleted() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		assertNotNull(doctor.getUserAccount());
		
		userService.deleteUserAccount(doctor.getId());
		userService.deleteUserAccount(doctor.getId());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void activateUserAccount_NullCode() throws Exception {
		userService.activateUserAccount(null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void activateUserAccount_BlankCode() throws Exception {
		userService.activateUserAccount(" ");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalStateException.class)
	public void activateUserAccount_UserNotExists() throws Exception {
		userService.activateUserAccount("aaaa");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void activateUserAccount() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		String actuid = "aaa";
		doctor.setActivationUid(actuid);
		userService.update(doctor);
		
		doctor = (Doctor)userService.activateUserAccount(actuid);
		assertNotNull(doctor);
		assertNull(doctor.getActivationUid());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected=IllegalStateException.class)
	public void findUserByActivationUID_InactiveUserNotExits() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		userService.findUserByActivationUID("aaa");
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findUserByActivationUID() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		String actuid = "aaa";
		doctor.setActivationUid(actuid);
		doctor.setState(UserState.R);
		userService.update(doctor);
		
		Doctor doctor2 = (Doctor) userService.findUserByActivationUID("aaa");
		assertNotNull(doctor2);		
		assertEquals(doctor, doctor2);
		assertEquals(actuid, doctor2.getActivationUid());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void acceptInvitation() throws Exception {
		
		UserConnectionService userConnectionBo = (UserConnectionService)applicationContext.getBean(ComponentNames.USER_CONNECTION_SERVICE);
		assertNotNull(userConnectionBo);
		
		User owner = userService.login("sysuser1", "12345");
		assertNotNull(owner);
		assertTrue(Patient.class.isInstance(owner));
		
		User user = userService.login("sysuser1", "12345");
		assertNotNull(user);
		assertTrue(Patient.class.isInstance(user));
		
		try {
			userConnectionBo.acceptInvitation(user.getId(), null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Owner id must not be null"));
		}
		
		try {
			userConnectionBo.acceptInvitation(null, owner.getId());
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("User id must not be null"));
		}
		
		try {
			userConnectionBo.acceptInvitation(user.getId(), owner.getId());
			fail();
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().equals("Connection not exists"));
		}
		
		List<UserConnection> connections = userConnectionBo.findAll();
    	int oldSize = connections.size();
    	
    	UserConnection userConnection = new UserConnection();
    	userConnection.setState(UserConnectionState.P);
    	userConnection.setOwner(owner);
    	userConnection.setUser(user);
    	userConnection.setCreated(new Date());
    	Long userConnectionId = userConnectionBo.save(userConnection);
    	assertTrue(!userConnectionId.equals(0L));
    	assertNotNull(userConnectionId);
    	
    	connections = userConnectionBo.findAll();
    	assertTrue(connections.size() == oldSize + 1);
    	
    	userConnectionBo.acceptInvitation(user.getId(), owner.getId());
    	
    	UserConnection connection = userConnectionBo.findById(userConnectionId);
    	assertTrue(connection.getState().equals(UserConnectionState.A));
    	
    	try {
			userConnectionBo.acceptInvitation(user.getId(), owner.getId());
			fail();
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().equals("Connection wrong state, it isn't pending"));
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void register() throws Exception {
		
		String name = "name";
		String login = "login";
		String password = "12345";
		UserType userType = UserType.P;
		String email = "test@gmail.com";
		
		try {
			userService.register((Patient)null, login, password, email, null, "projecth.com");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("user must not be null"));
		}
		
		try {
			userService.register(new Patient(), null, password, email, null, "projecth.com");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("login must not be null or empty"));
		}
		
		try {
			userService.register(new Patient(), login, null, email, null, "projecth.com");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("password must not be null or empty"));
		}
		
		try {
			userService.register(new Patient(), login, password, null, null, "projecth.com");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("email must not be null or empty"));
		}
		
		try {
			userService.register(new Patient(), login, password, "pat1@projecth.com", null, "projecth.com");
			fail();
		} catch (UserRegistrationException e) {
			assertEquals(UserRegistrationException.Reason.EMAIL_ALREADY_EXISTS, e.getReason());
		}
		
		Patient patient = new Patient();
		patient.setName(new Name(name, null));
		Long userId = userService.register(patient, login, password, email, null, "projecth.com");
		flushSession();
		assertTrue(wiser.getMessages().size() == 1);
		wiser.getMessages().clear();
		
		User user = userService.findById(userId);
		assertTrue(user.getName().getName().equals(name));
		assertTrue(user.getUserAccount().getLogin().equals(login));
		assertTrue(user.getUserAccount().getEmail().equals(email));
		assertTrue(user.getUserType().equals(userType));
		
		userService.activateUserAccount(user.getActivationUid());
		
		user = userService.login(login, password);
		assertNotNull(user);
		assertTrue(user.getName().getName().equals(name));
		assertTrue(user.getUserAccount().getLogin().equals(login));
		assertTrue(user.getUserAccount().getEmail().equals(email));
		assertTrue(user.getUserType().equals(userType));
		
		assertTrue(user.getActivationUid() == null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void registerDoctor() throws Exception {
		
		String login = "login";
		String password = "12345";
		UserType userType = UserType.D;
		String email = "jozef.novak@test.com";
		
		Doctor doctor = new Doctor();
		doctor.setName(new Name("Jan", "Novak"));
		MedicalInstitution medicalInstitution = new MedicalInstitution();
		medicalInstitution.setName("MI Berlin");
		medicalInstitution.setPhoneNumber(new PhoneNumber("49", "098765431"));
		medicalInstitution.setFaxNumber(new PhoneNumber("49", "098765432"));
		doctor.setMedicalInstitution(medicalInstitution);
		
		Long userId = userService.register(doctor, login, password, email, null, "projecth.com");
				
		assertTrue(wiser.getMessages().size() == 1);
		wiser.getMessages().clear();
		
		User user = userService.findById(userId);
		
		List<UserConnection> connections = patientService.findAllByDoctor(userId);
		assertEquals(1, connections.size());
		assertEquals(UserConnectionState.A, connections.get(0).getState());
		
		
		userService.activateUserAccount(user.getActivationUid());
		flushSession();
		// unverified ok
		user = userService.login(login, password);
		
		userService.verifyUser(user.getId());
		user = userService.login(login, password);
		user = userService.findById(user.getId());
		assertNotNull(user);
		assertEquals("Jan", user.getName().getName());
		assertEquals("Novak", user.getName().getSurname());
		assertTrue(user.getUserAccount().getLogin().equals(login));
		assertTrue(user.getUserAccount().getEmail().equals(email));
		assertTrue(user.getUserType().equals(userType));
		
		assertTrue(user.getActivationUid() == null);
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void resetCredientials_NullString() throws Exception {
		userService.resetCredientials(null, "projecth.com");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void resetCredientials_BlankString() throws Exception {
		userService.resetCredientials(" ", "projecth.com");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void resetCredientials_UnknownEmail() throws Exception {
		userService.resetCredientials("aaaaa", "projecth.com");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void resetCredientials() throws Exception {
		String name = "name";
		String login = "login";
		String password = "12345";
		String email = "test@gmail.com";
		
		Patient patient = new Patient();
		patient.setName(new Name(name, name));
		Long userId = userService.register(patient, login, password, email, null, "projecth.com");
		
		assertTrue(wiser.getMessages().size() == 1);
		wiser.getMessages().clear();
		
		User user = userService.findById(userId);
		
		user = userService.activateUserAccount(user.getActivationUid());
		assertTrue(user.getState().equals(UserState.A));
		
		userService.resetCredientials(email, "projecth.com");
		assertTrue(wiser.getMessages().size() == 1);
		wiser.getMessages().clear();
		
		try {
			userService.login(login, password);
			fail();
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.USER_IS_NOT_ACTIVE, e.getReason());
		}
		
		user = userService.findById(userId);
		assertTrue(user.getState().equals(UserState.P));
		
		userService.changePassword(userId, null, password);
		
		user = userService.login(login, password);
		assertTrue(user.getState().equals(UserState.A));
		
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addAndRemoveDiseasesToUser() throws Exception {
		
		DiseaseService diseaseService = (DiseaseService)applicationContext.getBean(ComponentNames.DISEASE_SERVICE);
		assertNotNull(diseaseService);
		
		DiseaseGroup diseaseGroup = new DiseaseGroup();
		diseaseGroup.setCode("code");
		diseaseGroup.setName("name");
		
		Long diseaseGroupId = diseaseService.saveGroup(diseaseGroup);
		assertNotNull(diseaseGroupId);
		assertTrue(!diseaseGroupId.equals(0L));
		
		DiseaseSubgroup diseaseSubgroup = new DiseaseSubgroup();
		diseaseSubgroup.setCode("code");
		diseaseSubgroup.setName("name");
		diseaseSubgroup.setDiseaseGroup(diseaseGroup);
		
		Long diseaseSubgroupId = diseaseService.saveSubgroup(diseaseSubgroup);
		assertNotNull(diseaseSubgroupId);
		assertTrue(!diseaseSubgroupId.equals(0L));
		
		Disease disease1 = new Disease();
		disease1.setCode("code1");
		disease1.setName("name1");
		disease1.setImageName("imageName1");
		disease1.setDiseaseSubgroup(diseaseSubgroup);
		
		Disease disease2 = new Disease();
		disease2.setCode("code2");
		disease2.setName("name2");
		disease2.setImageName("imageName2");
		disease2.setDiseaseSubgroup(diseaseSubgroup);
		
		Long diseaseId1 = diseaseService.save(disease1);
		assertNotNull(diseaseId1);
		assertTrue(!diseaseId1.equals(0L));
		
		Long diseaseId2 = diseaseService.save(disease2);
		assertNotNull(diseaseId2);
		assertTrue(!diseaseId2.equals(0L));
		
		List<Disease> diseases = diseaseService.findAll();
		
		String name1 = "name1";
		String name2 = "name2";
		String login1 = "login1";
		String login2 = "login2";
		String password = "12345";
		String email1 = "email1@email.com";
		String email2 = "email2@email.com";
		
		Patient patient1 = new Patient();
		patient1.setName(new Name(name1, null));
		Long userId1 = userService.register(patient1, login1, password, email1, null, "projecth.com");
		User user1 = userService.findById(userId1);
		userService.activateUserAccount(user1.getActivationUid());
		user1 = userService.login(login1, password);
		assertNotNull(user1);
		
		Patient patient2 = new Patient();
		patient2.setName(new Name(name2, null));
		Long userId2 = userService.register(patient2, login2, password, email2, null, "projecth.com");
		User user2 = userService.findById(userId2);
		userService.activateUserAccount(user2.getActivationUid());
		user2 = userService.login(login2, password);
		assertNotNull(user2);
		
		userService.addDiseasesToUser(user1.getId(), diseases);
		user1 = userService.findById(user1.getId());
		assertTrue(user1.getDiseases().size() == diseases.size());
		
		List<Disease> diseases2 = new ArrayList<Disease>();
		Disease disease20 = diseases.get(0);
		diseases2.add(disease20);
		userService.addDiseasesToUser(user2.getId(), diseases2);
		user2 = userService.findById(user2.getId());
		assertTrue(user2.getDiseases().size() == diseases2.size());
		
		int oldSize = user2.getDiseases().size();
		
		userService.removeDiseasesFromUser(user2.getId(), diseases2);
		user2 = userService.findById(user2.getId());
		assertTrue(user2.getDiseases().size() == oldSize - diseases2.size());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void cancelInvitation() throws Exception {
		
		UserConnectionService userConnectionBo = (UserConnectionService)applicationContext.getBean(ComponentNames.USER_CONNECTION_SERVICE);
		assertNotNull(userConnectionBo);
		
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Patient user = (Patient)userService.login("sysuser1", "12345");
		assertNotNull(user);
				
    	int oldSize = userConnectionBo.findAll().size();
    	
    	List<UserConnection> connections = userConnectionBo.findConfirmedConnections(doctor.getId());
    	assertEquals(1, connections.size());
    	assertEquals(UserConnectionState.A, connections.get(0).getState());
    	
    	userConnectionBo.cancelInvitation(doctor.getId(), user.getId());
    	UserConnection connection = userConnectionBo.findById(connections.get(0).getId());
    	assertEquals(UserConnectionState.C, connection.getState());
    	
    	assertEquals(oldSize, userConnectionBo.findAll().size());
    	
    	try {
			userConnectionBo.cancelInvitation(doctor.getId(), user.getId());
			fail();
		} catch (IllegalStateException e) {
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void invite() throws Exception {
		
		UserConnectionService userConnectionBo = (UserConnectionService)applicationContext.getBean(ComponentNames.USER_CONNECTION_SERVICE);
		assertNotNull(userConnectionBo);
		
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Patient user = (Patient)userService.login("sysuser1", "12345");
		assertNotNull(user);
				
    	int oldSize = userConnectionBo.findAll().size();
    	
    	List<UserConnection> connections = userConnectionBo.findConfirmedConnections(doctor.getId());
    	assertEquals(1, connections.size());
    	assertEquals(UserConnectionState.A, connections.get(0).getState());
    	
    	userConnectionBo.cancelInvitation(doctor.getId(), user.getId());
    	UserConnection connection = userConnectionBo.findById(connections.get(0).getId());
    	assertEquals(UserConnectionState.C, connection.getState());
    	
    	
    	Long id = userConnectionBo.invite(doctor.getId(), user.getId());
    	assertNotNull(id);
    	assertEquals(connection.getId(), id);
    	assertEquals(oldSize, userConnectionBo.findAll().size());
    	
    	connection = userConnectionBo.findById(connections.get(0).getId());
    	assertEquals(UserConnectionState.P, connection.getState());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void invitePatientByUnverifiedDoctor() throws Exception {
				
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Patient user = (Patient)userService.login("sysuser1", "12345");
		assertNotNull(user);
		
    	List<UserConnection> connections = userConnectionService.findConfirmedConnections(doctor.getId());
    	assertEquals(1, connections.size());
    	assertEquals(UserConnectionState.A, connections.get(0).getState());
    	
    	userConnectionService.cancelInvitation(doctor.getId(), user.getId());
    	UserConnection connection = userConnectionService.findById(connections.get(0).getId());
    	assertEquals(UserConnectionState.C, connection.getState());
    	
    	userService.unverifyUser(doctor.getId());
    	
    	try {
    		userConnectionService.invite(doctor.getId(), user.getId());
    		fail();
		} catch (UserConnectionException e) {
			assertEquals(UserConnectionException.Reason.INVITING_USER_IS_NOT_VERIFIED, e.getReason());
		}
    	
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void inviteUnverifiedDoctorByPatient() throws Exception {
				
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Patient user = (Patient)userService.login("sysuser1", "12345");
		assertNotNull(user);
		
    	List<UserConnection> connections = userConnectionService.findConfirmedConnections(doctor.getId());
    	assertEquals(1, connections.size());
    	assertEquals(UserConnectionState.A, connections.get(0).getState());
    	
    	userConnectionService.cancelInvitation(doctor.getId(), user.getId());
    	UserConnection connection = userConnectionService.findById(connections.get(0).getId());
    	assertEquals(UserConnectionState.C, connection.getState());
    	
    	userService.unverifyUser(doctor.getId());
    	
    	try {
    		userConnectionService.invite(user.getId(), doctor.getId());
    		fail();
		} catch (UserConnectionException e) {
			assertEquals(UserConnectionException.Reason.INVITED_USER_IS_NOT_VERIFIED, e.getReason());
		}
    	
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void setConnectionRequestsBlocked() throws Exception {
		User doctor = userService.login("sysuser3", "54321");
		assertFalse(doctor.isConnectionRequestsBlocked());
		
		userService.setConnectionRequestsBlocked(doctor.getId(), true);
		
		doctor = userService.login("sysuser3", "54321");
		assertTrue(doctor.isConnectionRequestsBlocked());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void changePassword_NullIdString() throws Exception {
		userService.changePassword(null, "aaaa");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void changePassword_IdNullString() throws Exception {
		userService.changePassword(1L, null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void changePassword_IdEmptyString() throws Exception {
		userService.changePassword(1L, " ");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void changePasswordIdString() throws Exception {
		
		String login = "sysuser1";
		String oldPassword = "12345";
		String newPassword = "54321";
		
		User patient = userService.login(login, oldPassword);
		assertNotNull(patient);
		String oldPasswordHash = patient.getUserAccount().getPassword();
		userService.changePassword(patient.getId(), newPassword);
		
		patient = userService.findById(patient.getId());
		assertFalse(oldPasswordHash.equals(patient.getUserAccount().getPassword()));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findUserByEmail() throws Exception {
		User doctor = userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		assertNull(userService.findUserByEmail("aaa"));
		assertEquals(doctor, userService.findUserByEmail(doctor.getUserAccount().getEmail()));
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void incrementNumberOfLogins() throws Exception {
				
		User user = userService.login("sysuser3", "54321");
		assertNotNull(user);
		
		int oldLoginsCount = user.getLoginsCount();
		Date oldLastLoginDate = user.getLastLogin();
		Thread.sleep(500);
		
		userService.incrementNumberOfLogins(user.getId());
		
		user = userService.findById(user.getId());
		assertEquals(oldLoginsCount + 1, user.getLoginsCount());
		assertFalse(oldLastLoginDate.equals(user.getLastLogin()));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void updateDoctorWeigth() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		List<UserWeight> weights = doctor.getWeights();
		assertTrue(weights.isEmpty());
		
		weights.add(new UserWeight());
		weights.get(0).setDate(dateFormat.parse("1.13.2003"));
		weights.get(0).setWeight(new BigDecimal("99.00"));
		
		userService.update(doctor);
		
		doctor = (Doctor)userService.findById(doctor.getId());
		weights = doctor.getWeights();
		assertFalse(weights.isEmpty());
		assertEquals(dateFormat.parse("1.13.2003"), weights.get(0).getDate());
		assertEquals(new BigDecimal("99.00"), weights.get(0).getWeight());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void updatePatientRights() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		Set<GrantedRight> rights = doctor.getGrantedDataRights();
		assertTrue(rights.isEmpty());
		
		rights.add(GrantedRight.BASE_DATA_ALL);
		rights.add(GrantedRight.BASE_DATA_CONNECTIONS);
				
		userService.update(doctor);
		
		doctor = (Doctor)userService.findById(doctor.getId());
		rights = doctor.getGrantedDataRights();
		assertEquals(2, rights.size());
		assertTrue(rights.contains(GrantedRight.BASE_DATA_CONNECTIONS));
		assertTrue(rights.contains(GrantedRight.BASE_DATA_ALL));
		
		rights.remove(GrantedRight.BASE_DATA_ALL);
		userService.update(doctor);
		
		doctor = (Doctor)userService.findById(doctor.getId());
		rights = doctor.getGrantedDataRights();
		assertEquals(1, rights.size());
		assertFalse(rights.contains(GrantedRight.BASE_DATA_ALL));
		assertTrue(rights.contains(GrantedRight.BASE_DATA_CONNECTIONS));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void updateAboutMe() throws Exception {
		Doctor doctor = (Doctor)userService.login("sysuser3", "54321");
		assertNotNull(doctor);
		
		assertNull(doctor.getAboutMe());
		
		userService.updateAboutMe(doctor.getId(), "AAAAAA");
		doctor = (Doctor)userService.login("sysuser3", "54321");
		assertEquals("AAAAAA", doctor.getAboutMe());
		
		userService.updateAboutMe(doctor.getId(), null);
		doctor = (Doctor)userService.login("sysuser3", "54321");
		assertEquals(null, doctor.getAboutMe());
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void login_activationState() throws Exception {
		
		userService.findAll();
		Doctor doctor = (Doctor)userService.findUserByLogin("sysuser3");
		assertNotNull(doctor);
		
		
		userService.unverifyUser(doctor.getId());
		// unverified ok
		userService.login(doctor.getUserAccount().getLogin(), "54321");
		
		userService.blockUser(doctor.getId());
		
		try {
			userService.login(doctor.getUserAccount().getLogin(), "54321");
			fail();
		} catch (LoginException e) {
			assertEquals(LoginException.Reason.BLOCKED, e.getReason());
		}
		
		userService.unblockUser(doctor.getId());
		userService.login(doctor.getUserAccount().getLogin(), "54321");
		
		userService.verifyUser(doctor.getId());
		doctor = (Doctor)userService.login(doctor.getUserAccount().getLogin(), "54321");
		assertNotNull(doctor);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findUsersByLoginNamePlace() throws Exception {
		
		String filter = "sysuser3";
		List<User> users = userService.findUsersByLoginNamePlace(filter);
		assertEquals(1, users.size());
	}
	
}
