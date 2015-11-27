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
 * Project: projecth
 * 
 * @author mobileman
 * @date 3.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.chart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represent type of thy chart.
 * 
 * @author mobileman
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "chart_type", schema = "public")
public class ChartType extends com.mobileman.projecth.domain.Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumerates a type of a chart
	 * @author mobileman
	 *
	 */
	public enum Type {
		
		/**
		 * Line chart. A line chart shows trends in data at equal intervals.
		 */
		LINE,
		
		/**
		 * Pie chart. A pie chart shows the size of items that make up a data series, proportional to the sum of the items.
		 */
		PIE,
		
		/**
		 * Bar chart. A bar chart illustrates comparisons among individual items.
		 */
		BAR,
		
		/**
		 * Bubble chart. A bubble chart is a type of xy (scatter) chart
		 */
		BUBLE,
		
		/**
		 * Area chart. An area chart emphasizes the magnitude of change over time.
		 */
		AREA,
		
		/**
		 * Scatter chart. An xy (scatter) chart shows the relationships among the numeric values in several data series, or plots two groups of numbers as one series of xy coordinates.
		 */
		SCATTER,
		
		/**
		 * Image chart. An chart shows the images.
		 */
		IMAGE,
		
		/**
		 * Multiple pie chart. A pie chart shows the size of items that make up a data series, proportional to the sum of the items.
		 */
		MULTIPLE_PIE;
	}
	
	private Type type;
	
	private String description;
	
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
	 * @return description of given chart type
	 */
	@Column(name = "description", nullable = true, length = 255)
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @param description description of given chart type
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return type
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type", nullable = false)
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type new value of type
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	
}
