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

package com.mobileman.projecth.domain.util.questionary;

import java.util.Arrays;
import java.util.List;

/**
 * @author mobileman
 *
 */
public final class MorbusBechterewQuestionaryUtil {

	private MorbusBechterewQuestionaryUtil(){}
	
	/**
	 * Question "Wie ausgeprägt war Ihre Morgensteifigkeit nach dem aufwachen?"
	 * @return id 
	 */
	public static Long getMorgensteifigkeitQuestionId() {
		return 2004L;
	}
	
	/**
	 * Question
	 * "Wie ausgeprägt war Ihre Morgensteifigkeit nach dem aufwachen?"
	 * "Wenn ja, wie lange dauerte diese Morgensteifigkeit im Allgemeinen?"
	 * @return id
	 */
	public static Long getMorgensteifigkeitLangeQuestionId() {
		return 2005L;
	}
	
	/**
	 * "Wie würden Sie Ihre allgemeine Müdigkeit und Erschöpfung beschreiben?"
	 * "Wie stark waren ihre Schmerzen in Nacken, Rücken oder Hüfte?"
	 * "Wie stark waren Ihre Schmerzen oder Schwellungen an anderen Gelenken?"
	 * "Wie unangenehm waren für Sie berührungs- oder druckempfindliche Körperstellen?"
	 * @return ids
	 */
	public static List<Long> getValue2QuestionsId() {
		return Arrays.asList(2000L, 2001L, 2002L, 2003L);
	}
}
