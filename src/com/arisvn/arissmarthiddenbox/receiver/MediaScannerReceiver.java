/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.receiver - MediaScannerReceiver.java
 * Date create: 2:58:44 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arisvn.arissmarthiddenbox.listener.Listener;

// TODO: Auto-generated Javadoc
/**
 * The Class MediaScannerReceiver.
 */
public class MediaScannerReceiver extends BroadcastReceiver{

	/** The listen. */
	private Listener listen;
	
	/**
	 * Instantiates a new media scanner receiver.
	 *
	 * @param listen the listen
	 */
	public MediaScannerReceiver(Listener listen){
		this.listen = listen;
	}
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 String action = intent.getAction();
         if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
        	 if(listen != null)
        		 listen.doListenner();
         }
	}

}
