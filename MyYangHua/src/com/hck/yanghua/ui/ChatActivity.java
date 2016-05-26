package com.hck.yanghua.ui;

import android.content.Intent;
import android.os.Bundle;

import com.easemob.easeui.ui.EaseBaseActivity;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.ui.EaseChatFragment.InitViewOk;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.util.LogUtil;

public class ChatActivity extends EaseBaseActivity implements InitViewOk {
	public static ChatActivity activityInstance;
	private EaseChatFragment chatFragment;
	private String toChatUsername;
	private UserBean userBean;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_chat);
		activityInstance = this;
		chatFragment = new EaseChatFragment();
		chatFragment.setInitViewOkListener(this);
		// 传入参数
		userBean = (UserBean) getIntent().getSerializableExtra("user");
		String fromImg = getIntent().getStringExtra("fromImg");
		String fromUserName=getIntent().getStringExtra("fromUserName");
		Bundle bundle = new Bundle();
		
		bundle.putString("toUserName", userBean.getName());
		bundle.putString("msgid", userBean.getUserId());  
		bundle.putString("fromImg", fromImg);
		bundle.putString("toImg", userBean.getTouxiang());
		bundle.putString("fromUserName", fromUserName);
		chatFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, chatFragment).commit();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		chatFragment.onBackPressed();
	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	@Override
	public void initTitleOk() {
//		try {
//			chatFragment.getTitleBar().setBackgroundColor(
//					getResources().getColor(R.color.red_anniu_bt_color));
//			chatFragment.getTitleBar().setLeftImageResource(R.drawable.back_img);
//		} catch (Exception e) {
//		}

	}

	




}
