package com.hck.yanghua.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.GuanZhuBean;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.db.MsgInviteBean;
import com.hck.yanghua.fragment.GuanZhuMsgFragment;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GuanZhuAdpter extends BaseAdapter {
	public List<GuanZhuBean> guanZhuBeans;
	private Context context;
	private OnTongZhiListener onTongZhiListener;
	private boolean isMyFenSi;

	public interface OnTongZhiListener {
		void caozuo(GuanZhuBean guanZhuBean);
	}

	public GuanZhuAdpter(List<GuanZhuBean> guanZhuBeans, Context context,
			OnTongZhiListener onTongZhiListener, boolean isMyFensi) {
		this.guanZhuBeans = guanZhuBeans;
		this.context = context;
		this.onTongZhiListener = onTongZhiListener;
		this.isMyFenSi = isMyFensi;
	}

	@Override
	public int getCount() {
		return guanZhuBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return guanZhuBeans.get(position);
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
					R.layout.list_item_gz, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.gz_user_img);
			viewHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.gz_time);
			viewHolder.userTextView = (TextView) convertView
					.findViewById(R.id.gz_userName);
			viewHolder.caozuoTextView = (TextView) convertView
					.findViewById(R.id.tz_caozuo);
			viewHolder.xinbeiImageView = (ImageView) convertView
					.findViewById(R.id.gz_img);
			viewHolder.guanzhuImageView = (TextView) convertView
					.findViewById(R.id.guanzhu);
			setListener(viewHolder.caozuoTextView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.timeTextView.setText(TimeUtil.forTime(guanZhuBeans.get(
				position).getTime()));
		viewHolder.userTextView.setText(guanZhuBeans.get(position).getName()
				+ "");
		int xingbie = guanZhuBeans.get(position).getXingbie();
		if (isMyFenSi) {
			viewHolder.guanzhuImageView.setVisibility(View.GONE);
		} else {
			viewHolder.guanzhuImageView.setVisibility(View.VISIBLE);
		}
		if (xingbie == 1) {
			viewHolder.xinbeiImageView.setBackgroundResource(R.drawable.nan);
		} else {
			viewHolder.xinbeiImageView.setBackgroundResource(R.drawable.nv);
		}
		ImageLoader.getInstance().displayImage(
				guanZhuBeans.get(position).getTouxiang(), viewHolder.imageView,
				MyTools.getoptions());
		viewHolder.caozuoTextView.setTag(guanZhuBeans.get(position));
		return convertView;
	}

	private void setListener(TextView textView) {
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GuanZhuBean guanZhuBean = (GuanZhuBean) v.getTag();
				onTongZhiListener.caozuo(guanZhuBean);

			}
		});
	}

	public void updateData(List<GuanZhuBean> guanZhuBeans) {
		this.guanZhuBeans.addAll(guanZhuBeans);
		this.notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView imageView, xinbeiImageView;
		TextView timeTextView, userTextView, caozuoTextView, guanzhuImageView;
	}

}
