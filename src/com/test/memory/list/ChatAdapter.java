package com.test.memory.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.memory.R;
import com.test.memory.utils.Customization;
import com.test.memory.utils.FLog;

public class ChatAdapter extends BaseAdapter {

	private static final String TAG = "ChatAdapter";
	private static final boolean VERBOSE = false;
	private static final boolean INFO = false;
	private static final boolean LOGV = Customization.VERBOSE ? VERBOSE : false;
	private static final boolean LOGD = Customization.DEBUG;
	private static final boolean LOGI = Customization.INFO ? INFO : false;
	private static final boolean LOGW = Customization.WARNING;
	
	private static final int VIEW_TYPE_GAP = 25;
	
	private static final int VIEW_TYPE_LOAD_HISTORY = 0 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_SYSTEM 		= 1 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_TEXT_IN		= 2 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_TEXT_OUT		= 3 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_STICKER_IN	= 4 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_STICKER_OUT	= 5 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_FILE_IN 		= 6 * VIEW_TYPE_GAP;
	private static final int VIEW_TYPE_FILE_OUT		= 7 * VIEW_TYPE_GAP;
	
	private LayoutInflater mInflater;
	
	public ChatAdapter(Activity context,  LayoutInflater inflater) {
		mInflater = inflater;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)  {
		if(LOGV) FLog.v(TAG, "getView # ENTER");
		
		if (convertView == null || convertView.getTag() == null
				|| !(convertView.getTag() instanceof ViewChat)) {
			convertView = createViewHolder(position);
		}
		
		if(LOGV) FLog.v(TAG, "getView # EXIT");
		return convertView;
	}
	
	
	private View createViewHolder(int position) {
		ViewChat viewHolder = null;

		int viewType = (getItemViewType(position) / VIEW_TYPE_GAP) * VIEW_TYPE_GAP;
		if (LOGV)
			FLog.v(TAG, "createViewHolder # viewType: %d", viewType);

		int layoutResId = -1;

		switch (viewType) {
		case VIEW_TYPE_TEXT_OUT:
			layoutResId = R.layout.layout_chat_outgoing_text;
			viewHolder = new ViewChat();
			break;
		case VIEW_TYPE_TEXT_IN:
			layoutResId = R.layout.layout_chat_incoming_text;
			viewHolder = new ViewChat();
			break;
		default:
			layoutResId = R.layout.layout_chat_incoming_text;
			viewHolder = new ViewChat();
			break;
//		case VIEW_TYPE_STICKER_OUT:
//			layoutResId = R.layout.layout_chat_outgoing_sticker;
//			viewHolder = new ViewChat();
//			break;
//		case VIEW_TYPE_STICKER_IN:
//			layoutResId = R.layout.layout_chat_incoming_sticker;
//			viewHolder = new ViewChat();
//			break;
//		case VIEW_TYPE_FILE_OUT:
//			layoutResId = R.layout.layout_chat_outgoing_file;
//			viewHolder = new ViewChat();
//			break;
//		case VIEW_TYPE_FILE_IN:
//			layoutResId = R.layout.layout_chat_incoming_file;
//			viewHolder = new ViewChat();
//			break;
//		case VIEW_TYPE_SYSTEM:
//			layoutResId = R.layout.layout_chat_system_message;
//			viewHolder = new ViewSystemMessage();
//			break;
//		case VIEW_TYPE_LOAD_HISTORY:
//			layoutResId = R.layout.layout_loading_more_list_items;
//			viewHolder = new ViewLoading();
//			break;
		}

		View convertView = mInflater.inflate(layoutResId, null);
		convertView.setTag(viewHolder);

		return convertView;
	}
	
	class ViewChat {
		TextView date;
		TextView displayName;
		TextView time;
		TextView message;
		ImageView avatar;
		ImageView avatarAnimation;
		ImageView imagePlaySticker;
		ImageView imageVideoPlayBtn;
		ImageView imageFileThumbnail;
		ProgressBar uploadProgress;
		View layoutProgress;

		ImageView msgRead;
		ImageButton failed;
		View layout_main;
	}

}
