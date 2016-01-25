package com.hck.yanghua.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.yanghua.R;

public class Pdialog {
	static Dialog loadingDialog;

	public static Dialog creatDialog(Context context, String msg,
			boolean cancelable) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);
		loadingDialog = new Dialog(context, R.style.loading_dialog);
		loadingDialog.setCancelable(cancelable);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return loadingDialog;

	}

	public static void showDialog(Context context, String msg,
			boolean cancelable) {
		try {
			if (loadingDialog != null && context != null) {
				loadingDialog.dismiss();
				loadingDialog = null;
			}
			loadingDialog = creatDialog(context, msg, cancelable);
			loadingDialog.show();
		} catch (Exception e) {
		}

	}

	/**
	 * 加载数据
	 */
	public static void showLoading(Context context, boolean cancelable) {
		showDialog(context, "加载中...", cancelable);
	}

	public static void hiddenDialog() {
		try {
			if (loadingDialog != null) {
				loadingDialog.dismiss();
				loadingDialog = null;
			}
		} catch (Exception e) {
		}

	}

}
