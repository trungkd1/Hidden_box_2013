/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - PrivacyDialog.java
 * Date create: 2:53:40 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.listener.DialogListener;

// TODO: Auto-generated Javadoc
/**
 * The Class PrivacyDialog. This class is used to the privacy for using this application. It's only shown at the first time users open this application.
 */
public class PrivacyDialog extends BaseDialog {

    /** The btn_ok. */
    private Button btn_ok;
    
    /** The btn_cancel. */
    private Button btn_cancel;
    
    /** The text_content. */
    private TextView text_content;
    
    /** The text_title. */
    private TextView text_title;

    /**
     * Instantiates a new privacy dialog.
     *
     * @param context the context
     * @param listener the listener
     */
    public PrivacyDialog(Context context, DialogListener listener) {
        super(context, R.style.CustomDialogTheme, listener);
        setContentView(R.layout.layout_privacy_dialog);
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
        text_content = (TextView) findViewById(R.id.content_privacy);
        text_title = (TextView) findViewById(R.id.text_title_privacy);
        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "UTMAvoBold.ttf");
        btn_ok.setTypeface(font);
        btn_cancel.setTypeface(font);
        text_content.setTypeface(font);
        text_title.setTypeface(font);
    }
}
