package com.hck.yanghua.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.HuiFuAdapter;
import com.hck.yanghua.adapter.HuiFuMsgAdpter;
import com.hck.yanghua.adapter.TieZiAdapter;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MsgData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.Pdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HuiFuMsgFragment extends BaseFragment {
	private PullToRefreshListView pullToRefreshListView;
	private int page = 1;
	private MsgData msgDatas = new MsgData();
	private boolean isUpdate;
	private HuiFuMsgAdpter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_huifu_meg, null);
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

	private void getData() {
		Pdialog.showLoading(this.getActivity(), true);
		params = new RequestParams();
		params.put("page", page + "");
		Request.getHuiFuMsg(Constant.METHOD_GET_HUIFU_MSG, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						Pdialog.hiddenDialog();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response.toString());
						try {
							int code = response.getInt("code");
							if (code == 0) {
								msgDatas = JsonUtils.parse(response.toString(),
										MsgData.class);
								updateView();
							}
						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content);
						MyToast.showCustomerToast("网络异常获取数据失败");
					}
				});

	}

	private void updateView() {
		if (isUpdate) {
			if (adapter != null && adapter != null) {
				adapter.msgBeans.clear();
				adapter.msgBeans = null;
			}
			adapter = null;
		}
		if (adapter == null) {

			adapter = new HuiFuMsgAdpter(this.getActivity(),
					msgDatas.getMsgBeans());
			pullToRefreshListView.setAdapter(adapter);
		} else {
			// adapter.addData(msgDatas.getMsgBeans());
		}
		isUpdate = false;
	}

	private void setEndLabel() {
		ILoadingLayout endLabel = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLabel.setPullLabel(getString(R.string.load_more));
		endLabel.setReleaseLabel(getString(R.string.load_more));
		endLabel.setRefreshingLabel(getString(R.string.is_loading));
	}

	private void setListener() {

	}

	private void initView(View mRootView) {
		pullToRefreshListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.huifu_list);
		pullToRefreshListView.setMode(Mode.BOTH);
	}

}
