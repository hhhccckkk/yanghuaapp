package com.hck.yanghua.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.MsgBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.fragment.HuiFuMsgFragment;
import com.hck.yanghua.util.ExpressionUtil;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HuiFuMsgAdpter extends BaseAdapter {
	private Context context;
	public List<MsgBean> msgBeans;
	private LiaoTianCallBack callBack;

	public interface LiaoTianCallBack {
		void startLiaoTian(Object object);
	}

	public HuiFuMsgAdpter(Context context, List<MsgBean> msgBeans,
			LiaoTianCallBack callBack) {
		this.context = context;
		this.msgBeans = msgBeans;
		this.callBack = callBack;
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
			viewHolder.liaotianButton = (TextView) convertView
					.findViewById(R.id.msg_huifu_liaotian);
			viewHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.msg_huifu_name);
			viewHolder.xingbieImageView = (ImageView) convertView
					.findViewById(R.id.msg_huifu_xingbei);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.timeTextView.setText(TimeUtil.forTime(msgBeans.get(position)
				.getTime()));
		SpannableString spannableString1 = ExpressionUtil.getExpressionString(
				context, msgBeans.get(position).getYuantie(), Constant.zhengze);
		viewHolder.yuantieTextView.setText(spannableString1);

		SpannableString spannableString = ExpressionUtil.getExpressionString(
				context, msgBeans.get(position).getContent(), Constant.zhengze);
		viewHolder.contentTextView.setText(spannableString);

		viewHolder.nameTextView.setText(msgBeans.get(position).getUserName());
		ImageLoader.getInstance().displayImage(
				msgBeans.get(position).getTouxiang(),
				viewHolder.touxiangImageView, MyTools.getoptions());
		viewHolder.liaotianButton.setTag(msgBeans.get(position));
		if (msgBeans.get(position).getXingbie() == 1) {
			viewHolder.xingbieImageView.setBackgroundResource(R.drawable.nan);
		} else {
			viewHolder.xingbieImageView.setBackgroundResource(R.drawable.nv);
		}
		setListener(viewHolder.liaotianButton);
		return convertView;
	}

	class ViewHolder {
		ImageView touxiangImageView, xingbieImageView;
		TextView nameTextView, timeTextView;
		TextView yuantieTextView, contentTextView;
		TextView liaotianButton;
	}

	private void setListener(View view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.startLiaoTian(v.getTag());
				}

			}
		});
	}

	public void updateView(List<MsgBean> msgBeans) {
		this.msgBeans.addAll(msgBeans);
		this.notifyDataSetChanged();
	}

}
