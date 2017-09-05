/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - FileBrowserActivity.java
 * Date create: 2:46:40 PM - Oct 15, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.dialog.ConfirmDialogFragment;
import com.arisvn.arissmarthiddenbox.util.Utils;

/**
 * The Class FileBrowserActivity.
 */
public class FileBrowserActivity extends FragmentActivity implements
OnClickListener, OnItemClickListener {
    private List<String> item = null;
    private String root = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private int mode;

	private String currentPath;
	private ListView listView;
	
	private Button btn_ok;
	private Button btn_cancel;
	private TextView text_title;
	
	private Typeface font;
	
	private String parentString = "../";

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		font = Typeface.createFromAsset(getAssets(), "UTMAvoBold.ttf"); 
		setContentView(R.layout.activity_filebrowser);
		Intent intent = getIntent();
		mode = intent.getIntExtra(Utils.FILE_MODE, Utils.IMPORT);
		if (mode == Utils.IMPORT) {
			findViewById(R.id.action_panel).setVisibility(View.GONE);
		} else {
			findViewById(R.id.action_panel).setVisibility(View.VISIBLE);
			btn_ok = (Button)findViewById(R.id.ok);
			btn_cancel = (Button)findViewById(R.id.cancel);
			btn_cancel.setOnClickListener(this);
			btn_ok.setOnClickListener(this);
			
			btn_cancel.setTypeface(font);
			btn_ok.setTypeface(font);
		}
		listView = (ListView) findViewById(android.R.id.list);
		text_title = (TextView)findViewById(R.id.text_title);
		text_title.setTypeface(font);

		
		getDir(root);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (App.needShowLogin&&!App.isShowing) {
			Intent intent = new Intent(this,
					LoginActivity.class);
			intent.setAction(Utils.ACTION_CATEGORY);
			startActivity(intent);
			App.isShowing=true;
		} else {
			//the next onResume will show login if not call onCreate method
			App.needShowLogin=true;
		}
	}

	/**
	 * Gets the dir.
	 * 
	 * @param dirPath
	 *            the dir path
	 * @return the dir
	 */
	private void getDir(String dirPath)

	{
        if (Utils.isSDCardExist()) {
            ArrayList<String> directoryTemp = new ArrayList<String>();
            ArrayList<String> fileTemp = new ArrayList<String>();

            currentPath = dirPath;
            text_title.setText(currentPath);

            item = new ArrayList<String>();

            File f = new File(dirPath);

            File[] files = f.listFiles();

            if (!dirPath.equals(root))

            {

                item.add(root);
                item.add(parentString);

            }

            for (int i = 0; i < files.length; i++)

            {

                File file = files[i];

                if (mode == Utils.EXPORT) {
                    // Add item for export mode
                    if (file.isDirectory())
                        item.add(file.getName());
                } else {
                    // add item for import mode
                    if (file.isDirectory())
                        directoryTemp.add(file.getName());
                    else
                        fileTemp.add(file.getName());
                }

            }
            if (mode == Utils.IMPORT) {
                Collections.sort(directoryTemp);
                Collections.sort(fileTemp);
                item.addAll(directoryTemp);
                item.addAll(fileTemp);
            }

            Adapter adapter = new Adapter(this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        } else {
            ConfirmDialogFragment.show(this, getString(R.string.unmount_sdcard));
        }

    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentPath.trim().equals(root)) {
			super.onBackPressed();
		} else {
			File file = new File(currentPath);
			getDir(file.getParent());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ok:
			Intent intent = new Intent();
			intent.putExtra(Utils.FILE_NAME, currentPath);
			setResult(RESULT_OK, intent);
			break;
		case R.id.cancel:
			setResult(RESULT_CANCELED);
			break;
		default:
			break;
		}
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		File file;
		if (item.get(arg2).equals(parentString)) {
			file = new File(currentPath);
			file = file.getParentFile();
		} else if (item.get(arg2).equals(root)) {
			file = new File(item.get(arg2));
		} else {
			file = new File(currentPath + "/" + item.get(arg2));
		}
		if (file.isDirectory())

		{
			if (file.canRead())

				getDir(file.getAbsolutePath());

		} else {
			// only show file in the case of import a back up file into the
			// application. We select this file to import.
            if (mode == Utils.IMPORT&&file.isFile()) {
				Intent intent = new Intent();
				intent.putExtra(Utils.FILE_NAME, file.getAbsolutePath());
				setResult(RESULT_OK, intent);
				finish();
			}

		}
	}
	
	/**
	 * Create adapter for browser file
	 *
	 */
	class Adapter extends BaseAdapter{

		
		public Adapter(Context context){
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return item.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return item.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
		public class ViewHolder {
			public ImageView imgViewFlag;
			public TextView rowtext;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder view ;
			if (convertView == null) {
				view = new ViewHolder();
				convertView = getLayoutInflater().inflate( R.layout.file_row, null);
				view.rowtext = (TextView) convertView
						.findViewById(R.id.rowtext);
				view.imgViewFlag = (ImageView) convertView
						.findViewById(R.id.imageView1);
				convertView.setTag(view);
			}else{
				view = (ViewHolder) convertView.getTag();
			}
			
			view.rowtext.setText(item.get(position));
			File file = new File(currentPath + "/" + item.get(position));
			view.rowtext.setTypeface(font);
			if (item.get(position).equalsIgnoreCase(root)) {
				view.imgViewFlag.setImageResource(R.drawable.ic_folder);
			} else if (file.isDirectory()) {
				view.imgViewFlag.setImageResource(R.drawable.ic_folder);
			} else {
				view.imgViewFlag.setImageResource(R.drawable.ic_file);
			}
			
			return convertView;
		}
		
	}

}
