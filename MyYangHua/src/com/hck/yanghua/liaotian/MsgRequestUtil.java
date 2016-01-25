package com.hck.yanghua.liaotian;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.FriendData;
import com.hck.yanghua.db.DBUtil;
import com.hck.yanghua.db.MsgInviteBean;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

public class MsgRequestUtil {
	private static RequestParams params;
	public static final boolean isGetFriendsOk = false;

	public static void getFriends3() {
		RequestParams params = new RequestParams();
		Request.getFriends(params, new JsonHttpResponseHandler()

		{
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure:" + error + content);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess:" + response.toString());
				try {
					int code = response.getInt("code");
					if (code == Request.REQUEST_SUCCESS) {
						FriendData data = JsonUtils.parse(response.toString(),
								FriendData.class);
						if (data != null && data.getFriendBeans() != null) {
							MyPreferences.saveString("friend",
									response.toString());
						} else {
							MyToast.showCustomerToast("您还没有好友");
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				Pdialog.hiddenDialog();

			}
		});
	}

	// 通过用户账号，从服务器获取用户信息
	public static void getUserInfo(String userStringId, final String content,
			final Context context) {
		RequestParams params = new RequestParams();
		params.put("uid", userStringId);
		Request.getUserInfoByStId(Constant.METHOD_GET_USER_BY_STRING_ID,
				params, new JsonHttpResponseHandler() {
					@Override
					public void onFinish(String url) {
						super.onFinish(url);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response.toString());
						try {
							UserBean userBean = JsonUtils.parse(
									response.getString("data"), UserBean.class);
							LogUtil.D("userName: " + userBean.getName());
							saveToDB(userBean, content, context);
						} catch (Exception e) {
							LogUtil.D("eee: " + e.toString());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content + error);
					}
				});
	}

	private static void saveToDB(UserBean userBean, String content,
			Context context) {
		MsgInviteBean msgInviteBean2 = new DBUtil(context)
				.getOneMsgInfoByUid(userBean.getUserId());
		if (msgInviteBean2 == null) {
			MsgInviteBean msgInviteBean = new MsgInviteBean();
			msgInviteBean.setContent(content);
			msgInviteBean.setState(0);
			msgInviteBean.setTouxiang(userBean.getTouxiang());
			msgInviteBean.setUserMsgId(userBean.getUserId());
			msgInviteBean.setUserName(userBean.getName());
			msgInviteBean.setUserId(userBean.getUid());
			new DBUtil(context).add(msgInviteBean);
			int allSize = MyPreferences.getInt(
					Constant.PREFERENCES_KEY_MSG_ALL_SIZE, 0);
			MyPreferences.saveInt(Constant.PREFERENCES_KEY_MSG_ALL_SIZE,
					allSize + 1);

			sendNotionMsg(context);
		}

	}

	private static void sendNotionMsg(Context context) {
		Intent intent = new Intent();
		intent.setAction(Constant.MSG_INVENT);
		intent.putExtra("type", 1);
		context.sendBroadcast(intent);

	}

	public static void addFriend(final String userMsgId) {
		params = new RequestParams();
		params.put("userMsgId", userMsgId);
		Request.addFriend(Constant.METHOD_ADDFRIEND, params,
				new JsonHttpResponseHandler() {
					public void onFinish(String url) {
					};

					public void onSuccess(int statusCode, JSONObject response) {
						LogUtil.D("onSuccess: " + response.toString());
					};

					public void onFailure(Throwable error, String content) {
						LogUtil.D("onFailure: " + error + content);
						MsgUtil.deleteFriend(userMsgId);
					};
				});
	}
}
