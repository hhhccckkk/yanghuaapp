package com.hck.yanghua.adapter;

import java.util.List;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.HuoDongBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HuoDongAdapter extends BaseAdapter {
	private List<HuoDongBean> huoDongBeans;
	private LayoutInflater inflater;

	public HuoDongAdapter(List<HuoDongBean> huoDongBeans, Context context) {
		inflater = LayoutInflater.from(context);
		this.huoDongBeans = huoDongBeans;
	}

	@Override
	public int getCount() {
		return huoDongBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return huoDongBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_huodong_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.huodong_img);
			viewHolder.contentTextView = (TextView) convertView
					.findViewById(R.id.huodong_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		HuoDongBean huoDongBean = huoDongBeans.get(position);
		viewHolder.contentTextView.setText(huoDongBean.getContent());
		ImageLoader.getInstance().displayImage(huoDongBean.getImg(),
				viewHolder.imageView);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView contentTextView;
		TextView jinbiTextView;

	}

}
