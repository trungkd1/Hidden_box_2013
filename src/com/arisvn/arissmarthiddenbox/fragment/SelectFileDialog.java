/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.fragment - SelectFileDialog.java
 * Date create: 10:10:53 AM - Oct 16, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.App;
import com.arisvn.arissmarthiddenbox.FileBrowserActivity;
import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.dialog.ConfirmDialogFragment;
import com.arisvn.arissmarthiddenbox.listener.FileDialogListener;
import com.arisvn.arissmarthiddenbox.util.Utils;

/**
 * The Class SelectFileDialog.
 */
public class SelectFileDialog extends DialogFragment implements OnClickListener {
	int mode;
	private EditText filePath;
	private EditText fileName;
	private TextView fileExtension;
	private FileDialogListener listener;
	private ImageView icon_view;
	private TextView text_title;
	private Button btn_ok;
	private Button btn_cancel;
	private Button btn_browser;

	/**
	 * Instantiates a new select file dialog.
	 *
	 * @param listener the listener
	 */
	public SelectFileDialog(FileDialogListener listener) {
		// Empty constructor required for DialogFragment
		setCancelable(false);
		this.listener = listener;
	}

	/**
	 * New instance.
	 *
	 * @param mode the mode
	 * @param listener the listener
	 * @return the select file dialog
	 */
	public static SelectFileDialog newInstance(int mode,
			FileDialogListener listener) {
		SelectFileDialog f = new SelectFileDialog(listener);
		// // Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt(Utils.FILE_MODE, mode);
		f.setArguments(args);

		return f;
	}

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mode = getArguments().getInt(Utils.FILE_MODE);
		View view = inflater.inflate(R.layout.dialog_select_file, container);
		text_title = (TextView)view.findViewById(R.id.text_title);
		icon_view = (ImageView)view.findViewById(R.id.icon_view);		
		btn_browser = (Button)view.findViewById(R.id.btn_browser);
		btn_ok = (Button)view.findViewById(R.id.btn_ok);
		btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
		filePath = (EditText) view.findViewById(R.id.filePath);
		fileName = (EditText) view.findViewById(R.id.filename);
		fileExtension = (TextView) view.findViewById(R.id.file_extension);
		
		btn_browser.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "UTMAvoBold.ttf"); 
		text_title.setTypeface(font);
		btn_ok.setTypeface(font);
		btn_cancel.setTypeface(font);
		fileName.setTypeface(font);
		filePath.setTypeface(font);
		fileExtension.setTypeface(font);
		btn_browser.setTypeface(font);
		
		
		switch (mode) {
		case Utils.EXPORT:
			fileName.setVisibility(View.VISIBLE);
			fileExtension.setVisibility(View.VISIBLE);
			filePath.setHint(R.string.file_path);
			text_title.setText(getString(R.string.export_title));
			icon_view.setImageResource(R.drawable.ic_backup);
			break;
		case Utils.IMPORT:
			fileName.setVisibility(View.GONE);
			fileExtension.setVisibility(View.GONE);
			filePath.setHint(R.string.file);
			text_title.setText(getString(R.string.import_title));
			icon_view.setImageResource(R.drawable.ic_restore);
			break;

		default:
			break;
		}
		
		return view;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_browser:
                if (Utils.isSDCardExist()) {
                    App.needShowLogin = false;
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), FileBrowserActivity.class);
                        intent.putExtra(Utils.FILE_MODE, mode);
                        startActivityForResult(intent, Utils.REQUEST_FILE);
                    }
                } else {
                    ConfirmDialogFragment.show(getActivity(), getString(R.string.unmount_sdcard));
                }

                break;
            case R.id.btn_ok:
                if (Utils.isSDCardExist()) {
                    if (listener != null) {
                        if (mode == Utils.EXPORT) {
                            if (filePath.getText().toString().trim().length() == 0
                                    || fileName.getText().toString().trim().length() == 0) {
                                ConfirmDialogFragment.show(getActivity(),
                                        getString(R.string.export_empty));
                            } else {
                                File file = new File(filePath.getText().toString().trim());
                                if (!file.exists()) {
                                    ConfirmDialogFragment.show(getActivity(),
                                            getString(R.string.export_folder_not_exit));
                                } else {
                                    listener.onFileSelected(filePath.getText().toString() + "/"
                                            + fileName.getText().toString() + ".bak", mode);
                                    dismiss();
                                }

                            }
                        } else if (mode == Utils.IMPORT) {
                            if (filePath.getText().toString().trim().length() == 0) {
                                ConfirmDialogFragment.show(getActivity(),
                                        getString(R.string.import_empty));
                            } else {
                                File file = new File(filePath.getText().toString().trim());
                                if (!file.exists()) {
                                    ConfirmDialogFragment.show(getActivity(),
                                            getString(R.string.import_file_not_exit));
                                } else {
                                    listener.onFileSelected(filePath.getText().toString(), mode);
                                    dismiss();
                                }

                            }
                        }
                    }
                } else {
                    ConfirmDialogFragment.show(getActivity(), getString(R.string.unmount_sdcard));
                }
                break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		App.needShowLogin=false;
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Utils.REQUEST_FILE
				&& resultCode == Activity.RESULT_OK) {
			filePath.setText(data.getStringExtra(Utils.FILE_NAME));
		}
	}
}
