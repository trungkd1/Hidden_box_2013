/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - SaveData.java
 * Date create: 2:59:19 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.util;

import android.content.Context;
import android.content.SharedPreferences;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveData.
 */
public class SaveData {

	/** The save data. */
	private static SaveData saveData = null;

	/** The is first time. */
	private static String IS_FIRST_TIME = "isFirstTime";
	
	/** The password. */
	private static String PASSWORD = "password";
	
	/** The is show list. */
	private static String IS_SHOW_LIST = "isShowList";
	
	/** The sort. */
	private static String SORT = "sort";
	
	/** The share preference. */
	private SharedPreferences sharePreference;

	/**
	 * Instantiates a new save data.
	 * 
	 * @param context
	 *            the context
	 */
	public SaveData(Context context) {
		sharePreference = context.getSharedPreferences("hiddenbox",
				Context.MODE_PRIVATE);
	}

	/**
	 * Gets the single instance of SaveData.
	 * 
	 * @param context
	 *            the context
	 * @return single instance of SaveData
	 */
	public static SaveData getInstance(Context context) {
		if (saveData == null) {
			saveData = new SaveData(context);
		}
		return saveData;
	}
	
	/**
	 * Checks if is show list.
	 *
	 * @return true, if is show list
	 */
	public boolean isShowList() {
		return sharePreference.getBoolean(IS_SHOW_LIST, true);
	}

	/**
	 * Sets the show list.
	 *
	 * @param isShowList the new show list
	 */
	public void setShowList(boolean isShowList) {
		sharePreference.edit().putBoolean(IS_SHOW_LIST, isShowList).commit();

	}

	/**
	 * Checks if is first time.
	 *
	 * @return true, if is first time
	 */
	public boolean isFirstTime() {
		return sharePreference.getBoolean(IS_FIRST_TIME, true);
	}

	/**
	 * Sets the first time.
	 *
	 * @param isFirstTime the new first time
	 */
	public void setFirstTime(boolean isFirstTime) {
		sharePreference.edit().putBoolean(IS_FIRST_TIME, isFirstTime).commit();

	}

	/**
	 * Sets the password.
	 *
	 * @param pass the new password
	 */
	public void setPassword(String pass) {
		sharePreference.edit().putString(PASSWORD, pass).commit();
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return sharePreference.getString(PASSWORD, "");
	}
	
	/**
	 * Checks if is copy local db.
	 *
	 * @return true, if is copy local db
	 */
	public boolean isCopyLocalDB() {
		return sharePreference.getBoolean("isCopyLocalDB", true);
	}

	/**
	 * Sets the checks if is copy local db.
	 *
	 * @param isCopyLocalDB the new checks if is copy local db
	 */
	public void setIsCopyLocalDB(boolean isCopyLocalDB) {
		sharePreference.edit().putBoolean("isCopyLocalDB", isCopyLocalDB).commit();
	}

	
	/**
	 * Sets sort size or name base type input.
	 *
	 * @param type the new sort
	 */
	public void setSort(int type){
		sharePreference.edit().putInt(SORT, type).commit();
	}
	
	/**
	 * Get type sort.
	 *
	 * @return the sort
	 */
	public int getSort(){
		return sharePreference.getInt(SORT, 0);
	}
}
