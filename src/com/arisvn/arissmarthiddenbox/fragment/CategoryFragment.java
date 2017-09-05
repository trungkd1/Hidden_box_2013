/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.fragment - CategoryFragment.java
 * Date create: 2:57:36 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arisvn.arissmarthiddenbox.R;
import com.arisvn.arissmarthiddenbox.util.Utils;
import com.arisvn.arissmarthiddenbox.view.ProgressWheel;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryFragment.
 */
public class CategoryFragment extends Fragment implements OnClickListener {

	/** The running. */
	private boolean running;
	
	/** The pw_two. */
	private ProgressWheel pw_two;

	/** The image_audio. */
	private ImageView image_audio;
	
	/** The image_photo. */
	private ImageView image_photo;
	
	/** The image_video. */
	private ImageView image_video;
	
	/** The selected category. */
	private TextView selectedCategory;
	
	/** The wheel. */
	private int wheel = 0;
	
	/** The progress. */
	private int progress = 0;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_main, container, false);

		image_audio = (ImageView) view.findViewById(R.id.image_audio);
		image_photo = (ImageView) view.findViewById(R.id.image_photo);
		image_video = (ImageView) view.findViewById(R.id.image_video);
		selectedCategory=(TextView) view.findViewById(R.id.selectCategory);
		image_video.setOnClickListener(this);
		image_photo.setOnClickListener(this);
		image_audio.setOnClickListener(this);

		pw_two = (ProgressWheel) view.findViewById(R.id.progressBarTwo);
		pw_two.setBarLength(60);
		pw_two.setBarWidth(15);
		pw_two.setRimWidth(20);
		pw_two.setSpinSpeed(3);
		pw_two.setTextColor(0xff02457c);
		pw_two.setBarColor(0xffFFD700);
		pw_two.setRimColor(0xff999999);
		pw_two.setTextSize(34);
		pw_two.resetCount("");

		return view;
	}

	/** The r. */
	final Runnable r = new Runnable() {
		public void run() {
			running = true;
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			while (progress < wheel) {
				pw_two.incrementProgress();
				progress++;
				delay(5);
			}
			if (wheel == 270) {
				transaction.replace(R.id.fragment_container,
						new AudioBoxFragment(), Utils.FRAGMENT_AUDIO);
				transaction.addToBackStack(Utils.FRAGMENT_AUDIO);
			} else if (wheel == 180) {			
				transaction.replace(R.id.fragment_container,
						new VideoBoxFragment(), Utils.FRAGMENT_VIDEO);
				transaction.addToBackStack(Utils.FRAGMENT_VIDEO);
			} else if (wheel == 90) {				
				transaction.replace(R.id.fragment_container,
						new PhotoBoxFragment(), Utils.FRAGMENT_PHOTO);
				transaction.addToBackStack(Utils.FRAGMENT_PHOTO);
			}
			running = false;
			transaction.commit();
		}
	};

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		String title = null;
		Utils.setTypeface(getActivity(), selectedCategory);
		switch (v.getId()) {
		case R.id.image_audio:
			wheel = 270;
			title = getActivity().getString(R.string.audio);
			image_audio.setImageResource(R.drawable.menu_audio_selected);
			Utils.enableDisableView(image_audio, true);
			Utils.enableDisableView(image_photo, false);
			Utils.enableDisableView(image_video, false);
			break;
		case R.id.image_photo:
			wheel = 90;
			title = getActivity().getString(R.string.photo);
			image_photo.setImageResource(R.drawable.menu_photo_selected);
			Utils.enableDisableView(image_audio, false);
			Utils.enableDisableView(image_photo, true);
			Utils.enableDisableView(image_video, false);
			break;
		case R.id.image_video:
			wheel = 180;
			title = getActivity().getString(R.string.video);
			image_video.setImageResource(R.drawable.menu_video_selected);
			Utils.enableDisableView(image_audio, false);
			Utils.enableDisableView(image_photo, false);
			Utils.enableDisableView(image_video, true);

			break;
		}
		selectedCategory.setText(title);
		wheel(title);
	}

	/**
	 * Start wheel at the target point.
	 *
	 * @param title the title
	 */
	private void wheel(String title) {
		if (!running) {
			progress = 0;
			pw_two.resetCount(title);
			Thread s = new Thread(r);
			s.start();
		}
	}
	
	/**
	 * rotational speed of progresswheel.
	 *
	 * @param i the i
	 */
	private void delay(int i){
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
