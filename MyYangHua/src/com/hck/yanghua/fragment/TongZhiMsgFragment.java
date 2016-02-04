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
import com.hck.yanghua.adapter.HuiFuMsgAdpter;
import com.hck.yanghua.adapter.HuiFuMsgAdpter.LiaoTianCallBack;
import com.hck.yanghua.bean.MsgBean;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MsgData;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.ui.ChatActivity;
import com.hck.yanghua.ui.TieZiXiangXiActivity;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.BadgeView;
import com.hck.yanghua.view.CustomAlertDialog;
import com.hck.yanghua.view.Pdialog;

public class TongZhiMsgFragment extends BaseFragment implements LiaoTianCallBack {
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
						pullToRefreshListView.onRefreshComplete();
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
		if (isUpdate && msgDatas.getMsgBeans() != null
				&& !msgDatas.getMsgBeans().isEmpty()) {
			if (adapter != null && adapter != null) {
				adapter.msgBeans.clear();
				adapter.msgBeans = null;
			}
			adapter = null;
		}
		if (adapter == null) {
			adapter = new HuiFuMsgAdpter(this.getActivity(),
					msgDatas.getMsgBeans(), this);
			pullToRefreshListView.setAdapter(adapter);
		} else {
			if (msgDatas.getMsgBeans() != null) {
				adapter.updateView(msgDatas.getMsgBeans());
			}
		}
		pullToRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				pullToRefreshListView.onRefreshComplete();
			}
		}, 1000);

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

		pullToRefreshListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						MsgBean msgBean = (MsgBean) adapter
								.getItem(position - 1);
						alertDeleteD(msgBean.getId(), position - 1);
						return false;
					}
				});

		pullToRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						LogUtil.D("onItemClickonItemClickonItemClick");
						MsgBean msgBean = (MsgBean) adapter
								.getItem(position - 1);
						TieZiBean tieZiBean = new TieZiBean();
						tieZiBean.setName(msgBean.getTieziUserName());
						tieZiBean.setTouxiang(msgBean.getFatieUserTX());
						tieZiBean.setFensi(msgBean.getFensi());
						tieZiBean.setContent(msgBean.getYuantie());
						tieZiBean.setTupian1(msgBean.getImage1());
						tieZiBean.setTupian2(msgBean.getImage2());
						tieZiBean.setTupian3(msgBean.getImage3());
						tieZiBean.setTupian4(msgBean.getImage4());
						tieZiBean.setTupian5(msgBean.getImage5());
						tieZiBean.setAddress(msgBean.getAddress());
						tieZiBean.setTime(msgBean.getTime().substring(0, 11));
						tieZiBean.setTid(msgBean.getTid());
						Intent intent = new Intent();
						intent.putExtra("tiezi", tieZiBean);
						intent.setClass(TongZhiMsgFragment.this.getActivity(),
								TieZiXiangXiActivity.class);
						intent.putExtra("type", msgBean.getSaleOrNorm());
						startActivity(intent);

					}
				});
	}

	private void alertDeleteD(final Long msgId, final int pos) {
		CustomAlertDialog alertDialog = new CustomAlertDialog(
				this.getActivity());
		alertDialog.setTitle("提示");
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setLeftText("取消");
		alertDialog.setRightText("删除");
		alertDialog.setMessage("确定删除吗?");
		alertDialog.setOnRightListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteMsg(msgId, pos);
			}
		});
		alertDialog.show();
	}

	private void deleteMsg(long msgId, final int pos) {
		Pdialog.showDialog(getActivity(), "删除中...", true);
		params = new RequestParams();
		params.put("msgId", msgId + "");
		Request.deleteHuiFuMsg(params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MyToast.showCustomerToast("删除失败");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					int code = response.getInt("code");
					if (code == 0) {
						adapter.msgBeans.remove(pos);
						adapter.notifyDataSetChanged();
					} else {
						MyToast.showCustomerToast("删除失败");
					}
				} catch (Exception e) {
					LogUtil.D("eee: " + e.toString());
				}

			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				Pdialog.hiddenDialog();
			}
		});

	}

	private void initView(View mRootView) {
		pullToRefreshListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.huifu_list);
		pullToRefreshListView.setMode(Mode.BOTH);
	}

	public void startChatActivity(Object data) {

	}

	@Override
	public void startLiaoTian(Object object) {
		MsgBean msgBean = (MsgBean) object;
		UserBean userBean = new UserBean();
		if (msgBean != null) {
			userBean.setName(msgBean.getUserName());
			userBean.setTouxiang(msgBean.getTouxiang());
			userBean.setUserId(msgBean.getUserMsgId());
		}
		Intent intent = new Intent();
		intent.putExtra("user", userBean);

		UserBean myDataBean = MyData.getData().getUserBean();
		intent.putExtra("fromImg", myDataBean.getTouxiang());
		intent.putExtra("fromUserName", myDataBean.getName());
		intent.setClass(this.getActivity(), ChatActivity.class);
		startActivity(intent);

	}

}
