package com.hck.yanghua.ui;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.fragment.MainFragment;
import com.hck.yanghua.fragment.MainMenuFragment;
import com.hck.yanghua.jiqiren.JiqirenMainActivity;
import com.hck.yanghua.liaotian.MainMsgReceiver;
import com.hck.yanghua.liaotian.MainMsgReceiver.HasNewMsgGet;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.AppManager;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.CustomAlertDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener, HasNewMsgGet {
	public static SlidingMenu mSlidingMenu;

	private MainMenuFragment mMenuFrag;
	private MainFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainMsgReceiver.getMainMsgReceiver(this).initReceiver();
		
		startBaiDuPushServices();
		setContentView(R.layout.activity_main);
		initSlidingMenu(savedInstanceState);
		initFragment();
		initHome();
		MainMsgReceiver.getMainMsgReceiver(this).setHasNewMsgListener(this);

	}

	private void startBaiDuPushServices() {
		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,
				Constant.BAIDU_PUSH_KEY);
	}

	private void initFragment() {
		mainFragment = new MainFragment();
	}

	private void initHome() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, mainFragment).commit();

	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mMenuFrag = MainMenuFragment.newInstance(this);
			t.replace(R.id.menu_frame, mMenuFrag);
			t.commit();
		} else {
			mMenuFrag = (MainMenuFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
			mMenuFrag.setMainAct(this);
		}

		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT); // 左边滑动
		mSlidingMenu.setShadowDrawable(R.drawable.shadow); // 阴影效果
		mSlidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setFadeEnabled(true);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			mSlidingMenu.toggle();
			break;
		case R.id.exit:
			alertExitD();
			break;
		case R.id.near_user:
			startShowNearUserActivity();
			break;
		case R.id.jiqiren:
			startJiQiRenActivity();
			break;
		case R.id.yule:
			getDuiHuanUrl();
			break;
		case R.id.dapeng:
			MyToast.showCustomerToast("暂未开放");
		default:
			break;
		}
	}

	private void getDuiHuanUrl() {
		RequestParams params = new RequestParams();
		UserBean userBean = MyData.getData().getUserBean();
		params.put("uid", userBean.getUid() + "");
		params.put("point", userBean.getJinbi() + "");
		Request.getDuiHuanUrl(params, new JsonHttpResponseHandler() {
			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MyToast.showCustomerToast("网络异常");
				LogUtil.D("onFailure: " + content + ": " + error);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					String url = response.getString("url");
					startDuiHuanActivity(url);
				} catch (Exception e) {
				}
			}
		});
	}

	private void startDuiHuanActivity(String url) {
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.setClass(this, CreditActivity.class);
		startActivity(intent);
	}

	private void startJiQiRenActivity() {
		Intent intent = new Intent();
		intent.setClass(this, JiqirenMainActivity.class);
		startActivity(intent);
	}

	private void startShowNearUserActivity() {
		Intent intent = new Intent();
		intent.setClass(this, ShowNearUserActivity.class);
		startActivity(intent);
	}

	private void alertExitD() {
		CustomAlertDialog alertDialog = new CustomAlertDialog(this);
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setLeftText("退出");
		alertDialog.setRightText("好评");
		alertDialog.setTitle("提示");
		alertDialog.setMessage("确定退出当前账号?");
		alertDialog.setOnLeftListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				exit();
			}
		});

		alertDialog.setOnRightListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				haoPing();
			}
		});
		alertDialog.show();

	}

	private void haoPing() {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	private void exit() {
		MyPreferences.saveString("user", null);
		clearnUser();
		AppManager.getAppManager().AppExit();
		finish();
	}

	private void clearnUser() {
		Platform qq = ShareSDK.getPlatform(QQ.NAME);
		if (qq != null) {
			qq.removeAccount();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyPreferences.saveInt(Constant.PREFERENCES_KEY_MSG_ALL_SIZE, 0);
		finish();
		System.gc();
		System.exit(0);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mainFragment.updateMsgSize();
		};
	};

	@Override
	public void haseNewMsg() {
		handler.sendEmptyMessage(0);

	}

	

}
