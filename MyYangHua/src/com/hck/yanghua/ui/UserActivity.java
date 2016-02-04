package com.hck.yanghua.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserActivity extends BaseActivity {
	public static final int MY_FENSI = 1;
	public static final int MY_GUAN_ZHU = 2;
	private ImageView touxiangImageView;
	private TextView dengjiTextView, huabiTextView, dongtaiTextView,
			fensiTextView, guanzhuTextView, userTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initTitle();
		initView();
		getMyTieZiSize();

	}

	public void showMyTieZi(View view) {
		startActivity(new Intent(this, ShowMyTieZiActivity.class));
	}

	public void showMyFenSi(View view) {
		Intent intent = new Intent();
		intent.putExtra("type", MY_FENSI);
		intent.putExtra("fensi", true);
		intent.setClass(this, ShowMyFenSiActivity.class);
		startActivity(intent);
	}

	public void showMyGuanZhu(View view) {
		Intent intent = new Intent();
		intent.putExtra("type", MY_GUAN_ZHU);
		intent.putExtra("fensi", true);
		intent.setClass(this, ShowMyFenSiActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void getMyTieZiSize() {
		RequestParams params = new RequestParams();
		Request.getMyTieZiSize(params, new JsonHttpResponseHandler() {
			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + error + content);
				dongtaiTextView.setText(0 + "");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					int size = response.getInt("size");
					dongtaiTextView.setText(size + "");
				} catch (Exception e) {
					dongtaiTextView.setText(0 + "");
				}
			}
		});
	}

	private void initView() {
		touxiangImageView = (ImageView) findViewById(R.id.userImg);
		dengjiTextView = (TextView) findViewById(R.id.user_dengji);
		huabiTextView = (TextView) findViewById(R.id.user_huabi);
		dongtaiTextView = (TextView) findViewById(R.id.user_dongtai);
		fensiTextView = (TextView) findViewById(R.id.user_fensi);
		guanzhuTextView = (TextView) findViewById(R.id.user_guanzhu);
		userTextView = (TextView) findViewById(R.id.user_name);

	}

	private void initData() {
		UserBean userBean = MyData.getData().getUserBean();
		if (userBean != null) {
			ImageLoader.getInstance().displayImage(userBean.getTouxiang(),
					touxiangImageView, MyTools.getImageOptions(35));
			huabiTextView.setText(userBean.getJinbi() + "");
			fensiTextView.setText(userBean.getFensi() + "");
			guanzhuTextView.setText(userBean.getGuanzhu() + "");
			userTextView.setText(userBean.getName() + "");
		}
	}

	private void initTitle() {
		leftTextView.setVisibility(View.GONE);
		centerTextView.setText("用户中心");
		centerTextView.setBackgroundResource(R.color.transparent);
		rightTextView.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& !MainActivity.mSlidingMenu.isMenuShowing()) {
			alertExitD();
			return false;
		}

		return true;
	}

}
