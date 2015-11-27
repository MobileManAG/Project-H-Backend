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
package com.mobileman.projecth.business.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mobileman.projecth.business.MailManager;
import com.mobileman.projecth.business.exception.MailException;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.util.text.HTMLTextParser;

/**
 * Mail manager implementation.
 * @author MobileMan GmbH
 *
 */
public class MailManagerImpl implements MailManager {
	
	private static final Logger log = Logger.getLogger(MailManagerImpl.class);

	private JavaMailSender mailSender;
	
	private String systemAdminEmail;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	private static final String EMAIL_ENCODING = "UTF-8";
    
	/**
	 * @param mailSender
	 */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
	 * Setter for systemAdminEmail
	 *
	 * @param systemAdminEmail new value of systemAdminEmail
	 */
	public void setSystemAdminEmail(String systemAdminEmail) {
		this.systemAdminEmail = systemAdminEmail;
	}
	
	/**
	 * @return systemAdminEmail
	 */
	public String getSystemAdminEmail() {
		return this.systemAdminEmail;
	}
	
	/**
	 * @param mimeMessage
	 * @throws MessagingException
	 */
	private void prepareMessage(MimeMessage mimeMessage)
			throws MessagingException {
		if (log.isDebugEnabled()) {
			log.debug("prepareMessage(MimeMessage) - start"); //$NON-NLS-1$
		}

		mimeMessage.addHeader("Content-Type", "text/plain;charset=UTF-8");
		mimeMessage.setSentDate(new Date());

		if (log.isDebugEnabled()) {
			log.debug("prepareMessage(MimeMessage) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendActivationEmail(User, String)
	 */
	@Override
	public void sendActivationEmail(final User user, final String serverDnsName) {
		if (log.isDebugEnabled()) {
			log.debug("sendActivationEmail(" + user + ", " + serverDnsName + ") - start");
		}
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	        
			/**
			 * {@inheritDoc}
			 * @see org.springframework.mail.javamail.MimeMessagePreparator#prepare(javax.mail.internet.MimeMessage)
			 */
			@Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());				
				messageHelper.setTo(user.getUserAccount().getEmail());
				messageHelper.setFrom("mitglied@projecth.com");
				messageHelper.setSubject("Ihre Anmeldung bei projecth®");
				
				String tmpServerDnsName = serverDnsName;
                if (tmpServerDnsName == null || tmpServerDnsName.trim().length() == 0) {
                	tmpServerDnsName = "projecth.de";
                }
                
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("user", user);
                model.put("dns_server_name", tmpServerDnsName);
                
                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
                   velocityEngine, "sign-up-activation-email-body.vm", model);
                String textMessage = HTMLTextParser.htmlToText(htmlMessage);
                messageHelper.setText(textMessage, htmlMessage);
                 
                if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator); 
        
		if (log.isDebugEnabled()) {
			log.debug("sendActivationEmail(User) - returns"); //$NON-NLS-1$
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendResetCredientialsEmail(User, String)
	 */
	@Override
	public void sendResetCredientialsEmail(final User user, final String serverDnsName) {
		if (log.isDebugEnabled()) {
			log.debug("sendResetCredientialsEmail(" + user.getId() + ", " + serverDnsName + ") - start");
		}
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	        			
			@Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());				
				messageHelper.setTo(user.getUserAccount().getEmail());
				messageHelper.setFrom("mitglied@projecth.com");
				messageHelper.setSubject("Passwort zurücksetzen");
				
				String tmpServerDnsName = serverDnsName;
                if (tmpServerDnsName == null || tmpServerDnsName.trim().length() == 0) {
                	tmpServerDnsName = "projecth.de";
                }
                
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("user", user);
                model.put("dns_server_name", tmpServerDnsName);
                
                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
                   velocityEngine, "reset-credentials-email-body.vm", model);
                String textMessage = HTMLTextParser.htmlToText(htmlMessage);
                messageHelper.setText(textMessage, htmlMessage);

				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator);
        
