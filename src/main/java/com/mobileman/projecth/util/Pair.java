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
 * Pair.java
 * 
 * Project: projecth
 * 
 * @author mobileman
 * @date 30.1.2011
 * @version 1.0
 * 
 * (c) 2010 MobileMan GmbH
 */

package com.mobileman.projecth.util;

import java.io.Serializable;

/**
 * @author mobileman
 *
 * @param <A> 
 * @param <B> 
 */
public class Pair<A, B> implements Serializable {
	
	/**  */
	private static final long serialVersionUID = -9072340279900469116L;
	
	/** First object. */
	private A first;
	/** Second object. */
	private B second;

	/**
	 * @param first
	 * @param second
	 */
	public Pair(A first, B second) {
		setFirst(first);
		setSecond(second);
	}
	
	/**
	 * @return <code>first</code>
	 */
	public A getFirst() {
		return first;
	}
	/**
	 * @param first new <code>first</code>.
	 */
	private void setFirst(A first) {
		this.first = first;
	}
	/**
	 * @return <code>second</code>
	 */
	public B getSecond() {
		return second;
	}
	/**
	 * @param second new <code>second</code>.
	 */
	private void setSecond(B second) {
		this.second = second;
	}
	
	/**
	 * Creates new <code>Pair</code>
	 * @param <A> 
	 * @param <B> 
	 * @param first 
	 * @param second 
	 * @return new <code>Pair.</code>
	 */
	public static <A extends Serializable, B extends Serializable> Pair<A, B> create(A first, B second) {
		return new Pair<A, B>(first, second);
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((first == null) ? 0 : first.hashCode());
		result = PRIME * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "{" + first + ":" + second + "}";
	}
}

