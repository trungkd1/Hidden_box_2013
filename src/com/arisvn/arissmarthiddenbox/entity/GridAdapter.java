package com.arisvn.arissmarthiddenbox.entity;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.R;

public class GridAdapter extends BaseviewAdapter{

	Activity activity;
	public GridAdapter(Activity activity, ArrayList<FileItem> fileItems,
			int type) {
		super(activity, fileItems, type);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		if (convertView == null) {
			view = new ViewHolder();
			imageLoader.memoryCache.clearCache();
			LayoutInflater inflator = activity.getLayoutInflater();
			convertView = inflator.inflate(R.layout.gridview_row, null);
			view.txtViewTitle = (TextView) convertView
					.findViewById(R.id.text_title);
			view.txtSize = (TextView) convertView.findViewById(R.id.text_size);
			view.imgViewFlag = (ImageView) convertView
					.findViewById(R.id.imageView1);
			view.frame = (View)convertView.findViewById(R.id.frame_layout);
			view.image_selected = (ImageView) convertView
					.findViewById(R.id.image_selected);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		
		return super.getView(position, convertView, parent);
	}
}
