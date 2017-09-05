/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.entity - GridviewAdapter.java
 * Date create: 2:57:16 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.entity;

import java.util.ArrayList;

import android.app.Activity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.util.Utils;
import com.arisvn.arissmarthiddenbox.util.lazyloading.ImageLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class GridviewAdapter.
 */
public class BaseviewAdapter extends BaseAdapter {
	/** The activity. */
	private Activity activity;
	/** The objs. */
	private ArrayList<FileItem> objs;
	/** The image loader. */
	protected ImageLoader imageLoader;
	/** The type. */
	private int type;

	private boolean isLoadThumbnail;


	/**
	 * Instantiates a new gridview adapter.
	 * 
	 * @param activity
	 *            the activity
	 * @param listen
	 *            the listen
	 */
	public BaseviewAdapter(Activity activity, ArrayList<FileItem> fileItems,
			int type) {
		super();
		this.activity = activity;
		this.type = type;
		imageLoader = new ImageLoader(activity, this.type);
		setLoadThumbnail(true);
		this.objs = fileItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (objs!=null) {
			return objs.size();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public FileItem getItem(int position) {
		return objs.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * The Class ViewHolder.
	 */
	public static class ViewHolder {
		/** The frame. */
		public View frame;
		/** The img view flag. */
		public ImageView imgViewFlag;
		/** The image_selected. */
		public ImageView image_selected;
		/** The txt view title. */
		public TextView txtViewTitle;
		/** The txt size. */
		public TextView txtSize;
	}
	/** The is show list. */
	boolean isShowList;
	/**
	 * Show list or Grid.
	 * 
	 * @param isShowList
	 *            the new show list
	 */
	public void setShowList(boolean isShowList){
		this.isShowList =isShowList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder view = (ViewHolder) convertView.getTag();
		FileItem obj = objs.get(position);

		if(objs.get(position).isCheck()){
			view.image_selected.setVisibility(View.VISIBLE);
			view.frame.setBackgroundResource(R.drawable.frame_bg);
		}else{
			view.image_selected.setVisibility(View.GONE);
			view.frame.setBackgroundResource(android.R.color.transparent);
		}

		if (isLoadThumbnail) {
				imageLoader.displayImage("" + obj.getId(), view.imgViewFlag);
				if (obj.getName() != null) {
					view.txtViewTitle.setText(obj.getName());
					Utils.setTypeface(activity, view.txtViewTitle);
					Utils.setTypeface(activity, view.txtSize);
				}
				view.txtSize.setText(Formatter.formatFileSize(activity,
						obj.getSize()));
				Log.e(Utils.TAG, "1");
		} else {
			Log.e(Utils.TAG, "3");
			view.txtViewTitle.setText(null);
			view.txtSize.setText(null);
			view.imgViewFlag.setImageBitmap(null);
		}
		return convertView;
	}

	/**
	 * Store checked position.
	 *
	 * @param pos
	 *            the pos
	 * @param isCheck
	 *            the is check
	 */
	private void setChecked(int pos,boolean isCheck) {
		objs.get(pos).setCheck(isCheck);
	}
	/**
	 * Select objects.
	 * 
	 */
	public void checkedAll() {
		if (getSelectedItem().size()==objs.size()) {
			return;
		}
		for (int i = 0; i < objs.size(); i++) {
			setChecked(i,true);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * De-select objects.
	 * 
	 */
	public void uncheckedAll() {
		if (getSelectedItem().size()==0) {
			return;
		}
		for (int i = 0; i < objs.size(); i++) {
			setChecked(i,false);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * Remove a object in adapter.
	 *
	 * @param obj
	 *            the obj
	 */
	public void remove(FileItem obj) {
		this.objs.remove(obj);
	}

	/**
	 * Remove objects in adapter.
	 *
	 * @param objs
	 *            the objs
	 */
	public void remove(ArrayList<FileItem> objs) {
		for (FileItem fileItem : objs) {
			this.objs.remove(fileItem);
		}
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
	 * Get selected objects.
	 *
	 * @return the selected item
	 */
	public ArrayList<FileItem> getSelectedItem() {
		ArrayList<FileItem> fileItems = new ArrayList<FileItem>();
		if (objs!=null&&objs.size()>0) {
			for (FileItem fileItem : objs) {
				if (fileItem.isCheck()) {
					fileItems.add(fileItem);
				}
			}
		}
		return fileItems;
	}

	/**
	 * @return the isLoadThumbnail
	 */
	public boolean isLoadThumbnail() {
		return isLoadThumbnail;
	}

	/**
	 * @param isLoadThumbnail
	 *            the isLoadThumbnail to set
	 */
	public void setLoadThumbnail(boolean isLoadThumbnail) {
		this.isLoadThumbnail = isLoadThumbnail;
	}

}
