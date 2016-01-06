package com.hck.yanghua.fragment;

import com.hck.yanghua.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 if (mRootView == null) {
	            mRootView = inflater.inflate(R.layout.fragment_home, null);
	        }
	        ViewGroup parent = (ViewGroup) mRootView.getParent();
	        if (parent != null) {
	            parent.removeView(mRootView);
	        }
	        return mRootView;
	}

}
