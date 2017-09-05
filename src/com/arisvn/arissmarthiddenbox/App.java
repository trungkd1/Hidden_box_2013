/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - App.java
 * Date create: 2:46:21 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox;


import com.arisvn.arissmarthiddenbox.database.SQLiteAdapter;
import com.arisvn.arissmarthiddenbox.util.HiddenBoxDBUtil;
import com.arisvn.arissmarthiddenbox.util.SaveData;
import com.arisvn.arissmarthiddenbox.util.Utils;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class App. This class is used to get connection to local DB.
 */
public class App extends android.app.Application {

    /** The need show login. */
    public static boolean needShowLogin = true;
    
    /** The is showing. */
    public static boolean isShowing = false;
	
	/** The db. */
	private static SQLiteAdapter db;
	
	/** The context. */
	private static Context context;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		Utils.makeFolder(Utils.RESTORE_FOLDER);
		// check to import exist DB from SDCard or create new DB in SDCard
		if (SaveData.getInstance(context).isCopyLocalDB()) {
			HiddenBoxDBUtil.getInstance().checkAndCopyDBToApp(context,
					Utils.FOLDER + "/" + SQLiteAdapter.MYDATABASE_NAME,
					SQLiteAdapter.MYDATABASE_NAME);
		}
		db = new SQLiteAdapter(context);
		if (!HiddenBoxDBUtil.getInstance().checkDatabase(Utils.FOLDER + "/" + SQLiteAdapter.MYDATABASE_NAME)) {
			String appDB = context.getDatabasePath(SQLiteAdapter.MYDATABASE_NAME)
					.getAbsolutePath();
			HiddenBoxDBUtil.getInstance().copyDatabase(appDB, Utils.FOLDER + "/" + SQLiteAdapter.MYDATABASE_NAME);			
		}
	}

	/**
	 * Gets the db.
	 *
	 * @return the db
	 */
	public static SQLiteAdapter getDB() {
		if (!db.isReady()) {
			db = new SQLiteAdapter(context);
		}
		return db;
	}

	/**
	 * Close.
	 */
	public static void close() {
		db.close();
	}
}
