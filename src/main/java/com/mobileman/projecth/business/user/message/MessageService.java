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
 * MessageService.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 24.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.business.user.message;

import java.util.List;

import com.mobileman.projecth.business.SearchService;
import com.mobileman.projecth.domain.user.message.Message;
import com.mobileman.projecth.domain.user.message.MessageGroupType;

/**
 * Declares service For {@link Message}
 * 
 * @author mobileman
 * 
 */
public interface MessageService extends SearchService<Message> {

	/**
	 * Marks given message as read
	 * 
	 * @param messageId
	 * @param readed
	 * @throws IllegalArgumentException
	 *             if <li>messageId == null</li>
	 */
	void markAsRead(Long messageId, boolean readed) throws IllegalArgumentException;

	/**
	 * Marks given message as spam
	 * 
	 * @param messageId
	 * @param isSpam
	 * @throws IllegalArgumentException
	 *             if <li>messageId == null</li>
	 */
	void markAsSpam(Long messageId, boolean isSpam) throws IllegalArgumentException;

	/**
	 * Deletes given message
	 * 
	 * @param messageId
	 * @throws IllegalArgumentException
	 *             if <li>messageId == null</li>
	 */
	void deleteMessage(Long messageId) throws IllegalArgumentException;

	/**
	 * Sends message to receiver
	 * 
	 * @param senderId
	 * @param receiverId
	 * @param text
	 * @param title
	 * @param sendCopyToSender
	 * 
	 * @throws IllegalArgumentException
	 *             if <li>senderId == null</li> <li>receiverId == null</li> <li>
	 *             text == null</li> <li>title == null</li>
	 */
	void sendMessage(Long senderId, Long receiverId, String title, String text, boolean sendCopyToSender)
			throws IllegalArgumentException;

	/**
	 * Sends reply to message to receiver
	 * 
	 * @param replyToMessageId
	 * @param text
	 * @param sendCopyToSender
	 * 
	 * @throws IllegalArgumentException
	 *             if <li>inReplyToMessageId == null</li> <li>text == null</li>
	 */
	void sendReplyToMessage(Long replyToMessageId, String text, boolean sendCopyToSender)
			throws IllegalArgumentException;

	/**
	 * @param userId
	 * @param messageGroupType
	 * @return list of all massages of given group for user
	 */
	List<Message> findAll(Long userId, MessageGroupType messageGroupType);
	
	/**
	 * @param userId
	 * @param messageGroupType
	 * @param page 
	 * @param count 
	 * @return list of all massages of given group for user
	 */
	List<Message> findAll(Long userId, MessageGroupType messageGroupType, int page, int count);

	/**
	 * @param userId
	 * @param messageId
	 */
	void deleteMessage(Long userId, Long messageId);
}
