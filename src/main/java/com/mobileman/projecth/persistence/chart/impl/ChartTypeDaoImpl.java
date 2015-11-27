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
 * ChartTypeDaoImpl.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 9.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.persistence.chart.impl;

import org.springframework.stereotype.Repository;

import com.mobileman.projecth.domain.chart.ChartType;
import com.mobileman.projecth.persistence.chart.ChartTypeDao;
import com.mobileman.projecth.persistence.impl.DaoImpl;

/**
 * @author mobileman
 *
 */
@Repository("chartTypeDao")
public class ChartTypeDaoImpl extends DaoImpl<ChartType> implements ChartTypeDao {

}
