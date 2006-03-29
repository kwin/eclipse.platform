/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.team.internal.ui.synchronize;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.ui.synchronize.ISynchronizePageSite;
import org.eclipse.ui.*;
import org.eclipse.ui.part.IPageSite;

/**
 * Maps a workbench part to a synchronize page site.
 */
public class WorkbenchPartSynchronizePageSite implements ISynchronizePageSite {
	private IWorkbenchPart part;
	private IDialogSettings settings;
	private IPageSite site;

	public WorkbenchPartSynchronizePageSite(IWorkbenchPart part, IPageSite site, IDialogSettings settings) {
		this.part = part;
		this.site = site;
		this.settings = settings;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getPart()
	 */
	public IWorkbenchPart getPart() {
		return part;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getShell()
	 */
	public Shell getShell() {
		return part.getSite().getShell();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getSelectionProvider()
	 */
	public ISelectionProvider getSelectionProvider() {
		return site.getSelectionProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#setSelectionProvider(org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		site.setSelectionProvider(provider);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getWorkbenchSite()
	 */
	public IWorkbenchSite getWorkbenchSite() {
		return part.getSite();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getKeyBindingService()
	 */
	public IKeyBindingService getKeyBindingService() {
		return part.getSite().getKeyBindingService();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#setFocus()
	 */
	public void setFocus() {
		part.getSite().getPage().activate(part);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getPageSettings()
	 */
	public IDialogSettings getPageSettings() {
		return settings;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#getActionBars()
	 */
	public IActionBars getActionBars() {
		return site.getActionBars();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.ui.synchronize.ISynchronizePageSite#isModal()
	 */
	public boolean isModal() {
		return false;
	}
}
