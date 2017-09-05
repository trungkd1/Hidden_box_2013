/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.fragment - BaseFragment.java
 * Date create: 2:32:37 PM - Nov 8, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.arisvn.arissmarthiddenbox.App;
import com.arisvn.arissmarthiddenbox.CategoryActivity;
import com.arisvn.arissmarthiddenbox.MediaPlayerActivity;
import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.dialog.ConfirmDialogFragment;
import com.arisvn.arissmarthiddenbox.dialog.SynDialogFragment;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.entity.BaseviewAdapter;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.entity.GridAdapter;
import com.arisvn.arissmarthiddenbox.entity.ListAdapter;
import com.arisvn.arissmarthiddenbox.listener.FileDialogListener;
import com.arisvn.arissmarthiddenbox.listener.Listener;
import com.arisvn.arissmarthiddenbox.listener.OnSelectedItemChangeListener;
import com.arisvn.arissmarthiddenbox.util.HiddenBoxDBUtil;
import com.arisvn.arissmarthiddenbox.util.SaveData;
import com.arisvn.arissmarthiddenbox.util.Utils;
import com.arisvn.arissmarthiddenbox.util.ZipUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseFragment.
 */
public abstract class BaseFragment extends Fragment implements
		OnItemClickListener, FileDialogListener, OnScrollListener {
	
	/** The Constant PLAY. */
	public final static int PLAY = 99;

	/** The objs. */
	protected ArrayList<FileItem> objs;

	/** The grid view. */
	protected GridView gridView;
	
	/** The list view. */
	protected ListView listView;

	/** The m adapter. */
	protected BaseviewAdapter mAdapter;

	/**
	 * @return the mAdapter
	 */
	public BaseviewAdapter getmAdapter() {
		return mAdapter;
	}
	
	public boolean flagChangeData;

	/**
	 * @param mAdapter the mAdapter to set
	 */
	public void setmAdapter(BaseviewAdapter mAdapter) {
		this.mAdapter = mAdapter;
	}

	/** The m type. */
	protected int mType = 0;


	/** The is edit. */
	protected boolean isEdit;
	
	/**
	 * Checks if is edits the.
	 *
	 * @return the isEdit
	 */
	public boolean isEdit() {
		return isEdit;
	}

	/**
	 * Sets the edits the.
	 *
	 * @param isEdit the isEdit to set
	 */
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	/** The item change listener. */
	protected OnSelectedItemChangeListener itemChangeListener;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		makeFolder();
		View view = inflater
				.inflate(R.layout.layout_base_box, container, false);
		gridView = (GridView) view.findViewById(R.id.gridView1);
		listView = (ListView) view.findViewById(R.id.listView1);
		gridView.setOnItemClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		gridView.setOnScrollListener(this);
		new LoadHiddenFileTask().execute();
		showMode();
		return view;
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		flagChangeData = true;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.e(Utils.TAG, "onAttach");
		objs=new ArrayList<FileItem>();
		try {
			itemChangeListener =(OnSelectedItemChangeListener) activity;
			if (activity instanceof CategoryActivity) {
				isEdit=((CategoryActivity)activity).isEdit();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(Utils.TAG, activity.getClass().getName() + " have to implement OnSelectedItemChangeListener");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLAY) {
		App.needShowLogin=false;
		getActivity().supportInvalidateOptionsMenu();
	}
		
	}
	/**
	 * Show manage.
	 */
	public void manage(){
		isEdit = true;
	}
	
	/**
	 * Unhide file.
	 */
	public void unHide(){
		onUnHide();
	}
	
	/**
	 * Delete selected files.
	 */
	public void delete(){
//		onDelete();
		new DeleteTask().execute();
	}
	
	/**
	 * Cancel.
	 */
	public void cancel(){
		isEdit = false;
		mAdapter.uncheckedAll();
		if (itemChangeListener!=null) {
			itemChangeListener.onSelectedItemChanged(0);
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
	 * Display format's gridview.
	 */
	public void showGridMode() {
		gridView.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		
		mAdapter = null;
		mAdapter = new GridAdapter(getActivity(),objs,Utils.TYPE_THUMBNAIL);
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
		
		mAdapter = new ListAdapter(getActivity(),objs,Utils.TYPE_THUMBNAIL);
		listView.setAdapter(mAdapter);
		gridView.removeAllViewsInLayout();
		
		SaveData.getInstance(getActivity()).setShowList(true);
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
	 * Start backup progress.
	 */
	public void startBackupProgress() {
		// TODO Auto-generated method stub
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(
				Utils.SELECT_FILE_DIALOG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = SelectFileDialog.newInstance(Utils.EXPORT,
				this);
		newFragment.show(ft, Utils.SELECT_FILE_DIALOG);

	}

	/**
	 * Start import progress.
	 */
	public void startRestoreFilesProgress() {
		// TODO Auto-generated method stub
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(
				Utils.SELECT_FILE_DIALOG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = SelectFileDialog.newInstance(Utils.IMPORT,
				this);
		newFragment.show(ft, Utils.SELECT_FILE_DIALOG);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> apdater, View view, int position,
			long id) {
		
		if(isEdit){
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
			}
			//just update action when selected items turn from 0 to 1 or vice versa.
			int numSelectedItems=mAdapter.getSelectedItem().size();
			if (numSelectedItems==0||numSelectedItems==1) {
				if (itemChangeListener != null) {
					itemChangeListener.onSelectedItemChanged(numSelectedItems);
				}
			}
			
		} else {
			FileItem obj = objs.get(position);
			onPlay(obj);
		}
		
	}

	/**
	 * Create class Restore file using asynctask.
	 */
	protected class TaskUnHide extends AsyncTask<Void, Void, Boolean> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SynDialogFragment.show(getActivity(), getString(R.string.unhiding));
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				Utils.processDecrypt(getActivity(), objs);
			} catch (Exception e1) {
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
			if (getActivity()==null) {
				return;
			}
			mAdapter.notifyDataSetChanged();
			Utils.refreshActionBar(getActivity());
            SynDialogFragment.dismiss(getActivity());
            if (itemChangeListener!=null) {
				itemChangeListener.onSelectedItemChanged(0);
			}
		}
	}


	/** Listener when items in box be changed. */
	Listener listen = new Listener() {

		@Override
		public void doListenner() {
			if (gridView.getVisibility()==View.VISIBLE) {
				updateUI(gridView);
			} else {
                updateUI(listView);
			}
	        if (itemChangeListener!=null) {
				itemChangeListener.onFinishLoadItems();
			}
			SynDialogFragment.dismiss(getActivity());
		}

		@Override
		public void doListenner(int count) {
			//Update action items base on selected items
			if (itemChangeListener!=null) {
				itemChangeListener.onSelectedItemChanged(count);
			}
			
		}
	};
	
	
	/**
	 * Picker action.
	 */
	public void pickerAction(){
		onPicker();
	}

	/**
	 * Navigate to picker screen to choose files need to encode.
	 */
	protected abstract void onPicker();

	/**
	 * Play file.
	 *
	 * @param path the path
	 */
	protected  void onPlay(FileItem obj){
		if(Utils.isSDCardExist()){
			App.needShowLogin = false;
			Intent intent=new Intent(getActivity(), MediaPlayerActivity.class);
			intent.putExtra(Utils.FILE_PATH,obj);
			startActivityForResult(intent,PLAY);
		}else{
			ConfirmDialogFragment.show(getActivity(), getString(R.string.unmount_sdcard));
		}
	}

	/**
	 * Restore files that imported before.
	 */
	protected void onUnHide() {
		if(Utils.isSDCardExist()){
			  new TaskUnHide().execute();
		}else{
			ConfirmDialogFragment.show(getActivity(), getString(R.string.unmount_sdcard));
		}
       
	}

	/**
	 * Create folder contain encoded files.
	 */
	protected abstract void makeFolder();

	/**
	 * Load encoded files.
	 */
	protected abstract List<FileItem> loadFile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arisvn.arissmarthiddenbox.listener.FileDialogListener#onFileSelected
	 * (java.lang.String, int)
	 */
	@Override
	public void onFileSelected(String path, int mode) {
		// TODO Auto-generated method stub
		String[] params = { path, "" + mode };
		switch (mode) {
		case Utils.EXPORT:
			new BackUpAndRestoreTask().execute(params);
			break;
		case Utils.IMPORT:
			new BackUpAndRestoreTask().execute(params);
		default:
			break;
		}

	}

	/**
	 * The Class BackUpAndRestoreTask.
	 */
	class BackUpAndRestoreTask extends AsyncTask<String, Void, Boolean> {
		
		/** The params. */
		private String[] params;

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SynDialogFragment.show(getActivity());
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (params != null && params.length == 2) {
				this.params = params;
				String filePath = params[0];
				int mode = Integer.parseInt(params[1]);
				switch (mode) {
				case Utils.EXPORT:
					List<FileItem> fileItems = mAdapter.getSelectedItem();
					List<File> files = new ArrayList<File>();
					for (FileItem fileItem : fileItems) {
						File file = new File(fileItem.getPathNew());
						files.add(file);
						String pathFrom = fileItem.getPathFrom();
						if (pathFrom.startsWith(Utils.SDCARD)) {
							pathFrom = pathFrom.substring(pathFrom
									.indexOf(Utils.SDCARD)
									+ Utils.SDCARD.length());
						}
						fileItem.setPathFrom(pathFrom);
						String pathTo = fileItem.getPathNew();
						if (pathTo.startsWith(Utils.FOLDER)) {
							pathTo = pathTo.substring(pathTo
									.indexOf(Utils.FOLDER)
									+ Utils.FOLDER.length());
						}
						fileItem.setPathNew(pathTo);
					}
					Utils.writeObject(Utils.FOLDER + "/" + Utils.BACKUP_SQL,
							fileItems);
					File sqlFile = new File(Utils.FOLDER + "/"
							+ Utils.BACKUP_SQL);
					if (sqlFile.exists()) {
						files.add(sqlFile);
						if (ZipUtil.compress(files, filePath)) {
							return Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
						} else {
							File file=new File(filePath);
							file.delete();
							ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.backup_error), filePath));
						}
					}
					break;
				case Utils.IMPORT:
					if (Utils.decrypt(getActivity(), filePath)) {
						if (ZipUtil.extract(new File(filePath),
								Utils.RESTORE_FOLDER)) {
							File file = new File(Utils.RESTORE_FOLDER + "/"
									+ Utils.BACKUP_SQL);
							if (file.exists()) {
								List<FileItem> fileItemsRestore = Utils
										.readObject(file.getAbsolutePath());
								if (fileItemsRestore != null
										&& fileItemsRestore.size() > 0) {
									File restoreFolder = new File(
											Utils.RESTORE_FOLDER);
									if (restoreFolder.exists()) {
										String[] restorefiles = restoreFolder
												.list();
										for (FileItem fileItem : fileItemsRestore) {
											for (int i = 0; i < restorefiles.length; i++) {
												String fileName = fileItem
														.getPathNew()
														.substring(
																fileItem.getPathNew()
																		.lastIndexOf(
																				"/") + 1);
												if (fileName
														.equals(restorefiles[i])) {
													// move file into this
													// location in
													// encrypted folder and
													// insert into
													// DB
													//Append sdcard folder into the start of pathNew and pathFrom
													if (!fileItem.getPathNew().startsWith(Utils.FOLDER)) {
														fileItem.setPathNew(Utils.FOLDER+fileItem.getPathNew());
													}
													if (!fileItem.getPathFrom().startsWith(Utils.SDCARD)) {
														fileItem.setPathFrom(Utils.SDCARD+fileItem.getPathFrom());
													}
													File filePathNew = new File( fileItem
																			.getPathNew());
													File restoreFile = new File(
															Utils.RESTORE_FOLDER
																	+ "/"
																	+ restorefiles[i]);
													if (filePathNew.exists()&&!App.getDB().isEncryptedFileExist(fileItem.getPathNew())) {
														filePathNew.delete();
													}
													if (!filePathNew.exists()) {
														// Just move file into
														// encrypted
														// folder if it's not
														// exist now.
														restoreFile
																.renameTo(filePathNew);
														App.getDB()
																.insertFile(
																		fileItem);
														HiddenBoxDBUtil.getInstance().insertFileSdcardDB(fileItem);
													} else {
														Log.e(Utils.TAG,
																filePathNew
																		.exists()
																		+ " is exist now. Can not restore file");
													}
													break;

												}
											}
										}
										Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
										return true;
									} else {
										Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
										Log.e(Utils.TAG, "restoreFolder is not exist");
										ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.no_restore_folder), Utils.RESTORE_FOLDER));
									}

								} else {
									Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
									Log.e(Utils.TAG, "Can not read fileItems in backup file");
									ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.restore_error_nofiles), filePath));
								}

							} else {
								Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
								Log.e(Utils.TAG, Utils.RESTORE_FOLDER + "/"
										+ Utils.BACKUP_SQL + " is not exist");
								ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.restore_error), filePath));
							}
						} else {
							//Can not extract back up file 
							Utils.encrypt(getActivity(), filePath,Encryption.BYTES_TO_ENCRYPT_PHOTO);
							ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.restore_error), filePath));
						}
					} else {
						Log.e(Utils.TAG, "filePath :"+filePath+" is not a backup file for this app");
						ConfirmDialogFragment.show(getActivity(), String.format(getString(R.string.restore_error), filePath));
					}
					break;
				default:
					break;
				}
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (getActivity()==null) {
				return;
			}
			
			//empty restore folder
			File restoreFolder = new File(
					Utils.RESTORE_FOLDER);
			Utils utils = new Utils();
			utils.emptyRestoreFolder(restoreFolder
					.getAbsolutePath());
			if (result && params != null && params.length == 2) {
				int mode = Integer.parseInt(params[1]);
				switch (mode) {
				case Utils.EXPORT:
					File sqlFile = new File(Utils.FOLDER + "/"
							+ Utils.BACKUP_SQL);
					if (sqlFile.exists()) {
						sqlFile.delete();
					}
					break;
				case Utils.IMPORT:
					if(objs != null){
						objs.clear();
						objs.addAll(loadFile());
					}
					
					if (gridView.getVisibility()==View.VISIBLE) {
						updateUI(gridView);
					}else {
						updateUI(listView);
					}
					if (itemChangeListener!=null) {
						itemChangeListener.onFinishLoadItems();
					}
					break;

				default:
					break;
				}
			}
			SynDialogFragment.dismiss(getActivity());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Activity activity=getActivity();
		if (activity instanceof CategoryActivity) {
			((CategoryActivity)activity).setEdit(false);
		}
	}
	
	class LoadHiddenFileTask extends AsyncTask<Void, Void, List<FileItem>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SynDialogFragment.show(getActivity(), getString(R.string.loading));
		}

		@Override
		protected List<FileItem> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(flagChangeData){
				return loadFile();
			}else{
				return objs;
			}
			
		}
		@Override
		protected void onPostExecute(List<FileItem> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			SynDialogFragment.dismiss(getActivity());
			synchronized (objs) {
				if(result.size() != objs.size()){
					if (objs!=null) {
						objs.clear();
					}	
					objs.addAll(result);
				}
				
			}
			if (gridView.getVisibility()==View.VISIBLE) {
				updateUI(gridView);
			} else {
                updateUI(listView);
			}
			if (itemChangeListener!=null) {
				itemChangeListener.onFinishLoadItems();
			}
		}

	}
	
	
	/**
	 * Class delete file
	 *
	 */
	class DeleteTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SynDialogFragment.show(getActivity(), getString(R.string.deleting));
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			if(Utils.isSDCardExist()){
				for (Iterator<FileItem> iterator = objs.iterator(); iterator.hasNext();) {
					FileItem obj = (FileItem) iterator.next();
					if (!obj.isCheck())
						continue;
					File file = new File(obj.getPathNew());
					if(file.delete()){
						App.getDB().deleteFile(obj);
						iterator.remove();
						HiddenBoxDBUtil.getInstance().deleteFile(obj);
					}else{
						//File is deleted on SDCard
						if(!file.exists()){
							App.getDB().deleteFile(obj);
							iterator.remove();
							HiddenBoxDBUtil.getInstance().deleteFile(obj);
						}
					}
				}
		
			}else{
				ConfirmDialogFragment.show(getActivity(), getString(R.string.unmount_sdcard));
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			SynDialogFragment.dismiss(getActivity());
			mAdapter.notifyDataSetChanged();
					Utils.refreshActionBar(getActivity());
					
					// end copying DB
					if (itemChangeListener!=null) {
						itemChangeListener.onSelectedItemChanged(0);
						itemChangeListener.onFinishLoadItems();
					}
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
		// TODO Auto-generated method stub
		mAdapter.setLoadThumbnail(true);
		mAdapter.notifyDataSetChanged();
	}
}
