package com.test.memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class A11Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a11);
	}
	
	public void next(View view) {
		Intent intent = new Intent(getApplicationContext(), A12Activity.class);
		startActivity(intent);
		finish();
	}
}
