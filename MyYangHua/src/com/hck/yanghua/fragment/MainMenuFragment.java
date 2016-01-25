package com.hck.yanghua.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.ui.MainActivity;
import com.hck.yanghua.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 主界面menu界面
 */
@SuppressLint("ValidFragment")
public class MainMenuFragment extends Fragment {
	private static MainActivity mMainActivity = null;
	private ImageView toxuiangImageView;
	private TextView userNameTextView;

	public MainMenuFragment() {
	}

	public static MainMenuFragment newInstance(MainActivity act) {
		mMainActivity = act;
		MainMenuFragment f = new MainMenuFragment();
		return f;
	}

	public void setMainAct(MainActivity act) {
		mMainActivity = act;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_menu, container,
				false);
		view.findViewById(R.id.home).setOnClickListener(mMainActivity);
		view.findViewById(R.id.near_user).setOnClickListener(mMainActivity);
		view.findViewById(R.id.safe).setOnClickListener(mMainActivity);
		view.findViewById(R.id.yunli).setOnClickListener(mMainActivity);
		view.findViewById(R.id.xiaoji).setOnClickListener(mMainActivity);
		view.findViewById(R.id.user).setOnClickListener(mMainActivity);
		view.findViewById(R.id.exit).setOnClickListener(mMainActivity);
		toxuiangImageView = (ImageView) view.findViewById(R.id.touxiang);
		userNameTextView = (TextView) view.findViewById(R.id.userName);
		setData();
		return view;
	}

	private void setData() {
		UserBean userBean = MyData.getData().getUserBean();
		if (userBean != null) {
			userNameTextView.setText(userBean.getName());
			ImageLoader.getInstance().displayImage(userBean.getTouxiang(),
					toxuiangImageView);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
