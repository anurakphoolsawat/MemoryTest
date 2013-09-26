package com.test.memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class A9Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a9);
	}
	
	public void next(View view) {
		Intent intent = new Intent(getApplicationContext(), A10Activity.class);
		startActivity(intent);
		finish();
	}
}
