package com.hck.yanghua.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.httpserver.RequestParams;
import com.hck.yanghua.view.BaseTitleBar;

public class BaseTitleActivity extends Activity {
	public RequestParams params;
	public BaseTitleBar mTitleBar;
	public TextView centerTextView;
	public Button righButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initTitleView(String centerString) {
		centerTextView.setText(centerString);
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
		mTitleBar = new BaseTitleBar(this);
		centerTextView = mTitleBar.getCenterTextView();
	}

}
