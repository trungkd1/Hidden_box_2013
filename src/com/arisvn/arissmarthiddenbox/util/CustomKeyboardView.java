/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - CustomKeyboardView.java
 * Date create: 2:58:58 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.LoginActivity;
import com.arisvn.arissmarthiddenbox.PINSetupActivity;
import com.arisvn.arissmarthiddenbox.R;


// TODO: Auto-generated Javadoc
/**
 * The Class CustomKeyboardView.
 */
public class CustomKeyboardView extends LinearLayout {

	/** The context. */
	private Context context;
	
	/** The go listener. */
	private GoListener goListener;
	
	/** The edit text. */
	private EditText editText;
	
	/** The notice text. */
	private TextView noticeText;
	
	/** The hint text id. */
	private int hintTextId;

	/**
	 * The listener interface for receiving go events.
	 * The class that is interested in processing a go
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addGoListener<code> method. When
	 * the go event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see GoEvent
	 */
	public interface GoListener{
		
		/**
		 * On go.
		 *
		 * @param text the text
		 */
		public void onGo(String text);
	}

	/**
	 * Instantiates a new custom keyboard view.
	 *
	 * @param context the context
	 */
	public CustomKeyboardView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom keyboard view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CustomKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * Inits the.
	 */
	public void init(){	
		Activity activity = (Activity)context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.layout_keyboard, this);
		Typeface font = Typeface.createFromAsset(context.getAssets(), "HoboStd.otf");  
		Typeface font2 = Typeface.createFromAsset(context.getAssets(), "UTMAvoBold.ttf"); 
		TextView text0 = (TextView)findViewById(R.id.key_button_0);
		TextView text1 = (TextView)findViewById(R.id.key_button_1);
		TextView text2 = (TextView)findViewById(R.id.key_button_2);
		TextView text3 = (TextView)findViewById(R.id.key_button_3);
		TextView text4 = (TextView)findViewById(R.id.key_button_4);
		TextView text5 = (TextView)findViewById(R.id.key_button_5);
		TextView text6 = (TextView)findViewById(R.id.key_button_6);
		TextView text7 = (TextView)findViewById(R.id.key_button_7);
		TextView text8 = (TextView)findViewById(R.id.key_button_8);
		TextView text9 = (TextView)findViewById(R.id.key_button_9);
		text0.setTypeface(font);
		text1.setTypeface(font);
		text2.setTypeface(font);
		text3.setTypeface(font);
		text4.setTypeface(font);
		text5.setTypeface(font);
		text6.setTypeface(font);
		text7.setTypeface(font);
		text8.setTypeface(font);
		text9.setTypeface(font);
		
		editText = (EditText)findViewById(R.id.edittext);		
		noticeText = (TextView)findViewById(R.id.notice);	
		editText.setTypeface(font);
		noticeText.setTypeface(font2);
		View leftView = (View)findViewById(R.id.left_edit_text);
		View rightView = (View)findViewById(R.id.right_edit_text);
		
		editText.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() == 0){
					noticeText.setText(hintTextId);	
				}
				else{
					noticeText.setText(null);						
				}
			}
		});
		
		Button clear = (Button)findViewById(R.id.key_button_clear);
		clear.setOnLongClickListener(new OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				editText.setText(null);
				return false;
			}
		});
		
		if(activity instanceof PINSetupActivity){
			hintTextId = R.string.set_password;
		}
		else if(activity instanceof LoginActivity){
			hintTextId = R.string.login_code;
		}
		leftView.setVisibility(View.VISIBLE);
		rightView.setVisibility(View.VISIBLE);	
		reset();
	}

	/**
	 * On click key.
	 *
	 * @param tag the tag
	 */
	public void onClickKey(String tag){
		int value = Integer.parseInt(tag);
		String text = editText.getText().toString();

		switch(value){
		case -1:
			// Clear text
			int length = text.length();
			if(length > 0){
				editText.setText(null);
				editText.append(text.substring(0, length-1));
			}
			break;
		case -2:
			// Go clicked 
			goListener.onGo(text);
			break;
		default:
			noticeText.setText(null);
			// Input text
			editText.append(tag);
			break;
		}
	}
	
	/**
	 * On incorrect.
	 */
	public void onIncorrect(){
		editText.setText(null);
		noticeText.setText(R.string.incorrect);
	}
	
	/**
	 * On confirm.
	 */
	public void onConfirm(){
		hintTextId = R.string.confirm_password;
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		editText.setText(null);
		noticeText.setText(hintTextId);
	}

	/**
	 * Sets the on go listener.
	 *
	 * @param goListener the new on go listener
	 */
	public void setOnGoListener(GoListener goListener){
		this.goListener = goListener;
	}
}
