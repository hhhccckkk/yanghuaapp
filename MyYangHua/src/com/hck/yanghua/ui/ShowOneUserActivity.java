package com.hck.yanghua.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.exceptions.EaseMobException;
import com.google.android.gms.internal.el;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.view.Pdialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowOneUserActivity extends BaseTitleActivity implements
		OnClickListener {
	private ImageView touxiangImageView, xingbieImageView;
	private TextView userNameTextView, qianmingTextView, fensiTextView,
			guanzhuTextView, addressTextView;
	private Button liaotianButton;
	private UserBean userBean;
	private String uid;
	private Button guanzhuButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_one_user);
		userBean = new UserBean();
		uid = getIntent().getStringExtra("uid");
		initTitleView("用户信息");
		initView();
		UserBean myDataBean = MyData.getData().getUserBean();
		if (myDataBean.getUserId().equals(uid)) {
			userBean = myDataBean;
			liaotianButton.setVisibility(View.GONE);
			guanzhuButton.setVisibility(View.GONE);
			updateView();
		} else {
			getUserInfo();
		}

	}

	private void updateView() {
		if (userBean != null) {
			ImageLoader.getInstance().displayImage(userBean.getTouxiang(),
					touxiangImageView, MyTools.getoptions());
			userNameTextView.setText(userBean.getName());
			fensiTextView.setText(userBean.getFensi() + "");
			guanzhuTextView.setText(userBean.getGuanzhu() + "");
			LogUtil.D("性别: " + userBean.getXingbie());
			if (userBean.getXingbie() == 1) {
				xingbieImageView.setImageResource(R.drawable.nan);
			} else {
				xingbieImageView.setImageResource(R.drawable.nv);
			}
			addressTextView.setText(userBean.getAddress());
			if (!TextUtils.isEmpty(userBean.getAihao())) {
				qianmingTextView.setText(userBean.getAihao());
			}

		}
	}

	private void initView() {
		liaotianButton = (Button) findViewById(R.id.user_liaotian);
		touxiangImageView = (ImageView) findViewById(R.id.one_user_img);
		xingbieImageView = (ImageView) findViewById(R.id.one_user_xingbei);
		userNameTextView = (TextView) findViewById(R.id.one_user_name);
		qianmingTextView = (TextView) findViewById(R.id.one_user_qianming);
		fensiTextView = (TextView) findViewById(R.id.one_user_fensi);
		guanzhuTextView = (TextView) findViewById(R.id.one_user_guanzhu);
		addressTextView = (TextView) findViewById(R.id.one_user_address);
		guanzhuButton = (Button) findViewById(R.id.show_one_user_guanzhu);

		liaotianButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_liaotian:
			try {
				UserBean userBean2 = MyData.getData().getUserBean();
				Intent intent = new Intent();
				intent.putExtra("user", userBean);
				intent.setClass(ShowOneUserActivity.this, ChatActivity.class);
				intent.putExtra("fromImg", userBean2.getTouxiang());
				intent.putExtra("fromUserName", userBean2.getName());
				startActivity(intent);
			} catch (Exception e) {
			}

			break;

		default:
			break;
		}
	}

	private void getUserInfo() {
		Pdialog.showDialog(this, "正在获取用户信息", false);
		params = new RequestParams();
		params.put("uid", uid);
		Request.getUserInfoByStId(Constant.METHOD_GET_USER_BY_STRING_ID,
				params, new JsonHttpResponseHandler() {
					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						Pdialog.hiddenDialog();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response.toString());
						try {
							userBean = JsonUtils.parse(
									response.getString("data"), UserBean.class);
							updateView();
						} catch (Exception e) {
							LogUtil.D("eeeeee: " + e.toString());
							MyToast.showCustomerToast("获取用户数据失败");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + error + content);
						MyToast.showCustomerToast("获取用户数据失败");
					}
				});
	}

	private void addFriend() {
		Pdialog.showDialog(this, "处理中...", true);
		new Thread() {
			public void run() {
				try {
					LogUtil.D("getUserId: " + uid);
					EMContactManager.getInstance().addContact(uid, "加个好友呗");
					runOnUiThread(new Runnable() {
						public void run() {
							Pdialog.hiddenDialog();
							Toast.makeText(getApplicationContext(),
									"加好友请求发送成功", 4).show();
						}
					});
				} catch (EaseMobException e) {
					LogUtil.D("EaseMobException: " + e.toString());
					runOnUiThread(new Runnable() {
						public void run() {
							Pdialog.hiddenDialog();
							Toast.makeText(getApplicationContext(), "加好友请求失败",
									4).show();
						}
					});

				}
			};
		}.start();

	}
}
