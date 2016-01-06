package com.hck.yanghua.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hck.yanghua.R;
import com.hck.yanghua.ui.MainActivity;



/**
 * 主界面menu界面
 */
@SuppressLint("ValidFragment")
public class MainMenuFragment extends Fragment {
    private static MainActivity mMainActivity = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        view.findViewById(R.id.home).setOnClickListener(mMainActivity);
        view.findViewById(R.id.order).setOnClickListener(mMainActivity);
        view.findViewById(R.id.safe).setOnClickListener(mMainActivity);
        view.findViewById(R.id.yunli).setOnClickListener(mMainActivity);
        view.findViewById(R.id.xiaoji).setOnClickListener(mMainActivity);
        view.findViewById(R.id.user).setOnClickListener(mMainActivity);
        view.findViewById(R.id.exit).setOnClickListener(mMainActivity);
        return view;
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
