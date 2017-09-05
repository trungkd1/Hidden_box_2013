/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - Utils.java
 * Date create: 2:46:43 PM - Nov 8, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.App;
import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.fragment.PickerFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 *
 * @author trungkd
 */
public class Utils {
	
	/** The folder. */
	public static String FOLDER = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Encrypte";
	
	/** The sdcard. */
	public static String SDCARD = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	
	/** The restore folder. */
	public static String RESTORE_FOLDER = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Encrypte/restore";
	
	/** The  audio. */
	public static String _AUDIO = "/audio";
	
	/** The  video. */
	public static String _VIDEO = "/video";
	
	/** The  photo. */
	public static String _PHOTO = "/photo";

	/** The sort size. */
	public static int SORT_SIZE = 0;
	
	/** The sort name. */
	public static int SORT_NAME = 1;
	
	/** The action category. */
	public static String ACTION_CATEGORY = "ACTION_CATEGORY";
	
	/** The fragment photo. */
	public static String FRAGMENT_PHOTO = "FRAGMENT_PHOTO";
	
	/** The fragment video. */
	public static String FRAGMENT_VIDEO = "FRAGMENT_VIDEO";
	
	/** The fragment audio. */
	public static String FRAGMENT_AUDIO = "FRAGMENT_AUDIO";
	
	/** The fragment picker. */
	public static String FRAGMENT_PICKER = "FRAGMENT_PICKER";
	
	/** The fragment category. */
	public static String FRAGMENT_CATEGORY = "FRAGMENT_CATEGORY";

	/** The Constant TYPE. */
	public static final String TYPE = "TYPE";
	
	/** The Constant TYPE_PHOTO. */
	public static final int TYPE_PHOTO = 0;
	
	/** The Constant TYPE_VIDEO. */
	public static final int TYPE_VIDEO = 1;
	
	/** The Constant TYPE_AUDIO. */
	public static final int TYPE_AUDIO = 2;
	
	/** The Constant TYPE_THUMBNAIL. */
	public static final int TYPE_THUMBNAIL = 6;

	/** The Constant IMPORT. */
	public static final int IMPORT = 4;
	
	/** The Constant EXPORT. */
	public static final int EXPORT = 5;
	
	/** The Constant FILE_MODE. */
	public static final String FILE_MODE = "file_mode";
	
	/** The Constant FILE_NAME. */
	public static final String FILE_NAME = "file_name";
	
	/** The Constant REQUEST_FILE. */
	public static final int REQUEST_FILE = 3;
	
	/** The Constant SELECT_FILE_DIALOG. */
	public static final String SELECT_FILE_DIALOG = "select file dialog";
	
	/** The Constant BACKUP_SQL. */
	public static final String BACKUP_SQL = "backup_file.txt";
    

	/** The tag. */
	public static String TAG = "debug";

	public static final String ENCRYPT_EXTENSION = ".ashb";

	public static final String FILE_PATH = "file_path";

//	public static final int TYPE_FILE = -1;

