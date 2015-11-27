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
 * MailManagerTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 18.12.2010
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
public class MailManagerTest extends TestCaseBase {

	@Autowired
	private MailManager mailManager;
	
	
	/**
	 * @throws Exception
	 */
	@Test
	public void sendMessageToAdmin() throws Exception {
		
		mailManager.sendMessageToAdmin("aaa@gmail.com", "subject1", "body");
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(new InternetAddress("aaa@gmail.com"), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("mitglied@projecth.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("subject1", messages.get(0).getMimeMessage().getSubject());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void sendMessageToAdmin_NullFrom() throws Exception {
		
		mailManager.sendMessageToAdmin(null, "subject1", "body");
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(new InternetAddress("projecth@projecth.com"), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("mitglied@projecth.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("subject1", messages.get(0).getMimeMessage().getSubject());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void sendMessageToAdmin_EmptyFrom() throws Exception {
		
		mailManager.sendMessageToAdmin(" ", "subject1", "body");
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(new InternetAddress("projecth@projecth.com"), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("mitglied@projecth.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("subject1", messages.get(0).getMimeMessage().getSubject());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendTellAFriendMessage() throws Exception {
		String sender = "mitglied@projecth.com";
		mailManager.sendTellAFriendMessage("Jan Novak", "test@test.com", "receiver1@test.com,receiver2@test.com", "body");
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(3, messages.size());
		assertEquals(new InternetAddress(sender), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("receiver1@test.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Mitteilung von projecth®", messages.get(0).getMimeMessage().getSubject());
		
		assertEquals(new InternetAddress(sender), messages.get(1).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("receiver2@test.com"), messages.get(1).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Mitteilung von projecth®", messages.get(1).getMimeMessage().getSubject());
		
		assertEquals(new InternetAddress(sender), messages.get(2).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("test@test.com"), messages.get(2).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Mitteilung von projecth®", messages.get(2).getMimeMessage().getSubject());
		
		mailManager.sendTellAFriendMessage(null, "test@test.com", "receiver1@test.com,receiver2@test.com", "body");
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendNewDiseaseGroupRequestEmail() throws Exception {
		
		mailManager.sendNewDiseaseGroupRequestEmail("disease1", "sender@test.com", UserType.D);
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(2, messages.size());
		
		assertEquals(new InternetAddress("sender@test.com"), messages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("mitglied@projecth.com"), messages.get(0).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Neue Gesundheitsgruppe anmelden: disease1", messages.get(0).getMimeMessage().getSubject());
		
		assertEquals(new InternetAddress("gesundheitsgruppen@projecth.com"), messages.get(1).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("sender@test.com"), messages.get(1).getMimeMessage().getRecipients(Message.RecipientType.TO)[0]);
		assertEquals("Anfrage zur Erweiterung von projecth mit disease1", messages.get(1).getMimeMessage().getSubject());
		
	}
}
