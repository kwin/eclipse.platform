/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ant.tests.core.tests;


import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.ant.core.AntCorePreferences;
import org.eclipse.ant.core.Type;
import org.eclipse.ant.tests.core.AbstractAntTest;
import org.eclipse.ant.tests.core.testplugin.AntTestChecker;
import org.eclipse.core.runtime.CoreException;

public class TypeTests extends AbstractAntTest {

	public TypeTests(String name) {
		super(name);
	}

	public void testAddType() throws MalformedURLException, CoreException {
		AntCorePreferences prefs =AntCorePlugin.getPlugin().getPreferences();
		URL[] urls= prefs.getExtraClasspathURLs();
		Type newType= new Type();
		newType.setLibrary(urls[0]);
		newType.setTypeName("anttestpath");
		newType.setClassName("org.eclipse.ant.tests.core.support.types.AntTestPath");
		prefs.setCustomTypes(new Type[]{newType});
		
		run("CustomType.xml");
		String msg= (String)AntTestChecker.getDefault().getMessages().get(1);
		assertTrue("Message incorrect: " + msg, msg.equals("Test adding a custom type"));
		assertSuccessful();
	}
	
	public void testRemoveType() throws CoreException {
		AntCorePreferences prefs =AntCorePlugin.getPlugin().getPreferences();
		prefs.setCustomTypes(new Type[]{});
		try {
			run("CustomType.xml");
		} catch (CoreException ce) {
			assertTrue("Exception from undefined type is incorrect", ce.getMessage().endsWith("Unexpected element \"anttestpath\""));
			return;
		} finally {
			restorePreferenceDefaults();	
		}
		assertTrue("Build should have failed as type no longer defined", false);
		
	}
	
	public void testTypeDefinedInExtensionPoint() throws CoreException {
		run("ExtensionPointType.xml");
		String msg= (String)AntTestChecker.getDefault().getMessages().get(1);
		assertTrue("Message incorrect: " + msg, msg.equals("Ensure that an extension point defined type is present"));
		assertSuccessful();
	}
	
	public void testTypeDefinedInExtensionPointHeadless() throws CoreException {
		AntCorePlugin.getPlugin().setRunningHeadless(true);
		try {
			run("ExtensionPointType.xml");
		} catch (CoreException ce) {
			assertTrue("Exception from undefined type is incorrect", ce.getMessage().endsWith("Unexpected element \"coolType\""));
			return;
		} finally {
			AntCorePlugin.getPlugin().setRunningHeadless(false);
		}
		assertTrue("Build should have failed as type was not defined to run in headless", false);
	}
	
}