	/**
	 * Encrypt file.
	 *
	 * @param context the context
	 * @param filePath the file path
	 * @return true, if successful
	 */
	public static boolean encrypt(Context context, String filePath,
			int bufferLenght) {
		if (Utils.isSDCardExist()) {
        try {
            Encryption encryption = new Encryption(context);
            // encrypt
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(filePath, "rw");
          
            byte[] plainByteArray = new byte[(int)Math.min(localRandomAccessFile.length(),
                   bufferLenght)];
            localRandomAccessFile.seek(0L);
            localRandomAccessFile.readFully(plainByteArray);
            byte[] encryptedByte = encryption.getRequiredEncryptedBytesFromByte(plainByteArray);
            if (encryptedByte == null) {
                Log.e("Tamle", "encrypt error: ");
                localRandomAccessFile.close();
            } else {
                localRandomAccessFile.seek(0L);
                localRandomAccessFile.write(encryptedByte);
                localRandomAccessFile.close();
            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
		} else {
			Log.e("Tamle", "encrypt error: there is no SDCard");
			return false;
		}

    }

    /**
     * Decrypt.
     *
     * @param context the context
     * @param filePath the file path
     * @return true, if successful
     */
    public static boolean decrypt(Context context, String filePath) {
        try {
        	int byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_PHOTO;
        	
        	if(filePath.startsWith(FOLDER+_AUDIO)){
        		byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_AUDIO;
        		
        	}else if(filePath.startsWith(FOLDER+_PHOTO)){
        		byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_PHOTO;

        	}else if(filePath.startsWith(FOLDER+_VIDEO)){
        		byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_VIDEO;

        	}
        	
            Encryption encryption = new Encryption(context);
            // decrypt
            RandomAccessFile localRandomAccessFile2 = new RandomAccessFile(filePath, "rw");
            byte[] encryptedByteArray = new byte[(int) Math.min(localRandomAccessFile2.length(),byte_to_encrypte)];
            localRandomAccessFile2.seek(0L);
            localRandomAccessFile2.read(encryptedByteArray);
            byte[] decryptedByteArray = encryption
                    .getRequiredDecryptedBytesFromByte(encryptedByteArray);
            if (decryptedByteArray == null) {
				Log.e(Utils.TAG, "decrypt error: ");
                localRandomAccessFile2.close();
            } else {
                localRandomAccessFile2.seek(0L);
                localRandomAccessFile2.write(decryptedByteArray);
                localRandomAccessFile2.close();
            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
			Log.e(Utils.TAG, "Utils: encrypt: " + e.toString());
            return false;
        }
	}

	/**
	 * Processing encode file.
	 *
	 * @param context
	 *            the context
	 * @param objs
	 *            the objs
	 * @throws Exception
	 *             the exception
	 */
	public static void processEncrypt(Context context, ArrayList<FileItem> objs)
			throws Exception {
		for (Iterator<FileItem> iterator = objs.iterator(); iterator.hasNext();) {
			FileItem fileItem = (FileItem) iterator.next();
			if (!fileItem.isCheck())
				continue;
			if (Utils.isSDCardExist()) {
				if (!App.getDB().isExistFileName(fileItem.getPathFrom())) {
					String input = fileItem.getPathFrom();

			String output = null;
			int byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_PHOTO;
			Uri uri = null;
			
			if (fileItem.getType() == Utils.TYPE_AUDIO) {
				output = Utils.FOLDER + Utils._AUDIO + "/"
						+ (System.currentTimeMillis() / 10);
				byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_AUDIO;
				uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			} else if (fileItem.getType() == Utils.TYPE_PHOTO) {
				output = Utils.FOLDER + Utils._PHOTO + "/"
						+ (System.currentTimeMillis() / 10);
				byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_PHOTO;
				uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			} else if (fileItem.getType() == Utils.TYPE_VIDEO) {
				output = Utils.FOLDER + Utils._VIDEO + "/"
						+ (System.currentTimeMillis() / 10);
				byte_to_encrypte =  Encryption.BYTES_TO_ENCRYPT_VIDEO;
				uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
			}
					output = output + ENCRYPT_EXTENSION;
					fileItem.setPathNew(output);

					if (fileItem.getThumbnail() == null) {
						Bitmap bitmap = Utils.createThumbnail(context,
								fileItem.getId(), fileItem.getType());
						fileItem.setThumbnail(Utils.getByteArray(bitmap));
						System.gc();
						if (bitmap != null) {
							bitmap.recycle();
						}
						bitmap = null;
					}

					if (encrypt(context, input, byte_to_encrypte)) {
						boolean moving = new File(input).renameTo(new File(
								output));
	    				if (moving) {
							context.getContentResolver().delete(
									uri,
									PickerFragment._ID + " = ?",
									new String[] { String.valueOf(fileItem
											.getId()) });
							App.getDB().insertFile(fileItem);
							HiddenBoxDBUtil.getInstance().insertFileSdcardDB(
									fileItem);
							iterator.remove();
	    				} else {
	    					decrypt(context, input);
	    				}
	            	}
			}
			} else {
				Log.e(Utils.TAG, "processEncrypt faile: there is no SDCard");
				break;

		}
	 }
    }
    /**
     * Processing decode file.
     *
	 * @param context
	 *            the context
	 * @param objs
	 *            the objs
	 * @throws Exception
	 *             the exception
	 */
	public static void processDecrypt(Context context, ArrayList<FileItem> objs)
			throws Exception {
		for (Iterator<FileItem> iterator = objs.iterator(); iterator.hasNext();) {
			FileItem fileItem = (FileItem) iterator.next();
			if (!fileItem.isCheck())
				continue;
			if (Utils.isSDCardExist()) {
				if (decrypt(context, fileItem.getPathNew())) {
					File pathFromFolder = (new File(fileItem.getPathFrom()))
							.getParentFile();
				     if (!pathFromFolder.exists()) {
				      pathFromFolder.mkdirs();
				     }
					boolean isMoving = new File(fileItem.getPathNew())
							.renameTo(new File(fileItem.getPathFrom()));
					if (isMoving) {
						App.getDB().deleteFile(fileItem);
						HiddenBoxDBUtil.getInstance().deleteFile(fileItem);
						iterator.remove();
					} else {
						String filePath = fileItem.getPathNew();
						int byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_PHOTO;
						if (filePath.startsWith(FOLDER + _AUDIO)) {
							byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_AUDIO;

						} else if (filePath.startsWith(FOLDER + _PHOTO)) {
							byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_PHOTO;

						} else if (filePath.startsWith(FOLDER + _VIDEO)) {
							byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_VIDEO;

						}
						encrypt(context, filePath, byte_to_encrypte);
						Log.e(Utils.TAG, "Move fail from " + fileItem.getPathNew()
								+ " to:  " + fileItem.getPathFrom());
					}
				} else {
					Log.e(Utils.TAG, "Decrypt fail " + fileItem.getPathFrom());
                }
			} else {
				Log.e(Utils.TAG, "processDecrypt faile: there is no SDCard");
				break;
			}
		}

	}

	/**
	 * Create folder stores file.
	 *
	 * @param path the path
	 */
	public static void makeFolder(String path) {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * Create thumbnail base on ID param.
	 *
	 * @param context the context
	 * @param id the id
	 * @param type the type
	 * @return the bitmap
	 */
	public static Bitmap createThumbnail(Context context, long id, int type) {
		if (type == TYPE_AUDIO) {
			return createThumbnailForAudio(context, id);
		} else if (type == TYPE_VIDEO) {
			return createThumbnailForVideo(context, id);
		} else {
			return createThumbnailForImage(context, id);
		}
	}

	/**
	 * Create image thumbnail.
	 *
	 * @param context the context
	 * @param id the id
	 * @return the bitmap
	 */
	private static Bitmap createThumbnailForImage(Context context, long id) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(
				context.getContentResolver(), id,
				MediaStore.Images.Thumbnails.MICRO_KIND, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) 100);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) 100);
		// If both of the ratios are greater than 1, one of the sides of
		// the image is greater than the screen
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		// Decode it for real
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmpFactoryOptions.inPreferredConfig = Config.ARGB_8888;
		bmpFactoryOptions.inPurgeable = true;
	    DisplayMetrics dm = context.getResources().getDisplayMetrics();
	    bmpFactoryOptions.inDensity = dm.densityDpi;
		bmp = MediaStore.Images.Thumbnails.getThumbnail(
				context.getContentResolver(), id,
				MediaStore.Images.Thumbnails.MICRO_KIND, bmpFactoryOptions);
		return bmp;
	}

