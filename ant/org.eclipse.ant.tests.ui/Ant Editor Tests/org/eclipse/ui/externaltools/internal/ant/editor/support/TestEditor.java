/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.externaltools.internal.ant.editor.support;

import org.eclipse.ui.externaltools.internal.ant.editor.AntEditor;
import org.eclipse.ui.externaltools.internal.ant.editor.test.CodeCompletionTest;

public class TestEditor extends AntEditor {
	private final CodeCompletionTest TestAntEditor;
	TestEditor(CodeCompletionTest TestAntEditor) {
		this.TestAntEditor = TestAntEditor;
	}
    public void initializeEditor() {
    }
    /** 
     * Returns '10'.
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#getCursorPosition()
     */
    protected String getCursorPosition() {
        return "10";
    }

}