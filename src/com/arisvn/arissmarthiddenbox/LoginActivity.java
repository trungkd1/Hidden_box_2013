/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - LoginActivity.java
 * Date create: 3:03:30 PM - Nov 8, 2013 - 2013
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
 * The Class LoginActivity.
 */
public class LoginActivity extends Activity {

	/** The key_board. */
	private CustomKeyboardView key_board;

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
				String encryptedString;
				try {
					encryptedString = Encryption
							.encrypt(getPackageName(), text);

					if (SaveData.getInstance(LoginActivity.this).getPassword()
							.equals(encryptedString)) {
					    startActivity(getIntent().getAction());
					} else {
						key_board.reset();
					    Toast.makeText(LoginActivity.this,
							getString(R.string.password_not_match),
							Toast.LENGTH_SHORT).show();
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(Utils.TAG, "Encryption fail due to: "+e.toString());
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

	/**
	 * Start activity.
	 *
	 * @param action the action
	 */
	public void startActivity(String action) {
		if (action == null) {
			Intent intent = new Intent(LoginActivity.this,
					CategoryActivity.class);
			startActivity(intent);
		}
		App.isShowing=false;
		App.needShowLogin=false;
		finish();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = getIntent();
		if (intent != null) {
			String action = intent.getAction();
			if (action == null) {
				super.onBackPressed();
			} else {
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(startMain);
			}
		}
	}
}
