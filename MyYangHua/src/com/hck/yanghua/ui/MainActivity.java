package com.hck.yanghua.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.hck.yanghua.R;
import com.hck.yanghua.fragment.HomeFragment;
import com.hck.yanghua.fragment.MainMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private SlidingMenu mSlidingMenu;
	private Fragment mContent;
	private MainMenuFragment mMenuFrag;
	private Fragment homFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSlidingMenu(savedInstanceState);
		initFragment();
		initHome();
	}

	private void initFragment() {
		homFragment = new HomeFragment();
	}

	private void initHome() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, homFragment).commit();
		mContent=homFragment;
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
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 边线才有效果
		mSlidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	}

	@Override
	public void onClick(View v) {

	}

}
