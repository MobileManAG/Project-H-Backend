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
 * MessageServiceTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 24.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.messages;

import static org.junit.Assert.*;

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.subethamail.wiser.WiserMessage;

import com.mobileman.projecth.TestCaseBase;
import com.mobileman.projecth.business.PatientService;
import com.mobileman.projecth.business.UserService;
import com.mobileman.projecth.business.exception.MessageException;
import com.mobileman.projecth.business.user.message.MessageService;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.message.Message;
import com.mobileman.projecth.domain.user.message.MessageGroupType;

/**
 * @author mobileman
 *
 */
public class MessageServiceTest extends TestCaseBase {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PatientService patientService;

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAll() throws Exception {
		assertEquals(3, messageService.findAll().size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void markAsSpam_NullId() throws Exception {
		messageService.markAsSpam(null, true);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void markAsSpam() throws Exception {
		Message message = messageService.findAll().get(0);
		assertFalse(message.isSpam());
		
		List<WiserMessage> mailMessages = wiser.getMessages();
		assertEquals(0, mailMessages.size());
		
		messageService.markAsSpam(message.getId(), true);
		flushSession();
		
		message = messageService.findById(message.getId());
		assertTrue(message.isSpam());
		
		mailMessages = wiser.getMessages();
		assertEquals(1, mailMessages.size());
		assertEquals(new InternetAddress("doc1@projecth.com"), mailMessages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("spam@projecth.com"), mailMessages.get(0).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(0).getMimeMessage().getSubject());
	}
	
	/**
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void markAsRead_NullId() throws Exception {
		messageService.markAsRead(null, true);
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void markAsRead() throws Exception {
		Message message = messageService.findAll().get(0);
		assertFalse(message.isRead());
		
		messageService.markAsRead(message.getId(), true);
		flushSession();
		
		message = messageService.findAll().get(0);
		assertTrue(message.isRead());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void deleteBySender() throws Exception {
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		
		List<Message> messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX);
		assertEquals(2, messages.size());
		Message message = messages.get(0);
		
		messageService.deleteMessage(pat1.getId(), message.getId());
		
		messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX);
		assertEquals(1, messages.size());
		assertFalse(message.equals(messages.get(0)));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void deleteByReceiver() throws Exception {
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		
		List<Message> messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX);
		assertEquals(2, messages.size());
		Message message = messages.get(0);
		
		messageService.deleteMessage(message.getReceiver().getId(), message.getId());
		
		messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX);
		assertEquals(2, messages.size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void delete() throws Exception {
		Message message = messageService.findAll().get(1);
		
		List<Message> messagesOld = messageService.findAll();
		
		try {
			messageService.deleteMessage(message.getId());
			fail();
		} catch (MessageException e) {
			assertEquals(MessageException.Reason.MESSAGE_IS_USED_IN_REPLY, e.getReason());
		}
		
		message = messageService.findAll().get(0);
		messageService.deleteMessage(message.getId());
		
		assertEquals(messagesOld.size() - 1, messageService.findAll().size());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendMessage() throws Exception {
		
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		User pat2 = userService.findUserByLogin("sysuser2");
		assertNotNull(pat2);
		
		List<Message> messagesOld = messageService.findAll();
		
		messageService.sendMessage(pat1.getId(), pat2.getId(), "Hallo pat2", "How are you?", false);
		
		List<Message> messages = messageService.findAll();
		assertEquals(messagesOld.size() + 1, messages.size());
		Message message = messages.get(messages.size() - 1);
		assertEquals(pat1.getId(), message.getSender().getId());
		assertEquals(pat2.getId(), message.getReceiver().getId());
		
		List<WiserMessage> mailMessages = wiser.getMessages();
		assertEquals(1, mailMessages.size());
		assertEquals(new InternetAddress("nachrichten@projecth.com"), mailMessages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("doc1@projecth.com"), mailMessages.get(0).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(0).getMimeMessage().getSubject());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendMessageWithCopy() throws Exception {
		
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		User pat2 = userService.findUserByLogin("sysuser2");
		assertNotNull(pat2);
		
		List<Message> messagesOld = messageService.findAll();
		
		messageService.sendMessage(pat1.getId(), pat2.getId(), "Hallo pat2", "How are you?", true);
		
		List<Message> messages = messageService.findAll();
		assertEquals(messagesOld.size() + 1, messages.size());
		Message message = messages.get(messages.size() - 1);
		assertEquals(pat1.getId(), message.getSender().getId());
		assertEquals(pat2.getId(), message.getReceiver().getId());
		
		List<WiserMessage> mailMessages = wiser.getMessages();
		assertEquals(2, mailMessages.size());
		assertEquals(new InternetAddress("nachrichten@projecth.com"), mailMessages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("doc1@projecth.com"), mailMessages.get(0).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(0).getMimeMessage().getSubject());
		
		Object mp = MimeMultipart.class.cast(mailMessages.get(0).getMimeMessage().getContent()).getBodyPart(0).getContent();
		
		assertEquals(new InternetAddress("nachrichten@projecth.com"), mailMessages.get(1).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("pat1@projecth.com"), mailMessages.get(1).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(1).getMimeMessage().getSubject());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void replyToMessage() throws Exception {
		
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		User pat2 = userService.findUserByLogin("sysuser2");
		assertNotNull(pat2);
		
		List<Message> messagesOld = messageService.findAll();
		Message replyToMessage = messagesOld.get(0);
		
		messageService.sendReplyToMessage(replyToMessage.getId(), "I am fine", true);
		
		List<Message> messages = messageService.findAll();
		assertEquals(messagesOld.size() + 1, messages.size());
		Message message = messages.get(messages.size() - 1);
		assertEquals(replyToMessage.getSender().getId(), message.getReceiver().getId());
		assertEquals(replyToMessage.getReceiver().getId(), message.getSender().getId());		
		
		List<WiserMessage> mailMessages = wiser.getMessages();
		assertEquals(2, mailMessages.size());
		assertEquals(new InternetAddress("nachrichten@projecth.com"), mailMessages.get(0).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("pat1@projecth.com"), mailMessages.get(0).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(0).getMimeMessage().getSubject());
		
		assertEquals(new InternetAddress("nachrichten@projecth.com"), mailMessages.get(1).getMimeMessage().getFrom()[0]);
		assertEquals(new InternetAddress("doc1@projecth.com"), mailMessages.get(1).getMimeMessage().getRecipients(javax.mail.Message.RecipientType.TO)[0]);
		assertEquals("Hallo pat2", mailMessages.get(1).getMimeMessage().getSubject());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void findAllPaginate() throws Exception {
		User pat1 = userService.findUserByLogin("sysuser1");
		assertNotNull(pat1);
		
		List<Message> messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX, -1, 20);
		assertEquals(2, messages.size());
		
		messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX, 1, 20);
		assertEquals(0, messages.size());
		
		messages = messageService.findAll(pat1.getId(), MessageGroupType.OUTBOX, 0, 1);
		assertEquals(1, messages.size());
	}
}
