package com.test.memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class A2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a2);
	}
	
	public void next(View view) {
		Intent intent = new Intent(getApplicationContext(), A3Activity.class);
		startActivity(intent);
//		finish();
	}
}
