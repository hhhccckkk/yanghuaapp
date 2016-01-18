package com.hck.yanghua.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.TieZiAdapter;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.data.TieZiData;
import com.hck.yanghua.ui.TieZiXiangXiActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class HotTieZiActivity extends BaseFragment {
	private PullToRefreshListView pullToRefreshListView;
	private static final int MAX_SIZE = 10;
	public static final int ZAN = 1;
	public static final int PING_LUN = 2;
	private int page = 1;
	private boolean isUpdate;
	private TieZiAdapter adapter;
	private TieZiData tieZiBeans;

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
				.findViewById(R.id.hot_tieziList);
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
				intent.setClass(HotTieZiActivity.this.getActivity(),
						TieZiXiangXiActivity.class);
				intent.putExtra("pos", position);
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

	}
}
