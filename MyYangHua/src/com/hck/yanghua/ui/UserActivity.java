package com.hck.yanghua.ui;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.LogUtil;

public class UserActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initTitle();
		initTitle();

	}

	private void initTitle() {
		leftTextView.setVisibility(View.GONE);
		centerTextView.setText("用户中心");
		centerTextView.setBackgroundResource(R.color.transparent);
		rightTextView.setVisibility(View.GONE);
	}

	public void showMyFriend(View view) {
		startActivity(new Intent(this, FriendActivity.class));

	}

	private void testSendMsg() {
		// 01-21 11:21:57.129: D/hck(29767): usernames:
		// hckyanghuaa833ff80948b5a8716ce6dad3116efdd
		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation("hckyanghuab2291d06d4a3b16f52774659b1aeccef");
		// 创建一条文本消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 如果是群聊，设置chattype,默认是单聊
		// message.setChatType(ChatType.GroupChat);
		// 设置消息body
		TextMessageBody txtBody = new TextMessageBody("哈哈哈");
		message.addBody(txtBody);
		// 设置接收人
		message.setReceipt("hckyanghuaa833ff80948b5a8716ce6dad3116efdd");
		// 把消息加入到此会话对象中
		conversation.addMessage(message);
		// 发送消息
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LogUtil.D("onSuccess: ");
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				LogUtil.D("onProgress: " + arg0 + arg1);

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtil.D("onError: " + arg0 + arg1);
			}
		});

	}

	public void showMyMSG(View view) {
		testGetLiaotian();
	}

	private void testGetLiaotian() {
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation("hckyanghuaa833ff80948b5a8716ce6dad3116efdd");
		// 获取此会话的所有消息
		List<EMMessage> messages = conversation.getAllMessages();
		// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
		// 获取startMsgId之前的pagesize条消息，此方法获取的messages
		// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		// List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId,
		// pagesize);
		for (int i = 0; i < messages.size(); i++) {
			LogUtil.D("messages: " + messages.get(i).getUserName());
			LogUtil.D("messages: " + messages.get(i).getMsgTime());
			LogUtil.D("messages: " + messages.get(i).getFrom());
			LogUtil.D("messages: " + messages.get(i).getBody().toString());
		}

	}
}
