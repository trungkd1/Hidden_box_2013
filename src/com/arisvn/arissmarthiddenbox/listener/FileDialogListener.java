/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.listener - FileDialogListener.java
 * Date create: 2:58:27 PM - Nov 21, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving fileDialog events.
 * The class that is interested in processing a fileDialog
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileDialogListener<code> method. When
 * the fileDialog event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FileDialogEvent
 */
public interface FileDialogListener {
	
	/**
	 * On file selected.
	 *
	 * @param path the path
	 * @param mode the mode
	 */
	public void onFileSelected(String path, int mode);

}
