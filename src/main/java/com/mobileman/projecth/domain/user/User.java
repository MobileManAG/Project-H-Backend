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
package com.mobileman.projecth.domain.user;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.mobileman.projecth.domain.data.Name;
import com.mobileman.projecth.domain.disease.Disease;
import com.mobileman.projecth.domain.index.country.Country;
import com.mobileman.projecth.domain.index.education.Education;
import com.mobileman.projecth.domain.index.family.FamilySituation;
import com.mobileman.projecth.domain.index.race.Race;
import com.mobileman.projecth.domain.user.rights.GrantedRight;
import com.mobileman.projecth.domain.util.security.AESEncoderDecoder;

/**
 * Represents user.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "user", schema = "public")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type", discriminatorType=DiscriminatorType.CHAR, length=1)
@DiscriminatorValue("U")
@Indexed
public class User extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Activation state of a user
	 */
	private UserActivationState activationState;
	
	private UserAccount userAccount;
	private UserType userType;
	private UserState state;
	private String photo;
	private String activationUid;
	private Date created;
	private Date lastLogin;
	private List<Disease> diseases;
	
	private boolean connectionRequestsBlocked;
	
	private Integer sex;
	private String gender;
	private Integer birthday;
	
	private String title;
	
	private Name name;
	
	/**
	 * Number of user's successful logins
	 */
	private int loginsCount;
	
	/**
	 * Number of unsuccessful user's logins
	 */
	private int unsuccessfulLoginsCount;
	
	/**
	 * Birth date of the user
	 */
	private Date dateOfBirth;
	
	/**
	 * User's country of living
	 */
	private Country country;
	
	/**
	 * User's nationality
	 */
	private Country nationality;
	
	/**
	 * User's race
	 */
	private Race race;
	
	/**
	 * User's education
	 */
	private Education education;
	
	/**
	 * User's family situation
	 */
	private FamilySituation familySituation;
	
	/**
	 * User's height
	 */
	private Integer height;
	
	/**
	 * Date/time of last users' (any) update
	 */
	private Date lastUpdate;
	
	/**
	 * User's weight in time
	 */
	private List<UserWeight> weights;
	
	/**
	 * Defines rights which user can choose to grant access to other users
	 */
	private Set<GrantedRight> grantedDataRights;
	
	private String aboutMe;

	/**
	 * 
	 */
	public User() {
		this.setUserType(UserType.U);
	}

	/**
	 * @param userType
	 */
	public User(UserType userType) {
		this.userType = userType;
	}
	
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
	 * @return userAccount
	 */
	@OneToOne(fetch = FetchType.LAZY, optional=true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "user_account_id", nullable = true)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	/**
	 * @param userAccount
	 */
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @return type of the user
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", nullable = false, columnDefinition = "char(1) default 'U'", insertable = false, updatable = false)
	public UserType getUserType() {
		return this.userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "user_state", nullable = false, columnDefinition = "char(1) default 'R'")
	public UserState getState() {
		return this.state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	/**
	 * @return user's name
	 */
	@Embedded
	@IndexedEmbedded
	public Name getName() {
		return this.name;
	}

	/**
	 * @param name user's name
	 */
	public void setName(Name name) {
		this.name = name;
	}

	@Column(name = "activation_uid")
	public String getActivationUid() {
		return this.activationUid;
	}

	public void setActivationUid(String activationUid) {
		this.activationUid = activationUid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login", nullable = false, length = 29)
	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinTable(name = "user_disease", 
			joinColumns = { 
				@JoinColumn(name = "user_id") 
			}, 
			inverseJoinColumns = { 
				@JoinColumn(name = "disease_id") 
				}
	)
	public List<Disease> getDiseases() {
		return this.diseases;
	}

	public void setDiseases(List<Disease> diseases) {
		this.diseases = diseases;
	}

	/**
	 * Gets a flag by which user can block other users to request a connection (standard setting: no).
	 *
	 * @return flag by which user can block other users to request a connection (standard setting: no).
	 */
	@Column(name = "connection_requests_blocked", nullable=false, columnDefinition = "boolean default false")
	public boolean isConnectionRequestsBlocked() {
		return this.connectionRequestsBlocked;
	}
	
	/**
	 * Setter for a flag by which user can block other users to request a connection (standard setting: no).
	 *
	 * @param connectionRequestsBlocked flag by which user can block other users to request a connection (standard setting: no).
	 */
	public void setConnectionRequestsBlocked(boolean connectionRequestsBlocked) {
		this.connectionRequestsBlocked = connectionRequestsBlocked;
	}
	
	/**
	 * @return a number of user's logins (also autologins)
	 */
	@Column(name = "logins_count", nullable = false, columnDefinition = "integer default 0")
	public int getLoginsCount() {
		return this.loginsCount;
	}
	
	/**
	 * @param loginsCount a number of user's logins (also autologins)
	 */
	public void setLoginsCount(int loginsCount) {
		this.loginsCount = loginsCount;
	}
	
	/**
	 * @return Date/time of last users' (any) update
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update", nullable = false)
	public Date getLastUpdate() {
		return this.lastUpdate;
	}
	
	/**
	 * @param lastUpdate Date/time of last users' (any) update
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@Column(name = "sex")
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "gender", length = 20)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "birthday")
	public Integer getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Integer birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return Number of unsuccessful user's logins
	 */
	@Column(name = "unsuccessful_logins_count", nullable=false)
	public int getUnsuccessfulLoginsCount() {
		return this.unsuccessfulLoginsCount;
	}

	/**
	 * @param unsuccessfulLoginsCount Number of unsuccessful user's logins
	 */
	public void setUnsuccessfulLoginsCount(int unsuccessfulLoginsCount) {
		this.unsuccessfulLoginsCount = unsuccessfulLoginsCount;
	}

	/**
	 * @return title
	 */
	@Column(name="title", length=100, nullable=true)
	protected String getTitleEncoded() {
		return this.title;
	}
	
	/**
	 * @return title
	 */
	@Transient
	public String getTitle() {
		return getDecryptedValue("title", getTitleEncoded());
	}

	/**
	 * @param title new value of title
	 */
	protected void setTitleEncoded(String title) {
		this.title = title;
	}
	
	/**
	 * @param title new value of title
	 */
	public void setTitle(String title) {
		setTitleEncoded(AESEncoderDecoder.encode(title));
		setDecryptedValue("title", title);
	}

	/**
	 * @return Birth date of the user
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", nullable = true)
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	/**
	 * @param dateOfBirth Birth date of the user
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return User's country of living
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "country_of_living_id", nullable = true)
	@ForeignKey(name="fk_user_country_of_living_id")
	public Country getCountry() {
		return this.country;
	}

	/**
	 * @param country User's country of living
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return User's nationality
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "nationality_id", nullable = true)
	@ForeignKey(name="fk_user_nationality_id")
	public Country getNationality() {
		return this.nationality;
	}

	/**
	 * @param nationality User's nationality
	 */
	public void setNationality(Country nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return User's race
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "race_id", nullable = true)
	@ForeignKey(name="fk_user_race_id")
	public Race getRace() {
		return this.race;
	}

	/**
	 * @param race User's race
	 */
	public void setRace(Race race) {
		this.race = race;
	}

	/**
	 * @return User's education
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "education_id", nullable = true)
	@ForeignKey(name="fk_user_education_id")
	public Education getEducation() {
		return this.education;
	}

	/**
	 * @param education User's education
	 */
	public void setEducation(Education education) {
		this.education = education;
	}

	/**
	 * @return User's height
	 */
	@Column(name = "height", nullable=true)
	public Integer getHeight() {
		return this.height;
	}

	/**
	 * @param height User's height
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * @return User's family situation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "family_situation_id", nullable = true)
	@ForeignKey(name="fk_user_family_situation_id")
	public FamilySituation getFamilySituation() {
		return this.familySituation;
	}

	/**
	 * @param familySituation User's family situation
	 */
	public void setFamilySituation(FamilySituation familySituation) {
		this.familySituation = familySituation;
	}

	/**
	 * @return User's weight in time
	 */
	@ElementCollection
	@CollectionTable(name = "user_weight", joinColumns = @JoinColumn(name = "user_id"))
	@OrderBy("date")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<UserWeight> getWeights() {
		return this.weights;
	}

	/**
	 * @param weights User's weight in time
	 */
	public void setWeights(List<UserWeight> weights) {
		this.weights = weights;
	}

	/**
	 * @return photo
	 */
	@Column(name = "photo", nullable=true)
	public String getPhoto() {
		return this.photo;
	}

	/**
	 * @param photo new value of photo
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return grantedUserDataRights
	 */
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="user_granted_right", joinColumns=@JoinColumn(name = "user_id"), 
			uniqueConstraints={
				@UniqueConstraint(columnNames={"user_id", "\"right\""})})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Column(name="\"right\"")
	public Set<GrantedRight> getGrantedDataRights() {
		return this.grantedDataRights;
	}

	/**
	 * @param grantedUserDataRights new value of grantedUserDataRights
	 */
	public void setGrantedDataRights(Set<GrantedRight> grantedUserDataRights) {
		this.grantedDataRights = grantedUserDataRights;
	}

	/**
	 * @return aboutMe
	 */
	@Basic(fetch=FetchType.LAZY)
	@Column(name="about_me", length=4000, nullable=true)
	public String getAboutMe() {
		return this.aboutMe;
	}

	/**
	 * @param aboutMe new value of aboutMe
	 */
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	/**
	 * @return Activation state of a user
	 */
	@Column(name="activation_state", nullable=false)
	public UserActivationState getActivationState() {
		return this.activationState;
	}

	/**
	 * @param activationState Activation state of a user
	 */
	public void setActivationState(UserActivationState activationState) {
		this.activationState = activationState;
	}
	
}
