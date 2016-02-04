package com.hck.yanghua.ui;

import com.hck.yanghua.R;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.fragment.NewTieZiFragment;

import android.os.Bundle;

public class ShowMyTieZiActivity extends BaseTitleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_my_tiezi);
		initTitleView("我的帖子");
		initData();
	}

	private void initData() {
		NewTieZiFragment tieZiFragment = new NewTieZiFragment();
		Bundle bundle = new Bundle();
		bundle.putLong("uid", MyData.getData().getUserId());
		tieZiFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.my_tiezi, tieZiFragment).commit();
	}

}
