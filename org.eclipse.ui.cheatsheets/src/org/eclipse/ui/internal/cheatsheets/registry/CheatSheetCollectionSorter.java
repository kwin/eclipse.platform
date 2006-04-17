/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.cheatsheets.registry;

import com.ibm.icu.text.Collator;

import org.eclipse.jface.viewers.*;
import org.eclipse.ui.model.WorkbenchAdapter;

/**
 *	A Viewer element sorter that sorts Elements by their name attribute.
 *	Note that capitalization differences are not considered by this
 *	sorter, so a < B < c.
 */
public class CheatSheetCollectionSorter extends ViewerSorter {
	public final static CheatSheetCollectionSorter INSTANCE = new CheatSheetCollectionSorter();
	private Collator collator = Collator.getInstance();

	/**
	 * Creates an instance of <code>NewWizardCollectionSorter</code>.  Since this
	 * is a stateless sorter, it is only accessible as a singleton; the private
	 * visibility of this constructor ensures this.
	 */
	private CheatSheetCollectionSorter() {
		super();
	}

	/**
	 * The 'compare' method of the sort operation.
	 *
	 * @return  the value <code>0</code> if the argument o1 is equal to o2;
	 * 			a value less than <code>0</code> if o1 is less than o2;
	 *			and a value greater than <code>0</code> if o1 is greater than o2.
	 */
	public int compare(Viewer viewer, Object o1, Object o2) {
		String name1 = ((WorkbenchAdapter)o1).getLabel(o1);
		String name2 = ((WorkbenchAdapter)o2).getLabel(o2);

		if (name1.equals(name2))
			return 0;

		return collator.compare(name1, name2);
	}

	/**
	 *	Return true if this sorter is affected by a property 
	 *	change of propertyName on the specified element.
	 */
	public boolean isSorterProperty(Object object, String propertyId) {
		return propertyId.equals(IBasicPropertyConstants.P_TEXT);
	}
}
