/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.database - SQLiteAdapter.java
 * Date create: 2:50:12 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLiteAdapter. This class is used to manipulate with local DB.
 */
public class SQLiteAdapter {

	/** The Constant VERSION_DB. */
	private static final int VERSION_DB = 2;

	/** The Constant MYDATABASE_NAME. */
	public static final String MYDATABASE_NAME = "ARISLAB_SP";
	
	/** The Constant TABLE_HIDDEN_FILE. */
	public static final String TABLE_HIDDEN_FILE = "HIDDEN_FILE";

	/** The Constant KEY_FILE_ID. */
	public static final String KEY_FILE_ID = "_id";
	
	/** The Constant KEY_FILE_NAME. */
	public static final String KEY_FILE_NAME = "name";
	
	/** The Constant KEY_FILE_PATH_FROM. */
	public static final String KEY_FILE_PATH_FROM = "path_from";
	
	/** The Constant KEY_FILE_PATH_NEW. */
	public static final String KEY_FILE_PATH_NEW = "path_new";
	
	/** The Constant KEY_FILE_EXTENSION. */
	public static final String KEY_FILE_EXTENSION = "extention";
	
	/** The Constant KEY_FILE_TYPE. */
	public static final String KEY_FILE_TYPE = "type";
	
	/** The Constant KEY_FILE_SIZE. */
	public static final String KEY_FILE_SIZE = "size";
	
	/** The Constant KEY_FILE_THUMBNAIL. */
	public static final String KEY_FILE_THUMBNAIL = "thumbnail";

	/** The Constant SCRIPT_CREATE_HIDDEN_FILE. */
	private static final String SCRIPT_CREATE_HIDDEN_FILE = "create table if not exists "
			+ TABLE_HIDDEN_FILE
			+ " ("
			+ KEY_FILE_ID
			+ " integer primary key autoincrement, "
			+ KEY_FILE_NAME
			+ " text, "
			+ KEY_FILE_PATH_FROM
			+ " text, "
			+ KEY_FILE_PATH_NEW
			+ " text, "
			+ KEY_FILE_EXTENSION
			+ " text, "
			+ KEY_FILE_TYPE
			+ " integer,"
			+ KEY_FILE_SIZE
			+ " real,"+ KEY_FILE_THUMBNAIL + " blob)";

	/** The Constant columnHiddenFile. */
	private static final String[] columnHiddenFile = { KEY_FILE_ID,
			KEY_FILE_NAME, KEY_FILE_PATH_FROM, KEY_FILE_PATH_NEW,
			KEY_FILE_EXTENSION,KEY_FILE_TYPE, KEY_FILE_SIZE, KEY_FILE_THUMBNAIL };

	/** The sq lite helper. */
	private SQLiteHelper sqLiteHelper;
	
	/** The sq lite database. */
	private SQLiteDatabase sqLiteDatabase;

	/** The context. */
	private Context context;

	/**
	 * Instantiates a new sQ lite adapter.
	 *
	 * @param c the c
	 */
	public SQLiteAdapter(Context c) {
		context = c;
		open();
	}

	
	/**
	 * Check Database is open or close.
	 *
	 * @return true, if is ready
	 */
	public boolean isReady() {
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Open Database when start app.
	 *
	 * @return the sQ lite adapter
	 * @throws SQLException the sQL exception
	 */
	public SQLiteAdapter open() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				VERSION_DB);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close Database.
	 */
	public void close() {
		sqLiteHelper.close();
	}

	/**
	 * Insert file into Database.
	 *
	 * @param obj the obj
	 * @return the long
	 */
	public long insertFile(FileItem obj) {
		return sqLiteDatabase.insert(TABLE_HIDDEN_FILE, null,
				getFileValues(obj));
	}

	/**
	 * Delete file in Database.
	 *
	 * @param obj the obj
	 * @return the int
	 */
	public int deleteFile(FileItem obj) {
		return sqLiteDatabase.delete(TABLE_HIDDEN_FILE,
				KEY_FILE_ID + "=" + obj.getId(), null);
	}

