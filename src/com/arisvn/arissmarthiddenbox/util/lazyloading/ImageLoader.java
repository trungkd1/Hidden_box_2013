/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util.lazyloading - ImageLoader.java
 * Date create: 2:59:58 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.util.lazyloading;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.arisvn.arissmarthiddenbox.App;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageLoader.
 */
public class ImageLoader {

	/** The memory cache. */
	public MemoryCache memoryCache = new MemoryCache();
	
	/** The image views. */
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	
	/** The executor service. */
	ExecutorService executorService;
	
	/** The context. */
	Context context;
	
	/** The stub_bitmap. */
	Bitmap stub_bitmap;
	
	/** The type. */
	int type;

	/**
	 * Instantiates a new image loader.
	 *
	 * @param context the context
	 * @param type the type
	 */
	public ImageLoader(Context context, int type) {
		this.context = context;
		executorService = Executors.newFixedThreadPool(5);
		// stub_bitmap = Utils.getBitmapFromId(context, R.drawable.ic_launcher);
		this.type = type;
	}

	/**
	 * Display image.
	 *
	 * @param imageID the image id
	 * @param imageView the image view
	 */
	public void displayImage(String imageID, ImageView imageView) {
		imageViews.put(imageView, imageID);
		Bitmap bitmap = memoryCache.get(imageID);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(imageID, imageView);
			imageView.setImageBitmap(stub_bitmap);
		}
	}

	/**
	 * Queue photo.
	 *
	 * @param imageName the image name
	 * @param imageView the image view
	 */
	private void queuePhoto(String imageName, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(imageName, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * Gets the bitmap.
	 *
	 * @param imageID the image id
	 * @param type the type
	 * @return the bitmap
	 */
	private Bitmap getBitmap(String imageID, int type) {
		// from SD
		if (type == Utils.TYPE_THUMBNAIL) {
			//BaseFragment
			try {
				FileItem obj = App.getDB().getFile(Integer.parseInt(imageID));

				if(obj != null){
					if(obj.getThumbnail() != null){
						return BitmapFactory.decodeByteArray(obj.getThumbnail(),
								0, obj.getThumbnail().length);
					}else{
						return Utils.getBitmapViaType(context, obj.getType());
					}
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}
		Bitmap b = Utils
				.createThumbnail(context, Long.parseLong(imageID), type);
		return b;
	}

	// Task for the queue
	/**
	 * The Class PhotoToLoad.
	 */
	private class PhotoToLoad {
		
		/** The image name. */
		public String imageName;
		
		/** The image view. */
		public ImageView imageView;

		/**
		 * Instantiates a new photo to load.
		 *
		 * @param u the u
		 * @param i the i
		 */
		public PhotoToLoad(String u, ImageView i) {
			imageName = u;
			imageView = i;
		}
	}

	/**
	 * The Class PhotosLoader.
	 */
	class PhotosLoader implements Runnable {
		
		/** The photo to load. */
		PhotoToLoad photoToLoad;

		/**
		 * Instantiates a new photos loader.
		 *
		 * @param photoToLoad the photo to load
		 */
		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.imageName, type);
			if (bmp == null) {
				bmp = Utils.getBitmapViaType(context, type);
			}
			memoryCache.put(photoToLoad.imageName, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * Image view reused.
	 *
	 * @param photoToLoad the photo to load
	 * @return true, if successful
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.imageName))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	/**
	 * The Class BitmapDisplayer.
	 */
	class BitmapDisplayer implements Runnable {
		
		/** The bitmap. */
		Bitmap bitmap;
		
		/** The photo to load. */
		PhotoToLoad photoToLoad;

		/**
		 * Instantiates a new bitmap displayer.
		 *
		 * @param b the b
		 * @param p the p
		 */
		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageBitmap(stub_bitmap);
		}
	}

}
