package com.hck.yanghua.liaotian;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;

public class MsgUtil {
	public static void deleteFriend(final String userMsgId) {
		new Thread(){
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(userMsgId);
				} catch (EaseMobException e) {
					e.printStackTrace();
				}// 需异步处理
			};
		}.start();
		
	}
}
