package com.hck.yanghua.adapter;

import java.util.List;

import com.google.android.gms.internal.el;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.UserData;
import com.hck.yanghua.util.MyTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearUserAdapter extends BaseAdapter {
	private List<UserBean> userDatas;
	private Context context;

	public NearUserAdapter(List<UserBean> userDatas, Context context) {
		this.userDatas = userDatas;
		this.context = context;
	}

	@Override
	public int getCount() {
		return userDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return userDatas.get(position);
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
					R.layout.list_near_user_item, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.near_user_img);
			viewHolder.userTextView = (TextView) convertView
					.findViewById(R.id.near_user_name);
			viewHolder.fensiTextView = (TextView) convertView
					.findViewById(R.id.near_fensi);
			viewHolder.juliTextView = (TextView) convertView
					.findViewById(R.id.near_juli);
			viewHolder.xingbieImageView = (ImageView) convertView
					.findViewById(R.id.xingbie);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		UserBean userBean = userDatas.get(position);
		viewHolder.userTextView.setText(userBean.getName() + "");
		viewHolder.fensiTextView.setText("粉丝:"+userBean.getFensi());
		double juli=userBean.getJuli();
		double juli2=juli/1000;
		viewHolder.juliTextView.setText(juli2 + "km");
		if (userBean.getXingbie() == 1) {
			viewHolder.xingbieImageView.setBackgroundResource(R.drawable.nan);
		} else {
			viewHolder.xingbieImageView.setBackgroundResource(R.drawable.nv);
		}
		ImageLoader.getInstance().displayImage(userBean.getTouxiang(), viewHolder.imageView,MyTools.getImageOptions(35));
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView userTextView;
		TextView juliTextView;
		TextView fensiTextView;
		ImageView xingbieImageView;
	}

}
