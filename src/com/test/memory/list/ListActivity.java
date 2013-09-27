package com.test.memory.list;

import com.test.memory.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class ListActivity extends Activity{
	private ListView listView;
	private ChatAdapter mChatAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_list);
		
		listView = (ListView) findViewById(R.id.list_chat_message);
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mChatAdapter = new ChatAdapter(this, inflater);
		listView.setAdapter(mChatAdapter);
	}
}
