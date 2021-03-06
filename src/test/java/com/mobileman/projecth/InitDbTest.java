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
 * InitDbTest.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 4.11.2010
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobileman
 *
 */
public class InitDbTest extends TestCaseBase {
	
	@Autowired
	private AnnotationSessionFactoryBean sessionFactory;
	
	@Autowired
	private DriverManagerDataSource dataSource;
	
	protected void export() throws Exception{	
				
		SchemaExport export = new SchemaExport(sessionFactory.getConfiguration());
		export.setOutputFile("sql.ddl");
		export.setDelimiter(";");
		//export.drop(false, true);
		//export.create(false, true);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void loadData() throws Exception {
		executeSqlScript("/scripts/data.sql", true);
	}
}
