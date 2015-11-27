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
 * PatientQuestionImageAnswer.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 13.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.patient;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents patient's answer to a question asking for an image
 * 
 * @author mobileman
 *
 */
@Entity
@DiscriminatorValue("2")
public class PatientQuestionImageAnswer extends PatientQuestionAnswer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Date of Photo-Taking
	 */
	private Date dateTaken;
	
	/**
	 * @return Date of Photo-Taking
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_taken", nullable=true)
	public Date getDateTaken() {
		return this.dateTaken;
	}

	/**
	 * @param dateTaken Date of Photo-Taking
	 */
	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

}
