package com.hck.yanghua.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.BanBenBean;
import com.hck.yanghua.downapp.UpdateManager;
import com.hck.yanghua.downapp.UpdateUtil;
import com.hck.yanghua.downapp.UpdateUtil.UpdateAppCallBack;
import com.hck.yanghua.util.AppManager;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.view.CustomAlertDialog;

public class SplashActivity extends Activity implements UpdateAppCallBack {
	private BanBenBean banBenBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		startMainActivity();
		// new UpdateUtil().isUpdate(this);
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

			}
		} catch (Exception e) {
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
