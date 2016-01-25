package com.hck.yanghua.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.MsgBean;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HuiFuMsgAdpter extends BaseAdapter {
	private Context context;
	public List<MsgBean> msgBeans;

	public HuiFuMsgAdpter(Context context, List<MsgBean> msgBeans) {
		this.context = context;
		this.msgBeans = msgBeans;
		if (this.msgBeans == null) {
			this.msgBeans = new ArrayList<>();
		}
	}

	@Override
	public int getCount() {
		return msgBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return msgBeans.get(position);
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
					R.layout.list_item_huifu_msg, null);
			viewHolder.touxiangImageView = (ImageView) convertView
					.findViewById(R.id.msg_huifu_tx);
			viewHolder.contentTextView = (TextView) convertView
					.findViewById(R.id.msg_huifu_content);
			viewHolder.yuantieTextView = (TextView) convertView
					.findViewById(R.id.msg_yuantie);
			viewHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.msg_huifu_time);
			viewHolder.liaotianButton = (Button) convertView
					.findViewById(R.id.msg_huifu_liaotian);
			viewHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.msg_huifu_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.timeTextView.setText(TimeUtil.forTime(msgBeans.get(position)
				.getTime()));
		viewHolder.yuantieTextView.setText(msgBeans.get(position).getYuantie());
		viewHolder.contentTextView.setText(msgBeans.get(position).getContent());
		viewHolder.nameTextView.setText(msgBeans.get(position).getUserName());
		ImageLoader.getInstance().displayImage(
				msgBeans.get(position).getTouxiang(),
				viewHolder.touxiangImageView,MyTools.getoptions());
		return convertView;
	}

	class ViewHolder {
		ImageView touxiangImageView;
		TextView nameTextView, timeTextView;
		TextView yuantieTextView, contentTextView;
		Button liaotianButton;
	}

}
