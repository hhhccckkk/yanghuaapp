package com.hck.yanghua.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.HuoDongAdapter;
import com.hck.yanghua.data.HuoDongdata;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

public class YuLeActivity extends BaseTitleActivity {
	private ListView huoDongListView;
	private HuoDongdata huoDongdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yule);
		initTitleView("娱乐抽奖");
		setTitleSize(18);
		huoDongListView = (ListView) findViewById(R.id.huodong_list);
		getData();
		setListener();
	}

	private void getData() {
		Pdialog.showLoading(this, true);
		Request.getHuoDongs(params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailureonFailure: " + content + error);
				MyToast.showCustomerToast("网络异常获取数据失败");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccessonSuccess: " + response.toString());
				try {
					huoDongdata = JsonUtils.parse(response.toString(),
							HuoDongdata.class);
					updateView();
				} catch (Exception e) {
					MyToast.showCustomerToast("网络异常获取数据失败");
				}
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				LogUtil.D("onFinish: " + url);
				Pdialog.hiddenDialog();
			}
		});
	}

	private void updateView() {
		if (huoDongdata != null && huoDongdata.getHuoDongBeans() != null) {
			huoDongListView.setAdapter(new HuoDongAdapter(huoDongdata
					.getHuoDongBeans(), this));
		}
	}

	private void setListener() {
		huoDongListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getHuoDongUrl(huoDongdata.getHuoDongBeans().get(position)
						.getUrl());
			}
		});
	}

	private void getHuoDongUrl(String url) {
		Pdialog.showDialog(this, "请稍等...", true);
		params = new RequestParams();
		params.put("url", url);
		Request.getHuoDongUrl(params, new JsonHttpResponseHandler() {
			public void onFinish(String url) {
				Pdialog.hiddenDialog();
			};

			public void onSuccess(int statusCode, JSONObject response) {
				LogUtil.D("onSuccess: " + response.toString());
				try {
					String url = response.getString("url");
					startYuLeActivity(url);
				} catch (Exception e) {
					MyToast.showCustomerToast("网络异常");
					LogUtil.D("ddd: " + e.toString());
				}
			};

			public void onFailure(Throwable error, String content) {
				LogUtil.D("onSuccess: " + error + content);
				MyToast.showCustomerToast("网络异常");
			};
		});
	}

	private void startYuLeActivity(String url) {
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.setClass(this, CreditActivity.class);
		startActivity(intent);
	}
}
