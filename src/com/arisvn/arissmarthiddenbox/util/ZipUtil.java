/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - ZipUtil.java
 * Date create: 10:06:51 AM - Oct 15, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.util.Log;

/**
 * The Class ZipUtil.
 */
public class ZipUtil {

	/**
	 * Compress.
	 * 
	 * @param sourceFiles
	 *            the source files
	 * @param zipFileName
	 *            the zip file name
	 * @param zipDestinationDir
	 *            the zip destination dir
	 * @return true, if successful
	 */
	public static boolean compress(List<File> sourceFiles, String path) {
		boolean success = true;
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ZipOutputStream zos = new ZipOutputStream(fos);

			byte[] buffer = new byte[1024];
			for (File f : sourceFiles) {
				FileInputStream fis = new FileInputStream(f);
				zos.putNextEntry(new ZipEntry(f.getName()));
				int len;
				while ((len = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
			fos.close();
		} catch (Exception e) {
			Log.e(Utils.TAG, "Compress file: "+path+ " fail due to: "+e.toString());
			success = false;
		}
		return success;
	}

	/**
	 * Extract.
	 * 
	 * @param zipFile
	 *            the zip file
	 * @param extractDestinationDir
	 *            the extract destination dir
	 * @return true, if successful
	 */
	public static boolean extract(File zipFile, String extractDestinationDir) {
		boolean success = true;
		try {
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(fis);

			byte[] buffer = new byte[1024];
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				FileOutputStream fos = new FileOutputStream(
						extractDestinationDir.concat("/").concat(
								entry.getName()));

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
			}
			zis.close();
			fis.close();
		} catch (Exception e) {
			Log.e(Utils.TAG, "Extract file: "+zipFile.getAbsolutePath()+ " fail due to: "+e.toString());
			success = false;
		}
		return success;
	}
}
