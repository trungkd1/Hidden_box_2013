/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.listener - DialogListener.java
 * Date create: 2:58:09 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving dialog events.
 * The class that is interested in processing a dialog
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDialogListener<code> method. When
 * the dialog event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DialogEvent
 */
public interface DialogListener {

	/**
	 * Accept listenr.
	 */
	public void acceptListenr();
	
	/**
	 * Cancel listenr.
	 */
	public void cancelListenr();
}
