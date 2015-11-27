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
 * HTMLTextParser.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 2.2.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.util.text;

import java.io.ByteArrayInputStream;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author mobileman
 *
 */
public class HTMLTextParser {
	
	private static void processNode(StringBuffer textBuffer, Node node) {
		if (node == null)
			return;

		if (node.getNodeType() == Node.TEXT_NODE) {
			textBuffer.append(node.getNodeValue());
		} else if (node.hasChildNodes()) {
			// Process the Node's children

			NodeList childList = node.getChildNodes();
			int childLen = childList.getLength();

			for (int count = 0; count < childLen; count++)
				processNode(textBuffer, childList.item(count));
		} else
			return;
	}

	/**
	 * @param html
	 * @return text from HTML
	 */
	public static String htmlToText(String html) {

		DOMFragmentParser parser = new DOMFragmentParser();
		StringBuffer buffer = new StringBuffer();
		
		try {
			ByteArrayInputStream fin = new ByteArrayInputStream(html.getBytes("UTF-8"));
			InputSource inSource = new InputSource(fin);
			CoreDocumentImpl codeDoc = new CoreDocumentImpl();
			DocumentFragment doc = codeDoc.createDocumentFragment();
			parser.parse(inSource, doc);
			processNode(buffer, doc);
			fin.close();
		} catch (Exception e) {
			return null;
		}

		return buffer.toString();
	}

}
