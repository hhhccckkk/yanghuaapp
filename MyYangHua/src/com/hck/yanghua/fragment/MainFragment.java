package com.hck.yanghua.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.hck.yanghua.R;
import com.hck.yanghua.R.color;
import com.hck.yanghua.ui.BaoDianActivity;
import com.hck.yanghua.ui.FaTieActivity;
import com.hck.yanghua.ui.HomeActivity;
import com.hck.yanghua.ui.UserActivity;
import com.hck.yanghua.ui.XiaoXiActivity;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.view.BadgeView;
import com.hck.yanghua.view.PopupWindowView;
import com.hck.yanghua.view.PopupWindowView.PopCallBack;

public class MainFragment extends BaseFragment implements
		OnCheckedChangeListener, PopCallBack {

	private static final String HOME = "home";
	private static final String BAODIAN = "baodian";
	private static final String FATIE = "fatie";
	private static final String XIAOXI = "xiaoxi";
	private static final String USER = "user";
	private TabHost tabHost; // tabhost对象
	private TabSpec tabSpec1, tabSpec2, tabSpec3, tabSpec4, tabSpec5; // 现象卡对象
	public RadioButton homeButton, baodianButton, fatieButton, newButton,
			userButton; // 设置背景
	private RadioGroup radioGroup;
	private Activity activity;
	private LocalActivityManager localActivityManager;
	private BadgeView badgeView;
	private int oldCheckId;
	private View remindView;
	private PopupWindowView pWindowView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_main, null);
			initView(mRootView);
			setListener();
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	private void initView(View view) {
		tabHost = (TabHost) view.findViewById(R.id.tabHost);
		tabHost.setup(localActivityManager);
		radioGroup = (RadioGroup) view.findViewById(R.id.RadioG);
		homeButton = (RadioButton) view.findViewById(R.id.home_id);
		baodianButton = (RadioButton) view.findViewById(R.id.baodian_id);
		fatieButton = (RadioButton) view.findViewById(R.id.fatie_id);
		newButton = (RadioButton) view.findViewById(R.id.xiaoxi_id);
		userButton = (RadioButton) view.findViewById(R.id.user_id);
		remindView = view.findViewById(R.id.view_remind);
		addSpec();
		badgeView = new BadgeView(this.getActivity(), remindView);
		remind(badgeView, "12");

	}

	private void remind(BadgeView remindView, String size) {
		if (remindView == null) {
			return;
		}
		remindView.setText(size); // 需要显示的提醒类容
		remindView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
		remindView.setTextColor(Color.WHITE); // 文本颜色
		remindView.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
		remindView.setTextSize(8); // 文本大小
		remindView.setBadgeMargin(8); // 各边间隔
		remindView.show();// 只有显示

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localActivityManager = new LocalActivityManager(getActivity(), true);
		localActivityManager.dispatchCreate(savedInstanceState);

	}

	private void setListener() { // 设置点击监听事件
		radioGroup.setOnCheckedChangeListener(this);
	}

	private void addSpec() {
		tabSpec1 = tabHost.newTabSpec(HOME).setIndicator(HOME)
				.setContent(new Intent(this.getActivity(), HomeActivity.class)); // 点击第一个button，跳转到哪个activity（点击跳到MainLeft界面）
		tabHost.addTab(tabSpec1);
		tabSpec2 = tabHost
				.newTabSpec(BAODIAN)
				.setIndicator(BAODIAN)
				.setContent(
						new Intent(this.getActivity(), BaoDianActivity.class));// 点击第2个button，跳转到哪个activity
		tabHost.addTab(tabSpec2);

		tabSpec3 = tabHost
				.newTabSpec(FATIE)
				.setIndicator(FATIE)
				.setContent(
						new Intent(this.getActivity(), BaoDianActivity.class));// 点击第3个button，跳转到哪个activity
		tabHost.addTab(tabSpec3);

		tabSpec4 = tabHost
				.newTabSpec(XIAOXI)
				.setIndicator(XIAOXI)
				.setContent(
						new Intent(this.getActivity(), XiaoXiActivity.class));// 点击第3个button，跳转到哪个activity
		tabHost.addTab(tabSpec4);

		tabSpec5 = tabHost.newTabSpec(USER).setIndicator(USER)
				.setContent(new Intent(this.getActivity(), UserActivity.class));// 点击第3个button，跳转到哪个activity
		tabHost.addTab(tabSpec5);
		oldCheckId = R.id.home_id;

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) { // 点击按钮事件
		switch (checkedId) {
		case R.id.home_id: // 点击第一个按钮
			tabHost.setCurrentTab(0); // 显示第一个选项卡，即跳到MainLeft
			oldCheckId = R.id.home_id;
			break;
		case R.id.baodian_id:
			tabHost.setCurrentTab(1);
			oldCheckId = R.id.baodian_id;
			break;
		case R.id.fatie_id:
			fatieButton.setChecked(false);
			onFaTieBtn();
			showPopWindown();
			startAnimation();
			break;
		case R.id.xiaoxi_id:
			tabHost.setCurrentTab(3);
			oldCheckId = R.id.xiaoxi_id;
			break;
		case R.id.user_id:
			tabHost.setCurrentTab(4);
			oldCheckId = R.id.user_id;
			break;
		}

	}

	private void showPopWindown() {
		if (pWindowView != null) {
			hidenPop();
		} else {
			pWindowView = new PopupWindowView();
			pWindowView.showFaTieView(fatieButton, this.getActivity(),this);
		}

	}

	private void hidenPop() {
		if (pWindowView != null && pWindowView.popupWindow != null) {
			pWindowView.popupWindow.dismiss();
			pWindowView.popupWindow = null;
		}
		pWindowView = null;

	}

	private void startAnimation() {
		if (pWindowView == null) {
			startLeftRoat();
		} else {
			startRightRoat();
		}

	}

	private void startRightRoat() {
		fatieButton.clearAnimation();
		Animation operatingAnim = AnimationUtils.loadAnimation(
				this.getActivity(), R.anim.btn_right_rotate);
		fatieButton.startAnimation(operatingAnim);

	}

	private void startLeftRoat() {
		fatieButton.clearAnimation();
		Animation operatingAnim = AnimationUtils.loadAnimation(
				this.getActivity(), R.anim.btn_left_rotate);
		fatieButton.startAnimation(operatingAnim);
	}

	// 回复刚选中的按钮状态
	private void onFaTieBtn() {
		switch (oldCheckId) {
		case R.id.home_id:
			homeButton.setChecked(true);
			break;
		case R.id.baodian_id:
			baodianButton.setChecked(true);
			break;
		case R.id.xiaoxi_id:
			newButton.setChecked(true);
			break;
		case R.id.user_id:
			userButton.setChecked(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (localActivityManager != null) {
			localActivityManager.dispatchResume();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		if (localActivityManager != null) {
			localActivityManager.dispatchPause(getActivity().isFinishing());
		}

	}

	@Override
	public void fatie() {
		Intent intent=new Intent();
		intent.putExtra("type", 1); //1一般分享帖子,2出售帖子,3视频
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	@Override
	public void fatieSale() {
		Intent intent=new Intent();
		intent.putExtra("type", 1); //1一般分享帖子,2出售帖子
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	@Override
	public void fatieVideo() {
		Intent intent=new Intent();
		intent.putExtra("type", 1); //1一般分享帖子,3视频
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	private void startFaTieActivity(Intent intent) {
		hidenPop();
		startActivity(intent);
	}

}
