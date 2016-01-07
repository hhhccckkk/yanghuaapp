package com.hck.yanghua.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.BanBenBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.downapp.UpdateManager;
import com.hck.yanghua.downapp.UpdateUtil;
import com.hck.yanghua.downapp.UpdateUtil.UpdateAppCallBack;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.AppManager;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		if (qq != null && qq.isAuthValid()) {// QQ登录过没有过去，直接登录
			loginQQ();
		} else {
			loginBtn.setVisibility(View.VISIBLE);
		}
	}

	private void setListener() {
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loginBtn.setFocusable(false);
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
				message.obj = arg0;
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

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinishing()) {
				return;
			}
			loginBtn.setFocusable(true);
			if (msg == null || msg.what == LOGIN_ERROR
					|| msg.what == LOGIN_CANCEL) {
				MyToast.showCustomerToast("登录失败 您可以重新登录");
				clearnUser();
			} else if (msg.what == LOGIN_SUCCESS) {
				loginBtn.setVisibility(View.GONE);
				pBar.setVisibility(View.VISIBLE);
				Platform platform = (Platform) msg.obj;
				PlatformDb platDB = platform.getDb();
				if (platDB != null) {
					addUser(platDB);
				} else {
					MyToast.showCustomerToast("登录失败");
					clearnUser();
				}
			}

		};
	};

	/**
	 * userId:用户id 必需 String xingbie:用户性别 必需 touxiang:用户头像 必需 jingdu:定位经度
	 * weidu:纬度 address:地址
	 * 
	 * @param platDB
	 */
	private void addUser(PlatformDb platDB) {
		if (platDB == null) {
			MyToast.showCustomerToast("登录失败");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("userId", platDB.getUserId());
		params.put("xingbie", platDB.getUserGender());
		params.put("touxiang", platDB.getUserIcon());
		params.put("userName", platDB.getUserName());
		params.put("jingdu", 38.12433+"");
		params.put("weidu", 49.76455+"");
		Request.addUser(Constant.METHOD_ADD_USER, params,
				new HCKHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: "+content);
					}

					@Override
					public void onSuccess(String content, String requestUrl) {
						super.onSuccess(content, requestUrl);
						LogUtil.D("onSuccess: "+content);
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
					}
				});

	}

	// 清楚用户登录信息
	private void clearnUser() {
		Platform qq = ShareSDK.getPlatform(QQ.NAME);
		if (qq != null) {
			qq.removeAccount();
		}
	}

	private void startMainActivity() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	@Override
	public void backAppInfo(BanBenBean bean) {
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
