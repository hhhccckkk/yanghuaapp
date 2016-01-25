package com.hck.yanghua.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.liaotian.MainMsgReceiver;
import com.hck.yanghua.ui.BaoDianActivity;
import com.hck.yanghua.ui.FaTieActivity;
import com.hck.yanghua.ui.HomeActivity;
import com.hck.yanghua.ui.UserActivity;
import com.hck.yanghua.ui.XiaoXiActivity;
import com.hck.yanghua.util.LogUtil;
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
	private int oldCheckId;
	private PopupWindowView pWindowView;
	private  TextView remindTextView;
    private ClearnMsgReceiver clearnMsgReceiver =new ClearnMsgReceiver();
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
    private void registerBroadcastReceiver(){
    	IntentFilter intentFilter =new IntentFilter();
    	intentFilter.addAction(Constant.CLEARN_NEW_MSG_SIZE);
    	getActivity().registerReceiver(clearnMsgReceiver, intentFilter);
    }
    class ClearnMsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
         remind(0);			
		}
    	
    }
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		registerBroadcastReceiver();
		
	}

	private void initView(View view) {
		LogUtil.D("initViewinitViewinitView");
		tabHost = (TabHost) view.findViewById(R.id.tabHost);
		tabHost.setup(localActivityManager);
		radioGroup = (RadioGroup) view.findViewById(R.id.RadioG);
		homeButton = (RadioButton) view.findViewById(R.id.home_id);
		baodianButton = (RadioButton) view.findViewById(R.id.baodian_id);
		fatieButton = (RadioButton) view.findViewById(R.id.fatie_id);
		newButton = (RadioButton) view.findViewById(R.id.xiaoxi_id);
		userButton = (RadioButton) view.findViewById(R.id.user_id);
		remindTextView = (TextView) mRootView.findViewById(R.id.view_remind);
		updateMsgSize();
		addSpec();
	}

	public void updateMsgSize() {
		int size = MainMsgReceiver.getMainMsgReceiver(this.getActivity())
				.getMsgSize();
		remind(size);
	}

	private void remind(int size) {
		if (size > 0) {
			remindTextView.setText(size+"");
			remindTextView.setVisibility(View.VISIBLE);
			
		} else {
			remindTextView.setVisibility(View.INVISIBLE);
		}

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
            remindTextView.setVisibility(View.GONE);
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
			pWindowView.showFaTieView(fatieButton, this.getActivity(), this);
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
		Intent intent = new Intent();
		intent.putExtra("type", 1); // 1一般分享帖子,2出售帖子,3视频
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	@Override
	public void fatieSale() {
		Intent intent = new Intent();
		intent.putExtra("type", 2); // 1一般分享帖子,2出售帖子
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	@Override
	public void fatieVideo() {
		Intent intent = new Intent();
		intent.putExtra("type", 3); // 1一般分享帖子,3视频
		intent.setClass(this.getActivity(), FaTieActivity.class);
		startFaTieActivity(intent);
	}

	private void startFaTieActivity(Intent intent) {
		hidenPop();
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        getActivity().unregisterReceiver(clearnMsgReceiver);
	}



}
