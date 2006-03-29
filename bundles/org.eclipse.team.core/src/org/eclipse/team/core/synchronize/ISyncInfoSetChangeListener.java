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
package org.eclipse.team.core.synchronize;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.ITeamStatus;

/**
 * Classes which implement this interface provide methods that deal with the
 * change events that are generated by a {@link SyncInfoSet}.
 * <p>
 * Implementors of this interface it can
 * be added to a sync info set using the <code>addSyncSetChangedListener</code>
 * method and removed using the <code>removeSyncSetChangedListener</code>
 * method.
 * </p><p>
 * The originating sync set holds modification locks on the sync info set to ensure 
 * that no more changes occur until after the current change event is processed.
 * The implementors of this interface must not modify the set within the scope of
 * the listener's methods. If modifications are attempted a runtime exception will occur.
 * </p>
 * @see ISyncInfoSetChangeEvent
 * @since 3.0
 */
public interface ISyncInfoSetChangeListener {
	
	/**
	 * Sent when the contents of a {@link SyncInfoSet} have been reset or the
	 * listener has been connected to the set for the first time using
	 * <code>SyncInfoSet#connect(ISyncInfoSetChangeListener, IProgressMonitor)</code>. Listeners
	 * should discard any state they have accumulated from the originating sync info set
	 * and re-obtain their state from the set. The originating sync set will be 
	 * locked for modification when this method is called.
	 * <p>
	 * Clients should not modify the set within this method and other threads that try to
	 * modify the set will be blocked until the reset is processed.
	 * </p>
	 * @param set the originating {@link SyncInfoSet}
	 */
	public void syncInfoSetReset(SyncInfoSet set, IProgressMonitor monitor);

	/**
	 * Sent when a {@link SyncInfoSet} changes. For example, when a resource's 
	 * synchronization state changes. The originating sync set will be 
	 * locked for modification when this method is called.
	 * Clients should not modify the set within this method and other threads that try to
	 * modify the set will be blocked until the change is processed.
	 * <p>
	 * If the originating set is an instance of <code>SyncInfoTree</code> then
	 * the event will be an instance of <code>ISyncInfoTreeChangeEvent</code>.
	 * Clients can determine this using an <code>instancof</code> check.
	 * </p>
	 * @param event an event containing information about the change.
	 */
	public void syncInfoChanged(ISyncInfoSetChangeEvent event, IProgressMonitor monitor);
	
	/**
	 * This method is called when errors have occurred calculating the <code>SyncInfo</code>
	 * for a resource. The resource associated with the error is available from the 
	 * <code>ITeamStatus</code>. This event only provides the latest errors that occurred.
	 * An array of all errors can be retrieved directly from the set.
	 * 
	 * @param set the originating {@link SyncInfoSet}
	 * @param errors the errors that occurred during the latest set modifications
	 * @param monitor a progress monitor
	 */
	public void syncInfoSetErrors(SyncInfoSet set, ITeamStatus[] errors, IProgressMonitor monitor);
	
}
