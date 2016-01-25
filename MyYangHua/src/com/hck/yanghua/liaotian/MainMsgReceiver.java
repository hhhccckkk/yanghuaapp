package com.hck.yanghua.liaotian;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;

import com.easemob.EMConnectionListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.hck.yanghua.util.LogUtil;

public class MainMsgReceiver implements EMEventListener {
	private static MainMsgReceiver mainMsgReceiver;
	private static Context context;

	public static MainMsgReceiver getMainMsgReceiver(Context context) {
		MainMsgReceiver.context = context;
		if (mainMsgReceiver == null) {
			mainMsgReceiver = new MainMsgReceiver();
		}
		return mainMsgReceiver;
	}

	public void initReceiver() {
		EMChatManager.getInstance().getChatOptions().setUseRoster(true);
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		options.setAcceptInvitationAlways(false);
		EMChatManager.getInstance().setChatOptions(options);
		registerackMessageReceiver();
		registerJieShouMsg();
		registConnectionListener();
		registerdeliveryAckMessageReceiver();
		EMChat.getInstance().setAppInited();

	}

	private void registConnectionListener() {
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());

	}

	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {

		}

		@Override
		public void onDisconnected(final int error) {

		}
	}

	public void initContactReceiver() {
		EMContactManager.getInstance().setContactListener(
				new EMContactListener() {

					@Override
					public void onContactRefused(String arg0) {
						LogUtil.D("onContactRefused: " + arg0);
					}

					@Override
					public void onContactInvited(String arg0, String arg1) {
						LogUtil.D("onContactInvited: " + arg0 + arg1);
						MsgRequestUtil.getUserInfo(arg0, arg1, context);

					}

					@Override
					public void onContactDeleted(List<String> arg0) {
						LogUtil.D("onContactDeleted: " + arg0);
					}

					@Override
					public void onContactAgreed(String arg0) {
						LogUtil.D("onContactAgreed: " + arg0);
						MsgRequestUtil.addFriend(arg0);
					}

					@Override
					public void onContactAdded(List<String> arg0) {
						LogUtil.D("onContactAdded: " + arg0.toString());
						if (arg0 != null && !arg0.isEmpty()) {
							MsgRequestUtil.addFriend(arg0.get(0));
						}
					}
				});
		EMChat.getInstance().setAppInited();
	}

	private void registerdeliveryAckMessageReceiver() {
		EMChatManager.getInstance().getChatOptions()
				.setRequireDeliveryAck(true);
		// 如果用到已发送的回执需要把这个flag设置成true

		IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
				EMChatManager.getInstance()
						.getDeliveryAckMessageBroadcastAction());
		deliveryAckMessageIntentFilter.setPriority(5);
		context.registerReceiver(deliveryAckMessageReceiver,
				deliveryAckMessageIntentFilter);

	}

	/**
	 * 消息送达BroadcastReceiver
	 */
	private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				LogUtil.D("deliveryAckMessageReceiver" + msg.toString());
				if (msg != null) {
					msg.isDelivered = true;
				}
			}
		}
	};

	private void registerackMessageReceiver() {
		EMChatManager.getInstance().getChatOptions().setRequireAck(true);
		// 如果用到已读的回执需要把这个flag设置成true

		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		context.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

	}

	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				LogUtil.D("ackMessageReceiver: " + msg.toString());
				if (msg != null) {
					msg.isAcked = true;
				}
			}

		}
	};

	private void registerJieShouMsg() {
		// NewMessageBroadcastReceiver msgReceiver = new
		// NewMessageBroadcastReceiver();
		// IntentFilter intentFilter = new IntentFilter(EMChatManager
		// .getInstance().getNewMessageBroadcastAction());
		// intentFilter.setPriority(3);
		// context.registerReceiver(msgReceiver, intentFilter);
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
	}

	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 注销广播
			// abortBroadcast();
			// if (hasNewMsgGet != null) {
			// hasNewMsgGet.haseNewMsg();
			// }
			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
			String msgId = intent.getStringExtra("msgid");
			// 发送方
			String username = intent.getStringExtra("from");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			LogUtil.D("dddd: NewMessageBroadcastReceiver" + message.toString());
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(username);
			// 如果是群聊消息，获取到group id
			if (message.getChatType() == ChatType.GroupChat) {
				username = message.getTo();
				LogUtil.D("username" + username);
			}
			if (!username.equals(username)) {
				// 消息不是发给当前会话，return
				return;
			}
		}
	}

	private List<HasNewMsgGet>  hasNewMsgGets =new ArrayList<>();

	public interface HasNewMsgGet {
		void haseNewMsg();
	}

	public void setHasNewMsgListener(HasNewMsgGet hasNewMsgGet) {

		this.hasNewMsgGets.add(hasNewMsgGet);
	}
	
	
	
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage:
			for (int i = 0; i < hasNewMsgGets.size(); i++) {
				hasNewMsgGets.get(i).haseNewMsg();
			}
			break;
		default:
		}

	}

	public int getMsgSize() {
		int msgSize = 0;
		try {
			Hashtable<String, EMConversation> conversations = EMChatManager
					.getInstance().getAllConversations();

			for (EMConversation conversation : conversations.values()) {
				int size = conversation.getUnreadMsgCount();
				msgSize = msgSize + size;
			}
		} catch (Exception e) {
		}
		return msgSize;
	}
}
