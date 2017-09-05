/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util.lazyloading - MemoryCache.java
 * Date create: 2:59:52 PM - Nov 21, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.util.lazyloading;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class MemoryCache.
 */
public class MemoryCache {

    /** The Constant TAG. */
    private static final String TAG = "MemoryCache";
    
    /** The cache. */
    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));// Last argument
    // true for LRU
    // ordering
    /** The size. */
    private long size = 0;// current allocated size
    
    /** The limit. */
    private long limit = 1000000;// max memory in bytes

    /**
     * Instantiates a new memory cache.
     */
    public MemoryCache() {
        // use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    /**
     * Sets the limit.
     *
     * @param new_limit the new limit
     */
    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.d(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    /**
     * Gets the.
     *
     * @param id the id
     * @return the bitmap
     */
    public Bitmap get(String id) {
        try {
            if (!cache.containsKey(id))
                return null;
            // NullPointerException sometimes happen here
            // http://code.google.com/p/osmdroid/issues/detail?id=78
            return cache.get(id);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    /**
     * Put.
     *
     * @param id the id
     * @param bitmap the bitmap
     */
    public void put(String id, Bitmap bitmap) {
        try {
            if (cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * Check size.
     */
    private void checkSize() {
        Log.d(TAG, "cache size=" + size + " length=" + cache.size());
        if (size > limit) {
            /*
             * least recently accessed item will be the first one iterated
             */
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.d(TAG, "Clean cache. New size " + cache.size());
        }
    }

    /**
     * Gets the size in bytes.
     *
     * @param bitmap the bitmap
     * @return the size in bytes
     */
    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    public void clearCache(){
   		cache.clear();
    	
    	
    }
}
