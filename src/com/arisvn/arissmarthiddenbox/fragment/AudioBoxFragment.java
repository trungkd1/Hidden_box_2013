/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.fragment - AudioBoxFragment.java
 * Date create: 2:57:24 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arisvn.arissmarthiddenbox.App;
import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class AudioBoxFragment.
 */
public class AudioBoxFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mType = Utils.TYPE_AUDIO;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see com.arisvn.arissmarthiddenbox.fragment.BaseFragment#onPicker()
	 */
	@Override
	protected void onPicker() {
		PickerFragment pickerFrg = new PickerFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Utils.TYPE, Utils.TYPE_AUDIO);
		pickerFrg.setArguments(bundle);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();		
		transaction.replace(R.id.fragment_container, pickerFrg,Utils.FRAGMENT_PICKER);
		transaction.addToBackStack(Utils.FRAGMENT_PICKER);
		transaction.commit(); 
	}

	/* (non-Javadoc)
	 * @see com.arisvn.arissmarthiddenbox.fragment.BaseFragment#makeFolder()
	 */
	@Override
	protected void makeFolder() {
		// TODO Auto-generated method stub
		Utils.makeFolder(Utils.FOLDER + Utils._AUDIO);
	}

	/* (non-Javadoc)
	 * @see com.arisvn.arissmarthiddenbox.fragment.BaseFragment#loadFile()
	 */
	@Override
	protected List<FileItem> loadFile() {
		// TODO Auto-generated method stub
		return App.getDB().getAllFile(mType);
	}
}