	/**
	 * Create audio thumbnail.
	 *
	 * @param context the context
	 * @param id the id
	 * @return the bitmap
	 */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	private static Bitmap createThumbnailForAudio(Context context, long id) {
		try {
			MediaMetadataRetriever mmr = new MediaMetadataRetriever();
			byte[] rawArt;
			Bitmap art = null;
			BitmapFactory.Options bfo = new BitmapFactory.Options();
			bfo.inJustDecodeBounds = true;
			Uri uri = ContentUris.withAppendedId(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

			mmr.setDataSource(context, uri);
			rawArt = mmr.getEmbeddedPicture();

			// if rawArt is null then no cover art is embedded in the file or is
			// not
			// recognized as such.
			if (null != rawArt)
				art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length,
						bfo);
			int heightRatio = (int) Math.ceil(bfo.outHeight
					/ (float) 100);
			int widthRatio = (int) Math.ceil(bfo.outWidth
					/ (float) 100);
			// If both of the ratios are greater than 1, one of the sides of
			// the image is greater than the screen
			if (heightRatio > 1 && widthRatio > 1) {
				if (heightRatio > widthRatio) {
					// Height ratio is larger, scale according to it
					bfo.inSampleSize = heightRatio;
				} else {
					// Width ratio is larger, scale according to it
					bfo.inSampleSize = widthRatio;
				}
			}
			// Decode it for real
			bfo.inJustDecodeBounds = false;
			bfo.inPreferredConfig = Config.ARGB_8888;
			bfo.inPurgeable = true;
		    DisplayMetrics dm = context.getResources().getDisplayMetrics();
		    bfo.inDensity = dm.densityDpi;
		    art = MediaStore.Video.Thumbnails.getThumbnail(
					context.getContentResolver(), id,
					MediaStore.Video.Thumbnails.MICRO_KIND, bfo);
			return art;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Create video thumbnail.
	 *
	 * @param context the context
	 * @param id the id
	 * @return the bitmap
	 */
	private static Bitmap createThumbnailForVideo(Context context, long id) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = MediaStore.Video.Thumbnails.getThumbnail(
				context.getContentResolver(), id,
				MediaStore.Video.Thumbnails.MICRO_KIND, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) 100);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) 100);
		// If both of the ratios are greater than 1, one of the sides of
		// the image is greater than the screen
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		// Decode it for real
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmpFactoryOptions.inPreferredConfig = Config.ARGB_8888;
		bmpFactoryOptions.inPurgeable = true;
	    DisplayMetrics dm = context.getResources().getDisplayMetrics();
	    bmpFactoryOptions.inDensity = dm.densityDpi;
		bmp = MediaStore.Video.Thumbnails.getThumbnail(
				context.getContentResolver(), id,
				MediaStore.Video.Thumbnails.MICRO_KIND, bmpFactoryOptions);
		return bmp;
	}

	/**
	 * Gets the bitmap from id.
	 * 
	 * @param context
	 *            the context
	 * @param id
	 *            the id
	 * @return the bitmap from id
	 */
	public static Bitmap getBitmapFromId(Context context, int id) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Config.ARGB_8888;
        opts.inPurgeable = true;
        opts.inSampleSize = 1;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        opts.inDensity = dm.densityDpi;
        Resources res = context.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, id, opts);
        return bmp;
	}
	
	/**
	 * Get the bitmap base on type param.
	 *
	 * @param context the context
	 * @param type the type
	 * @return the bitmap via type
	 */
	public static Bitmap getBitmapViaType(Context context,int type){
		Bitmap bmp = null;
		switch (type) {
		case Utils.TYPE_AUDIO:
            bmp=Utils.getBitmapFromId(context, R.drawable.ic_no_audio);
			break;
		case Utils.TYPE_VIDEO:
			bmp=Utils.getBitmapFromId(context, R.drawable.ic_no_video);
			break;
		case Utils.TYPE_PHOTO:
			bmp=Utils.getBitmapFromId(context, R.drawable.ic_no_image);
			break;
		}
		return bmp;
	}

	/**
	 * Delete directory.
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void deleteDirectory(File file) throws IOException {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				Log.d(Utils.TAG, "Restore folder is empty");
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					Log.d(Utils.TAG, " Remove " + fileDelete.getPath());
					// recursive delete
					if (!fileDelete.isFile())
						continue;
					if (!fileDelete.delete())
						Log.d(Utils.TAG,
								"Couldn't remove " + fileDelete.getPath());
				}
			}
		}
	}

	/**
	 * Empty restore folder.
	 * 
	 * @param dirName
	 *            the dir name
	 */
	public void emptyRestoreFolder(String dirName) {
		// TODO Auto-generated method stub
		if (isSDCardExist()) {
			File imagesDir = new File(dirName);
			try {
				deleteDirectory(imagesDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if is sD card exist.
	 * 
	 * @return true, if is sD card exist
	 */
	public static boolean isSDCardExist() {
		return isExternalStorageAvailable() && !isExternalStorageReadOnly();
	}

	/**
	 * Checks if is external storage available.
	 * 
	 * @return true, if is external storage available
	 */
	public static boolean isExternalStorageAvailable() {
		boolean state = false;
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			state = true;
		}
		return state;
	}

	/**
	 * Checks if is external storage read only.
	 * 
	 * @return true, if is external storage read only
	 */
	public static boolean isExternalStorageReadOnly() {
		boolean state = false;
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			state = true;
		}
		return state;
	}

	/**
	 * Read object.
	 * 
	 * @param file
	 *            the file
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static List<FileItem> readObject(String file) {
		List<FileItem> obj = null;
		FileInputStream fi;
		try {
			fi = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fi);
			obj = (List<FileItem>) ois.readObject();
			fi.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "readObject exception: " + e.toString());
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "readObject exception: " + e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "readObject exception: " + e.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "readObject exception: " + e.toString());
		}

		return obj;
	}

	/**
	 * Write object.
	 * 
	 * @param file
	 *            the file
	 * @param objItems
	 *            the obj items
	 */
	public static void writeObject(String file, List<FileItem> objItems) {
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fo);
			oos.writeObject(objItems);
			oos.flush();
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "writeObject exception: " + e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "writeObject exception: " + e.toString());
		}

	}
	
	/**
	 * Refresh action bar.
	 *
	 * @param activity the activity
	 */
	public static void refreshActionBar(FragmentActivity activity){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			//API level 11 or above
			activity.invalidateOptionsMenu();
		} else{
		    // API level < 11
			activity.supportInvalidateOptionsMenu();
			
		}
		
	}
	
	/**
	 * Set font for textview.
	 *
	 * @param context the context
	 * @param text the text
	 */
	public static void setTypeface(Context context,TextView text){
			Typeface	font = Typeface.createFromAsset(context.getAssets(), "UTMAvoBold.ttf");
		text.setTypeface(font);
	}
	
	/**
	 * Enable disable view.
	 *
	 * @param view the view
	 * @param enabled the enabled
	 */
	public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;
            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
		/**
		 * Gets the byte array.
		 *
		 * @param bmp the bmp
		 * @return the byte array
		 */
	 public static byte[] getByteArray(Bitmap bmp) {
		  if (bmp != null) {
			  ByteArrayOutputStream stream = new ByteArrayOutputStream();
			  bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			  bmp.recycle();
			  bmp = null;
			  return stream.toByteArray();
		  	} else {
			  return null;
		  	}
	}
}
