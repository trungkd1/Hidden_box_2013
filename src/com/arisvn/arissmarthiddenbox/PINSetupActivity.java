/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - PINSetupActivity.java
 * Date create: 2:48:28 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.util.CustomKeyboardView;
import com.arisvn.arissmarthiddenbox.util.SaveData;
import com.arisvn.arissmarthiddenbox.util.Utils;
import com.arisvn.arissmarthiddenbox.util.CustomKeyboardView.GoListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class PINSetupActivity. This class is used to set up PIN for this application.
 */
public class PINSetupActivity extends Activity {

	/** The key_board. */
	private CustomKeyboardView key_board;
	
	/** The password. */
	private String password = null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);

		this.key_board = (CustomKeyboardView) findViewById(R.id.keyboard_view);
		this.key_board.setOnGoListener(new GoListener() {

			@Override
			public void onGo(String text) {
				if (text == null || text.equals("")) {
					Toast.makeText(PINSetupActivity.this,
							getString(R.string.please_input_password),
							Toast.LENGTH_SHORT).show();
				} else {
					if (password != null) {
						if (password.equals(text)) {
							try {
								String encryptedString=Encryption.encrypt(getPackageName(), password);
							    SaveData.getInstance(PINSetupActivity.this)
										.setPassword(encryptedString);
								Intent intent = new Intent(PINSetupActivity.this,
										CategoryActivity.class);
								startActivity(intent);
								finish();
							} catch (Exception e) {
								// TODO: handle exception
								Log.e(Utils.TAG, "Set up PIN fail due to : "+e.toString());
							}
							
						} else {
							key_board.reset();
							Toast.makeText(PINSetupActivity.this,
									getString(R.string.password_not_match),
									Toast.LENGTH_SHORT).show();
						}

					} else {
						password = text;
						key_board.onConfirm();
						key_board.reset();
					}

				}
			}
		});
		
	}
	
	/**
	 * On click key.
	 *
	 * @param v the v
	 */
	public void onClickKey(View v) {
		key_board.onClickKey(v.getTag().toString());
	}
}
