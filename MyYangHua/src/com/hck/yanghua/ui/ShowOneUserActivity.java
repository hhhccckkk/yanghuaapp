package com.hck.yanghua.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.hck.yanghua.data.GuanZhuMsgData;
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
	private Button guanzhuButton;
	private long uid;
	private GuanZhuMsgData guanZhuData;
	private long gid;
	private TextView fensiTextViewTitle, guanzhuTextViewTitle,
			dongtaiTextViewTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_one_user);
		userBean = new UserBean();
		uid = getIntent().getLongExtra("uid", -1);
		LogUtil.D("uid: " + uid);
		initTitleView("用户信息");
		initView();
		UserBean myDataBean = MyData.getData().getUserBean();
		if (myDataBean.getUid() == uid) {
			userBean = myDataBean;
			liaotianButton.setVisibility(View.GONE);
			guanzhuButton.setVisibility(View.GONE);
			updateView();
			initTitleLab("我");
		} else {
			getUserInfo();
			getGuanZhu();
		}

	}

	private void updateView() {
		if (userBean != null) {
			ImageLoader.getInstance().displayImage(userBean.getTouxiang(),
					touxiangImageView, MyTools.getoptions());
			userNameTextView.setText(userBean.getName());
			fensiTextView.setText(userBean.getFensi() + "");
			guanzhuTextView.setText(userBean.getGuanzhu() + "");
			if (userBean.getXingbie() == 1) {
				xingbieImageView.setImageResource(R.drawable.nan);
				initTitleLab("他");
			} else {
				xingbieImageView.setImageResource(R.drawable.nv);
				initTitleLab("她");
			}
			addressTextView.setText(userBean.getAddress());
			if (!TextUtils.isEmpty(userBean.getAihao())) {
				qianmingTextView.setText(userBean.getAihao());
			}
		}
	}

	private void initTitleLab(String title) {
		fensiTextViewTitle.setText(title + "的粉丝");
		guanzhuTextViewTitle.setText(title + "的关注");
		dongtaiTextViewTitle.setText(title + "的动态");
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
		fensiTextViewTitle = (TextView) findViewById(R.id.show_user_fensi_text);
		guanzhuTextViewTitle = (TextView) findViewById(R.id.show_user_guanzhu_text);
		dongtaiTextViewTitle = (TextView) findViewById(R.id.show_user_dongtai_text);
		guanzhuButton.setOnClickListener(this);
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
		case R.id.show_one_user_guanzhu:
			String textString = guanzhuButton.getText().toString();
			if ("关注".equals(textString)) {
				addGuanZhu();
			} else {
				deleteGuanZhu();

			}
			break;
		default:
			break;
		}
	}

	private void deleteGuanZhu() {
		params = new RequestParams();
		params.put("gid", gid + "");
		params.put("buid", userBean.getUid() + "");
		params.put("uid", MyData.getData().getUserId() + "");
		Request.deleteGuanZhu(params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + error + content);
				MyToast.showCustomerToast("取消关注失败");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				guanzhuButton.setText("关注");
				updateUser(-1);
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}
		});
	}

	private void getGuanZhu() {
		Pdialog.showLoading(this, false);
		params = new RequestParams();
		Request.getGuanZhuIds(params, new JsonHttpResponseHandler() {
			public void onFinish(String url) {
				Pdialog.hiddenDialog();
			};

			public void onFailure(Throwable error, String content) {
				LogUtil.D("onFailure: " + error + content);
				guanzhuButton.setVisibility(View.GONE);
			};

			public void onSuccess(int statusCode, JSONObject response) {
				LogUtil.D("onSuccess: " + response);
				try {
					guanZhuData = JsonUtils.parse(response.toString(),
							GuanZhuMsgData.class);
					updateGuanZhuBtn();
				} catch (Exception e) {
				}

			};
		});
	}

	private void updateGuanZhuBtn() {
		if (guanZhuData == null || guanZhuData.getBeans() == null) {
			guanzhuButton.setText("关注");
			return;
		}
		for (int i = 0; i < guanZhuData.getBeans().size(); i++) {
			if (guanZhuData.getBeans().get(i).getBuid() == uid) {
				gid = guanZhuData.getBeans().get(i).getGid();
				guanzhuButton.setText("取消关注");
				return;
			} else {
				guanzhuButton.setText("关注");
			}
		}
	}

	private void addGuanZhu() {
		Pdialog.showDialog(this, "处理中...", true);
		params = new RequestParams();
		params.put("buid", userBean.getUid() + "");
		Request.addGuanZhu(params, new JsonHttpResponseHandler() {
			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					int code = response.getInt("code");
					if (code == 0) {
						gid = response.getLong("gid");
						guanzhuButton.setText("取消关注");
						updateUser(1);
					} else {
						MyToast.showCustomerToast("网络异常 关注失败");
					}
				} catch (Exception e) {
				}
				Pdialog.hiddenDialog();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + error + content);
				MyToast.showCustomerToast("网络异常 关注失败");
			}
		});
	}
private void updateUser(int size){
	UserBean userBean=MyData.getData().getUserBean();
	userBean.setGuanzhu(userBean.getGuanzhu()+size);
	MyData.getData().setUserBean(userBean);
}

	private void getUserInfo() {
		Pdialog.showDialog(this, "正在获取用户信息", false);
		params = new RequestParams();
		params.put("uid", uid + "");
		Request.getUserData(Constant.METHOD_GET_USER_DATA, false, params,
				new JsonHttpResponseHandler() {
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

}
