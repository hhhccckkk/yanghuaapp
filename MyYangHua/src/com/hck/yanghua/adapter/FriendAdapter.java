package com.hck.yanghua.adapter;

import java.util.List;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.FriendBean;
import com.hck.yanghua.util.MyTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter {
	private List<FriendBean> friendBeans;
	private Context context;

	public FriendAdapter(List<FriendBean> friendBeans, Context context) {
		this.friendBeans = friendBeans;
		this.context = context;
	}

	@Override
	public int getCount() {
		return friendBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return friendBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_friend, null);
			viewHolder.userImageView = (ImageView) convertView
					.findViewById(R.id.frined_tx);
			viewHolder.usernameTextView = (TextView) convertView
					.findViewById(R.id.friend_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(
				friendBeans.get(position).getUserImg(),
				viewHolder.userImageView, MyTools.getImageOptions());
		viewHolder.usernameTextView.setText(friendBeans.get(position)
				.getUserName());
		return convertView;
	}

	class ViewHolder {
		TextView usernameTextView;
		ImageView userImageView;
	}

}
