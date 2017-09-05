/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.entity - FileItem.java
 * Date create: 2:57:06 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.entity;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class FileItem.
 */
public class FileItem implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The check. */
	private boolean check;
	
	/** The type. */
	private int type;
	
	/** The size. */
	private long size;
	
	/** The path from. */
	private String pathFrom;
	
	/** The path new. */
	private String pathNew;
	
	/** The extension. */
	private String extension;
	
	/** The name. */
	private String name;
	
	/** The thumbnail. */
	private byte[] thumbnail;

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(long size) {
		this.size = size;
	}


	/**
	 * Gets the thumbnail.
	 *
	 * @return the thumbnail
	 */
	public byte[] getThumbnail() {
		return thumbnail;
	}

	/**
	 * Sets the thumbnail.
	 *
	 * @param thumbnail the new thumbnail
	 */
	public void setThumbnail(byte [] thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the extension.
	 *
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the extension.
	 *
	 * @param extension the new extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/** The id. */
	private long id;

	/**
	 * Gets the path from.
	 *
	 * @return the path from
	 */
	public String getPathFrom() {
		return pathFrom;
	}

	/**
	 * Sets the path from.
	 *
	 * @param pathFrom the new path from
	 */
	public void setPathFrom(String pathFrom) {
		this.pathFrom = pathFrom;
	}

	/**
	 * Gets the path new.
	 *
	 * @return the path new
	 */
	public String getPathNew() {
		return pathNew;
	}

	/**
	 * Sets the path new.
	 *
	 * @param pathNew the new path new
	 */
	public void setPathNew(String pathNew) {
		this.pathNew = pathNew;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Checks if is check.
	 *
	 * @return true, if is check
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * Sets the check.
	 *
	 * @param check the new check
	 */
	public void setCheck(boolean check) {
		this.check = check;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id: "+id+ "type: "+type+ " size: "+size+ " old path: "+pathFrom+" new path"+pathNew;
	}

}
