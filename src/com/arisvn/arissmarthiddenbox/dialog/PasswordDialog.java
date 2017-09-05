/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - PasswordDialog.java
 * Date create: 2:51:58 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.dialog.BaseDialog;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.listener.DialogListener;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class PasswordDialog. This class is used for users to reset PIN.
 */
public class PasswordDialog extends BaseDialog {

	/** The edit_current_pass. */
	private EditText edit_current_pass;
	
	/** The edit_new_pass. */
	private EditText edit_new_pass;
	
	/** The btn_ok. */
	private Button btn_ok;
	
	/** The btn_cancel. */
	private Button btn_cancel;
	
	/** The text_current_pass. */
	private TextView text_current_pass;
	
	/** The text_new_pass. */
	private TextView text_new_pass;
	
	/** The text_title. */
	private TextView text_title;

	/**
	 * Instantiates a new password dialog.
	 *
	 * @param context the context
	 * @param listener the listener
	 */
	public PasswordDialog(Context context, DialogListener listener) {
		super(context, R.style.CustomDialogTheme, listener);
		setContentView(R.layout.layout_password_dialog);
		init(context);
	}

	/**
	 * Inits the.
	 *
	 * @param context the context
	 */
	private void init(Context context) {
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		edit_current_pass = (EditText) findViewById(R.id.edit_current_pass);
		edit_new_pass = (EditText) findViewById(R.id.edit_new_pass);
		text_current_pass = (TextView) findViewById(R.id.text_current_pass);
		text_new_pass = (TextView) findViewById(R.id.text_new_pass);
		text_title = (TextView) findViewById(R.id.text_title);
		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"UTMAvoBold.ttf");
		btn_ok.setTypeface(font);
		btn_cancel.setTypeface(font);
		text_new_pass.setTypeface(font);
		text_current_pass.setTypeface(font);
		text_title.setTypeface(font);
		InputMethodManager imm = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(
				InputMethodManager.SHOW_FORCED,0);
		edit_current_pass.requestFocus();
		
	}

	/**
	 * Checks if is match pass.
	 *
	 * @param password the password
	 * @return true, if is match pass
	 */
	public boolean isMatchPass(String password) {
		String currentPassText = edit_current_pass.getText().toString();
		String newPassText = edit_new_pass.getText().toString();

		if (currentPassText.equals("") || newPassText.equals(""))
			return false;
		String encryptedString;
		try {
			encryptedString = Encryption.encrypt(getContext().getPackageName(),
					currentPassText);

			if (encryptedString.equals(password)) {
			return true;

		} else {
			return false;
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(Utils.TAG, "Encryption fail due to: "+e.toString());
			return false;
		}
	}

	/**
	 * Gets the current pass text.
	 *
	 * @return the current pass text
	 */
	public String getcurrentPassText() {
		return edit_current_pass.getText().toString();
	}

	/**
	 * Gets the new pass text.
	 *
	 * @return the new pass text
	 */
	public String getnewPassText() {
		return edit_new_pass.getText().toString();
	}

	/**
	 * Reset.
	 */
	public void reset() {
		edit_new_pass.setText(null);
		edit_current_pass.setText(null);
	}
}
