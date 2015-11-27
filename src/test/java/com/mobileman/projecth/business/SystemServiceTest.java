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
 * SystemServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 22.12.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.subethamail.wiser.WiserMessage;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.domain.user.UserType;

/**
 * @author mobileman
 *
 */
public class SystemServiceTest extends TestCaseBase {

	@Autowired
	private SystemService systemService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void requestNewDiseaseGroup() throws Exception {
		
		systemService.requestNewDiseaseGroup("newDisease", "sender1", UserType.D);
		
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(new InternetAddress("sender1"), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("mitglied@projecth.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Neue Gesundheitsgruppe anmelden: newDisease", messages.get(0).getMimeMessage().getSubject());
		
		assertEquals(new InternetAddress("gesundheitsgruppen@projecth.com"), messages.get(1).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("sender1"), messages.get(1).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Anfrage zur Erweiterung von projecth mit newDisease", messages.get(1).getMimeMessage().getSubject());
	}
}
