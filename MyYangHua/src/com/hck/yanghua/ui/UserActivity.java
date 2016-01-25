package com.hck.yanghua.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.util.MyTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserActivity extends BaseActivity {
	private ImageView touxiangImageView;
	private TextView dengjiTextView, huabiTextView, dongtaiTextView,
			fensiTextView, guanzhuTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initTitle();
		initView();
		initData();

	}

	private void initView() {
		touxiangImageView = (ImageView) findViewById(R.id.userImg);
		dengjiTextView = (TextView) findViewById(R.id.user_dengji);
		huabiTextView = (TextView) findViewById(R.id.user_huabi);
		dongtaiTextView = (TextView) findViewById(R.id.user_dongtai);
		fensiTextView = (TextView) findViewById(R.id.user_fensi);
		guanzhuTextView = (TextView) findViewById(R.id.user_guanzhu);

	}

	private void initData() {
		UserBean userBean = MyData.getData().getUserBean();
		if (userBean != null) {
			ImageLoader.getInstance().displayImage(userBean.getTouxiang(),
					touxiangImageView, MyTools.getImageOptions(35));
			// dengjiTextView.setText(userBean.get);
			huabiTextView.setText(userBean.getJinbi() + "");
			// dongtaiTextView.setText(resid);
			fensiTextView.setText(userBean.getFensi() + "");
			guanzhuTextView.setText(userBean.getGuanzhu() + "");
		}
	}

	private void initTitle() {
		leftTextView.setVisibility(View.GONE);
		centerTextView.setText("用户中心");
		centerTextView.setBackgroundResource(R.color.transparent);
		rightTextView.setVisibility(View.GONE);
	}

	public void showMyFriend(View view) {
		startActivity(new Intent(this, FriendActivity.class));

	}

}
