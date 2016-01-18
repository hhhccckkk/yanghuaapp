package com.hck.yanghua.ui;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.yanghua.R;
import com.hck.yanghua.fragment.NewTieZiFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HomeActivity extends BaseActivity implements OnClickListener {
	private static final int NEW_TIEZI = 1;
	private static final int HOT_TIEZI = 2;
	private static final int SALE_TIEZI = 3;
	private NewTieZiFragment tieZiFragment;
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);
		initTitle();
		initFragment();
		setListener();
		initHomeFragment();
	}

	private void initHomeFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_content, tieZiFragment).commit();
		mFragment = tieZiFragment;
	}

	private void initFragment() {
		tieZiFragment = new NewTieZiFragment();
	}

	private void initTitle() {
		leftTextView.setText(R.string.home_title_new);
		centerTextView.setText(R.string.home_title_hot);
		rightTextView.setText(R.string.home_title_sale);
	}

	private void setListener() {
		leftTextView.setOnClickListener(this);
		centerTextView.setOnClickListener(this);
		rightTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_title_left:
			changeBg(NEW_TIEZI);
			break;
		case R.id.home_title_center:
			changeBg(HOT_TIEZI);
			break;
		case R.id.home_title_right:
			changeBg(SALE_TIEZI);
			break;
		default:
			break;
		}

	}

	private void changeBg(int flag) {
		switch (flag) {
		case NEW_TIEZI:
			leftTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			leftTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setTextColor(getResources().getColor(R.color.whilt));
			rightTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setBackgroundResource(R.color.transparent);
			rightTextView.setBackgroundResource(R.color.transparent);
			break;
		case HOT_TIEZI:
			leftTextView.setBackgroundResource(R.color.transparent);
			leftTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			rightTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			rightTextView.setBackgroundResource(R.color.transparent);
			break;
		case SALE_TIEZI:
			leftTextView.setBackgroundResource(R.color.transparent);
			leftTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setTextColor(getResources().getColor(R.color.whilt));
			rightTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setBackgroundResource(R.color.transparent);
			rightTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			break;
		default:
			break;
		}
	}

}
