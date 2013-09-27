package com.test.memory.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import com.test.memory.utils.Customization;


public class ChatListView extends ListView {
	
	private static final String TAG = "ChatListView";
	private static final boolean LOGV = Customization.VERBOSE;

	public ChatListView(Context context) {
		super(context);
	}
	
	public ChatListView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public ChatListView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (LOGV) Log.v(TAG, "onSizeChanged # ENTER");
		super.onSizeChanged(w, h, oldw, oldh);
		smoothScrollToPosition(getLastVisiblePosition());
		if (LOGV) Log.v(TAG, "onSizeChanged # EXIT");
	}

}