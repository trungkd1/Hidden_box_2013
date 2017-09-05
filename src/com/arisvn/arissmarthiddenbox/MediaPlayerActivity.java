/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox - MediaPlayerActivity.java
 * Date create: 10:54:38 AM - Nov 19, 2013 - 2013
 * 
 * 
 */
package com.arisvn.arissmarthiddenbox;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.arisvn.arissmarthiddenbox.dialog.ConfirmDialogFragment;
import com.arisvn.arissmarthiddenbox.dialog.SynDialogFragment;
import com.arisvn.arissmarthiddenbox.encryption.Encryption;
import com.arisvn.arissmarthiddenbox.entity.FileItem;
import com.arisvn.arissmarthiddenbox.util.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class MediaPlayerActivity.
 */
public class MediaPlayerActivity extends ActionBarActivity implements
		OnPreparedListener, OnErrorListener {

	/** The video view. */
	private VideoView videoView;

	/** The ready to play. */
	private boolean readyToPlay;

	/** The image view. */
	private ImageView imageView;

	/** The media controller. */
	private MediaController mediaController;

	/** The file path. */
	private String filePath;
	
	private ActionBar actionBar;
	
	private FileItem fileItem;
	int height;
	int width;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		Intent intent = getIntent();
		if (intent != null) {
			fileItem=(FileItem) intent.getSerializableExtra(Utils.FILE_PATH);
			filePath = fileItem.getPathNew();
		}
		videoView = (VideoView) findViewById(R.id.videoView1);
		// listeners for VideoView:
		videoView.setOnErrorListener(this);
		videoView.setOnPreparedListener(this);
		mediaController = new MediaController(this);
		mediaController.setMediaPlayer(videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		imageView = (ImageView) findViewById(R.id.image);
		actionBar();
	}
	
    /**
     * Action bar.
     */
    private void actionBar() {
    	 actionBar = getSupportActionBar();
         actionBar.setCustomView(R.layout.player_action_bar);
         actionBar.setDisplayShowTitleEnabled(false);
         actionBar.setDisplayShowCustomEnabled(true);
         actionBar.setDisplayShowHomeEnabled(false);
         actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
    }

	/**
	 * Inits the media.
	 * 
	 * @param path
	 *            the path
	 */
	public void initMedia(String path) {
		readyToPlay = false;
		stopMedia();
		Uri videoUri = Uri.fromFile(new File(path));
		if (path.startsWith(Utils.FOLDER + Utils._PHOTO)) {
			imageView.setVisibility(View.VISIBLE);
			new ImageLoadingTask().execute(path);
			videoView.setVisibility(View.GONE);
		} else if (path.startsWith(Utils.FOLDER + Utils._AUDIO)) {
			SynDialogFragment.show(this, getString(R.string.loading));
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(Utils.getBitmapFromId(this, R.drawable.icon_audio_player));
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoURI(videoUri);
			videoView.requestFocus();
		} else {
			SynDialogFragment.show(this, getString(R.string.loading));
			imageView.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoURI(videoUri);
			videoView.requestFocus();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (App.needShowLogin && !App.isShowing) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setAction(Utils.ACTION_CATEGORY);
			startActivity(intent);
			App.isShowing = true;
		} else {
			// the next onResume will show login if not call onCreate method
			App.needShowLogin = true;
		}
		if (filePath != null && filePath.trim().length() > 0) {
			if (fileItem!=null) {
				TextView textView=((TextView)actionBar.getCustomView().findViewById(R.id.title));
				textView.setText(fileItem.getName());
				textView.setSelected(true);
				Utils.setTypeface(this, textView);
			}
			if (filePath.endsWith(Utils.ENCRYPT_EXTENSION)) {
				File file=new File(filePath.substring(0,
						filePath.lastIndexOf(Utils.ENCRYPT_EXTENSION)));
				if (file.exists()) {
					initMedia(file.getAbsolutePath());
				}else {
				// this is encrypted file
				String decryptePath = "";
				if (filePath.endsWith(Utils.ENCRYPT_EXTENSION)) {
					decryptePath = filePath.substring(0,
							filePath.lastIndexOf(Utils.ENCRYPT_EXTENSION));
				} else {
					decryptePath = filePath;
				}
				if (!filePath.equals(decryptePath)) {
					boolean rename = new File(filePath).renameTo(new File(
							decryptePath));
					if (rename && Utils.decrypt(this, decryptePath)) {
						initMedia(decryptePath);
					}
				}
				}
				

			}

		}
	}

	/**
	 * Callback invoked when error occurs during buffering or playback.
	 * 
	 * @param player
	 *            MediaPlayer which error refers to
	 * @param what
	 *            type of error
	 * @param extra
	 *            more specific information about the error
	 * @return TRUE when method handled the error, FALSE if didn't
	 */
	@Override
	public boolean onError(MediaPlayer player, int what, int extra) {
		SynDialogFragment.dismiss(this);
		ConfirmDialogFragment.show(this, "Can not play this media file");
		return true;
	}

	/**
	 * Callback invoked when media is ready for playback.
	 * 
	 * @param mp
	 *            MediaPlayer that is ready
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.setLooping(false);
		SynDialogFragment.dismiss(this);
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				stopMedia();
				finish();
			}
		});

		readyToPlay = true;
		playMedia();
	}

	/**
	 * Play media.
	 */
	public void playMedia() {
		if (readyToPlay) {
			videoView.start();
		}
	}

	/**
	 * Stop media.
	 */
	public void stopMedia() {
		if (videoView != null && videoView.getCurrentPosition() != 0) {
			videoView.pause();
			videoView.seekTo(0);
			videoView.stopPlayback();
			videoView.setVideoURI(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopMedia();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (filePath != null && filePath.trim().length() > 0) {
			if (filePath.endsWith(Utils.ENCRYPT_EXTENSION)) {
				// this is encrypted file
				String decryptePath = "";
				if (filePath.endsWith(Utils.ENCRYPT_EXTENSION)) {
					decryptePath = filePath.substring(0,
							filePath.lastIndexOf(Utils.ENCRYPT_EXTENSION));
				} else {
					decryptePath = filePath;
				}
				if (!filePath.equals(decryptePath)) {
					File file = new File(decryptePath);
					if (file.exists()) {
						boolean rename = file.renameTo(new File(filePath));
						if (rename) {
							int byte_to_encrypte = 0;

							if (filePath
									.startsWith(Utils.FOLDER + Utils._AUDIO)) {
								byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_AUDIO;

							} else if (filePath.startsWith(Utils.FOLDER
									+ Utils._PHOTO)) {
								byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_PHOTO;

							} else if (filePath.startsWith(Utils.FOLDER
									+ Utils._VIDEO)) {
								byte_to_encrypte = Encryption.BYTES_TO_ENCRYPT_VIDEO;

							}
							Utils.encrypt(this, filePath, byte_to_encrypte);
						}
					}

				}

			}

		}
	}
	class ImageLoadingTask extends AsyncTask<String, Void, Bitmap>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SynDialogFragment.show(MediaPlayerActivity.this,
					getString(R.string.loading));
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (params != null && params.length > 0) {
				return getBitmap(params[0]);
			} 
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			SynDialogFragment.dismiss(MediaPlayerActivity.this);
			imageView.setImageBitmap(result);
		}
		
		private Bitmap getBitmap(String imageFilePath) {
			// Load up the image's dimensions not the image itself
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(imageFilePath,
					bmpFactoryOptions);
			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
					/ (float) height);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
					/ (float) width);
			Log.v("HEIGHTRATIO", "" + heightRatio);
			Log.v("WIDTHRATIO", "" + widthRatio);
			// If both of the ratios are greater than 1, one of the sides of
			// the image is greater than the screen
			if (heightRatio > 1 && widthRatio > 1) {
				if (heightRatio > widthRatio) {
					// Height ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = heightRatio;
				} else {
					// Width ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
			}
			// Decode it for real
			bmpFactoryOptions.inPreferredConfig = Config.ARGB_8888;
			bmpFactoryOptions.inPurgeable = true;
	        DisplayMetrics dm = getResources().getDisplayMetrics();
	        bmpFactoryOptions.inDensity = dm.densityDpi;
			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
			return bmp;
		}
	}

}
