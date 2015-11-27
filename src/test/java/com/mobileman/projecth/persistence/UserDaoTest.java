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
 * UserDaoTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 21.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.domain.admin.Admin;
import com.mobileman.projecth.domain.user.User;

/**
 * @author mobileman
 *
 */
public class UserDaoTest extends TestCaseBase {
	
	@Autowired
	UserDao userDao;

	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void saveAdmin() throws Exception {
		
		Admin admin = (Admin) userDao.findByLogin("admin");
		assertNotNull(admin);
		assertNotNull(admin.getUserAccount());
		assertNotNull(admin.getUserAccount().getUser());
		
		userDao.save(admin);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void deleteAdminAccount() throws Exception {
		
		Admin admin = (Admin) userDao.findByLogin("admin");
		assertNotNull(admin);
		assertNotNull(admin.getUserAccount());
		
		userDao.deleteUserAccount(admin.getUserAccount());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findPagination() throws Exception {
		
		List<User> users = userDao.findAll(0, 0);
		assertEquals(0, users.size());
		
		users = userDao.findAll(0, 1);
		assertEquals(1, users.size());
		
		List<User> users2 = userDao.findAll(1, 1);
		assertEquals(1, users2.size());
		assertFalse(users2.get(0).equals(users.get(0)));
	}
}
