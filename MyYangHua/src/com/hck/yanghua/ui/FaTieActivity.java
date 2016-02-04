package com.hck.yanghua.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.view.MyEditextView;
import com.hck.yanghua.view.MyGridView;
import com.hck.yanghua.view.MyGridView.GetBiaoQingCallBack;
import com.hck.yanghua.view.Pdialog;

public class FaTieActivity extends BaseTitleActivity implements
		GetBiaoQingCallBack {
	private static final int MAX_SIZE = 600;
	private static final int REQUEST_IMAGE = 1;
	private static final String HAS_IMAGE = "1";
	private MyEditextView contentEditText;
	private List<Bitmap> bitmaps = new ArrayList<>();
	private LinearLayout imageLayout;
	private ArrayList<String> imagePaths = new ArrayList<>();
	ArrayList<String> listfile = new ArrayList<String>();
	public static Activity fatieActivity;
	private int type = 0;
	private MyGridView gridView;
	private LinearLayout choicePicterLay;
	private ArrayList<String> mSelectPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 1); // type 1,一般帖子，2出售帖子
		fatieActivity = this;
		setContentView(R.layout.activity_fatie);
		initTitle();
		initView();
	}

	private void initView() {
		contentEditText = (MyEditextView) findViewById(R.id.fatie_content_ex);
		choicePicterLay = (LinearLayout) findViewById(R.id.choicePicterLay);
		contentEditText.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		contentEditText.setHint("内容不超过500个字");
		imageLayout = (LinearLayout) findViewById(R.id.fatie_lay);
		gridView = (MyGridView) findViewById(R.id.fatie_gridview);
		gridView.setGetBiaoQingCallBackListener(this);
		hidenPop();
	}

	private void initTitle() {
		centerTextView.setText("发表新帖");
		mTitleBar.setRightBtnText("提交");
		mTitleBar.righButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addTieZi();
			}
		});
	}

	private void addTieZi() {
		StringBuffer data = new StringBuffer("");
		String content = contentEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			MyToast.showCustomerToast("内容不能为空");
			return;
		}
		if (content.length() > MAX_SIZE) {
			MyToast.showCustomerToast("内容不能超过600个文字");
			return;
		}
		content = content.trim();
		Pdialog.showDialog(this, "提交中...", false);
		long uid = MyData.getData().getUserId();
		data.append("uid=" + uid).append("&content=" + content);
		data.append("&type=" + type);
		if (MyData.bdLocation != null) {
			data.append("&address=" + MyData.bdLocation.getCity()
					+ MyData.bdLocation.getDistrict()
					+ MyData.bdLocation.getStreet());

		}
		params = new RequestParams();
		if (imagePaths != null && !imagePaths.isEmpty()) {
			data.append("&hasImage=" + HAS_IMAGE);
		}
		File file = null;
		try {
			for (int i = 0; i < imagePaths.size(); i++) {
				switch (i) {
				case 0:
					file = new File(imagePaths.get(0));
					params.put("file", file);
					break;
				case 1:
					file = new File(imagePaths.get(1));
					params.put("file1", file);
					break;

				case 2:
					file = new File(imagePaths.get(2));
					params.put("file2", file);
					break;
				case 3:
					file = new File(imagePaths.get(3));
					params.put("file3", file);
					break;

				case 4:
					file = new File(imagePaths.get(4));
					params.put("file4", file);
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
		}
		Request.addTieZi(Constant.METHOD_ADD_TIEZI + data, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("失败: " + error + content);
						MyToast.showCustomerToast("网络异常发帖失败");
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response.toString());
						try {
							int code = response.getInt("code");
							if (code == Request.REQUEST_SUCCESS) {
								MyToast.showCustomerToast("发帖成功");
								sendBroadcast();
								finish();
							} else {
								MyToast.showCustomerToast("发帖失败");
							}
						} catch (Exception e) {

						}
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						LogUtil.D("onFinish: " + url);
						Pdialog.hiddenDialog();

					}
				});

	}

	private void sendBroadcast() {
		Intent intent = new Intent();
		if (type == 1) {
			intent.setAction(Constant.UPDATE_HOME_TIEZI_DATA);
		} else {
			intent.setAction(Constant.UPDATE_SALE_TIEZI_DATA);
		}
		intent.putExtra("tag", 3);
		sendBroadcast(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.D("onActivityResultonActivityResultonActivityResult: "
				+ requestCode + ":" + resultCode);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				for (String imgPath : mSelectPath) {
					Bitmap bitmap = MyTools.getSmallBitmap2(imgPath);
					if (bitmap != null) {
						addImagePath(imgPath);
						showImages(bitmap, imgPath);
					}
					bitmaps.add(bitmap);
				}
			}
		}

		if (requestCode == 1 && data != null) {
			String path = data.getStringExtra("img");
			int type = data.getIntExtra("type", -1);
			if (type == 100) {
				if (!TextUtils.isEmpty(path)) {
					addImagePath(path);
					Bitmap bitmap = MyTools.getSmallBitmap2(path);
					LogUtil.D("bitmapbitmap: " + bitmap);
					showImages(bitmap, path);
				}
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void destroyBitMap() {
		if (!bitmaps.isEmpty()) {
			for (int i = 0; i < bitmaps.size(); i++) {
				Bitmap bitmap = bitmaps.get(i);
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
	}

	private void addImagePath(String ptah) {
		imagePaths.add(ptah);
	}

	private void removePath(String tag) {
		imagePaths.remove(tag);
	}

	private void showImages(Bitmap bitmap, String path) {
		final LinearLayout view = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.fatie_image_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		view.setTag(path);
		imageView.setImageBitmap(bitmap);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = (String) v.getTag();
				removePath(tag);
				imageLayout.removeView(view);
			}
		});
		imageLayout.addView(view);
	}

	// 弹出选择获取图片的pop
	public void choicePicter(View view) {
		choicePicterLay.setVisibility(View.VISIBLE);
	}

	public void choiceNormPicter(View view) {
		hidenPop();
		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		int imageSize = imagePaths.size();

		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
				5 - imageSize);

		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	public void choiceBiaoQianPicter(View view) {
		hidenPop();
		if (imagePaths.size() >= 5) {
			MyToast.showCustomerToast("最多5张图，点击可删除");
			return;
		}
		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_SINGLE);
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	// 弹出选择表情界面
	public void showPopChiceImage(View view) {
		if (gridView.getVisibility() == View.VISIBLE) {
			gridView.setVisibility(View.GONE);
		} else {
			gridView.setVisibility(View.VISIBLE);
			choicePicterLay.setVisibility(View.GONE);
		}

	}

	// 隐藏选择表情界面
	private void hidenPop() {
		gridView.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (gridView.getVisibility() == View.VISIBLE) {
			gridView.setVisibility(View.GONE);
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		hidenPop();
		return true;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyBitMap();
		System.gc();
	}

	@Override
	public void getBiaoQing(SpannableString data) {
		if (data != null) {
			contentEditText.append(data);
		}

	}

}
