package com.hck.yanghua.ui;

import java.io.File;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.yanghua.R;
import com.hck.yanghua.bean.HuiTieBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.fragment.NewTieZiFragment;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.ExpressionUtil;
import com.hck.yanghua.util.GetImageUtil;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.TimeUtil;
import com.hck.yanghua.view.Pdialog;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing.GetBiaoQing;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HuiFuActivity extends BaseTitleActivity implements GetBiaoQing {
	private ImageView touxiangImageView;
	private ImageView imageView1, imageView2, imageView3;
	private TextView userTextView, fensiTextView, timeTextView;
	private ImageView xingbieImageView;
	private TextView yuantieTextView, contentTextView;
	private TextView addressTextView;
	private HuiTieBean huiTieBean;
	private LinearLayout addressLayout;
	private String imag1, imag2, imag3;
	private PopupWindowChiceBiaoQing pBiaoQing;
	private EditText contentEditText;
	private int pos;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huifu);
		huiTieBean = (HuiTieBean) getIntent().getSerializableExtra("data");
		type = getIntent().getIntExtra("type", -1);
		pos = getIntent().getIntExtra("pos", pos);
		initTitleView("回复");
		centerTextView.setTextSize(14);
		initView();
		initData();
	}

	private void initView() {
		touxiangImageView = (ImageView) findViewById(R.id.huifu_touxiang);
		imageView1 = (ImageView) findViewById(R.id.huifu_img1);
		imageView2 = (ImageView) findViewById(R.id.huifu_img2);
		imageView3 = (ImageView) findViewById(R.id.huifu_img3);
		userTextView = (TextView) findViewById(R.id.huifu_userName);
		fensiTextView = (TextView) findViewById(R.id.huifu_fensi);
		timeTextView = (TextView) findViewById(R.id.huifu_time);
		xingbieImageView = (ImageView) findViewById(R.id.huifu_xingbie);
		yuantieTextView = (TextView) findViewById(R.id.huifu_yuantie);
		contentTextView = (TextView) findViewById(R.id.huifu_content);
		addressLayout = (LinearLayout) findViewById(R.id.huifu_address_lay);
		addressTextView = (TextView) findViewById(R.id.huifu_address);
		contentEditText = (EditText) findViewById(R.id.huihu_huifu);
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(huiTieBean.getTouxiang(),
				touxiangImageView);
		userTextView.setText(huiTieBean.getName());
		fensiTextView.setText("丨粉丝" + huiTieBean.getFensi());
		timeTextView.setText(TimeUtil.forTime(huiTieBean.getTime()));
		SpannableString spannableString = ExpressionUtil.getExpressionString(
				this, huiTieBean.getContent(), Constant.zhengze);
		contentTextView.setText(spannableString);
		String yuanTie = huiTieBean.getYuantie();
		if (TextUtils.isEmpty(yuanTie)) {
			yuantieTextView.setVisibility(View.GONE);
		} else {
			yuantieTextView.setVisibility(View.VISIBLE);
			SpannableString spannableString2 = ExpressionUtil
					.getExpressionString(this, huiTieBean.getYuantie(),
							Constant.zhengze);
			yuantieTextView.setText(spannableString2);
		}
		imag1 = huiTieBean.getImage1();
		imag2 = huiTieBean.getIamge2();
		imag3 = huiTieBean.getIamge3();
		if (TextUtils.isEmpty(imag1)) {
			imageView1.setVisibility(View.GONE);
		} else {
			imageView1.setVisibility(View.VISIBLE);
			GetImageUtil.showImageDaTu(imag1, imageView1);
		}
		if (TextUtils.isEmpty(imag2)) {
			imageView2.setVisibility(View.GONE);
		} else {
			imageView2.setVisibility(View.VISIBLE);
			GetImageUtil.showImageDaTu(imag1, imageView2);
		}
		if (TextUtils.isEmpty(imag3)) {
			imageView3.setVisibility(View.GONE);
		} else {
			imageView3.setVisibility(View.VISIBLE);
			GetImageUtil.showImageDaTu(imag1, imageView3);
		}
		if (huiTieBean.isXingbie()) {
			xingbieImageView.setImageResource(R.drawable.nan);
		} else {
			xingbieImageView.setImageResource(R.drawable.nv);
		}
		String huitieString = huiTieBean.getAddress();
		if (TextUtils.isEmpty(huitieString)) {
			addressLayout.setVisibility(View.VISIBLE);
			addressTextView.setText(huitieString);
		} else {
			addressLayout.setVisibility(View.GONE);
		}

	}

	public void huifu(View view) {
		final String data = contentEditText.getText().toString();
		if (TextUtils.isEmpty(data)) {
			MyToast.showCustomerToast("评论不能为空");
			return;
		}
		Pdialog.showDialog(this, "提交数据中...", false);
		StringBuffer content = new StringBuffer("");
		UserBean userBean = MyData.getData().getUserBean();
		BDLocation bdLocation = MyData.bdLocation;
		content.append("uid=" + userBean.getUid());
		if (bdLocation != null) {
			content.append("&address=" + bdLocation.getCity()
					+ bdLocation.getDistrict() + bdLocation.getStreet());
		}
		content.append("&content=" + data)
				.append("&tid=" + huiTieBean.getTid()).append("&type=" + 2);
		content.append("&yunatie=" + huiTieBean.getContent());
		content.append("&userName=" + "回复:" + huiTieBean.getName());
		Request.addHuiTie(Constant.METHOD_ADD_HUIFU + content.toString(),
				params, new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + error + content);
						MyToast.showCustomerToast("回复失败");
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response);
						try {
							int code = response.getInt("code");
							if (code == 0) {
								MyToast.showCustomerToast("回复成功");
								sendBroadcast(pos);
								updateView(huiTieBean.getName(),
										huiTieBean.getContent(), data);
							} else {
								MyToast.showCustomerToast("回复失败");
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);

					}
				});
	}

	private void sendBroadcast(int pos) {
		Intent intent = new Intent();
		if (type == Constant.NEW_TIE_ZI) {
			intent.setAction(Constant.NEW_ADD_PL);
		} else if (type == Constant.HOT_TIE_ZI) {
			intent.setAction(Constant.HOT_ADD_PL);
		} else if (type == Constant.SALE_TIE_ZI) {
			intent.setAction(Constant.SALE_ADD_PL);
		}
		intent.putExtra("tag", 2);
		intent.putExtra("pos", pos);
		sendBroadcast(intent);
	}

	private void updateView(String userName, String yuanTie, String content) {
		Intent intent = new Intent();
		intent.setClass(this, TieZiXiangXiActivity.class);
		intent.putExtra("name", userName);
		intent.putExtra("yt", yuanTie);
		intent.putExtra("content", content);
		setResult(TieZiXiangXiActivity.PINGLUN_OK, intent);
		finish();

	}

	// 弹出选择表情界面
	public void showPopChiceImage(View view) {
		if (pBiaoQing == null) {
			pBiaoQing = new PopupWindowChiceBiaoQing();
			pBiaoQing.showFaTieView(view, this, this);
		}

	}

	// 隐藏选择表情界面
	private void hidenPop() {
		if (pBiaoQing != null && pBiaoQing.popupWindow != null) {
			pBiaoQing.popupWindow.dismiss();
		}
		pBiaoQing = null;
	}

	@Override
	public void getImage(SpannableString spannableString) {
		if (spannableString != null) {
			contentEditText.append(spannableString);
		}
		hidenPop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pBiaoQing != null) {
				hidenPop();
				return false;
			} else {
				finish();
				return true;
			}
		}
		return true;

	}
}
