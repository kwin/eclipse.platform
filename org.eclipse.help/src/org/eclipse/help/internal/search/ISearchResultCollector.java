/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */
package org.eclipse.help.internal.search;

import org.apache.lucene.search.Hits;

/**
 * Search result.  The search engine adds hits to it.
 */
public interface ISearchResultCollector {
	/**
	 * Adds hits to the result
	 * @param Hits hits
	 */
	public void addHits(Hits hits, String wordsSearched);
}