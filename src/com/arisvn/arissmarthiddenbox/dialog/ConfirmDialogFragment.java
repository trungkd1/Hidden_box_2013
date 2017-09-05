/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - ConfirmDialogFragment.java
 * Date create: 2:17:22 PM - Nov 11, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.dialog;

import com.arisvn.arissmarthiddenbox.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfirmDialogFragment. This class is used to show confirm dialog
 * to notify users something.
 * 
 * @author Tam-LT
 */
@SuppressLint("ValidFragment")
public class ConfirmDialogFragment extends DialogFragment {

	/** The Constant TAG. */
	public static final String TAG = "CONFIRM_DIALOG_FRAGMENT";

	/** The message. */
	private String message;

	/** The button_message. */
	private String button_message;

	/** The layout. */
	private View layout;

	/** The waiting dismiss. */
	private static boolean waitingDismiss = false;

	/**
	 * Gets the single instance of ConfirmDialogFragment.
	 * 
	 * @param message
	 *            the message
	 * @return single instance of ConfirmDialogFragment
	 */
	private static ConfirmDialogFragment getInstance(String message) {
		ConfirmDialogFragment f = new ConfirmDialogFragment(message);
		return f;
	}

	/**
	 * Instantiates a new confirm dialog fragment.
	 * 
	 * @param message
	 *            the message
	 */
	private ConfirmDialogFragment(String message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Panel);
		setCancelable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.confirm_layout, container, false);
		if (message != null) {
			((TextView) layout.findViewById(R.id.confirm_text))
					.setText(message);
		}
		if (button_message != null && !button_message.trim().isEmpty()) {
			((Button) layout.findViewById(R.id.confirm_ok))
					.setText(button_message);
		}
		layout.findViewById(R.id.confirm_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
		return layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.
	 * FragmentManager, java.lang.String)
	 */
	@Override
	public void show(FragmentManager arg0, String arg1) {
		waitingDismiss = false;
		try {
			super.show(arg0, arg1);
		} catch (IllegalStateException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.DialogFragment#dismiss()
	 */
	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (IllegalStateException e) {
			waitingDismiss = true;
		} catch (NullPointerException e) {
			waitingDismiss = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		if (waitingDismiss) {
			dismiss();
			waitingDismiss = false;
		}
	}

	/**
	 * Dismiss.
	 * 
	 * @param activity
	 *            the activity
	 */
	public static void dismiss(FragmentActivity activity) {
		if (activity != null) {
			DialogFragment progressDiag = ((DialogFragment) (activity
					.getSupportFragmentManager()
					.findFragmentByTag(ConfirmDialogFragment.TAG)));
			if (progressDiag != null) {
				progressDiag.dismiss();
			} else {
				waitingDismiss = true;
			}
		}

	}

	/**
	 * Show.
	 * 
	 * @param activity
	 *            the activity
	 * @param message
	 *            the message
	 */
	public static void show(FragmentActivity activity, String message) {
		getInstance(message).show(activity.getSupportFragmentManager(),
				ConfirmDialogFragment.TAG);
	}

}
