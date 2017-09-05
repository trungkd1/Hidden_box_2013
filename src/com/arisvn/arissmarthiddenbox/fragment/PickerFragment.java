/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.fragment - PickerFragment.java
 * Date create: 2:57:50 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.dialog.SynDialogFragment;
import com.arisvn.arissmarthiddenbox.entity.BaseviewAdapter;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.entity.GridAdapter;
import com.arisvn.arissmarthiddenbox.entity.ListAdapter;
import com.arisvn.arissmarthiddenbox.listener.Listener;
import com.arisvn.arissmarthiddenbox.listener.OnSelectedItemChangeListener;
import com.arisvn.arissmarthiddenbox.receiver.MediaScannerReceiver;
import com.arisvn.arissmarthiddenbox.util.Compartor;
import com.arisvn.arissmarthiddenbox.util.SaveData;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class PickerFragment.
 */
public class PickerFragment extends Fragment implements OnClickListener,OnItemClickListener, OnScrollListener {

	/** Called when the activity is first created. */

	public static final String _DATA = MediaStore.Images.Media.DATA;
	
	/** The Constant _ID. */
	public static final String _ID = MediaStore.Images.Media._ID;
	
	/** The Constant _DISPLAY_NAME. */
	public static final String _DISPLAY_NAME = MediaStore.Images.Media.DISPLAY_NAME;
	
	/** The Constant _MIME_TYPE. */
	public static final String _MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
	
	/** The Constant _SIZE. */
	public static final String _SIZE = MediaStore.Images.Media.SIZE;
	
	/** The m adapter. */
	private BaseviewAdapter mAdapter;

	/**
	 * Gets the m adapter.
	 *
	 * @return the mAdapter
	 */
	public BaseviewAdapter getmAdapter() {
		return mAdapter;
	}

	/**
	 * Sets the m adapter.
	 *
	 * @param mAdapter the mAdapter to set
	 */
	public void setmAdapter(BaseviewAdapter mAdapter) {
		this.mAdapter = mAdapter;
	}

	/** The grid view. */
	private GridView gridView;
	
	/** The list view. */
	private ListView listView;
	
	/** The objs. */
	private ArrayList<FileItem> objs;
	
	/** The m action. */
	private int mAction;
	
	/** The scan sd receiver. */
	private MediaScannerReceiver scanSdReceiver;
	
	/** The item change listener. */
	private OnSelectedItemChangeListener itemChangeListener;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		mAction = bundle.getInt(Utils.TYPE, 0);
		View view = inflater.inflate(R.layout.layout_picker, container, false);

