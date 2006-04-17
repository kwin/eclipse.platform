/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.intro.impl.model;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * An intro url command registration. This model class does not appear as a
 * child under any of the other model classes. It is returned by the
 * SharedConfigExtensionsManager when asked for commands.
 */
public class IntroURLAction extends AbstractIntroElement {

    public static final String TAG_ACTION = "action"; //$NON-NLS-1$

    private static final String ATT_NAME = "name"; //$NON-NLS-1$
    private static final String ATT_REPLACES = "replaces"; //$NON-NLS-1$

    private String name;
    private String replaces;


    /**
     * Note: model class with public constructor because it is not instantiated
     * by the model root.
     */
    public IntroURLAction(IConfigurationElement element) {
        super(element);
        name = element.getAttribute(ATT_NAME);
        replaces = element.getAttribute(ATT_REPLACES);
    }

    /**
     * @return Returns the className.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the pluginId.
     */
    public String getReplaceValue() {
        return replaces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.intro.impl.model.IntroElement#getType()
     */
    public int getType() {
        // this model class does not need a type so far.
        return 0;
    }
}
