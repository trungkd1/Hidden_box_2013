/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - HiddenBoxDBUtil.java
 * Date create: 2:59:12 PM - Nov 21, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.arisvn.arissmarthiddenbox.database.SQLiteAdapter;
import com.arisvn.arissmarthiddenbox.entity.FileItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class HiddenBoxDBUtil.
 */
public class HiddenBoxDBUtil {
	
	/** The instance. */
	private static HiddenBoxDBUtil instance;
	private static String dbSdcard = Utils.FOLDER + "/" + SQLiteAdapter.MYDATABASE_NAME;
	/**
	 * Gets the single instance of HiddenBoxDBUtil.
	 *
	 * @return single instance of HiddenBoxDBUtil
	 */
	public static HiddenBoxDBUtil getInstance() {
		if (instance == null) {
			instance = new HiddenBoxDBUtil();
		}

		return instance;
	}

	
	

	/**
	 * Check database.
	 * 
	 * @param path
	 *            the path
	 * @return true, if successful
	 */
	public boolean checkDatabase(String path) {
		synchronized (this) {
			boolean result = false;
			SQLiteDatabase checkdb = null;
			try {
				File file = new File(path);
				if (!file.exists()) {
					return false;

				}
				checkdb = SQLiteDatabase.openDatabase(path, null,
						SQLiteDatabase.OPEN_READWRITE);
				if (checkdb != null) {
					checkdb.close();
					result = true;
				}
			} catch (Exception e) {
				new RuntimeException(e);
			} finally {
				if (checkdb != null) {
					checkdb.close();
				}
			}
			return result;
		}
	}

	/**
	 * Check and copy from app db.
	 * 
	 * @param context
	 *            the context
	 * @param localPath
	 *            the copy to
	 * @param databaseName
	 *            the database name
	 */
	public void checkAndCopyDBToApp(Context context, String localPath,
			String databaseName) {
		synchronized (this) {
			boolean dbexist = checkDatabase(localPath);

			if (dbexist) {
				String appDB = context.getDatabasePath(databaseName)
						.getAbsolutePath();
				String string = appDB.substring(0, appDB.lastIndexOf("/"));
				File databaseDir = new File(string);
				if (!databaseDir.exists()) {
					databaseDir.mkdirs();
				}
				copyDatabase(localPath, appDB);
				SaveData.getInstance(context).setIsCopyLocalDB(false);
			}
		}
	}

	/**
	 * Import local data to app.
	 *
	 * @param dbSource the db source
	 * @param dbDestination the db destination
	 */
	public void copyDatabase(String dbSource, String dbDestination) {
		// TODO Auto-generated method stub
		synchronized (this) {
			SQLiteDatabase checkdb = null;
			try {
				File file = new File(dbSource);
				if (!file.exists()) {
					return;
				}
				checkdb = SQLiteDatabase.openDatabase(dbSource, null,
						SQLiteDatabase.OPEN_READWRITE);
				if (checkdb != null) {
					// import data here
					File file2 = new File(dbDestination);
					if (file2.exists()) {
						file2.delete();
					}
					copyFile(dbSource, dbDestination);
					checkdb.close();
				}
			} catch (Exception e) {
				new RuntimeException(e);
			} finally {
				if (checkdb != null) {
					checkdb.close();
				}
			}
		}

	}

	/**
	 * Copydatabase.
	 * 
	 * @param copyFrom
	 *            the copy from
	 * @param copyTo
	 *            the copy to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void copyFile(String copyFrom, String copyTo) throws IOException {
		synchronized (this) {
			InputStream myInput = null;
			OutputStream myOutPut = null;
			try {
				File file = new File(copyFrom);
				if (!file.exists()) {
					Log.e("Tamle", "copyFrom file is not exist");
					return;
				}
				myInput = new FileInputStream(file);
				myOutPut = new FileOutputStream(copyTo);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutPut.write(buffer, 0, length);
				}
				myOutPut.flush();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("Tamle", "Copy exception: " + e.toString());
			} finally {
				if (myOutPut != null) {
					myOutPut.close();
				}
				if (myInput != null) {
					myInput.close();
				}

			}
		}
	}
	
	/**
	 * Insert file into Database on Sdcard.
	 *
	 * @param obj the obj
	 * @return the long
	 */
	public long insertFileSdcardDB(FileItem obj) {
		
		SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(dbSdcard, null, SQLiteDatabase.OPEN_READWRITE);
		if (checkdb != null) {
			long result = 	checkdb.insert(SQLiteAdapter.TABLE_HIDDEN_FILE, null,
					getFileValues(obj));
			
			checkdb.close();
			return result;
		}else{
			return -1;
		}
		
	}
	
	
	/**
	 * Delete file in Database on Sdcard.
	 *
	 * @param obj the obj
	 * @return the int
	 */
	public int deleteFile(FileItem obj) {
		
		SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(dbSdcard, null, SQLiteDatabase.OPEN_READWRITE);
		if (checkdb != null) {
			int result = checkdb.delete(SQLiteAdapter.TABLE_HIDDEN_FILE,
					SQLiteAdapter.KEY_FILE_ID + "=" + obj.getId(), null);
			
			checkdb.close();
			return result;
		}else{
			return -1;
		}
		
	}

	
	/**
	 * Get file values.
	 *
	 * @param obj the obj
	 * @return the file values
	 */
	private ContentValues getFileValues(FileItem obj) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQLiteAdapter.KEY_FILE_NAME, obj.getName());
		contentValues.put(SQLiteAdapter.KEY_FILE_PATH_FROM, obj.getPathFrom());
		contentValues.put(SQLiteAdapter.KEY_FILE_PATH_NEW, obj.getPathNew());
		contentValues.put(SQLiteAdapter.KEY_FILE_EXTENSION, obj.getExtension());
		contentValues.put(SQLiteAdapter.KEY_FILE_TYPE, obj.getType());
		contentValues.put(SQLiteAdapter.KEY_FILE_SIZE, obj.getSize());
		if (obj.getThumbnail()!=null) {
			contentValues.put(SQLiteAdapter.KEY_FILE_THUMBNAIL,
					obj.getThumbnail());
		}
		return contentValues;
	}
}
