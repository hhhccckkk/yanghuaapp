package com.hck.yanghua.ui;

import java.io.File;
import java.io.FileNotFoundException;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.fragment.MainFragment;
import com.hck.yanghua.fragment.MainMenuFragment;
import com.hck.yanghua.util.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	public static SlidingMenu mSlidingMenu;
	private Fragment mContent;
	private MainMenuFragment mMenuFrag;
	private MainFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startBaiDuPushServices();
		setContentView(R.layout.activity_main);
		initSlidingMenu(savedInstanceState);
		initFragment();
		initHome();
		// addImge();

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
		mContent = mainFragment;
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
	}

	private void addImge() {
		// 01-06 21:21:50.020: D/hck(31169): dd: /storage/emulated/0

		File sdDir = null;

		sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		LogUtil.D("dd: " + sdDir.toString());
		RequestParams params = new RequestParams();
		File file = new File(sdDir, "psb.jpg");
		File file2 = new File(sdDir, "1.jpg");
		File file3 = new File(sdDir, "2.jpg");
		File file4 = new File(sdDir, "3.jpg");
		LogUtil.D("fff: " + file.toString());
		try {
			params.put("file", file);
			params.put("file2", file2);
			params.put("file3", file3);
			params.put("file4", file4);
			new HCKHttpClient().post(Constant.MAINHOST + "addImageP", params,
					new HCKHttpResponseHandler() {
						@Override
						public void onFinish(String url) {
							// TODO Auto-generated method stub
							super.onFinish(url);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							LogUtil.D("失败: " + content + error);
						}
					});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LogUtil.D("ddd: " + e.toString());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		System.gc();
		System.exit(0);
	}

}
