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
 * MessageServiceImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 24.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.user.message.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mobileman.projecth.business.ComponentNames;
import com.mobileman.projecth.business.ConfigurationService;
import com.mobileman.projecth.business.MailManager;
import com.mobileman.projecth.business.impl.BusinessServiceImpl;
import com.mobileman.projecth.business.user.message.MessageService;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.message.Message;
import com.mobileman.projecth.domain.user.message.MessageGroupType;
import com.mobileman.projecth.persistence.UserDao;
import com.mobileman.projecth.persistence.user.message.MessageDao;

/**
 * @author mobileman
 *
 */
@Service(ComponentNames.MESSAGE_SERVICE)
public class MessageServiceImpl extends BusinessServiceImpl<Message> implements MessageService {

	
	private MessageDao messageDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailManager mailManager;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	private static final String VM_SEND_MESSAGE_BODY = "message-center-send-message-body.vm";
	private static final String VM_SEND_MESSAGE_COPY_BODY = "message-center-send-message-copy-body.vm";
	
	/**
	 * @param messageDao new value of messageDao
	 */
	@Autowired
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
		super.setDao(messageDao);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#markAsRead(java.lang.Long, boolean)
	 */
	@Override
	public void markAsRead(Long messageId, boolean readed) throws IllegalArgumentException {
		if (messageId == null) {
			throw new IllegalArgumentException("messageId must not be null");
		}
		
		messageDao.markAsRead(messageId, readed);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#markAsSpam(java.lang.Long, boolean)
	 */
	@Override
	public void markAsSpam(Long messageId, boolean isSpam) throws IllegalArgumentException {
		if (messageId == null) {
			throw new IllegalArgumentException("messageId must not be null");
		}
		
		messageDao.markAsSpam(messageId, isSpam);
		
		Message message = messageDao.findById(messageId);
		mailManager.sendMessage(
				message.getReceiver().getUserAccount().getEmail(), 
				"spam@projecth.com", message.getSubject(), "Spam reported from user: " + message.getReceiver().getUserAccount().getLogin() + " \n\n" 
				+ "\n Sender email: " + message.getSender().getUserAccount().getEmail() + "\n" + message.getBody());
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#sendMessage(java.lang.Long, java.lang.Long, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void sendMessage(Long senderId, Long receiverId, String title, String text, boolean sendCopyToSender)
			throws IllegalArgumentException {
		if (senderId == null) {
			throw new IllegalArgumentException("senderId must not be null");
		}
		
		if (receiverId == null) {
			throw new IllegalArgumentException("receiverId must not be null");
		}
		
		if (title == null) {
			throw new IllegalArgumentException("title must not be null");
		}
		
		if (text == null) {
			throw new IllegalArgumentException("text must not be null");
		}
		
		User sender = userDao.findById(senderId);
		User receiver = userDao.findById(receiverId);
		Message message = new Message();
		message.setCreated(new Date());
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setRead(false);
		message.setSpam(false);
		message.setInReplyToMessage(null);
		message.setBody(text);
		message.setSubject(title);
		getDao().save(message);
		
		final String senderEmail = configurationService.getMessageCenterSenderEmail();
		final int receivedMessagesCount = messageDao.findReceivedMessagesCount(receiver.getId());
		final int unreadMessagesCount = messageDao.findUnreadMessagesCount(receiver.getId());
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("sender", sender);
        model.put("receiver", receiver);
        model.put("received_messages_count", receivedMessagesCount);
        model.put("unread_messages_count", unreadMessagesCount);
        
        String htmlMailSendMessage = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, VM_SEND_MESSAGE_BODY, model);
        
		mailManager.sendMessage(
				senderEmail, 
				message.getReceiver().getUserAccount().getEmail(), 
				message.getSubject(), htmlMailSendMessage);
		
		if (sendCopyToSender) {
			String htmlMailSendCopyMessage = VelocityEngineUtils.mergeTemplateIntoString(
			           velocityEngine, VM_SEND_MESSAGE_COPY_BODY, model);
			mailManager.sendMessage(
					senderEmail, 
					message.getSender().getUserAccount().getEmail(), 
					message.getSubject(), htmlMailSendCopyMessage);
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#deleteMessage(java.lang.Long)
	 */
	@Override
	public void deleteMessage(Long messageId) throws IllegalArgumentException {
		if (messageId == null) {
			throw new IllegalArgumentException("messageId must not be null");
		}
		
		messageDao.delete(messageId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#sendReplyToMessage(java.lang.Long, java.lang.String, boolean)
	 */
	@Override
	public void sendReplyToMessage(Long replyToMessageId, String text, boolean sendCopyToSender)
			throws IllegalArgumentException {
		if (replyToMessageId == null) {
			throw new IllegalArgumentException("replyToMessageId must not be null");
		}
		
		if (text == null) {
			throw new IllegalArgumentException("text must not be null");
		}
		
		Message replyToMessage = getDao().findById(replyToMessageId);
		
		User sender = replyToMessage.getReceiver();
		User receiver = replyToMessage.getSender();
		Message message = new Message();
		message.setCreated(new Date());
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setRead(false);
		message.setSpam(false);
		message.setInReplyToMessage(replyToMessage);
		message.setBody(text);
		message.setSubject(replyToMessage.getSubject());
		getDao().save(message);
		
		final String senderEmail = configurationService.getMessageCenterSenderEmail();
		final int receivedMessagesCount = messageDao.findReceivedMessagesCount(receiver.getId());
		final int unreadMessagesCount = messageDao.findUnreadMessagesCount(receiver.getId());
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("sender", sender);
        model.put("receiver", receiver);
        model.put("received_messages_count", receivedMessagesCount);
        model.put("unread_messages_count", unreadMessagesCount);
        
        String htmlMailSendMessage = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, VM_SEND_MESSAGE_BODY, model);
        
		mailManager.sendMessage(
				senderEmail, 
				receiver.getUserAccount().getEmail(), 
				message.getSubject(), htmlMailSendMessage);
		
		if (sendCopyToSender) {
			String htmlMailSendCopyMessage = VelocityEngineUtils.mergeTemplateIntoString(
			           velocityEngine, VM_SEND_MESSAGE_COPY_BODY, model);
			
			mailManager.sendMessage(
					senderEmail, 
					sender.getUserAccount().getEmail(), 
					message.getSubject(), htmlMailSendCopyMessage);
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#findAll(java.lang.Long, com.mobileman.projecth.domain.user.message.MessageGroupType)
	 */
	@Override
	public List<Message> findAll(Long userId, MessageGroupType messageGroupType) {
		return messageDao.findAll(userId, messageGroupType);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#deleteMessage(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteMessage(Long userId, Long messageId) {
		messageDao.deleteMessage(userId, messageId);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.user.message.MessageService#findAll(java.lang.Long, com.mobileman.projecth.domain.user.message.MessageGroupType, int, int)
	 */
	@Override
	public List<Message> findAll(Long userId, MessageGroupType messageGroupType, int page, int count) {
		return messageDao.findAll(userId, messageGroupType, page, count);
	}
}