        if (log.isDebugEnabled()) {
			log.debug("sendResetCredientialsEmail(" + user.getId() + ") - end");
		}
	}


	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendMessageToAdmin(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessageToAdmin(final String senderEmail, final String subject,
			final String body) {
		if (log.isDebugEnabled()) {
			log.debug("sendMessageToAdmin(String, String, String) - start"); //$NON-NLS-1$
		}
        
        sendMessage(senderEmail, getSystemAdminEmail(), subject, body);

		if (log.isDebugEnabled()) {
			log.debug("sendMessageToAdmin(String, String, String) - returns"); //$NON-NLS-1$
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(final String senderEmail, final String receiverEmail,
			final String subject, final String body) {
		if (log.isDebugEnabled()) {
			log.debug("sendMessage(" + senderEmail + ", " + receiverEmail + ", " + subject + ", " + body + ") - start");
		}
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	        
			/**
			 * {@inheritDoc}
			 * @see org.springframework.mail.javamail.MimeMessagePreparator#prepare(javax.mail.internet.MimeMessage)
			 */
            @Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}
				
            	MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());
				messageHelper.setSubject(subject);            	
				messageHelper.setTo(receiverEmail);
				
                if (senderEmail == null || senderEmail.trim().length() == 0) {
                	messageHelper.setFrom("projecth@projecth.com");
                } else {
                	messageHelper.setFrom(senderEmail);
                }
                
                String textMessage = HTMLTextParser.htmlToText(body);
                messageHelper.setText(textMessage, body);

				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator);
        
        if (log.isDebugEnabled()) {
			log.debug("sendMessage(...) - end");
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendTellAFriendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendTellAFriendMessage(final String senderName, final String senderEmail, final String receiverEmails, final String body) {
		if (log.isDebugEnabled()) {
			log.debug("sendMessage(" + senderName + ", " + senderEmail + ", " + receiverEmails + ", " + body + ") - start");
		}
				
		if (senderEmail == null || senderEmail.trim().length() == 0) {
			throw new MailException(MailException.Reason.SENDER_EMAIL_MISSING);
		}
		
		if (receiverEmails == null || receiverEmails.trim().length() == 0) {
			throw new MailException(MailException.Reason.SENDER_EMAIL_MISSING);
		}
				
		final String[] senderData = { "", "", ""};
		
		final String[] receivers = receiverEmails.split("[,]");
		for (int i = 0; i < receivers.length; i++) {
			final int idx = i;
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
		        
				/**
				 * {@inheritDoc}
				 * @see org.springframework.mail.javamail.MimeMessagePreparator#prepare(javax.mail.internet.MimeMessage)
				 */
	            @Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					if (log.isDebugEnabled()) {
						log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
					}
					
					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
					messageHelper.setSentDate(new Date());
					
					Map<String, Object> model = new HashMap<String, Object>();
	                model.put("body-text", body);
	                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
	                   velocityEngine, "tell-a-friend-email-body.vm", model);
	                String textMessage = HTMLTextParser.htmlToText(htmlMessage);
	                messageHelper.setText(textMessage, htmlMessage);
	                senderData[0] = htmlMessage;
	                senderData[1] = textMessage;
	                senderData[2] = "Mitteilung von projecth®";
	                
	                messageHelper.setSubject(senderData[2]);            	
					messageHelper.setTo(receivers[idx]);
					messageHelper.setFrom(getSystemAdminEmail());

					if (log.isDebugEnabled()) {
						log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
					}
	            }
	        };
	        
	        this.mailSender.send(preparator);
		}
		
		this.mailSender.send(new MimeMessagePreparator() {
            @Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());
				messageHelper.setText(senderData[1], senderData[0]);
                messageHelper.setSubject(senderData[2]);            	
				messageHelper.setTo(senderEmail);
				messageHelper.setFrom(getSystemAdminEmail());
            }
        });
        
        if (log.isDebugEnabled()) {
			log.debug("sendMessage(...) - end");
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.business.MailManager#sendResetCredientialsEmail(User, String)
	 */
	@Override
	public void sendNewDiseaseGroupRequestEmail(final String diseaseName, final String emailAddress, final UserType userType) {
		if (log.isDebugEnabled()) {
			log.debug("sendNewDiseaseGroupRequestEmail(" + diseaseName + ", " + emailAddress + ", " + userType + ") - start");
		}
		
		final Map<String, Object> model = new HashMap<String, Object>();
        model.put("disease_name", diseaseName);
        model.put("sender_email", emailAddress);
        model.put("sender_user_type", (UserType.P.equals(userType) ? "Patient" : "Arzt"));
        
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	        			
			@Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				String subject = MessageFormat.format("Neue Gesundheitsgruppe anmelden: {0}", diseaseName);
				
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());				
				messageHelper.setTo("mitglied@projecth.com");
				messageHelper.setFrom(emailAddress);
				messageHelper.setSubject(subject);
				                
                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
                   velocityEngine, "request-new-disease-group-system-email-body.vm", model);
                String textMessage = HTMLTextParser.htmlToText(htmlMessage);
                messageHelper.setText(textMessage, htmlMessage);

				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator);
        
        preparator = new MimeMessagePreparator() {
			
			@Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - start"); //$NON-NLS-1$
				}

				String subject = MessageFormat.format("Anfrage zur Erweiterung von projecth mit {0}", diseaseName);
				
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, EMAIL_ENCODING);
				messageHelper.setSentDate(new Date());				
				messageHelper.setTo(emailAddress);
				messageHelper.setFrom("gesundheitsgruppen@projecth.com");
				messageHelper.setSubject(subject);
				                
                String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
                   velocityEngine, "request-new-disease-group-sender-email-body.vm", model);
                String textMessage = HTMLTextParser.htmlToText(htmlMessage);
                messageHelper.setText(textMessage, htmlMessage);

				if (log.isDebugEnabled()) {
					log.debug("$MimeMessagePreparator.prepare(MimeMessage) - returns"); //$NON-NLS-1$
				}
            }
        };
        
        this.mailSender.send(preparator);
        
        if (log.isDebugEnabled()) {
			log.debug("sendNewDiseaseGroupRequestEmail(...) - end");
		}
	}
}
