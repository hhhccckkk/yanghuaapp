package com.hck.yanghua.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.ui.MainActivity;

/**
 * 公用title.
 */
public class BaseTitleBar extends LinearLayout {
	private LinearLayout mLeftBackBtn; // 左边返回按钮
	private TextView mCenterTextV; // 中间文本.
	private Context mContext;
	public Button righButton;

	public BaseTitleBar(Context context) {
		super(context);
		mContext = context;
		init(context);
	}

	public BaseTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context);
	}

	/**
	 * 初始化view.
	 * 
	 * @param context
	 */
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.base_title_bar, this);
		mLeftBackBtn = (LinearLayout) findViewById(R.id.titleBackBtn);
		mCenterTextV = (TextView) findViewById(R.id.titleCenterTV);
		righButton = (Button) findViewById(R.id.right_btn);
		setListener();
	}

	/**
	 * 点击返回按钮退出当前activity.
	 */
	private void setListener() {
		mLeftBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) mContext).finish();

			}
		});
	}

	public void setTitleLeftContent(String content) {
		mCenterTextV.setText(content);
	}

	public TextView getCenterTextView() {
		return mCenterTextV;
	}

	public void setCenterText(String textString) {
		mCenterTextV.setText(textString);
	}

	public void setCentrTextColor(int color) {
		mCenterTextV.setTextColor(color);
	}

	public void setRightBtnText(String text) {
		righButton.setVisibility(View.VISIBLE);
		righButton.setText(text);
	}

	public Button getRightBtn() {
		return righButton;
	}
}
