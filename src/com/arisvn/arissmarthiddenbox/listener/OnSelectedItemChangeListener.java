/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.listener - OnSelectedItemChangeListener.java
 * Date create: 1:42:52 PM - Nov 8, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving onSelectedItemChange events.
 * The class that is interested in processing a onSelectedItemChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOnSelectedItemChangeListener<code> method. When
 * the onSelectedItemChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OnSelectedItemChangeEvent
 */
public interface OnSelectedItemChangeListener {
	
	/**
	 * On selected item changed.
	 *
	 * @param selectedItem the selected item. The number of selected items.
	 */
	public void onSelectedItemChanged(int selectedItem);
	
	/**
	 * On finish load items.
	 *
	 * @param totalItems the total items
	 */
	public void onFinishLoadItems();
	
	/**
	 * Check a Chance in database
	 * 
	 * @param result
	 * @param action
	 */
	public void onChangedData(boolean result,int action);

}
