package com.hck.yanghua.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hck.yanghua.R;
import com.hck.yanghua.ui.MyApplication;

public class MyToast {
	 private static Toast sToast;
	 public static void showCustomerToast(String textString) {
	        View view = LayoutInflater.from(MyApplication.context).inflate(R.layout.toast, null);
	        TextView textView = (TextView) view.findViewById(R.id.toast_text);
	        textView.setText(textString);
	        if (sToast != null) {
	            sToast.cancel();
	            sToast = null;
	        }
	        sToast = new Toast(MyApplication.context);
	        sToast.setDuration(Toast.LENGTH_LONG);
	        sToast.setGravity(Gravity.CENTER, 0, 0);
	        sToast.setView(view);
	        sToast.show();

	    }
	 
	 public static void dissMissToast(){
	     if (sToast!=null) {
            sToast.cancel();
            sToast=null;
        }
	 }

}
