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
 * MessageDao.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 25.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.user.message;

import java.util.List;

import com.mobileman.projecth.domain.user.message.Message;
import com.mobileman.projecth.domain.user.message.MessageGroupType;
import com.mobileman.projecth.persistence.Dao;

/**
 * Represents DAO for the {@link Message} entity.
 * 
 * @author mobileman
 *
 */
public interface MessageDao extends Dao<Message> {

	/**
	 * Marks given message as read
	 * @param messageId
	 * @param readed
	 * @throws IllegalArgumentException if
	 * <li>messageId == null</li>
	 */
	void markAsRead(Long messageId, boolean readed) throws IllegalArgumentException;
	
	/**
	 * Marks given message as spam
	 * @param messageId
	 * @param isSpam
	 * @throws IllegalArgumentException if
	 * <li>messageId == null</li>
	 */
	void markAsSpam(Long messageId, boolean isSpam) throws IllegalArgumentException;
	
	/**
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * @param userId
	 * @param messageGroupType
	 * @return list of all massages of given group for user
	 */
	List<Message> findAll(Long userId, MessageGroupType messageGroupType);
	
	/**
	 * @param userId
	 * @param messageId
	 */
	void deleteMessage(Long userId, Long messageId);
	
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
	 * @return count of received messages of given user
	 */
	int findReceivedMessagesCount(Long userId);
	
	/**
	 * @param userId
	 * @return count of unread messages of given user
	 */
	int findUnreadMessagesCount(Long userId);
}
