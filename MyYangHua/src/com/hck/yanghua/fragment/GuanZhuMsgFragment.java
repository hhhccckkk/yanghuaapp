package com.hck.yanghua.fragment;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.GuanZhuAdpter;
import com.hck.yanghua.adapter.GuanZhuAdpter.OnTongZhiListener;
import com.hck.yanghua.bean.GuanZhuBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.GuanZhuData;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.db.DBUtil;
import com.hck.yanghua.db.MsgInviteBean;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.ui.ChatActivity;
import com.hck.yanghua.ui.ShowOneUserActivity;
import com.hck.yanghua.ui.UserActivity;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.CustomAlertDialog;
import com.hck.yanghua.view.Pdialog;

public class GuanZhuMsgFragment extends BaseFragment implements
		OnTongZhiListener {
	private PullToRefreshListView pullToRefreshListView;
	private int page = 1;
	private boolean isUpdate;
	private GuanZhuData guanZhuData;
	private GuanZhuAdpter guanZhuAdpter;
	private boolean isMyFenSi;
	private int type = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_tz, null);
			if (getArguments() != null) {
				isMyFenSi = getArguments().getBoolean("fensi");
				type = getArguments().getInt("type");
			}
			initView(mRootView);
			Pdialog.showLoading(this.getActivity(), true);
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

	private void getData() {
		params = new RequestParams();
		params.put("page", page + "");
		String method = null;
		if (type == UserActivity.MY_FENSI) {
			method = Constant.METHOD_GET_GUANZHU_MSG;
		} else {
			method = Constant.METHOD_GET_MY_GUANZHU;
			
		}
		Request.getGuanZhuMsg(method, params, new JsonHttpResponseHandler() {
			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				Pdialog.hiddenDialog();
				pullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + error + content);
				MyToast.showCustomerToast("网络异常 获取数据失败");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					guanZhuData = JsonUtils.parse(response.toString(),
							GuanZhuData.class);
					updateView();
				} catch (Exception e) {
					MyToast.showCustomerToast("没有数据");
				}
			}
		});
	}

	private void updateView() {
		if (isUpdate && guanZhuData != null
				&& guanZhuData.getGuanZhuBeans() != null
				&& !guanZhuData.getGuanZhuBeans().isEmpty()) {
			guanZhuAdpter = null;
		}
		if (guanZhuAdpter == null) {
			guanZhuAdpter = new GuanZhuAdpter(guanZhuData.getGuanZhuBeans(),
					this.getActivity(), this, isMyFenSi);
			pullToRefreshListView.setAdapter(guanZhuAdpter);
		} else {
			if (guanZhuData != null && guanZhuData.getGuanZhuBeans() != null) {
				guanZhuAdpter.updateData(guanZhuData.getGuanZhuBeans());
			}

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
		pullToRefreshListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// MsgInviteBean msgInviteBean = (MsgInviteBean) adpter
						// .getItem(position - 1);
						// alertD(msgInviteBean);
						return false;
					}
				});

		pullToRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						try {
							GuanZhuBean guanZhuBean = (GuanZhuBean) guanZhuAdpter
									.getItem(position - 1);
							Intent intent = new Intent(GuanZhuMsgFragment.this
									.getActivity(), ShowOneUserActivity.class);
							intent.putExtra("uid", guanZhuBean.getUid());
							startActivity(intent);
						} catch (Exception e) {
						}

					}
				});

		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				isUpdate = true;
				page = 1;
				getData();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				getData();
			}
		});

	}

	private void alertD(final MsgInviteBean msgInviteBean) {
		final CustomAlertDialog alertDialog = new CustomAlertDialog(
				getActivity());
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setLeftText("取消");
		alertDialog.setRightText("删除");
		alertDialog.setMessage("确定删除吗?");
		alertDialog.setTitle("提示");
		alertDialog.setOnLeftListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setOnRightListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DBUtil(getActivity()).deleteMsgInviteMsg(msgInviteBean);
				getData();
			}
		});
		alertDialog.show();
	}

	private void initView(View mRootView) {
		pullToRefreshListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.tongzhi_tieziList);
		pullToRefreshListView.setMode(Mode.BOTH);
	}

	public void onClickListener(MsgInviteBean msgInviteBean) {

	}

	@Override
	public void caozuo(GuanZhuBean guanZhuBean) {
		if (guanZhuBean != null) {

			UserBean userBean = new UserBean();

			userBean.setName(guanZhuBean.getName());
			userBean.setTouxiang(guanZhuBean.getTouxiang());
			userBean.setUserId(guanZhuBean.getStringUid());

			Intent intent = new Intent();
			intent.putExtra("user", userBean);

			UserBean myDataBean = MyData.getData().getUserBean();
			intent.putExtra("fromImg", myDataBean.getTouxiang());
			intent.putExtra("fromUserName", myDataBean.getName());
			intent.setClass(this.getActivity(), ChatActivity.class);
			startActivity(intent);
		}

	}

}
