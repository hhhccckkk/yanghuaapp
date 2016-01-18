package com.hck.yanghua.data;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.MyPreferences;

public class MyData {
	public static MyData myData;
	private UserBean userBean;
    public static BDLocation bdLocation;
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
