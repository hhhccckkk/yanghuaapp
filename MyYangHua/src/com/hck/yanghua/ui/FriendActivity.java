package com.hck.yanghua.ui;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.FriendAdapter;
import com.hck.yanghua.bean.FriendBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.FriendData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

public class FriendActivity extends BaseTitleActivity {
	private PullToRefreshListView pullToRefreshListView;
	private FriendAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		initTitleView("我的好友");
		initView();
		setEndLabel();
		getData();
	}

	private void initView() {
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.friend_list);
		pullToRefreshListView.setMode(Mode.PULL_FROM_START);
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				FriendBean friendBean = (FriendBean) adapter
						.getItem(position - 1);
				intent.putExtra("uid", friendBean.getUserMsgId());
				intent.setClass(FriendActivity.this, ShowOneUserActivity.class);
				startActivity(intent);
			}
		});
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
                         getData();						
					}
				});
	}

	private void setEndLabel() {
		ILoadingLayout endLabel = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLabel.setPullLabel(getString(R.string.load_more));
		endLabel.setReleaseLabel(getString(R.string.load_more));
		endLabel.setRefreshingLabel(getString(R.string.is_loading));
	}

	private void getData() {
		Pdialog.showDialog(this, "加载数据中..", true);
		RequestParams params = new RequestParams();
		Request.getFriends(params,
				new JsonHttpResponseHandler()

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
							FriendData data = JsonUtils.parse(
									response.toString(), FriendData.class);
							if (data != null && data.getFriendBeans() != null) {
								updateView(data.getFriendBeans());
							} else {
								MyToast.showCustomerToast("您还没有好友",FriendActivity.this);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						Pdialog.hiddenDialog();
						pullToRefreshListView.onRefreshComplete();
					}
				});
	}

	private void updateView(List<FriendBean> friendDatas) {
		adapter = new FriendAdapter(friendDatas, this);
		pullToRefreshListView.setAdapter(adapter);
	}
}
