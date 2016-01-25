package com.hck.yanghua.fragment;

import com.hck.httpserver.RequestParams;
import com.hck.yanghua.ui.ShowOneUserActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment{
	public View mRootView;
	public RequestParams params;
   public void startShowOneUserActivity(Context context,String uid){
	   Intent intent =new Intent();
       intent.putExtra("uid", uid);
       intent.setClass(context, ShowOneUserActivity.class);
       startActivity(intent);
   }
}
