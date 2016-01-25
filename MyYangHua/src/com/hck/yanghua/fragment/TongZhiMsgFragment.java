package com.hck.yanghua.fragment;

import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.ZhongZhiAdpter;
import com.hck.yanghua.adapter.ZhongZhiAdpter.OnTongZhiListener;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.db.DBUtil;
import com.hck.yanghua.db.MsgInviteBean;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.view.AlertDialog;
import com.hck.yanghua.view.CustomAlertDialog;

public class TongZhiMsgFragment extends BaseFragment implements
		OnTongZhiListener {
	private PullToRefreshListView pullToRefreshListView;
	private List<MsgInviteBean> msgInviteBeans;
	private ZhongZhiAdpter adpter;
	public static final int TONGYI = 1;
	public static final int JUJUE = 2;
	public static final int ERROR = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_tz, null);
			initView(mRootView);
			setListener();
			setEndLabel();
			getData();
			getData();
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
	}

	private void getData() {
		msgInviteBeans = new DBUtil(this.getActivity()).getInviteBeans();
		adpter = null;
		adpter = new ZhongZhiAdpter(msgInviteBeans, this.getActivity(), this);
		pullToRefreshListView.setAdapter(adpter);
		pullToRefreshListView.onRefreshComplete();

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
						MsgInviteBean msgInviteBean = (MsgInviteBean) adpter
								.getItem(position - 1);
						alertD(msgInviteBean);
						return false;
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
		pullToRefreshListView.setMode(Mode.PULL_FROM_START);
	}

	public void onClickListener(MsgInviteBean msgInviteBean) {

	}

	@Override
	public void caozuo(MsgInviteBean msgInviteBean) {
		LogUtil.D("msgInviteBean: " + msgInviteBean.getUserName());
		this.msgInviteBean = msgInviteBean;
		showAlertD(msgInviteBean);

	}

	private void showAlertD(final MsgInviteBean msgInviteBean) {
		AlertDialog alertDialog = new AlertDialog(getActivity());
		alertDialog.showAlert("是否加为好友", "同意", "拒绝", null);
		alertDialog.setOnTopListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.D("同意");
				tongyi(msgInviteBean.getUserMsgId());
			}
		});
		alertDialog.setOnCenterListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.D("拒绝");
				juJue(msgInviteBean.getUserMsgId());
			}
		});
		
	}

	private MsgInviteBean msgInviteBean = null;

	private void updateMsgState(MsgInviteBean msgInviteBean) {
		new DBUtil(getActivity()).updateMsgInviteMsg(msgInviteBean);
		getData();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == ERROR) {
				MyToast.showCustomerToast("处理失败");
			} else if (msg.what == JUJUE) {
				msgInviteBean.setState(JUJUE);
				updateMsgState(msgInviteBean);
			} else if (msg.what == TONGYI) {
				msgInviteBean.setState(TONGYI);
				updateMsgState(msgInviteBean);
			}
		};
	};

	private void tongyi(final String userName) {
		new Thread() {
			@Override
			public void run() {
				try {
					EMChatManager.getInstance().acceptInvitation(userName);
					handler.sendEmptyMessage(TONGYI);
					LogUtil.D("通过一同意");
				} catch (EaseMobException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(ERROR);
				}
			}
		}.start();

	}

	private void juJue(final String userName) {
		new Thread() {
			@Override
			public void run() {
				try {
					EMChatManager.getInstance().refuseInvitation(userName);
					handler.sendEmptyMessage(JUJUE);
				} catch (EaseMobException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(ERROR);
				}
			}
		}.start();

	}

}
