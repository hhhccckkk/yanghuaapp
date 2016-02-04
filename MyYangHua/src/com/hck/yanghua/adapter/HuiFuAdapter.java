package com.hck.yanghua.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.HuiTieBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.ui.ShowOneUserActivity;
import com.hck.yanghua.ui.TieZiXiangXiActivity;
import com.hck.yanghua.util.ExpressionUtil;
import com.hck.yanghua.util.GetImageUtil;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HuiFuAdapter extends BaseAdapter {

	private Context context;
	private List<HuiTieBean> huiTieBeans;

	public HuiFuAdapter(Context context, List<HuiTieBean> huiTieBeans) {
		this.context = context;
		this.huiTieBeans = huiTieBeans;
		if (this.huiTieBeans == null) {
			this.huiTieBeans = new ArrayList<>();
		}
	}

	@Override
	public int getCount() {
		return huiTieBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return huiTieBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_huitie_item, null);
			viewHolder = new ViewHolder();
			viewHolder.toxuaingImageView = (ImageView) convertView
					.findViewById(R.id.huitie_touxiang);
			viewHolder.xingbieImageView = (ImageView) convertView
					.findViewById(R.id.huitie_xingbie);
			viewHolder.userNameTextView = (TextView) convertView
					.findViewById(R.id.huitie_userName);
			viewHolder.tiemTextView = (TextView) convertView
					.findViewById(R.id.huitie_time);
			viewHolder.contentTextView = (TextView) convertView
					.findViewById(R.id.huitie_content);
			viewHolder.imageView1 = (ImageView) convertView
					.findViewById(R.id.huitie_img1);

			viewHolder.imageView2 = (ImageView) convertView
					.findViewById(R.id.huitie_img2);
			viewHolder.imageView3 = (ImageView) convertView
					.findViewById(R.id.huitie_img3);
			viewHolder.fensiTextView = (TextView) convertView
					.findViewById(R.id.huitie_fensi);
			viewHolder.addressTextView = (TextView) convertView
					.findViewById(R.id.huitie_address);
			viewHolder.huifuImageView = (ImageView) convertView
					.findViewById(R.id.huitie_image);
			viewHolder.yuantieTextView = (TextView) convertView
					.findViewById(R.id.huitie_yuantie);
			viewHolder.userTextView = (TextView) convertView
					.findViewById(R.id.huitie_yuan_userName);
			viewHolder.huiTieLayout = (LinearLayout) convertView
					.findViewById(R.id.huitie_huifu_lay);
			setListener(viewHolder.huifuImageView);
			viewHolder.headerLayout = (LinearLayout) convertView
					.findViewById(R.id.huitie_header);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		HuiTieBean huiTieBean = huiTieBeans.get(position);
		viewHolder.huifuImageView.setTag(huiTieBean);
		SpannableString spannableString = ExpressionUtil.getExpressionString(
				context, huiTieBean.getContent(), Constant.zhengze);
		viewHolder.contentTextView.setText(spannableString);
		viewHolder.userNameTextView.setText(huiTieBean.getName());
		viewHolder.addressTextView.setText(huiTieBean.getAddress());
		String yuantie = huiTieBean.getYuantie();
		if (TextUtils.isEmpty(yuantie)) {
			viewHolder.yuantieTextView.setVisibility(View.GONE);
			viewHolder.userTextView.setVisibility(View.GONE);
			viewHolder.huiTieLayout.setVisibility(View.GONE);
		} else {
			viewHolder.yuantieTextView.setVisibility(View.VISIBLE);
			viewHolder.userTextView.setVisibility(View.VISIBLE);
			viewHolder.huiTieLayout.setVisibility(View.VISIBLE);
			viewHolder.userTextView.setText(huiTieBean.getHuifuUserName());
			SpannableString spannableString2 = ExpressionUtil
					.getExpressionString(context, yuantie, Constant.zhengze);
			viewHolder.yuantieTextView.setText(spannableString2);
		}

		if (huiTieBean.isXingbie()) {
			viewHolder.xingbieImageView.setImageResource(R.drawable.nan);
		} else {
			viewHolder.xingbieImageView.setImageResource(R.drawable.nv);
		}
		String imag1, imag2, imag3;
		imag1 = huiTieBean.getImage1();
		imag2 = huiTieBean.getIamge2();
		imag3 = huiTieBean.getIamge3();
		if (TextUtils.isEmpty(imag1)) {
			viewHolder.imageView1.setVisibility(View.GONE);
		} else {
			viewHolder.imageView1.setVisibility(View.VISIBLE);
			if (huiTieBean.isBenDi()) {
				LogUtil.D("isBenDiisBenDiisBenDiisBenDi");
				ImageLoader.getInstance().displayImage("file://" + imag1,
						viewHolder.imageView1);
			} else {
				GetImageUtil.showImageDaTu(imag1, viewHolder.imageView1);
			}
			viewHolder.imageView1.setTag(huiTieBean);
			onCliceImgCallBack(viewHolder.imageView1);

		}
		if (TextUtils.isEmpty(imag2)) {
			viewHolder.imageView2.setVisibility(View.GONE);
		} else {
			viewHolder.imageView2.setVisibility(View.VISIBLE);
			if (huiTieBean.isBenDi()) {
				ImageLoader.getInstance().displayImage("file://" + imag2,
						viewHolder.imageView2);
			} else {
				GetImageUtil.showImageDaTu(imag2, viewHolder.imageView2);
			}
			viewHolder.imageView2.setTag(huiTieBean);
			onCliceImgCallBack(viewHolder.imageView2);

		}
		if (TextUtils.isEmpty(imag3)) {
			viewHolder.imageView3.setVisibility(View.GONE);
		} else {
			viewHolder.imageView3.setVisibility(View.VISIBLE);
			if (huiTieBean.isBenDi()) {
				ImageLoader.getInstance().displayImage("file://" + imag3,
						viewHolder.imageView3);
			} else {
				GetImageUtil.showImageDaTu(imag3, viewHolder.imageView3);
			}
			viewHolder.imageView3.setTag(huiTieBean);
			onCliceImgCallBack(viewHolder.imageView3);
		}
		viewHolder.tiemTextView.setText("丨"
				+ TimeUtil.forTime(huiTieBean.getTime()));
		viewHolder.fensiTextView.setText("粉丝" + huiTieBean.getFensi());
		ImageLoader.getInstance().displayImage(huiTieBean.getTouxiang(),
				viewHolder.toxuaingImageView, MyTools.getoptions());
		viewHolder.toxuaingImageView.setTag(huiTieBean.getUid());
		setShowUserActivity(viewHolder.toxuaingImageView);
		viewHolder.headerLayout.setTag(huiTieBean.getUid());
		setShowUserActivity(viewHolder.headerLayout);
		return convertView;
	}

	static class ViewHolder {
		ImageView toxuaingImageView, xingbieImageView, huifuImageView;
		TextView userNameTextView, tiemTextView, contentTextView,
				addressTextView, fensiTextView, yuantieTextView, userTextView;
		ImageView imageView1, imageView2, imageView3;
		LinearLayout huiTieLayout;
		LinearLayout headerLayout;
	}

	private void setListener(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HuiTieBean huiTieBean = (HuiTieBean) imageView.getTag();
				((TieZiXiangXiActivity) context).huiFu(huiTieBean);
			}
		});
	}

	private void onCliceImgCallBack(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HuiTieBean huiTieBean = (HuiTieBean) imageView.getTag();
				((TieZiXiangXiActivity) context).onclickImg(huiTieBean);
				
			}
		});
	}

	private void setShowUserActivity(View view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("uid", (Long) v.getTag());
				intent.setClass(context, ShowOneUserActivity.class);
				context.startActivity(intent);
			}
		});
	}

	public void updateView(List<HuiTieBean> huiTieBeans) {
		this.huiTieBeans.addAll(huiTieBeans);
		this.notifyDataSetChanged();
	}

	public void updateView(HuiTieBean huiTieBeans) {
		this.huiTieBeans.add(0, huiTieBeans);
		this.notifyDataSetChanged();
	}

}
