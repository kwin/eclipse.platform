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

import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.*;
import org.eclipse.ui.internal.intro.impl.util.*;
import org.osgi.framework.*;
import org.w3c.dom.*;

/**
 * An Intro Config component that captures launch bar shortcut information.
 * 
 * @since 3.1
 */
public class LaunchBarShortcutElement extends AbstractIntroElement {
    private static final String ATT_TOOLTIP = "tooltip"; //$NON-NLS-1$
    private static final String ATT_ICON = "icon"; //$NON-NLS-1$
    private static final String ATT_URL = "url"; //$NON-NLS-1$

    public LaunchBarShortcutElement(IConfigurationElement element) {
        super(element);
    }

    public LaunchBarShortcutElement(Element element, Bundle bundle) {
        super(element, bundle);
    }

    public int getType() {
        return LAUNCH_BAR_SHORTCUT;
    }

    /**
     * Returns the URL of this shortcut.
     * 
     * @return
     */
    public String getURL() {
        return getCfgElement().getAttribute(ATT_URL);
    }

    /**
     * Returns the tooltip of this shortcut.
     * 
     * @return
     */
    public String getToolTip() {
        return getCfgElement().getAttribute(ATT_TOOLTIP);
    }

    /**
     * Returns the relative icon path of this shortcut.
     * 
     * @return
     */
    private String getIcon() {
        return getCfgElement().getAttribute(ATT_ICON);
    }

    /**
     * Returns the icon image of this shortcut, or <code>null</code> if not
     * found.
     * 
     * @return
     */
    public ImageDescriptor getImageDescriptor() {
        return ImageUtil.createImageDescriptor(getBundle(), getIcon());
    }
}