package com.hck.yanghua.fragment;

import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
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
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.TieZiData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.ui.TieZiXiangXiActivity;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;

public class NewTieZiFragment extends BaseFragment {
	private static final int MAX_SIZE = 10;
	public static final int ZAN = 1;
	public static final int PING_LUN = 2;
	private PullToRefreshListView pullToRefreshListView;
	private int page = 1;
	private boolean isUpdate;
	private TieZiData tieZiBeans;
	private static TieZiAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_tiezi, null);
			initView(mRootView);
			setListener();
			setEndLabel();
			getData();
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
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


	public static void addZan(int pos) {
		if (adapter != null) {
			adapter.addZan(pos);
		}
	}

	public static void addPL(int pos) {
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
				intent.putExtra("pos",position);
				startActivity(intent);
			}
		});
	}

	private void getData() {
		params = new RequestParams();
		params.put("page", page + "");
		params.put("maxSize", MAX_SIZE + "");
		Request.getTieZi(Constant.METHOD_GET_TIEZI, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content + error);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess 帖子: "+response.toString());
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
									MyToast.showCustomerToast("没有更多数据了");
								}

							} else {
								MyToast.showCustomerToast("获取数据失败");
							}
						} catch (Exception e) {
							MyToast.showCustomerToast("获取数据失败");
						}
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						if (pullToRefreshListView != null) {
							pullToRefreshListView.onRefreshComplete();
						}

					}
				});

	}

	private void updateView() {
		LogUtil.D("updateViewupdateViewupdateViewupdateView");
		if (isUpdate) {
			if (adapter.tieZiBeans != null) {
				adapter.tieZiBeans.clear();
				adapter.tieZiBeans = null;
			}
			adapter = null;
		}
		if (adapter == null) {
			adapter = new TieZiAdapter(this.getActivity(),
					tieZiBeans.getTieZiBeans());
			pullToRefreshListView.setAdapter(adapter);
		} else {
			adapter.addData(tieZiBeans.getTieZiBeans());
		}
		isUpdate = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		boolean isFaTie = MyPreferences.getBoolean(Constant.KEY_ISFATIEOK,
				false);
		if (isFaTie) {
			getData();
			page = 1;
			isUpdate = true;
		}
	}

}
