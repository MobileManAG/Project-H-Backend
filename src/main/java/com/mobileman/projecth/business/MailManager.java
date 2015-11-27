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
package com.mobileman.projecth.business;

import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;

/**
 * Mail manager interface.
 * 
 * @author MobileMan GmbH
 * 
 */
public interface MailManager {

	/**
	 * @param user 
	 * @param serverDnsName dns name of server from which user is registered
	 */
	void sendActivationEmail(User user, final String serverDnsName);

	/**
	 * @param user
	 * @param serverDnsName dns name of server from which user is registered
	 */
	void sendResetCredientialsEmail(final User user, final String serverDnsName);

	/**
	 * Sends email message to projecth system admin
	 * 
	 * @param senderEmail
	 * @param subject
	 * @param body
	 */
	void sendMessageToAdmin(final String senderEmail, final String subject,
			final String body);

	/**
	 * Sends email message to the receiver
	 * 
	 * @param senderEmail
	 * @param receiverEmail
	 * @param subject
	 * @param body
	 */
	void sendMessage(String senderEmail, String receiverEmail,
			final String subject, final String body);
	
	/**
	 * Sends "Tell a friend" email message to the receivers
	 * @param senderName 
	 * 
	 * @param senderEmail
	 * @param receiverEmails
	 * @param body
	 */
	void sendTellAFriendMessage(String senderName, String senderEmail, String receiverEmails, final String body);
	
	/**
	 * <code>UC15</code> Request a new disease group
	 * 
	 * @param diseaseName
	 * @param emailAddress
	 * @param userType
	 * @throws IllegalArgumentException if
	 * <li>diseaseName == null</li>
	 * <li>diseaseName is empty</li>
	 * <li>userType == null</li>
	 */
	public void sendNewDiseaseGroupRequestEmail(final String diseaseName, final String emailAddress, final UserType userType);
}
