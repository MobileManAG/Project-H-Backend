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
 * TestCaseBase.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.subethamail.wiser.Wiser;

/**
 * @author mobileman
 *
 */
@ContextConfiguration(locations = {"/spring/config/BeanLocations.xml"})
@TransactionConfiguration(defaultRollback=true)
public class TestCaseBase extends AbstractTransactionalJUnit4SpringContextTests {

	private SessionFactory sessionFactory; 
	private Session session; 
	boolean sessionOwner;
	protected Wiser wiser;
		
	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {		
		sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		try {
			session = SessionFactoryUtils.getSession(sessionFactory, false);
			if (session == null) {
				sessionOwner = true;
				session = SessionFactoryUtils.getSession(sessionFactory, true);			
				TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
			} else {
				sessionOwner = false;
			}
		} catch (IllegalStateException e) {
		}
		
		if (wiser == null) {
			wiser = new Wiser();
			wiser.setPort(2525);
			wiser.start();
			@SuppressWarnings("unchecked")
			Map<String, JavaMailSenderImpl> ofType = applicationContext.getBeansOfType(org.springframework.mail.javamail.JavaMailSenderImpl.class);
			  for (Entry<String, JavaMailSenderImpl> bean : ofType.entrySet()) {
				JavaMailSenderImpl mailSender = bean.getValue();
				mailSender.setPort(2525);
				mailSender.setHost("localhost");
				mailSender.setUsername(null);
				mailSender.setPassword(null);
				Properties props = new Properties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.host", "localhost");
				props.put("mail.smtp.auth", "false");
				mailSender.setJavaMailProperties(props);
			  }
		}
		
		if (wiser.getMessages() != null) {
			wiser.getMessages().clear();
		}
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {		
		if (sessionOwner && session != null) {
			SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
			Session s = holder.getSession();
			s.flush();
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.closeSession(s);
		}	
		
		if (wiser != null) {
			wiser.stop();
		}
	}
	
	protected void flushSession() {
		if (session != null) {
			session.flush();
		}
	}
	
	/**
	 * @return sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
}
