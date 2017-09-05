/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - Compartor.java
 * Date create: 2:58:51 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.util;

import java.util.Comparator;

import com.arisvn.arissmarthiddenbox.entity.FileItem;

// TODO: Auto-generated Javadoc
/**
 * The Class Compartor.
 */
public class Compartor {

	/** Sort Name. */
	public static Comparator<FileItem> compareName = new Comparator<FileItem>() {
	
	@Override
		public int compare(FileItem lhs, FileItem rhs) {
			String name1 = lhs.getName().toUpperCase();
			String name2 = rhs.getName().toUpperCase();
			if(isAsc){
				return name1.compareTo(name2);
			}else{
				return name2.compareTo(name1);
			}
			
		}
	};
	
	/** Sort Size. */
	public static Comparator<FileItem> compareSize = new Comparator<FileItem>() {
		
		@Override
			public int compare(FileItem lhs, FileItem rhs) {
			
				long size1 = lhs.getSize();
				long size2 = rhs.getSize();
				if(isAsc){
					if(size1 == size2)
						return 0;
					else if(size1 < size2)
						return 1;
					else
						return -1;
				}else{
					if(size1 == size2)
						return 0;
					else if(size1 < size2)
						return -1;
					else
						return 1;
				}
				
			}
		};
		
	/** The is asc. */
	public static boolean isAsc;

}
