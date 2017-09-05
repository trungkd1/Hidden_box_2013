/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - UserGuideDialog.java
 * Date create: 2:55:32 PM - Nov 21, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.webkit.WebView;
import android.widget.Button;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.listener.DialogListener;
// TODO: Auto-generated Javadoc

/**
 * The Class UserGuideDialog. This class is used to shown user guide which is going to guide users how to use this application.
 */
public class UserGuideDialog extends BaseDialog {
    
    /** The web view. */
    private WebView webView;
    
    /** The btn_ok. */
    private Button btn_ok;
    
    /** The btn_cancel. */
    private Button btn_cancel;
    
    /**
     * Instantiates a new user guide dialog.
     *
     * @param context the context
     * @param listener the listener
     */
    public UserGuideDialog(Context context, DialogListener listener) {
        super(context, R.style.CustomDialogTheme, listener);
        setContentView(R.layout.layout_user_guide);
        init(context);
    }

    /**
     * Inits the.
     *
     * @param context the context
     */
    protected void init(Context context) {
        // TODO Auto-generated method stub
        setContentView(R.layout.layout_user_guide);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        webView = (WebView)findViewById(R.id.webview_help);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(getContext().getResources().getString(R.string.user_guide_url));
        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "UTMAvoBold.ttf");
        btn_ok.setTypeface(font);
        btn_cancel.setTypeface(font);
    }

}
