package com.hck.yanghua.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.NearUserAdapter;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.data.UserData;
import com.hck.yanghua.location.MyLocation;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

public class ShowNearUserActivity extends BaseTitleActivity {
	private ListView listView;
	private UserData userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_near_user);
		initTitleView("附近的人");
		initView();
		getUserLocation();
		setListener();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.showUserList);
	}

	private void getUserLocation() {
		Pdialog.showDialog(this, "获取数据中..", true);
		MyLocation.startLocation(this, new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				getNearUser(arg0);
				MyLocation.stopLocation();
			}
		});
	}

	private void getNearUser(BDLocation location) {
		if (location == null) {
			location = MyData.bdLocation;
		}
		if (location == null) {
			MyToast.showCustomerToast("无法获取您的位置");
			Pdialog.hiddenDialog();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("jindu", location.getLatitude() + "");
		params.put("weidu", location.getLongitude() + "");
		Request.getNearUser(params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + content + error);
				MyToast.showCustomerToast("网络异常获取数据失败");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					userData = JsonUtils.parse(response.toString(),
							UserData.class);
					LogUtil.D("eeee222: " + userData.getUserBeans().size());
					updateView();
				} catch (Exception e) {
					LogUtil.D("eeee: " + e.toString());

				}
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				Pdialog.hiddenDialog();
			}
		});
	}

	private void updateView() {
		if (userData != null && userData.getUserBeans() != null) {
			listView.setAdapter(new NearUserAdapter(userData.getUserBeans(),
					this));
		}
	}

	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					Intent intent = new Intent();
					intent.setClass(ShowNearUserActivity.this,
							ShowOneUserActivity.class);
					intent.putExtra("uid", userData.getUserBeans().get(position)
							.getUserId());
					startActivity(intent);
				} catch (Exception e) {
				}
				
			}
		});
	}

}
