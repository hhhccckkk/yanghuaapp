package com.hck.yanghua.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.BanBenBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.downapp.UpdateManager;
import com.hck.yanghua.downapp.UpdateUtil;
import com.hck.yanghua.downapp.UpdateUtil.UpdateAppCallBack;
import com.hck.yanghua.location.MyLocation;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.AppManager;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.view.CustomAlertDialog;

public class SplashActivity extends Activity implements UpdateAppCallBack {
	private static final int LOGIN_ERROR = 0;
	private static final int LOGIN_SUCCESS = 1;
	private static final int LOGIN_CANCEL = 2;
	private BanBenBean banBenBean; // 版本信息
	private Button loginBtn; // 登录按钮
	private View pBar; // 圈圈
	private BDLocation bdLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_splash);
		initView();
		setListener();
		new UpdateUtil().isUpdate(this); // 监测是否有新版本

	}

	private void initView() {
		loginBtn = (Button) findViewById(R.id.loginBtn);
		pBar = findViewById(R.id.pb);
	}

	private void userLogin() {
		Platform qq = ShareSDK.getPlatform(QQ.NAME);
		UserBean userBean = MyData.getData().getUserBean();
		if (qq != null && qq.isAuthValid() && userBean != null) {// QQ登录过没有过去，直接登录
			getUserData(userBean.getUid());
		} else {
			loginBtn.setVisibility(View.VISIBLE);
		}
	}

	private void getUserData(long uid) {
		RequestParams params = new RequestParams();
		params.put("uid", uid + "");
		Request.getUserData(Constant.METHOD_GET_USER_DATA, params,
				new JsonHttpResponseHandler() {
					public void onFinish(String url) {

					};

					public void onFailure(Throwable error, String content) {
						MyToast.showCustomerToast("网络异常登录失败");
						loginBtn.setVisibility(View.VISIBLE);
					};

					public void onSuccess(int statusCode, JSONObject response) {
						pareUserData(response);
					};
				});
	}

	private void pareUserData(JSONObject response) {
		try {
			int code = response.getInt("code");
			if (code == Request.REQUEST_SUCCESS) {
				String userString = response.getString("data");
				UserBean userBean = JsonUtils.parse(userString, UserBean.class);
				if (userBean != null) {
					MyPreferences.saveString("user", userString);
					MyData.getData().setUserBean(userBean);
					startMainActivity();
				} else {
					MyToast.showCustomerToast("网络异常登录失败");
					loginBtn.setVisibility(View.VISIBLE);
				}
			} else {
				MyToast.showCustomerToast("网络异常登录失败");
				loginBtn.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyToast.showCustomerToast("网络异常登录失败");
			loginBtn.setVisibility(View.VISIBLE);
		}
	}

	private void setListener() {
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loginBtn.setFocusable(false);
				loginBtn.setVisibility(View.GONE);
				loginQQ();
			}
		});
	}

	private void loginQQ() {
		Platform qq = ShareSDK.getPlatform(QQ.NAME);
		if (qq == null) {
			MyToast.showCustomerToast("登录失败");
			return;
		}
		if (qq != null && !qq.isAuthValid()) {
			MyToast.showCustomerToast("正在启动qq...");
		}
		qq.SSOSetting(false);
		qq.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Message message = new Message();
				message.what = LOGIN_ERROR;
				handler.sendMessage(message);
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				Message message = new Message();
				message.what = LOGIN_SUCCESS;

				message.obj = getUserData(arg0, arg2);

				handler.sendMessage(message);

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				Message message = new Message();
				message.what = LOGIN_CANCEL;
				handler.sendMessage(message);

			}
		});
		qq.showUser(null);

	}

	private UserBean getUserData(Platform arg0, HashMap<String, Object> data) {
		UserBean userBean = new UserBean();
		PlatformDb platDB = arg0.getDb();
		userBean.setAddress(data.get("province").toString()
				+ data.get("city").toString());
		userBean.setUserId(platDB.getUserId());
		userBean.setName(platDB.getUserName());
		userBean.setTouxiang(data.get("figureurl_qq_2").toString());
		userBean.setCity(data.get("city").toString());
		String xingbie = data.get("gender").toString();
		if (Constant.MAN.equals(xingbie)) {
			userBean.setXingbie(Constant.NAN);
		} else {
			userBean.setXingbie(Constant.NV);
		}
		return userBean;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinishing()) {
				return;
			}
			loginBtn.setFocusable(true);
			if (msg == null || msg.what == LOGIN_ERROR
					|| msg.what == LOGIN_CANCEL) {
				MyToast.showCustomerToast("登录失败 您可以重新登录");
				loginBtn.setVisibility(View.VISIBLE);
			} else if (msg.what == LOGIN_SUCCESS) {
				loginBtn.setVisibility(View.GONE);
				pBar.setVisibility(View.VISIBLE);
				UserBean userBean = (UserBean) msg.obj;
				if (userBean != null) {
					addUser(userBean);
				} else {
					loginBtn.setVisibility(View.VISIBLE);
					MyToast.showCustomerToast("登录失败");
				}
			}

		};
	};

	private void addUser(UserBean userBean) {
		if (userBean == null) {
			MyToast.showCustomerToast("登录失败");
			loginBtn.setVisibility(View.VISIBLE);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("userId", userBean.getUserId());
		params.put("xingbie", userBean.getXingbie() + "");
		params.put("touxiang", userBean.getTouxiang());
		params.put("userName", userBean.getName());
		if (MyData.bdLocation != null) {
			bdLocation = MyData.bdLocation;
		}
		if (bdLocation != null) {
			params.put("jingdu", bdLocation.getLongitude() + "");
			params.put("weidu", bdLocation.getLatitude() + "");
			params.put("address", bdLocation.getAddrStr() + "");
			params.put("city", bdLocation.getCity() + "");
		} else {
			params.put("jingdu", 0 + "");
			params.put("weidu", 0 + "");
			params.put("address", userBean.getAddress());
			params.put("city", userBean.getCity());
		}
		params.put("imei", MyTools.getImei(this) + "");
		Request.addUser(Constant.METHOD_ADD_USER, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content + error);
						MyToast.showCustomerToast("网络异常登录失败");
						loginBtn.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						pareUserData(response);
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
					}
				});

	}

	// // 清楚用户登录信息
	// private void clearnUser() {
	// Platform qq = ShareSDK.getPlatform(QQ.NAME);
	// if (qq != null) {
	// qq.removeAccount();
	// }
	// }

	private void startMainActivity() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	@Override
	public void backAppInfo(BanBenBean bean) {
		pBar.setVisibility(View.GONE);
		this.banBenBean = bean;
		try {
			if (isUpdate()) {
				showUpdateD(bean);
			} else {
				userLogin();
			}
		} catch (Exception e) {
			userLogin();
		}
	}

	private boolean isUpdate() {
		if (banBenBean != null) {
			if (banBenBean.getVersion() > MyTools.getVerCode(this)) {
				return true;
			}
		}
		return false;
	}

	private void showUpdateD(final BanBenBean bean) {
		if (isFinishing()) {
			return;
		}

		CustomAlertDialog dialog = new CustomAlertDialog(this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setLeftText("退出");
		dialog.setRightText("更新");
		dialog.setTitle("检测到新版本,请升级");
		dialog.setMessage(bean.getContent());
		dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyToast.dissMissToast();
				AppManager.getAppManager().AppExit();
				finish();
				System.gc();

			}
		});
		dialog.setOnRightListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startDownApp(bean.getUrl());
			}
		});
		try {
			if (!isFinishing() && dialog != null) {
				dialog.show();
			}
		} catch (Exception e) {
		}
	}

	private void startDownApp(String downUrl) {
		UpdateManager manager = new UpdateManager(this);
		manager.downloadApk(downUrl);
	}

}
