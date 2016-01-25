package com.hck.yanghua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.easemob.easeui.ui.EaseConversationListFragment.EaseConversationListItemClickListener;
import com.easemob.exceptions.EaseMobException;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.fragment.HuiFuMsgFragment;
import com.hck.yanghua.fragment.TongZhiMsgFragment;
import com.hck.yanghua.liaotian.MainMsgReceiver;
import com.hck.yanghua.liaotian.MainMsgReceiver.HasNewMsgGet;

public class XiaoXiActivity extends BaseActivity implements OnClickListener,
		HasNewMsgGet {
	private static final int LIAOTIAN_MSG = 1;
	private static final int HUIFU_MSG = 2;
	private static final int XITONG_MSG = 3;
	private EaseConversationListFragment conversationListFragment;
	private HuiFuMsgFragment huiFuMsgFragment;
	private TongZhiMsgFragment xiTongMsgFragment;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoxi);
		initTitle();
		initFragment();
		setListener();
		MainMsgReceiver.getMainMsgReceiver(this).setHasNewMsgListener(this);
		changeFragment(conversationListFragment);
	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void changeFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.xiaoxi_content, fragment).commit();
	}

	private void initFragment() {
		conversationListFragment = new EaseConversationListFragment();

		huiFuMsgFragment = new HuiFuMsgFragment();
		xiTongMsgFragment = new TongZhiMsgFragment();
	}

	private void initTitle() {
		leftTextView.setText(R.string.xiaoxi_liaotian);
		centerTextView.setText(R.string.xiaoxi_huifu);
		rightTextView.setText(R.string.xiaoxi_xitong);
	}

	private void setListener() {
		leftTextView.setOnClickListener(this);
		centerTextView.setOnClickListener(this);
		rightTextView.setOnClickListener(this);
		conversationListFragment
				.setConversationListItemClickListener(new EaseConversationListItemClickListener() {

					@Override
					public void onListItemClicked(EMConversation conversation) {
						sendClearnMsgSizeBroadcast();
						startChatActivity(conversation);
					}
				});
	}

	private void startChatActivity(EMConversation conversation) {
		Intent intent = new Intent();
		UserBean userBean = new UserBean();
		UserBean my = MyData.getData().getUserBean();
		String fromUsername = null, fromUserImg = null, toUserName = null, toUserImg = null;
		EMMessage message = conversation.getLastMessage();
		try {
			fromUsername = message.getStringAttribute("fromUserName");
			fromUserImg = message.getStringAttribute("fromImg");
			toUserName = message.getStringAttribute("toUserName");
			toUserImg = message.getStringAttribute("toImg");

		} catch (EaseMobException e) {
			e.printStackTrace();
			fromUsername = "花友";
		}
		if (message.direct == Direct.SEND) {
			userBean.setName(toUserName);
			userBean.setTouxiang(toUserImg);
		} else {
			userBean.setName(fromUsername);
			userBean.setTouxiang(fromUserImg);
		}
		userBean.setUserId(message.getUserName());
		intent.putExtra("user", userBean);

		intent.setClass(XiaoXiActivity.this, ChatActivity.class);
		intent.putExtra("fromImg", my.getTouxiang());
		intent.putExtra("fromUserName", my.getName());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_title_left:
			changeBg(LIAOTIAN_MSG);
			break;
		case R.id.home_title_center:
			changeBg(HUIFU_MSG);
			break;
		case R.id.home_title_right:
			changeBg(XITONG_MSG);
			break;
		default:
			break;
		}
	}

	private void changeBg(int flag) {
		switch (flag) {
		case LIAOTIAN_MSG:
			leftTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			leftTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setTextColor(getResources().getColor(R.color.whilt));
			rightTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setBackgroundResource(R.color.transparent);
			rightTextView.setBackgroundResource(R.color.transparent);
			changeFragment(conversationListFragment);
			break;
		case HUIFU_MSG:
			leftTextView.setBackgroundResource(R.color.transparent);
			leftTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			rightTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			rightTextView.setBackgroundResource(R.color.transparent);
			changeFragment(huiFuMsgFragment);
			break;
		case XITONG_MSG:
			leftTextView.setBackgroundResource(R.color.transparent);
			leftTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setTextColor(getResources().getColor(R.color.whilt));
			rightTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setBackgroundResource(R.color.transparent);
			rightTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			changeFragment(xiTongMsgFragment);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& !MainActivity.mSlidingMenu.isMenuShowing()) {
			alertExitD();
			return false;
		}

		return true;
	}
    
	@Override
	public void haseNewMsg() {
		if (conversationListFragment != null) {
			conversationListFragment.refresh();
		}
	}
	
	private void sendClearnMsgSizeBroadcast(){
		Intent intent =new Intent();
		intent.setAction(Constant.CLEARN_NEW_MSG_SIZE);
		sendBroadcast(intent);
	}

}
