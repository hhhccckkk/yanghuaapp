package com.hck.yanghua.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

}