		objs = new ArrayList<FileItem>();
		gridView = (GridView) view.findViewById(R.id.gridView1);
		listView = (ListView) view.findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(this);
		listView.setOnScrollListener(this);
		showMode();
		SynDialogFragment.show(getActivity(), getString(R.string.loading));
		scanSdReceiver = new MediaScannerReceiver(listen);
		scandFile();
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			itemChangeListener =(OnSelectedItemChangeListener) activity;
			itemChangeListener.onChangedData(false,mAction);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(Utils.TAG, activity.getClass().getName() + " have to implement OnSelectedItemChangeListener");
		}
	
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		FileItem fileItem = objs.get(position);
		boolean isCheck = fileItem.isCheck();
		fileItem.setCheck(!isCheck);
		ImageView image_selected = (ImageView) view
				.findViewById(R.id.image_selected);
		View frame = view.findViewById(R.id.frame_layout);
		if (image_selected != null) {
			if (fileItem.isCheck()) {
				image_selected.setVisibility(View.VISIBLE);
				frame.setBackgroundResource(R.drawable.frame_bg);
			} else {
				image_selected.setVisibility(View.GONE);
				frame
						.setBackgroundResource(android.R.color.transparent);
			}
			//just update action when selected items turn from 0 to 1 or vice versa.
			int numselectedItem=mAdapter.getSelectedItem().size();
			if (numselectedItem==0||numselectedItem==1) {
				if (itemChangeListener != null) {
					itemChangeListener.onSelectedItemChanged(numselectedItem);
				}
			}
		}
	}
	
	/**
	 * Select all.
	 */
	public void selectAll(){
		mAdapter.checkedAll();
		if (itemChangeListener!=null) {
			itemChangeListener.onSelectedItemChanged(mAdapter.getCount());
		}
	}
	
	/**
	 * De- select all.
	 */
	public void deselectAll(){
		mAdapter.uncheckedAll();
		if (itemChangeListener!=null) {
			itemChangeListener.onSelectedItemChanged(0);
		}
		
	}

	
	/**
	 * Show mode.
	 */
	private void showMode() {
		if (SaveData.getInstance(getActivity()).isShowList()) {
			showListMode();
		} else {
			showGridMode();
		}
	}
	
	/**
	 * Display format's gridview.
	 */
	public void showGridMode() {
		gridView.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		
		mAdapter = new GridAdapter(getActivity(),objs,mAction);
		gridView.setAdapter(mAdapter);
		listView.removeAllViewsInLayout();

		SaveData.getInstance(getActivity()).setShowList(false);
	}

	/**
	 * Display format's listview.
	 */
	public void showListMode() {
		listView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		
		mAdapter = new ListAdapter(getActivity(),objs,mAction);
		listView.setAdapter(mAdapter);
		gridView.removeAllViewsInLayout();

		SaveData.getInstance(getActivity()).setShowList(true);
	}
	

	/**
	 * Load all file via type after scanning SDcard.
	 *
	 * @param type the type
	 */
	private void loadItem(int type) {
		final String[] columns = { _DATA, _DISPLAY_NAME, _ID, _SIZE, _MIME_TYPE };

		Cursor cursor = null;
		if (type == Utils.TYPE_AUDIO) {
			cursor = getActivity().getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
					null, _DISPLAY_NAME);
		} else if (type == Utils.TYPE_VIDEO) {
			String[] strings = { "video/%" };
			cursor = getActivity().getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns,
					_MIME_TYPE + " like ?", strings, _DISPLAY_NAME);
		} else if (type == Utils.TYPE_PHOTO) {
			cursor = getActivity().getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, _DISPLAY_NAME);
		}
		// get all data from cursor
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int ID_column = cursor.getColumnIndex(_ID);
				do {
					FileItem item = new FileItem();
					String filePath = cursor.getString(cursor
							.getColumnIndex(_DATA));
					String externalStorageDirectory = Environment
							.getExternalStorageDirectory().getAbsolutePath();
					if (filePath.startsWith(externalStorageDirectory)) {
						int id = cursor.getInt(ID_column);
						item.setPathFrom(filePath);
					item.setName(cursor.getString(cursor
							.getColumnIndex(_DISPLAY_NAME)));
					item.setId(id);
					item.setType(type);
						item.setSize(cursor.getLong(cursor
								.getColumnIndex(_SIZE)));
					objs.add(item);
					}

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		// end
		getActivity().unregisterReceiver(scanSdReceiver);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
	}
	
	/**
	 * Hide file.
	 */
	public void HideFile(){
		new HideFilesTask().execute();
	}

	/**
	 * Create Thread for processing encode file.
	 * 
	 */
	class HideFilesTask extends AsyncTask<Void, Void, Boolean> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SynDialogFragment.show(getActivity(),getString(R.string.hiding));
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Utils.processEncrypt(getActivity(), objs);
                //end copying DB
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (getActivity()!=null) {
			    SynDialogFragment.dismiss(getActivity());
			    try {
			    	if (result) {
			    		if(itemChangeListener != null)
			    			itemChangeListener.onChangedData(result,mAction);
							getActivity().onBackPressed();
					} else {
						mAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(Utils.TAG, "HideFilesTask::onPostExecute: "+e.toString());
				}
				
			}
		}

	}
	
	
	/**
	 * The Class LoadFilesTask.
	 */
	class LoadFilesTask extends AsyncTask<Void, Void, Boolean> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			loadItem(mAction);
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (getActivity()==null) {
				return;
			}
			showMode();
			SynDialogFragment.dismiss(getActivity());
			if (itemChangeListener!=null&&objs!=null) {
				itemChangeListener.onFinishLoadItems();
			}
			
			if (gridView.getVisibility()==View.VISIBLE) {
				updateUI(gridView);
			} else {
				updateUI(listView);
			}
		}
	}
	/**
	 * Start scan all file in SDcard, after completing will call receiver.
	 */
	public void scandFile() {
		IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");

		getActivity().registerReceiver(scanSdReceiver, intentfilter);
		getActivity().sendBroadcast(
				new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	/** Listener when items in box be changed. */
	private Listener listen = new Listener() {

		@Override
		public void doListenner() {
			new LoadFilesTask().execute();
		}

		@Override
		public void doListenner(int count) {
			//update action bar items base on selected items.
			if (itemChangeListener!=null) {
				itemChangeListener.onSelectedItemChanged(count);
			}

		}
	};

	/**
	 * Sort via name or size of file.
	 *
	 * @param id the id
	 * @param isAsc the is asc
	 */
	public void sort(int id,boolean isAsc) {
		Compartor.isAsc = isAsc;
		switch (id) {
		case R.id.menu_popup_sort_name:
 			Collections.sort(objs, Compartor.compareName);
			break;
		case R.id.menu_popup_sort_size:
			Collections.sort(objs, Compartor.compareSize);
			break;
		}
		if (gridView.getVisibility()==View.VISIBLE) {
			updateUI(gridView);
		} else {
			updateUI(listView);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int first = view.getFirstVisiblePosition();
	    int count = view.getChildCount();
		if (scrollState == SCROLL_STATE_IDLE || (first + count > mAdapter.getCount()) ) {
			updateUI(view);
        } else {
			mAdapter.setLoadThumbnail(false);
		}
	}
	private void updateUI(AbsListView absListView){
		mAdapter.setLoadThumbnail(true);
		mAdapter.notifyDataSetChanged();
	}

}
