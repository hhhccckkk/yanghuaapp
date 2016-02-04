package com.hck.yanghua.ui;

import com.hck.yanghua.R;
import com.hck.yanghua.fragment.GuanZhuMsgFragment;

import android.os.Bundle;

public class ShowMyFenSiActivity extends BaseTitleActivity {
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_myfensi);
		type = getIntent().getIntExtra("type", 0);
		if (type == UserActivity.MY_FENSI) {
			initTitleView("我的粉丝");
		} else {
			initTitleView("我的关注");
		}
		initData();
	}

	private void initData() {
		GuanZhuMsgFragment guanZhuMsgFragment = new GuanZhuMsgFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("fensi", true);
		bundle.putInt("type", type);
		guanZhuMsgFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.show_my_fensi, guanZhuMsgFragment).commit();

	}

}
