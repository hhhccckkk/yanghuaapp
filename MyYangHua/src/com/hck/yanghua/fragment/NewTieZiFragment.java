package com.hck.yanghua.fragment;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.TieZiAdapter;
import com.hck.yanghua.adapter.TieZiAdapter.OnTouXiangCliceListener;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.TieZiData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.ui.ShowOneUserActivity;
import com.hck.yanghua.ui.TieZiXiangXiActivity;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

public class NewTieZiFragment extends BaseFragment implements
		OnTouXiangCliceListener {
	private static final int MAX_SIZE = 10;
	public static final int ZAN = 1;
	public static final int PING_LUN = 2;
	private PullToRefreshListView pullToRefreshListView;
	private int page = 1;
	private boolean isUpdate;
	private TieZiData tieZiBeans;
	private TieZiAdapter adapter;
	private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
	private long uid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		regestBrodCast();
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_tiezi, null);
			if (getArguments() != null) {
				uid = getArguments().getLong("uid");
			}
			initView(mRootView);
			setListener();
			setEndLabel();
			getData();
			Pdialog.showLoading(getActivity(), true);
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
	}

	private void regestBrodCast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.NEW_ADD_PL);
		filter.addAction(Constant.NEW_ADD_ZAN);
		filter.addAction(Constant.UPDATE_HOME_TIEZI_DATA);
		this.getActivity().registerReceiver(myBroadcastReceiver, filter);
	}

	class MyBroadcastReceiver extends BroadcastReceiver {
		// 1,增加赞，2增加评论数,3刷新数据

		@Override
		public void onReceive(Context context, Intent intent) {

			int tag = intent.getIntExtra("tag", -1);
			if (tag == ZAN) {
				int pos = intent.getIntExtra("pos", -1);
				if (pos >= 0) {
					addZan(pos);
				}
			} else if (tag == PING_LUN) {
				int pos = intent.getIntExtra("pos", -1);
				if (pos >= 0) {
					addPL(pos);
				}
			} else if (tag == 3) {
				getData();
				page = 1;
				isUpdate = true;
			}
		}

	}

	private void initView(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.tieziList);
		pullToRefreshListView.setMode(Mode.BOTH);
	}

	private void setEndLabel() {
		ILoadingLayout endLabel = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLabel.setPullLabel(getString(R.string.load_more));
		endLabel.setReleaseLabel(getString(R.string.load_more));
		endLabel.setRefreshingLabel(getString(R.string.is_loading));
	}

	public void addZan(int pos) {
		if (adapter != null) {
			adapter.addZan(pos);
		}
	}

	public void addPL(int pos) {
		if (adapter != null) {
			adapter.addPl(pos);
		}
	}

	@SuppressWarnings("unchecked")
	private void setListener() {
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page = 1;
				isUpdate = true;
				getData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				getData();
			}
		});

		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				TieZiBean tieZiBean = (TieZiBean) adapter.getItem(position - 1);
				intent.putExtra("tiezi", tieZiBean);
				intent.setClass(NewTieZiFragment.this.getActivity(),
						TieZiXiangXiActivity.class);
				intent.putExtra("pos", position);
				intent.putExtra("type", tieZiBean.getTieZieType());
				startActivity(intent);
			}
		});
	}

	private void getData() {
		params = new RequestParams();
		params.put("page", page + "");
		params.put("maxSize", MAX_SIZE + "");
		params.put("type", 1 + ""); // 1一般分享帖子
		params.put("uid", uid + "");
		String method = null;
		if (uid > 0) {
			method = Constant.METHOD_GET_MY_TIEZI;
		} else {
			method = Constant.METHOD_GET_TIEZI;
		}
		Request.getTieZi(method, params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + content + error);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				MyPreferences.saveBoolean(Constant.KEY_ISFATIEOK, false);
				try {
					int code = response.getInt("code");
					if (code == Request.REQUEST_SUCCESS) {
						tieZiBeans = null;
						tieZiBeans = JsonUtils.parse(response.toString(),
								TieZiData.class);
						if (tieZiBeans != null
								&& tieZiBeans.getTieZiBeans() != null
								&& !tieZiBeans.getTieZiBeans().isEmpty()) {
							LogUtil.D("updateView111");
							updateView();

						} else {
							MyToast.showCustomerToast("没有更多数据了",getActivity());
						}

					} else {
						MyToast.showCustomerToast("获取数据失败",getActivity());
					}
				} catch (Exception e) {
					MyToast.showCustomerToast("获取数据失败",getActivity());
					LogUtil.D("ddd: " + e.toString());
				}
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				if (pullToRefreshListView != null) {
					pullToRefreshListView.onRefreshComplete();
				}
				Pdialog.hiddenDialog();

			}
		});

	}

	private void updateView() {
		if (isUpdate) {
			if (adapter != null && adapter.tieZiBeans != null) {
				adapter.tieZiBeans.clear();
				adapter.tieZiBeans = null;
			}
			adapter = null;
		}
		if (adapter == null) {
			LogUtil.D("111111111111111dddddddddddddddddddddddddddddddddddddddddd");
			adapter = new TieZiAdapter(this.getActivity(),
					tieZiBeans.getTieZiBeans(), this);
			pullToRefreshListView.setAdapter(adapter);
		} else {
			LogUtil.D("dddddddddddddddddddddddddddddddddddddddddd");
			adapter.addData(tieZiBeans.getTieZiBeans());
		}
		isUpdate = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.getActivity().unregisterReceiver(myBroadcastReceiver);
		Pdialog.hiddenDialog();
	}

	@Override
	public void getUserId(Long uid) {
		startShowOneUserActivity(getActivity(), uid);
	}

}
