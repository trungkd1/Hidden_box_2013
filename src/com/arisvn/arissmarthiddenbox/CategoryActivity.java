/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - CategoryActivity.java
 * Date create: 2:39:22 PM - Nov 8, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.arisvn.arissmarthiddenbox.dialog.ConfirmDialogFragment;
import com.arisvn.arissmarthiddenbox.dialog.PasswordDialog;
import com.arisvn.arissmarthiddenbox.dialog.PrivacyDialog;
import com.arisvn.arissmarthiddenbox.dialog.UserGuideDialog;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.fragment.BaseFragment;
import com.arisvn.arissmarthiddenbox.fragment.CategoryFragment;
import com.arisvn.arissmarthiddenbox.fragment.PickerFragment;
import com.arisvn.arissmarthiddenbox.listener.DialogListener;
import com.arisvn.arissmarthiddenbox.listener.OnSelectedItemChangeListener;
import com.arisvn.arissmarthiddenbox.util.SaveData;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryActivity.
 */
public class CategoryActivity extends ActionBarActivity implements OnBackStackChangedListener,
OnClickListener, OnSelectedItemChangeListener {

	/** The action bar. */
	private ActionBar actionBar;
	
	/** The is edit. */
	private boolean isEdit = false;
	
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

	/** The is asc. */
	private boolean isAsc = false;
	
	/** The tag. */
	private String tag;
	
	/** The password_dialog. */
	private PasswordDialog password_dialog;
    
    /** The privacy_dialog. */
    private PrivacyDialog privacy_dialog;
    
    /** The user guide dialog. */
    private UserGuideDialog userGuideDialog;
	
	/** The popup window. */
	private PopupWindow popupWindow;
	
	/** The popup view. */
	private View popupView;
	
	/** The menu_popup_change_pass. */
	private LinearLayout menu_popup_change_pass;
	
	/** The menu_popup_user_guide. */
	private LinearLayout menu_popup_user_guide;
	
	/** The menu_popup_sort_size. */
	private LinearLayout menu_popup_sort_size;
	
	/** The menu_popup_sort_name. */
	private LinearLayout menu_popup_sort_name;
	
	/** The menu_popup_sort. */
	private LinearLayout menu_popup_sort;
	
	/** The menu_popup_display_gallery. */
	private LinearLayout menu_popup_display_gallery;
	
	/** The menu_popup_display_list. */
	private LinearLayout menu_popup_display_list;
	
	/** The menu_popup_add. */
	private LinearLayout menu_popup_add;
	
	/** The menu_popup_manage. */
	private LinearLayout menu_popup_manage;
	
	/** The menu_popup_unhide. */
	private View menu_popup_unhide;
	
	/** The menu_popup_hide. */
	private View menu_popup_hide;
	
	/** The menu_popup_delete. */
	private View menu_popup_delete;
	
	/** The menu_popup_restore. */
	private LinearLayout menu_popup_restore;
	
	/** The menu_popup_backup. */
	private LinearLayout menu_popup_backup;
	
	/** The menu_popup_select_all. */
	private LinearLayout menu_popup_select_all;
	
	/** The menu_popup_deselect_all. */
	private View menu_popup_deselect_all;
	
	/** The image_sort_size. */
	private ImageView image_sort_size;
	
	/** The image_sort_name. */
	private ImageView image_sort_name;

    /* (non-Javadoc)
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	// Clear all back stack.
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int i = 0; i < backStackCount; i++) {
			// Get the back stack fragment id.
			int backStackId = getSupportFragmentManager()
					.getBackStackEntryAt(i).getId();

			getSupportFragmentManager().popBackStack(backStackId,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);

		}
        App.needShowLogin = false;
        actionBar();
        onInit();
        onMenu();
        if (SaveData.getInstance(this).isFirstTime()) {
            showPrivacy();
        }

    }

    /**
     * Show privacy.
     */
    private void showPrivacy() {
        privacy_dialog = new PrivacyDialog(CategoryActivity.this, new DialogListener() {

            @Override
            public void cancelListenr() {
                // TODO Auto-generated method stub
                finish();
            }
            @Override
            public void acceptListenr() {
                // TODO Auto-generated method stub
                SaveData.getInstance(CategoryActivity.this) .setFirstTime(false);
                privacy_dialog.dismiss();

            }
        });
        privacy_dialog.setCancelable(false);
        privacy_dialog.show();
    }

    /**
     * Action bar.
     */
    private void actionBar() {
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_title);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
    }

	/**
	 * On init.
	 */
	private void onInit() {
		FragmentManager manager = getSupportFragmentManager();
		manager.addOnBackStackChangedListener(this);
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container, new CategoryFragment());
		transaction.addToBackStack(Utils.FRAGMENT_CATEGORY);
		transaction.commit();

	}

	/**
	 * Create popup menu such as sort name, sort size....
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void onMenu() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupView = layoutInflater.inflate(R.layout.layout_menu, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable().getCurrent());

		image_sort_size = (ImageView)popupView.findViewById(R.id.image_sort_size);
		image_sort_name = (ImageView)popupView.findViewById(R.id.image_sort_name);
		
		menu_popup_change_pass = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_change_pass);
		menu_popup_user_guide = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_user_guide);
		menu_popup_sort_size = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_sort_size);
		menu_popup_sort_name = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_sort_name);
		menu_popup_sort = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_sort);
		menu_popup_display_gallery = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_display_gallery);
		menu_popup_display_list = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_display_list);
		menu_popup_add = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_add);
		menu_popup_manage = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_manage);
		menu_popup_unhide = popupView
				.findViewById(R.id.menu_popup_unhide);
		menu_popup_hide = popupView
				.findViewById(R.id.menu_popup_hide);
		menu_popup_delete = popupView
				.findViewById(R.id.menu_popup_delete);
		menu_popup_restore = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_restore);
		menu_popup_backup = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_backup);
		menu_popup_select_all = (LinearLayout) popupView
				.findViewById(R.id.menu_popup_select_all);
		menu_popup_deselect_all = popupView
				.findViewById(R.id.menu_popup_deselect_all);

		menu_popup_change_pass.setOnClickListener(this);
		menu_popup_user_guide.setOnClickListener(this);
		menu_popup_sort_size.setOnClickListener(this);
		menu_popup_sort_name.setOnClickListener(this);
		menu_popup_sort.setOnClickListener(this);
		menu_popup_display_gallery.setOnClickListener(this);
		menu_popup_display_list.setOnClickListener(this);
		menu_popup_add.setOnClickListener(this);
		menu_popup_manage.setOnClickListener(this);
		menu_popup_unhide.setOnClickListener(this);
		menu_popup_hide.setOnClickListener(this);
		menu_popup_delete.setOnClickListener(this);
		menu_popup_restore.setOnClickListener(this);
		menu_popup_backup.setOnClickListener(this);
		menu_popup_select_all.setOnClickListener(this);
		menu_popup_deselect_all.setOnClickListener(this);

		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_sort_name)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_sort_size)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_sort)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_userguide)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_changepass)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_gallery)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_list)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_add)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_mange)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_unhide)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_hide)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_delete)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_restore)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_backup)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_select_all)));
		Utils.setTypeface(this, ((TextView) popupView.findViewById(R.id.text_deselect_all)));

	}


	/**
	 * Display popup menu.
	 *
	 * @param anchor the anchor
	 */
	private void showPopupMenu(View anchor) {
		refreshMenu();
		if (anchor.getId() == R.id.menu_settings) {
			menu_popup_change_pass.setVisibility(View.VISIBLE);
			menu_popup_user_guide.setVisibility(View.VISIBLE);

		} else if (anchor.getId() == R.id.menu_mode) {
			menu_popup_display_list.setVisibility(View.VISIBLE);
			menu_popup_display_gallery.setVisibility(View.VISIBLE);

		} else if (anchor.getId() == R.id.menu_popup_sort) {
			menu_popup_sort_size.setVisibility(View.VISIBLE);
			menu_popup_sort_name.setVisibility(View.VISIBLE);

		} else if (anchor.getId() == R.id.menu_edit_show) {
			if (isEdit) {
				menu_popup_unhide.setVisibility(View.VISIBLE);
				menu_popup_delete.setVisibility(View.VISIBLE);
				menu_popup_restore.setVisibility(View.VISIBLE);
				menu_popup_backup.setVisibility(View.VISIBLE);
				menu_popup_select_all.setVisibility(View.VISIBLE);
				menu_popup_deselect_all.setVisibility(View.VISIBLE);

			} else {
				if(tag.equals(Utils.FRAGMENT_PICKER)){
					menu_popup_select_all.setVisibility(View.VISIBLE);
					menu_popup_deselect_all.setVisibility(View.VISIBLE);
					menu_popup_sort.setVisibility(View.VISIBLE);
					menu_popup_hide.setVisibility(View.VISIBLE);
				}else{
					menu_popup_add.setVisibility(View.VISIBLE);
					menu_popup_manage.setVisibility(View.VISIBLE);
				}
				
			}
		}
		
			if (popupWindow.isShowing()) {
				dissmisPopupMenu();
			} else {
				popupWindow.showAsDropDown(anchor);
			}
		
	}
	
	/**
	 * Display popup menu sort from Sort's parent.
	 *
	 * @param anchor the anchor
	 */
	private void showPopupMenuSort(View anchor) {
		refreshMenu();
		menu_popup_sort_name.setVisibility(View.VISIBLE);
		menu_popup_sort_size.setVisibility(View.VISIBLE);
				popupWindow.showAsDropDown(anchor);
		
	}

	/**
	 * Dissmis popup menu.
	 */
	private void dissmisPopupMenu() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	/**
	 * Refresh menu after changing UI.
	 */
	private void refreshMenu() {
		menu_popup_change_pass.setVisibility(View.GONE);
		menu_popup_user_guide.setVisibility(View.GONE);
		menu_popup_display_gallery.setVisibility(View.GONE);
		menu_popup_display_list.setVisibility(View.GONE);
		menu_popup_sort_name.setVisibility(View.GONE);
		menu_popup_sort_size.setVisibility(View.GONE);
		menu_popup_sort.setVisibility(View.GONE);
		menu_popup_add.setVisibility(View.GONE);
		menu_popup_manage.setVisibility(View.GONE);
		menu_popup_unhide.setVisibility(View.GONE);
		menu_popup_hide.setVisibility(View.GONE);
		menu_popup_delete.setVisibility(View.GONE);
		menu_popup_restore.setVisibility(View.GONE);
		menu_popup_backup.setVisibility(View.GONE);
		menu_popup_select_all.setVisibility(View.GONE);
		menu_popup_deselect_all.setVisibility(View.GONE);
	}

	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() !=R.id.menu_popup_sort )
		dissmisPopupMenu();
		
		switch (v.getId()) {
		case R.id.menu_popup_change_pass:
			showDialog();
			((ImageView) popupView.findViewById(R.id.icon_changepass))
					.performClick();
			break;

		case R.id.menu_popup_user_guide:
            showUserGuide();
			((ImageView) popupView.findViewById(R.id.icon_userguide))
					.performClick();
			break;

		case R.id.menu_popup_sort_size:
			((PickerFragment) getSupportFragmentManager()
					.findFragmentByTag(tag)).sort(R.id.menu_popup_sort_size,isAsc);
			SaveData.getInstance(this).setSort(Utils.SORT_SIZE);
			isAsc =! isAsc;
			break;

		case R.id.menu_popup_sort_name:
			((PickerFragment) getSupportFragmentManager()
					.findFragmentByTag(tag)).sort(R.id.menu_popup_sort_name,isAsc);
			SaveData.getInstance(this).setSort(Utils.SORT_NAME);
			isAsc =! isAsc;
			break;
			
		case R.id.menu_popup_sort:
			image_sort_size.setVisibility(View.INVISIBLE);
			image_sort_name.setVisibility(View.INVISIBLE);
			
			if(SaveData.getInstance(this).getSort() == Utils.SORT_SIZE){
				image_sort_size.setImageResource(isAsc ?R.drawable.menu_sort_desc_bg : R.drawable.menu_sort_asc_bg);
				image_sort_size.setVisibility(View.VISIBLE);
			}else if(SaveData.getInstance(this).getSort() == Utils.SORT_NAME){
				image_sort_name.setImageResource(isAsc ?R.drawable.menu_sort_desc_bg : R.drawable.menu_sort_asc_bg);
				image_sort_name.setVisibility(View.VISIBLE);
			}
			menu_popup_select_all.setVisibility(View.VISIBLE);
			menu_popup_deselect_all.setVisibility(View.VISIBLE);
			menu_popup_sort.setVisibility(View.VISIBLE);
			showPopupMenuSort(v);
			break;

		case R.id.menu_popup_display_gallery:
			if (SaveData.getInstance(this).isShowList()) {
			if (tag.equals(Utils.FRAGMENT_PICKER)) {
				((PickerFragment) getSupportFragmentManager()
						.findFragmentByTag(tag)).showGridMode();
			} else {
				((BaseFragment) getSupportFragmentManager().findFragmentByTag(
						tag)).showGridMode();
			}
			}
			break;

		case R.id.menu_popup_display_list:
			if (!SaveData.getInstance(this).isShowList()) {
			if (tag.equals(Utils.FRAGMENT_PICKER)) {

				((PickerFragment) getSupportFragmentManager()
						.findFragmentByTag(tag)).showListMode();
			} else {
				((BaseFragment) getSupportFragmentManager().findFragmentByTag(
						tag)).showListMode();
			}
			}
			break;

		case R.id.menu_popup_add:
			if(Utils.isSDCardExist()){
				((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
				.pickerAction();
			}else{
				ConfirmDialogFragment.show(this, getString(R.string.unmount_sdcard));
			}
			break;

		case R.id.menu_popup_manage:
			isEdit = true;
			supportInvalidateOptionsMenu();
			((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
					.manage();
			break;

		case R.id.menu_popup_unhide:
			((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
					.unHide();
			break;
			
		case R.id.menu_popup_hide:
			((PickerFragment) getSupportFragmentManager().findFragmentByTag(tag))
					.HideFile();
			break;

		case R.id.menu_popup_delete:
			((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
					.delete();
			break;

            case R.id.menu_popup_restore:
                if(Utils.isSDCardExist()){
                    ((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
                    .startRestoreFilesProgress();
                }else{
                    ConfirmDialogFragment.show(this, getString(R.string.unmount_sdcard));
                }

                break;

            case R.id.menu_popup_backup:
                if(Utils.isSDCardExist()){
                    ((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
                    .startBackupProgress();
                }else{
                    ConfirmDialogFragment.show(this, getString(R.string.unmount_sdcard));
                }
                break;

		case R.id.menu_popup_select_all:
			if (tag.equals(Utils.FRAGMENT_PICKER)) {
				((PickerFragment) getSupportFragmentManager().findFragmentByTag(tag))
				.selectAll();
			} else {
				((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
				.selectAll();
			}
			
			break;

		case R.id.menu_popup_deselect_all:
			if (tag.equals(Utils.FRAGMENT_PICKER)) {
				((PickerFragment) getSupportFragmentManager().findFragmentByTag(tag))
				.deselectAll();
			} else {
				((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag))
				.deselectAll();
			}
			
			break;

		}
		if(v.getId() != R.id.menu_popup_sort){
			dissmisPopupMenu();
		}
	}

    /**
     * Show user guide.
     */
    private void showUserGuide() {
        // TODO Auto-generated method stub
        userGuideDialog = new UserGuideDialog(CategoryActivity.this, new DialogListener() {

            @Override
            public void cancelListenr() {
                // TODO Auto-generated method stub
            }
            @Override
            public void acceptListenr() {
                // TODO Auto-generated method stub
                userGuideDialog.dismiss();
            }
        });
        userGuideDialog.show();
    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentManager.OnBackStackChangedListener#
	 * onBackStackChanged() When click back button will catch change fragment
	 */
	@Override
	public void onBackStackChanged() {
		dissmisPopupMenu();
		Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (fragment instanceof BaseFragment||fragment instanceof PickerFragment) {
			updateActionBar(fragment);
		}
		try {
			tag = getSupportFragmentManager().getBackStackEntryAt(
					getSupportFragmentManager().getBackStackEntryCount()-1)
					.getName();
			ImageView actionBar_Logo = (ImageView) actionBar.getCustomView()
					.findViewById(R.id.actionBar_Logo);
			ImageView actionBar_title = (ImageView) actionBar.getCustomView()
					.findViewById(R.id.actionBar_title);

			if (tag == null)
				return;

			if (tag.equals(Utils.FRAGMENT_AUDIO)) {
				actionBar_title.setImageResource(R.drawable.text_audio);
				actionBar_Logo.setImageResource(R.drawable.ic_audio);

			} else if (tag.equals(Utils.FRAGMENT_PHOTO)) {
				
				actionBar_title.setImageResource(R.drawable.text_photo);
				actionBar_Logo.setImageResource(R.drawable.ic_photo);

			} else if (tag.equals(Utils.FRAGMENT_VIDEO)) {
				actionBar_title.setImageResource(R.drawable.text_video);
				actionBar_Logo.setImageResource(R.drawable.ic_video);

			} else if (tag.equals(Utils.FRAGMENT_PICKER)) {
				actionBar_title.setImageResource(R.drawable.text_picker);

			} else {
				actionBar_title.setImageResource(R.drawable.text_category);
				actionBar_Logo.setImageResource(R.drawable.ic_category);
				
			}
			supportInvalidateOptionsMenu();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			finish();

		}
	}
	
	private void updateActionBar(Fragment fragment) {
		// TODO Auto-generated method stub
		int selected=0;
		if (fragment instanceof PickerFragment) {
			PickerFragment pickerFragment=(PickerFragment) fragment;
			List<FileItem> fileItems=pickerFragment.getmAdapter().getSelectedItem();
			if (pickerFragment.getmAdapter()!=null&&fileItems!=null) {
				selected=fileItems.size();
				//If there is no item. Disable select All item.
				if (pickerFragment.getmAdapter().getCount()>0) {
					Utils.enableDisableView(menu_popup_select_all, true);
					menu_popup_select_all.setBackgroundColor(getResources().getColor(android.R.color.transparent));
					Utils.enableDisableView(menu_popup_sort, true);
					menu_popup_sort.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				} else {
					Utils.enableDisableView(menu_popup_select_all, false);
					menu_popup_select_all.setBackgroundColor(getResources().getColor(R.color.disable_color));
					Utils.enableDisableView(menu_popup_sort, false);
					menu_popup_sort.setBackgroundColor(getResources().getColor(R.color.disable_color));
				}
			}
		} else if (fragment instanceof BaseFragment) {
			BaseFragment baseFragment=(BaseFragment) fragment;
			List<FileItem> fileItems=baseFragment.getmAdapter().getSelectedItem();
			if (baseFragment.getmAdapter()!=null&&fileItems!=null) {
				selected=fileItems.size();
				//If there is no item. Disable select All, Sort item.
				if (baseFragment.getmAdapter().getCount()>0) {
					Utils.enableDisableView(menu_popup_select_all, true);
					menu_popup_select_all.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				} else {
					Utils.enableDisableView(menu_popup_select_all, false);
					menu_popup_select_all.setBackgroundColor(getResources().getColor(R.color.disable_color));
				}
			}
		}
		updateActionBar(selected);
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (App.needShowLogin&&!App.isShowing) {
			Intent intent = new Intent(CategoryActivity.this,
					LoginActivity.class);
			intent.setAction(Utils.ACTION_CATEGORY);
			startActivity(intent);
			App.isShowing=true;
		} else {
			//the next onResume will show login if not call onCreate method
			App.needShowLogin=true;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		menu.clear();
		inflater.inflate(R.menu.setting_menu, menu);
		if (tag.equals(Utils.FRAGMENT_PHOTO)
				|| tag.equals(Utils.FRAGMENT_VIDEO)
				|| tag.equals(Utils.FRAGMENT_AUDIO)) {
			MenuItem menu_mode = menu.findItem(R.id.menu_mode);
			MenuItem menu_edit_show = menu.findItem(R.id.menu_edit_show);
			MenuItem menu_cancel = menu.findItem(R.id.menu_cancel);
			menu_edit_show.setVisible(true);
			menu_mode.setVisible(!isEdit);
			menu_cancel.setVisible(isEdit);

		} else if (tag.equals(Utils.FRAGMENT_PICKER)) {
			MenuItem menu_mode = menu.findItem(R.id.menu_mode);
			MenuItem menu_edit_show = menu.findItem(R.id.menu_edit_show);
			menu_mode.setVisible(true);
			menu_edit_show.setVisible(true);
			
		} else {
			MenuItem menu_settings = menu.findItem(R.id.menu_settings);
			menu_settings.setVisible(true);
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			View setting = findViewById(R.id.menu_settings);
			showPopupMenu(setting);
			break;	
			
		case R.id.menu_mode:
			View mode = findViewById(R.id.menu_mode);
			showPopupMenu(mode);
			break;
			
		case R.id.menu_edit_show:
			View edit_show = findViewById(R.id.menu_edit_show);
			showPopupMenu(edit_show);
			break;
			
		case R.id.menu_cancel:
			isEdit = false;
			dissmisPopupMenu();
			BaseFragment basefragment = (BaseFragment) getSupportFragmentManager()
					.findFragmentByTag(tag);
			basefragment.cancel();
			supportInvalidateOptionsMenu();
			break;
		}
		// return super.onOptionsItemSelected(item);
		return false;
	}

	/**
	 * Show dialog change password.
	 */
	public void showDialog() {
		password_dialog = new PasswordDialog(this, new DialogListener() {

			@Override
			public void cancelListenr() {
				// TODO Auto-generated method stub
				password_dialog.dismiss();
			}

			@Override
			public void acceptListenr() {
				// TODO Auto-generated method stub

				if (password_dialog.isMatchPass(SaveData.getInstance(
						CategoryActivity.this).getPassword())) {
					String encryptedString;
					try {
						encryptedString = Encryption.encrypt(getPackageName(),
							password_dialog.getnewPassText());

						SaveData.getInstance(CategoryActivity.this)
								.setPassword(encryptedString);
					Toast.makeText(CategoryActivity.this,
							getString(R.string.saved_new_password),
							Toast.LENGTH_SHORT).show();
					password_dialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e(Utils.TAG, "Encryption fail due to: "+e.toString());
					}

				} else {

					if (password_dialog.getnewPassText() == null
							|| password_dialog.getnewPassText().equals("")) {
						Toast.makeText(CategoryActivity.this,
								getString(R.string.please_input_password),
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(CategoryActivity.this,
								getString(R.string.current_pass_invalid),
								Toast.LENGTH_SHORT).show();
						password_dialog.reset();
					}

				}

			}
		});
		password_dialog.show();
	}

	/* (non-Javadoc)
	 * @see com.arisvn.arissmarthiddenbox.listener.OnSelectedItemChangeListener#onSelectedItemChanged(int)
	 */
	@Override
	public void onSelectedItemChanged(int selectedItem) {
		// TODO Auto-generated method stub
		updateActionBar(selectedItem);
		
	}

	/**
	 * Update action bar.
	 *
	 * @param selectedItem the selected item
	 */
	private void updateActionBar(int selectedItem) {
		// TODO Auto-generated method stub
		tag = getSupportFragmentManager().getBackStackEntryAt(
				getSupportFragmentManager().getBackStackEntryCount()-1)
				.getName();
		if (tag == null)
			return;

		if (tag.equals(Utils.FRAGMENT_AUDIO)||tag.equals(Utils.FRAGMENT_PHOTO)||tag.equals(Utils.FRAGMENT_VIDEO)) {
			if (selectedItem>0) {
				Utils.enableDisableView(menu_popup_unhide, true);
				menu_popup_unhide.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				Utils.enableDisableView(menu_popup_delete, true);
				menu_popup_delete.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				Utils.enableDisableView(menu_popup_deselect_all, true);
				menu_popup_deselect_all.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				Utils.enableDisableView(menu_popup_backup, true);
				menu_popup_backup.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			} else {
				//Disable Unhide, delete , deselect all action item on Photo/Audio/Video screen.
				Utils.enableDisableView(menu_popup_unhide, false);
				menu_popup_unhide.setBackgroundColor(getResources().getColor(R.color.disable_color));
				Utils.enableDisableView(menu_popup_delete, false);
				menu_popup_delete.setBackgroundColor(getResources().getColor(R.color.disable_color));
				Utils.enableDisableView(menu_popup_deselect_all, false);
				menu_popup_deselect_all.setBackgroundColor(getResources().getColor(R.color.disable_color));
				Utils.enableDisableView(menu_popup_backup, false);
				menu_popup_backup.setBackgroundColor(getResources().getColor(R.color.disable_color));
			}

			
		} else if (tag.equals(Utils.FRAGMENT_PICKER)) {
			
			if (selectedItem>0) {
				Utils.enableDisableView(menu_popup_hide, true);
				menu_popup_hide.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				Utils.enableDisableView(menu_popup_deselect_all, true);
				menu_popup_deselect_all.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			} else {
				//Disable Hide,deselect all action item on Picker screen.
				Utils.enableDisableView(menu_popup_hide, false);
				menu_popup_hide.setBackgroundColor(getResources().getColor(R.color.disable_color));
				Utils.enableDisableView(menu_popup_deselect_all, false);
				menu_popup_deselect_all.setBackgroundColor(getResources().getColor(R.color.disable_color));

			}

		}
		supportInvalidateOptionsMenu();
	}

	@Override
	public void onFinishLoadItems() {
		// TODO Auto-generated method stub
		Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (fragment instanceof BaseFragment||fragment instanceof PickerFragment) {
			updateActionBar(fragment);
		}
	}


	@Override
	public void onChangedData(boolean result, int action) {
		// TODO Auto-generated method stub
		String TAG = null;
		if(action == Utils.TYPE_PHOTO){
			TAG = Utils.FRAGMENT_PHOTO;
		}else if(action == Utils.TYPE_VIDEO){
			TAG = Utils.FRAGMENT_VIDEO;
		}else if(action == Utils.TYPE_AUDIO){
			TAG = Utils.FRAGMENT_AUDIO;
		}
			
		((BaseFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG)).flagChangeData = result;
	}}
