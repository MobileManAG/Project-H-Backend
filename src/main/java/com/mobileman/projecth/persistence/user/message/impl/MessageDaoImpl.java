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
 * MessageDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 25.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.user.message.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.business.exception.MessageException;
import com.mobileman.projecth.domain.user.message.Message;
import com.mobileman.projecth.domain.user.message.MessageGroupType;
import com.mobileman.projecth.persistence.impl.DaoImpl;
import com.mobileman.projecth.persistence.user.message.MessageDao;

/**
 * @author mobileman
 *
 */
@Repository("messageDao")
public class MessageDaoImpl extends DaoImpl<Message> implements MessageDao {

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#markAsRead(java.lang.Long, boolean)
	 */
	@Override
	public void markAsRead(Long messageId, boolean read) throws IllegalArgumentException {
		if (messageId == null) {
			throw new IllegalArgumentException(
					"messageId must not be null");
		}
		
		findById(messageId).setRead(read);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#markAsSpam(java.lang.Long, boolean)
	 */
	@Override
	public void markAsSpam(Long messageId, boolean isSpam) throws IllegalArgumentException {
		if (messageId == null) {
			throw new IllegalArgumentException(
					"messageId must not be null");
		}
		
		findById(messageId).setSpam(isSpam);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#delete(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(Long id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"messageId must not be null");
		}
		
		List<Object> ids = (List)getHibernateTemplate().find("select m.id from Message m where m.inReplyToMessage.id=? ", id);
		if (!ids.isEmpty()) {
			throw new MessageException(
					MessageException.Reason.MESSAGE_IS_USED_IN_REPLY);
		}
		
		delete(findById(id));
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#findAll(java.lang.Long, com.mobileman.projecth.domain.user.message.MessageGroupType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> findAll(Long userId, MessageGroupType messageGroupType) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"userId must not be null");
		}
		
		if (messageGroupType == null) {
			throw new IllegalArgumentException(
					"messageGroupType must not be null");
		}
		
		final String query; 
		switch (messageGroupType) {
		case INBOX:
			query = "from Message m where m.receiver.id=? and m.deletedByReceiver is false order by m.created desc";
			break;
		case OUTBOX:
			query = "from Message m where m.sender.id=? and m.deletedBySender is false order by m.created desc";
			break;
		default:
			throw new IllegalArgumentException("Unknown messageGroupType=" + messageGroupType);
		}
		
		List<Message> messages = (List)getHibernateTemplate().find(query, userId);
		
		return messages;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#deleteMessage(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteMessage(Long userId, Long messageId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"userId must not be null");
		}
		
		if (messageId == null) {
			throw new IllegalArgumentException(
					"messageId must not be null");
		}
		
		Message message = findById(messageId);
		if (message.getSender().getId().equals(userId)) {
			message.setDeletedBySender(true);
		} else if (message.getReceiver().getId().equals(userId)) {
			message.setDeletedByReceiver(true);
		} else {
			throw new IllegalArgumentException("Message " + messageId + "not belongs to user: " + userId);
		}
		
		update(message);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#findAll(java.lang.Long, com.mobileman.projecth.domain.user.message.MessageGroupType, int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Message> findAll(Long userId, MessageGroupType messageGroupType, int page, int count) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"userId must not be null");
		}
		
		if (messageGroupType == null) {
			throw new IllegalArgumentException(
					"messageGroupType must not be null");
		}
		
		final String queryString; 
		switch (messageGroupType) {
		case INBOX:
			queryString = "from Message m where m.receiver.id=:_userId and m.deletedByReceiver is false order by m.created desc";
			break;
		case OUTBOX:
			queryString = "from Message m where m.sender.id=:_userId and m.deletedBySender is false order by m.created desc";
			break;
		default:
			throw new IllegalArgumentException("Unknown messageGroupType=" + messageGroupType);
		}
		
		Query query = getSession().createQuery(queryString);
		if (page <= 0) {
			page = 0;
		}
		
		query.setParameter("_userId", userId);
		query.setFirstResult(page * count);
		query.setMaxResults(count);
		
		List<Message> messages = query.list();
		
		return messages;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#findReceivedMessagesCount(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int findReceivedMessagesCount(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"userId must not be null");
		}
		
		Query query = getSession().createQuery("select count(m.id) from Message m where m.receiver.id=:_userId");
		query.setParameter("_userId", userId);
		
		List<Number> messages = query.list();
		int count = 0;
		if (!messages.isEmpty() && messages.get(0) != null) {
			count = messages.get(0).intValue();
		}
		
		return count;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.user.message.MessageDao#findUnreadMessagesCount(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int findUnreadMessagesCount(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"userId must not be null");
		}
		
		Query query = getSession().createQuery("select count(m.id) from Message m where m.receiver.id=:_userId and m.read is false");		
		query.setParameter("_userId", userId);
		
		List<Number> messages = query.list();
		int count = 0;
		if (!messages.isEmpty() && messages.get(0) != null) {
			count = messages.get(0).intValue();
		}
		
		return count;
	}

}
