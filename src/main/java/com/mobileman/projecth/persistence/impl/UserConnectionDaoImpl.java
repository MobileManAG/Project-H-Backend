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
 * UserConnectionDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author MobileMan GmbH
 * @date 31.10.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */
package com.mobileman.projecth.persistence.impl;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobileman.projecth.business.security.EncoderService;
import com.mobileman.projecth.domain.user.User;
import com.mobileman.projecth.domain.user.UserType;
import com.mobileman.projecth.domain.user.connection.UserConnection;
import com.mobileman.projecth.domain.user.connection.UserConnectionState;
import com.mobileman.projecth.persistence.UserConnectionDao;

/**
 * @author MobileMan GmbH
 *
 */
@Repository("userConnectionDao")
public class UserConnectionDaoImpl extends DaoImpl<UserConnection> implements UserConnectionDao {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(UserConnectionDaoImpl.class);

	@Autowired
	private EncoderService encoderService;
	
	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findConnections(java.lang.Long, com.mobileman.projecth.domain.user.connection.UserConnectionState)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<UserConnection> findConnections(Long userId,
			UserConnectionState connectionState)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findConnections(Long, UserConnectionState) - start"); //$NON-NLS-1$
		}
		
		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		if (connectionState == null) {
			throw new IllegalArgumentException("State must not be null");
		}
				
		List<UserConnection> connections = (List)getHibernateTemplate().find(
				"select uc " +
				"from UserConnection uc " +
				"where uc.owner.id=? and uc.state=?", 
				new Object[]{ userId, connectionState });
				

		if (log.isDebugEnabled()) {
			log.debug("findConnections(Long, UserConnectionState) - returns"); //$NON-NLS-1$
		}
		return connections;
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findConnection(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public UserConnection findConnection(Long userId, Long ownerId)
			throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("findConnection(Long, Long) - start"); //$NON-NLS-1$
		}
		
		UserConnection result = null;
				
		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner id must not be null");
		}
		
		List<UserConnection> connections = (List)getHibernateTemplate().find(
				"select uc " +
				"from UserConnection uc " +
				"where (uc.user.id=? and uc.owner.id=?) or (uc.owner.id=? and uc.user.id=?)", 
				new Object[]{ userId, ownerId, userId, ownerId });
		
		if(connections.size() > 0){
			result = connections.get(0);
		}
		

		if (log.isDebugEnabled()) {
			log.debug("findConnection(Long, Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findAllNotCanceledConnections(java.lang.Long)
	 */	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserConnection> findAllNotCanceledConnections(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("findAllNotCanceledConnections(Long) - start"); //$NON-NLS-1$
		}
		
		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
				
		List<UserConnection> connections = (List)getHibernateTemplate().find(
				"select uc " +
				"from UserConnection uc " +
				"where (uc.owner.id=? or uc.user.id=?) and uc.state!=? " +
				"order by uc.owner.id, uc.id", 
				new Object[]{ userId, userId, UserConnectionState.C });
				

		if (log.isDebugEnabled()) {
			log.debug("findAllNotCanceledConnections(Long) - returns"); //$NON-NLS-1$
		}
		return connections;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#existsAcceptedConnection(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean existsAcceptedConnection(Long userId1, Long userId2) {
		if (log.isDebugEnabled()) {
			log.debug("existsAcceptedConnection(Long, Long) - start"); //$NON-NLS-1$
		}
		
		if (userId1 == null || userId2 == null) {
			throw new IllegalArgumentException("Users id must not be null");
		}
				
		List<UserConnection> connections = (List)getHibernateTemplate().find(
				"select uc " +
				"from UserConnection uc " +
				"where uc.state=? " +
				"and ((uc.owner.id=? and uc.user.id=?) " +
				"or (uc.owner.id=? and uc.user.id=?)) ", 
				new Object[]{ UserConnectionState.A, userId1, userId2, userId2, userId1 });
		

		boolean returnboolean = !connections.isEmpty();
		if (log.isDebugEnabled()) {
			log.debug("existsAcceptedConnection(Long, Long) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#notCanceledConnectionExists(java.lang.Long, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean notCanceledConnectionExists(Long userId, String user2Login) {
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(Long, String) - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		if (user2Login == null) {
			throw new IllegalArgumentException("User login must not be null");
		}
		
		user2Login = this.encoderService.encode(user2Login);
				
		List<Number> connections = (List)getHibernateTemplate().find(
				"select count(uc.id) from UserConnection uc " +
				"where uc.state!=? and ((uc.owner.id=? and uc.user.userAccount.loginEncoded=?) " +
				"or (uc.owner.userAccount.loginEncoded=? and uc.user.id=?)) ", 
				new Object[]{ UserConnectionState.C, userId, user2Login, user2Login, userId });
		

		boolean returnboolean = !connections.isEmpty() && connections.get(0) != null && connections.get(0).intValue() > 0;
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(Long, String) - returns"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#deletaAllConnections(com.mobileman.projecth.domain.user.User)
	 */
	@Override
	public void deletaAllConnections(User user) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			log.debug("deletaAllConnections(User) - start"); //$NON-NLS-1$
		}

		if (user == null) {
			throw new IllegalArgumentException("User must not be null");
		}
		
		if (user.getId() == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
		
		getHibernateTemplate().bulkUpdate(
				"delete from UserConnection uc " +
				"where (uc.owner.id=? or uc.user.id=?)) ", 
				new Object[]{ user.getId(), user.getId() });

		if (log.isDebugEnabled()) {
			log.debug("deletaAllConnections(User) - returns"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findAllByDoctor(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserConnection> findAllByDoctor(Long doctorId) {
		if (log.isDebugEnabled()) {
			log.debug("findAllByDoctor(Long) - start"); //$NON-NLS-1$
		}

		List<UserConnection> result = List.class.cast(getHibernateTemplate().find(
				"select uc " +
				"from UserConnection uc " +
				"where ((uc.user.id = ? and uc.owner.userType = ?) " +
				"or (uc.owner.id = ? and uc.user.userType = ?)) " +
				"and uc.state != ?", 
				new Object[]{doctorId, UserType.P, doctorId, UserType.P, UserConnectionState.C}));

		if (log.isDebugEnabled()) {
			log.debug("findAllByDoctor(Long) - returns"); //$NON-NLS-1$
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findInvitableUsers(String, Long, Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findInvitableUsers(String filter, Long invitingUserId, Collection<UserType> includeUserTypes) {
		if (log.isDebugEnabled()) {
			log.debug("findInvitableUsers(" + filter +", " + invitingUserId + ", " + includeUserTypes + ") - start"); //$NON-NLS-1$
		}

		if (filter == null) {
			throw new IllegalArgumentException("Login must not be null");
		}
		
		if (filter.trim().length() == 0) {
			throw new IllegalArgumentException("Login must not be empty string");
		}
		
		String encodedFilter = encoderService.encode(filter).trim();
		Query query = getSession().createQuery("select u " +
				"from User u join u.userAccount ua " +
				"left join u.medicalInstitution.country country " +
				"where u.userAccount.id is not null " +
				"and u.id != :_iuid " +
				"and u.userType in (:_iut) " +
				"and not exists (select uc.id from UserConnection uc where ((uc.user.id=:_iuid and uc.owner.id=u.id) or (uc.owner.id=:_iuid and uc.user.id=u.id)) and uc.state!=:_ucs) " +
				"and (ua.loginEncoded=:_filt or u.name.nameEncoded=:_filt or u.name.surnameEncoded=:_filt " +
				"or u.medicalInstitution.nameEncoded=:_filt " +
				"or u.medicalInstitution.address.placeEncoded=:_filt " +
				"or u.medicalInstitution.address.numberEncoded=:_filt " +
				"or u.medicalInstitution.address.addressEncoded=:_filt " +
				"or u.medicalInstitution.address.postalCode.codeEncoded=:_filt " +
				// Location
				"or country.name=:_filtdec " +
				"or country.code=:_filtdec) ");
		query.setParameter("_filt", encodedFilter);
		query.setParameter("_filtdec", filter);
		query.setParameter("_iuid", invitingUserId);
		query.setParameterList("_iut", includeUserTypes);
		query.setParameter("_ucs", UserConnectionState.C);
		
		List<User> users = query.list();
		
		if (log.isDebugEnabled()) {
			log.debug("findUsersByLoginNamePlace(String) - returns"); //$NON-NLS-1$
		}
		return users;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#findInvitableUsers(java.lang.String, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findInvitableUsers(String filter, Long invitingUserId) {
		if (log.isDebugEnabled()) {
			log.debug("findInvitableUsers(" + filter +", " + invitingUserId +") - start"); //$NON-NLS-1$
		}

		if (filter == null) {
			throw new IllegalArgumentException("Login must not be null");
		}
		
		if (filter.trim().length() == 0) {
			throw new IllegalArgumentException("Login must not be empty string");
		}
		
		String encodedFilter = encoderService.encode(filter).trim();
		Query query = getSession().createQuery("select u " +
				"from User u join u.userAccount ua " +
				"left join u.medicalInstitution.country country " +
				"where u.userAccount.id is not null " +
				"and u.id != :_iuid " +
				"and not exists (select uc.id from UserConnection uc where ((uc.user.id=:_iuid and uc.owner.id=u.id) or (uc.owner.id=:_iuid and uc.user.id=u.id)) and uc.state!=:_ucs) " +
				"and (ua.loginEncoded=:_filt or u.name.nameEncoded=:_filt or u.name.surnameEncoded=:_filt " +
				"or u.medicalInstitution.nameEncoded=:_filt " +
				"or u.medicalInstitution.address.placeEncoded=:_filt " +
				"or u.medicalInstitution.address.numberEncoded=:_filt " +
				"or u.medicalInstitution.address.addressEncoded=:_filt " +
				"or u.medicalInstitution.address.postalCode.codeEncoded=:_filt " +
				// Location
				"or country.name=:_filtdec " +
				"or country.code=:_filtdec) ");
		query.setParameter("_filt", encodedFilter);
		query.setParameter("_filtdec", filter);
		query.setParameter("_iuid", invitingUserId);
		query.setParameter("_ucs", UserConnectionState.C);
		
		List<User> users = query.list();
		
		if (log.isDebugEnabled()) {
			log.debug("findInvitableUsers(String) - returns"); //$NON-NLS-1$
		}
		return users;
	}

	/** 
	 * {@inheritDoc}
	 * @see com.mobileman.projecth.persistence.UserConnectionDao#notCanceledConnectionExists(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean notCanceledConnectionExists(Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(" + userId + ") - start"); //$NON-NLS-1$
		}

		if (userId == null) {
			throw new IllegalArgumentException("User id must not be null");
		}
			
		List<Number> connections = (List)getHibernateTemplate().find(
				"select count(uc.id) from UserConnection uc " +
				"where uc.state!=? and (uc.owner.id=? or uc.user.id=?)", 
				new Object[]{ UserConnectionState.C, userId, userId });
		

		boolean returnboolean = !connections.isEmpty() && connections.get(0) != null && connections.get(0).intValue() > 0;
		if (log.isDebugEnabled()) {
			log.debug("notCanceledConnectionExists(Long, String) - returns"); //$NON-NLS-1$
		}
		
		return returnboolean;
	}
}
