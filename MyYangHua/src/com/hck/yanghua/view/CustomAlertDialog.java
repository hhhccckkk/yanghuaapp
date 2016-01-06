package com.hck.yanghua.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hck.yanghua.R;



public class CustomAlertDialog extends Dialog implements
        View.OnClickListener {
    private final static int LEFT_BUTTON_ID = 0;
    private final static int RIGHT_BUTTON_ID = 1;
    private TextView mTitle;
    private TextView mContent;
    private TextView mLeftBtn;
    private TextView mRightBtn;
    private RelativeLayout mContentView;
    private OnClickListener mLeftListener;
    private OnClickListener mRightListener;

    public CustomAlertDialog(Context context) {
        this(context, true);
    }

    public CustomAlertDialog(Context context, boolean cancelable) {
        super(context, R.style.alert_dialog);
        init(context);
        setCancelable(cancelable);
    }

    private void init(Context context) {
        setContentView(R.layout.custom_dialog_layout);
        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        mLeftBtn = (TextView) findViewById(R.id.left);
        mRightBtn = (TextView) findViewById(R.id.right);
        mContentView = (RelativeLayout) findViewById(R.id.content_view);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 自定义提醒内容布�?
     *
     * @param view
     */
    public void setMessageContentView(View view) {
        mContentView.removeAllViews();
        mContentView.addView(view);
    }

    /**
     * 自定义提醒内容布�?
     *
     * @param viewId
     */
    public void setMessageContentView(int viewId) {
        mContentView.removeAllViews();
        View view = View.inflate(getContext(), viewId, null);
        mContentView.addView(view);
    }

    /**
     * 返回自定义的View
     *
     * @return
     */
    public View getContentView() {
        return mContentView.getChildAt(0);
    }

    /**
     * 设置是否显示标题.
     *
     * @param resId
     */
    public void setTitleVisibility(int resId) {
        mTitle.setVisibility(resId);
    }

    /**
     * 设置提示内容.
     *
     * @param resId
     */
    public void setMessage(int resId) {
        if (resId > 0) {
            mContent.setText(resId);
        }
    }

    /**
     * 设置提示内容.
     *
     * @param message
     */
    public void setMessage(String message) {
        mContent.setText(message);
    }

    /**
     * 设置标题.
     *
     * @param resId
     */
    public void setTitle(int resId) {
        if (resId > 0) {
            mTitle.setText(resId);
        }
    }

    /**
     * 设置标题.
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置左侧按钮文本.
     *
     * @param resId
     */
    public void setLeftText(int resId) {
        if (resId > 0) {
            mLeftBtn.setText(resId);
        }
    }
    public void setLeftText(String txt) {
            mLeftBtn.setText(txt);
    }
    

    /**
     * 设置右侧按钮文本.
     *
     * @param resId
     */
    public void setRightText(int resId) {
        if (resId > 0) {
            mRightBtn.setText(resId);
        }
    }
    public void setRightText(String content) {
            mRightBtn.setText(content);
    }
    public void setOnRightListener(OnClickListener listener) {
        this.mRightListener = listener;
    }

    public void setOnLeftListener(OnClickListener listener) {
        this.mLeftListener = listener;
    }

    /**
     * 隐藏左边的按�?
     */
    public void hideLeftBtn() {
        mLeftBtn.setVisibility(View.GONE);
    }
    
    public void hideRightBtn(){
        mRightBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.left:
                if (mLeftListener != null) {
                    mLeftListener.onClick(this, LEFT_BUTTON_ID);
                }
                break;
            case R.id.right:
                if (mRightListener != null) {
                    mRightListener.onClick(this, RIGHT_BUTTON_ID);
                }
                break;
            default:
                break;
        }
    }
    public void setLeftButtonTextColor(int color){
        mLeftBtn.setTextColor(color);
    }
    public void setRightButtonTextColor(int color){
        mRightBtn.setTextColor(color);
    }
}
