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
 * Message.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 24.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.user.message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.user.User;

/**
 * Represents user's message (sent or recived)
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "message", schema = "public")
public class Message extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Date time when a message was created
	 */
	private Date created;
	
	/**
	 * Message is flaged as read
	 */
	private boolean read;
	
	/**
	 * Message is flaged as readed
	 */
	private boolean spam;
	
	/**
	 * Message is flaged as deleted by sender
	 */
	private boolean deletedBySender;
	
	/**
	 * Message is flaged as deleted by receiver
	 */
	private boolean deletedByReceiver;
	
	/**
	 * Sender - owner of a message
	 */
	private User sender;

	/**
	 * Receiver - user who receives a message
	 */
	private User receiver;
	
	/**
	 * Subject of a message
	 */
	private String subject;
	
	/**
	 * Message body
	 */
	private String body;
	
	private Message inReplyToMessage;
	
	/**
	 * @return entity id
	 */
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * @return Date time when a message was created
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 29)
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @param created Date time when a message was created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return Sender - owner of a message
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "sender_user_id", nullable = false, insertable=true, updatable=false)
	@ForeignKey(name="fk_message_sender_user_id")
	public User getSender() {
		return this.sender;
	}

	/**
	 * @param sender Sender - owner of a message
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
	 * @return Receiver - user who receives a message
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "receiver_user_id", nullable = false, insertable=true, updatable=false)
	@ForeignKey(name="fk_message_receiver_user_id")
	public User getReceiver() {
		return this.receiver;
	}

	/**
	 * @param receiver Receiver - user who receives a message
	 */
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return Subject of a message
	 */
	@Column(name="subject", length=255, nullable=false)
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject Subject of a message
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return Message body
	 */
	@Column(name="body", length=4000, nullable=false)
	public String getBody() {
		return this.body;
	}

	/**
	 * @param body Message body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return id message is flaged as read
	 */
	@Column(name="readed", nullable=false)
	public boolean isRead() {
		return this.read;
	}

	/**
	 * @param read Message is flaged as read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return spam
	 */
	@Column(name="spam", nullable=false)
	public boolean isSpam() {
		return this.spam;
	}

	/**
	 * @param spam new value of spam
	 */
	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	/**
	 * @return inReplyToMessage
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "in_reply_to_message_id", nullable = true, insertable=true, updatable=false)
	@ForeignKey(name="fk_message_in_reply_to_message_id")
	public Message getInReplyToMessage() {
		return this.inReplyToMessage;
	}

	/**
	 * @param inReplyToMessage new value of inReplyToMessage
	 */
	public void setInReplyToMessage(Message inReplyToMessage) {
		this.inReplyToMessage = inReplyToMessage;
	}

	/**
	 * @return deletedBySender
	 */
	@Column(name="deleted_by_sender", nullable=false)
	public boolean isDeletedBySender() {
		return this.deletedBySender;
	}

	/**
	 * @param deletedBySender new value of deletedBySender
	 */
	public void setDeletedBySender(boolean deletedBySender) {
		this.deletedBySender = deletedBySender;
	}

	/**
	 * @return deletedByReceiver
	 */
	@Column(name="deleted_by_receiver", nullable=false)
	public boolean isDeletedByReceiver() {
		return this.deletedByReceiver;
	}

	/**
	 * @param deletedByReceiver new value of deletedByReceiver
	 */
	public void setDeletedByReceiver(boolean deletedByReceiver) {
		this.deletedByReceiver = deletedByReceiver;
	}
	
	
}