	/**
	 * Get files from Database.
	 *
	 * @param type the type
	 * @return the all file
	 */
	public ArrayList<FileItem> getAllFile(int type) {
		ArrayList<FileItem> list = new ArrayList<FileItem>();
		Cursor cursor = sqLiteDatabase.query(TABLE_HIDDEN_FILE,
				columnHiddenFile, KEY_FILE_TYPE+"=?", new String[]{String.valueOf(type)}, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					FileItem obj = getFileItem(cursor);
					list.add(obj);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		return list;
	}

	
	/**
	 * Get each item base on cursor.
	 *
	 * @param cursor the cursor
	 * @return the file item
	 */
	private FileItem getFileItem(Cursor cursor) {
		FileItem obj = new FileItem();
		obj.setId(cursor.getInt(cursor.getColumnIndex(KEY_FILE_ID)));
		obj.setName(cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME)));
		obj.setPathFrom(cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH_FROM)));
		obj.setPathNew(cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH_NEW)));
		obj.setExtension(cursor.getString(cursor.getColumnIndex(KEY_FILE_EXTENSION)));
		obj.setType(cursor.getInt(cursor.getColumnIndex(KEY_FILE_TYPE)));
		obj.setSize(cursor.getLong(cursor.getColumnIndex(KEY_FILE_SIZE)));
		byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(KEY_FILE_THUMBNAIL));
		  if(byteArray != null)
		  obj.setThumbnail(byteArray);
		return obj;
	}

	/**
	 * Get file values.
	 *
	 * @param obj the obj
	 * @return the file values
	 */
	private ContentValues getFileValues(FileItem obj) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_FILE_NAME, obj.getName());
		contentValues.put(KEY_FILE_PATH_FROM, obj.getPathFrom());
		contentValues.put(KEY_FILE_PATH_NEW, obj.getPathNew());
		contentValues.put(KEY_FILE_EXTENSION, obj.getExtension());
		contentValues.put(KEY_FILE_TYPE, obj.getType());
		contentValues.put(KEY_FILE_SIZE, obj.getSize());
		if (obj.getThumbnail()!=null) {
			contentValues.put(KEY_FILE_THUMBNAIL,
					obj.getThumbnail());
		}
		return contentValues;
	}
	
	/**
	 * Checking file exist in Database.
	 *
	 * @param pathfrom the pathfrom
	 * @return true, if is exist file name
	 */
	public boolean isExistFileName(String pathfrom){
		Cursor cursor = sqLiteDatabase.query(TABLE_HIDDEN_FILE,
				columnHiddenFile, KEY_FILE_PATH_FROM+"=?", new String[]{pathfrom}, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Checks if is encrypted file exist.
	 *
	 * @param pathNew the path new
	 * @return true, if is encrypted file exist
	 */
	public boolean isEncryptedFileExist(String pathNew){
		Cursor cursor = sqLiteDatabase.query(TABLE_HIDDEN_FILE,
				columnHiddenFile, KEY_FILE_PATH_NEW+"=?", new String[]{pathNew}, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public byte[] getFileThumbnail(int id) {
		Cursor cursor = sqLiteDatabase.query(TABLE_HIDDEN_FILE,
				columnHiddenFile, KEY_FILE_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		byte[] byteArray =null ;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					 byteArray = cursor.getBlob(cursor.getColumnIndex(KEY_FILE_THUMBNAIL));
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		return byteArray;
	}
	
	
	public FileItem getFile(int id) {
		Cursor cursor = sqLiteDatabase.query(TABLE_HIDDEN_FILE,
				columnHiddenFile, KEY_FILE_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		FileItem obj = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					obj = getFileItem(cursor);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		return obj;
	}

	/**
	 * The Class SQLiteHelper.
	 */
	public class SQLiteHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new sQ lite helper.
		 *
		 * @param context the context
		 * @param name the name
		 * @param factory the factory
		 * @param version the version
		 */
		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SCRIPT_CREATE_HIDDEN_FILE);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);
			if (newVersion==2) {
				String password=SaveData.getInstance(context).getPassword();
				try {
					SaveData.getInstance(context).setPassword( Encryption.encrypt(context.getPackageName(),
								password));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
