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
 * HaqChart.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 7.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.domain.chart;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import com.mobileman.projecth.domain.Entity;
import com.mobileman.projecth.domain.disease.Haq;
import com.mobileman.projecth.domain.questionary.Question;

/**
 * @author mobileman
 *
 */
@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "haq_chart", schema = "public")
public class HaqChart extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Chart title
	 */
	private String title;
	
	/**
	 * Chart subtitle
	 */
	private String subtitle;
	
	/**
	 * Chart X axis name
	 */
	private String xAxisName;
	
	/**
	 * Chart Y axis name
	 */
	private String yAxisName;
	
	private ChartType chartType;
	
	private Haq haq;
	
	private int sortOrder;
	
	private List<Question> questions;
	
	private String tag;
	
	private Double width;
		
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
	 * @return chartType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chart_type_id", nullable = false)
	@ForeignKey(name = "fk_haqchart_chart_type_id")
	public ChartType getChartType() {
		return this.chartType;
	}

	/**
	 * @param chartType new value of chartType
	 */
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}

	/**
	 * @return haq
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "haq_id", nullable = false)
	@ForeignKey(name = "fk_haqchart_haq_id")
	public Haq getHaq() {
		return this.haq;
	}
	
	/**
	 * @param haq new value of haq
	 */
	public void setHaq(Haq haq) {
		this.haq = haq;
	}

	/**
	 * @return title
	 */
	@Column(name = "title", nullable = false, length = 255)
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title new value of title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return subtitle
	 */
	@Column(name = "subtitle", nullable = true, length = 255)
	public String getSubtitle() {
		return this.subtitle;
	}

	/**
	 * @param subtitle new value of subtitle
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return xAxisName
	 */
	@Column(name = "x_axis_name", nullable = true, length = 255)
	public String getxAxisName() {
		return this.xAxisName;
	}

	/**
	 * @param xAxisName new value of xAxisName
	 */
	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	/**
	 * @return yAxisName
	 */
	@Column(name = "y_axis_name", nullable = true, length = 255)
	public String getyAxisName() {
		return this.yAxisName;
	}

	/**
	 * @param yAxisName new value of yAxisName
	 */
	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	/**
	 * @return order of a chart in list of all haq's charts
	 */
	@Column(name = "sort_order", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	/**
	 * @param sortOrder order of a chart in list of all haq's charts
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return questions
	 */
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "haq_chart_question",
       joinColumns = { @JoinColumn(name = "haq_chart_id") }, 
       inverseJoinColumns = { @JoinColumn(name = "question_id") })
    @ForeignKey(name="fk_haqchq_haq_chart_id", inverseName="fk_haqchq_haq_question_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<Question> getQuestions() {
		return this.questions;
	}

	/**
	 * @param questions new value of questions
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * @return tag
	 */
	@Column(name = "tag", nullable = false)
	public String getTag() {
		return this.tag;
	}

	/**
	 * @param tag new value of tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return width width of a chart (<code>null</code> means 100%)
	 */
	@Column(name = "width", nullable = true)
	public Double getWidth() {
		return this.width;
	}

	/**
	 * @param width width of a chart (<code>null</code> means 100%)
	 */
	public void setWidth(Double width) {
		this.width = width;
	}
	
	
}
