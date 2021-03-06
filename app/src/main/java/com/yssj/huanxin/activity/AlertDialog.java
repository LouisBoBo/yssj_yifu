///**
// * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.yssj.huanxin.activity;
//
//import java.io.File;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.easemob.util.ImageUtils;
//import com.yssj.activity.R;
//import com.yssj.huanxin.utils.DownloadImageTask;
//import com.yssj.huanxin.utils.ImageCache;
//import com.yssj.ui.base.BasicActivity;
//
//public class AlertDialog extends Activity {
//	private TextView mTextView;
//	private Button mButton;
//	private int position;
//	private ImageView imageView;
//	private LinearLayout  queding,root;
//	private EditText editText;
//	private boolean isEditextShow;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.alert_dialog);
//		queding = (LinearLayout) findViewById(R.id.queding);
//		queding.setBackgroundColor(Color.WHITE);
//		root = (LinearLayout) findViewById(R.id.root);
//		root.setBackgroundColor(Color.WHITE);
//		mTextView = (TextView) findViewById(R.id.title);
//		mButton = (Button) findViewById(R.id.btn_cancel);
//		imageView = (ImageView) findViewById(R.id.image);
//		editText = (EditText) findViewById(R.id.edit);
//		//????????????
//		String msg = getIntent().getStringExtra("msg");
//		//????????????
//		String title = getIntent().getStringExtra("title");
//		position = getIntent().getIntExtra("position", -1);
//		//????????????????????????
//		boolean isCanceTitle=getIntent().getBooleanExtra("titleIsCancel", false);
//		//????????????????????????
//		boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
//		//???????????????????????????
//		isEditextShow = getIntent().getBooleanExtra("editTextShow",false);
//		//????????????????????????path
//		String path = getIntent().getStringExtra("forwardImage");
//		//
//		String edit_text = getIntent().getStringExtra("edit_text");
//
//		if(msg != null)
//		    ((TextView)findViewById(R.id.alert_message)).setText(msg);
//		if(title != null)
//			mTextView.setText(title);
//		if(isCanceTitle){
//			mTextView.setVisibility(View.GONE);
//		}
//		if(isCanceShow)
//			mButton.setVisibility(View.VISIBLE);
//		if(path != null){
//			 //???????????????????????????????????????
//			if(!new File(path).exists())
//				path = DownloadImageTask.getThumbnailImagePath(path);
//		    imageView.setVisibility(View.VISIBLE);
//		    ((TextView)findViewById(R.id.alert_message)).setVisibility(View.GONE);
//		    if(ImageCache.getInstance().get(path) != null){
//		        imageView.setImageBitmap(ImageCache.getInstance().get(path));
//		    }else{
//		        Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
//		        imageView.setImageBitmap(bm);
//		        ImageCache.getInstance().put(path, bm);
//		    }
//
//		}
//		if(isEditextShow){
//			editText.setVisibility(View.VISIBLE);
//			editText.setText(edit_text);
//		}
//	}
//
//	public void ok(View view){
//		setResult(RESULT_OK,new Intent().putExtra("position", position).
//				putExtra("edittext", editText.getText().toString())
//				/*.putExtra("voicePath", voicePath)*/);
//		if(position != -1)
//			ChatActivity.resendPos = position;
//		finish();
//
//	}
//
//	/*@Override
//	protected void onResume() {
//		super.onResume();
//		JPushInterface.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		JPushInterface.onPause(this);
//
//	}*/
//
//	public void cancel(View view){
//		finish();
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event){
//		finish();
//		return true;
//	}
//
//
//
//
//
//}
