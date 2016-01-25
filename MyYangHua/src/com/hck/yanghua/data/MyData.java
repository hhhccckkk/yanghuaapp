package com.hck.yanghua.data;

import java.util.List;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.hck.yanghua.bean.FriendBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.MyPreferences;

public class MyData {
	public static MyData myData;
	private UserBean userBean;
	public static BDLocation bdLocation;
	private List<FriendBean> friendBeans;

	public List<FriendBean> getFriendBeans() {
		if (friendBeans == null) {
			String friendString = MyPreferences.getString("friend", null);
			try {
				if (!TextUtils.isEmpty(friendString)) {
					FriendData friendData = JsonUtils.parse(friendString,
							FriendData.class);
					friendBeans = friendData.getFriendBeans();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return friendBeans;
	}

	public void setFriendBeans(List<FriendBean> friendBeans) {
		this.friendBeans = friendBeans;
	}

	public long getUserId() {
		userBean = getUserBean();
		return userBean.getUid();
	}

	public static void setMyData(MyData myData) {
		MyData.myData = myData;
	}

	public UserBean getUserBean() {
		if (userBean == null) {
			String userString = MyPreferences.getString("user", null);
			try {
				if (!TextUtils.isEmpty(userString)) {
					userBean = JsonUtils.parse(userString, UserBean.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public static MyData getData() {
		if (myData == null) {
			myData = new MyData();
		}
		return myData;
	}

}
