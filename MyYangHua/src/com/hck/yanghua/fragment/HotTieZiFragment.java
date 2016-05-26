package com.hck.yanghua.fragment;

import java.util.List;

import org.json.JSONObject;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.TieZiAdapter;
import com.hck.yanghua.adapter.TieZiAdapter.OnTouXiangCliceListener;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.TieZiData;
import com.hck.yanghua.fragment.NewTieZiFragment.MyBroadcastReceiver;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.ui.TieZiXiangXiActivity;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

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

public class HotTieZiFragment extends BaseFragment implements OnTouXiangCliceListener{
	private PullToRefreshListView pullToRefreshListView;
	private static final int MAX_SIZE = 10;
	public static final int ZAN = 1;
	public static final int PING_LUN = 2;
	private int page = 1;
	private boolean isUpdate;
	private TieZiAdapter adapter;
	private TieZiData tieZiBeans;
	private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		regestBrodCast();
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_hot_tiezi, null);
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
		filter.addAction(Constant.HOT_ADD_PL);
		filter.addAction(Constant.HOT_ADD_ZAN);
		filter.addAction(Constant.UPDATE_HOT_TIEZI_DATA);
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

	private void initView(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.hot_tieziList);
		pullToRefreshListView.setMode(Mode.BOTH);
	}

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
				intent.setClass(HotTieZiFragment.this.getActivity(),
						TieZiXiangXiActivity.class);
				intent.putExtra("pos", position);
				intent.putExtra("type", Constant.HOT_TIE_ZI);
				startActivity(intent);
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
		params = new RequestParams();
		params.put("page", page + "");
		params.put("maxSize", MAX_SIZE + "");
		LogUtil.D("page: " + page);
		Request.getHotTieZi(Constant.METHOD_GET_GETHOTTIEZI, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content + error);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess 帖子: " + response.toString());
						MyPreferences
								.saveBoolean(Constant.KEY_ISFATIEOK, false);
						try {
							int code = response.getInt("code");
							if (code == Request.REQUEST_SUCCESS) {
								tieZiBeans = null;
								tieZiBeans = JsonUtils.parse(
										response.toString(), TieZiData.class);
								if (tieZiBeans != null
										&& tieZiBeans.getTieZiBeans() != null
										&& !tieZiBeans.getTieZiBeans()
												.isEmpty()) {
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
			if (adapter.tieZiBeans != null) {
				adapter.tieZiBeans.clear();
				adapter.tieZiBeans = null;
			}
			adapter = null;
		}
		if (adapter == null) {
			adapter = new TieZiAdapter(this.getActivity(),
					tieZiBeans.getTieZiBeans(),this);
			pullToRefreshListView.setAdapter(adapter);
		} else {
			adapter.addData(tieZiBeans.getTieZiBeans());
		}
		isUpdate = false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.getActivity().unregisterReceiver(myBroadcastReceiver);
	}
	@Override
	public void getUserId(Long uid) {
       startShowOneUserActivity(getActivity(), uid);
	}
}
