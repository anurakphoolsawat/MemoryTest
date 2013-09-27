package com.test.memory;

import com.test.memory.utils.BitmapUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class A1Activity extends Activity {
	
	private ImageView mImageViewFullScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a1);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText("activity_a1");
		mImageViewFullScreen = (ImageView) findViewById(R.id.imageViewFullScreen);
		mImageViewFullScreen.setImageBitmap(BitmapUtil.getBitmap());
		mImageViewFullScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
		});

	}

	public void next() {
		
		Intent intent = new Intent(getApplicationContext(), A2Activity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		BitmapUtil.recycleBitmap(mImageViewFullScreen);
		super.onDestroy();
	}
}
