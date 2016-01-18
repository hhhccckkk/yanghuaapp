package com.hck.yanghua.ui;

import com.hck.yanghua.R;

import android.os.Bundle;
import android.view.View;

public class UserActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initTitle();
		initTitle();
	}

	private void initTitle() {
		leftTextView.setVisibility(View.GONE);
		centerTextView.setText("用户中心");
		centerTextView.setBackgroundResource(R.color.transparent);
		rightTextView.setVisibility(View.GONE);
	}
}
