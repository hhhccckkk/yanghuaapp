package com.hck.yanghua.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.hck.yanghua.util.AppManager;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.view.CustomAlertDialog;
import com.hck.yanghua.view.TitleBar;

public class BaseActivity extends FragmentActivity {
	public TitleBar mTitleBar;
	public TextView leftTextView, centerTextView, rightTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layout) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		AppManager.getAppManager().addActivity(this);
		initTitleBar();
		ViewGroup root = getRootView();
		View paramView = getLayoutInflater().inflate(layout, null);
		root.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		root.addView(paramView, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		super.setContentView(root);
	}

	public void setContentView(View view) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initTitleBar();
		ViewGroup root = getRootView();
		View paramView = view;
		root.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		root.addView(paramView, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		super.setContentView(root);
	}

	private ViewGroup getRootView() {
		LinearLayout localLinearLayout = new LinearLayout(this);
		localLinearLayout.setOrientation(LinearLayout.VERTICAL);
		return localLinearLayout;
	}

	private void initTitleBar() {
		mTitleBar = new TitleBar(this);
		leftTextView = mTitleBar.getLeftTextView();
		centerTextView = mTitleBar.getCenterTextView();
		rightTextView = mTitleBar.getRightTextView();
	}

	public TitleBar getTitleBar() {
		return mTitleBar;
	}

	public String getStringData(int id) {
		return getResources().getString(id);
	}
	public void alertExitD() {
		CustomAlertDialog alertDialog = new CustomAlertDialog(this);
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setLeftText("退出");
		alertDialog.setRightText("好评");
		alertDialog.setTitle("提示");
		alertDialog.setMessage("确定要退出吗?");
		alertDialog.setOnLeftListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				exit();
			}
		});

		alertDialog.setOnRightListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				haoPing();
			}
		});
		alertDialog.show();

	}
	private void haoPing() {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}


	private void exit() {
		AppManager.getAppManager().AppExit();
		finish();
	}

}
