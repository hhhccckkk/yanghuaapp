package com.hck.yanghua.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hck.yanghua.R;

public class AlertDialog extends Dialog implements View.OnClickListener {
	private final static int LEFT_BUTTON_ID = 0;
	private final static int RIGHT_BUTTON_ID = 1;
	private TextView mTitle;
	private TextView mTopTextView;
	private TextView mCenterTextView;
	private TextView mBottomTextView;
	private OnClickListener mTopListener;
	private OnClickListener mBottomListener;
	private OnClickListener mCenterListener;

	public AlertDialog(Context context) {
		this(context, true);
	}

	public AlertDialog(Context context, boolean cancelable) {
		super(context, R.style.alert_dialog);
		init(context);
		setCancelable(cancelable);
	}

	public void showAlert(String title, String top, String center, String bottom) {
		mTitle.setText(title);
		mTopTextView.setText(top);
		mCenterTextView.setText(center);
		mBottomTextView.setText(bottom);
		if (center == null) {
			mCenterTextView.setVisibility(View.GONE);
		}
		if (bottom==null) {
			mBottomTextView.setVisibility(View.GONE);
		}
		this.show();
	}

	public void setOnTopListener(OnClickListener listener) {
		this.mTopListener = listener;
	}

	public void setOnCenterListener(OnClickListener listener) {
		this.mCenterListener = listener;
	}

	public void setOnBottomListener(OnClickListener listener) {
		this.mBottomListener = listener;
	}

	private void init(Context context) {
		setContentView(R.layout.alert);
		mTitle = (TextView) findViewById(R.id.title);
		mTopTextView = (TextView) findViewById(R.id.alert_top);
		mCenterTextView = (TextView) findViewById(R.id.alert_center);
		mBottomTextView = (TextView) findViewById(R.id.alert_bottom);
		mTopTextView.setOnClickListener(this);
		mCenterTextView.setOnClickListener(this);
		mBottomTextView.setOnClickListener(this);
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.alert_top:
			if (mTopListener != null) {
				mTopListener.onClick(this, LEFT_BUTTON_ID);
			}
			break;
		case R.id.alert_center:
			if (mCenterListener != null) {
				mCenterListener.onClick(this, RIGHT_BUTTON_ID);
			}
			break;
		case R.id.alert_bottom:
			if (mBottomListener != null) {
				mBottomListener.onClick(this, RIGHT_BUTTON_ID);
			}
			break;
		default:
			break;
		}
	}

}
