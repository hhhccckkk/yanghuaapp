package com.hck.yanghua.ui;

import com.hck.yanghua.R;
import com.hck.yanghua.fragment.BaiKeFragment;
import com.hck.yanghua.fragment.ZaiPeiFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

public class BaoDianActivity extends BaseActivity implements OnClickListener {
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private ZaiPeiFragment zaiPeiFragment;
    private BaiKeFragment baiKeFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baodian);
		initTitle();
		initFragmentView();
		setData(zaiPeiFragment);
	}

	private void setData(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); // 得到一个管理fragment的事物
		ft.replace(R.id.baodian_content, fragment); // 把R.id.framt控件换成fragment
		ft.commit();// 执行
	}

	private void initFragmentView() {
		zaiPeiFragment = new ZaiPeiFragment();
		baiKeFragment=new BaiKeFragment();
		
	}

	private void initTitle() {
		leftTextView.setText("栽培领经");
		centerTextView.setText("花卉百科");
		rightTextView.setVisibility(View.GONE);
		leftTextView.setOnClickListener(this);
		centerTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_title_left:
			changeBg(LEFT);
			setData(zaiPeiFragment);
			break;
		case R.id.home_title_center:
			setData(baiKeFragment);
			changeBg(RIGHT);
			break;

		default:
			break;
		}
	}
  
	private void changeBg(int flag) {
		switch (flag) {
		case LEFT:
			leftTextView.setBackgroundResource(R.drawable.home_title_bt_shap);
			leftTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setBackgroundResource(R.color.transparent);

			break;
		case RIGHT:
			leftTextView.setBackgroundResource(R.color.transparent);
			leftTextView.setTextColor(getResources().getColor(R.color.whilt));
			centerTextView.setTextColor(getResources().getColor(
					R.color.red_anniu_bt_color));
			centerTextView.setBackgroundResource(R.drawable.home_title_bt_shap);

			break;
		default:
			break;
		}
	}
}
