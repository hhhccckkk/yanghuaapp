package com.hck.yanghua.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.view.PopupChoicePicter;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing.GetBiaoQing;

public class FaTieActivity extends BaseTitleActivity implements GetBiaoQing {
	private static final int MAX_SIZE = 600;
	private static final int GET_PHOTO = 1;
	public static final int GET_PICTER = 2;
	private static final String IMAGEFILE = "/yanghua/";
	private static final String HAS_IMAGE = "1";
	private EditText contentEditText;
	private PopupWindowChiceBiaoQing pBiaoQing;
	private List<Bitmap> bitmaps = new ArrayList<>();
	private String imagePath;
	private LinearLayout imageLayout;
	private ArrayList<String> imagePaths = new ArrayList<>();
	ArrayList<String> listfile = new ArrayList<String>();
	public static Activity fatieActivity;
    private int type=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type=getIntent().getIntExtra("type", 1);
		fatieActivity = this;
		setContentView(R.layout.activity_fatie);
		initTitle();
		initView();
	}

	private void initView() {
		contentEditText = (EditText) findViewById(R.id.fatie_content_ex);
		imageLayout = (LinearLayout) findViewById(R.id.fatie_lay);
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
		long uid = MyData.getData().getUserId();
		data.append("uid=" + uid).append("&content=" + content);
		data.append("&type="+type);
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
								MyPreferences.saveBoolean(
										Constant.KEY_ISFATIEOK, true);
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

					}
				});

	}

	// 获取一个路径，保存拍照后获取的照片
	public String getPath() {
		File sdDir = null;
		String path2;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			File file = null;
			File dir = new File(sdDir + IMAGEFILE);
			if (!dir.exists()) {
				dir.mkdir();
			}
			file = new File(dir, System.currentTimeMillis() + ".jpg");
			path2 = file.toString();
			return path2;
		} else {
			MyToast.showCustomerToast("没有sdcard");
			return null;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_PICTER) {
			if (data != null) {
				listfile = data.getStringArrayListExtra("files");
				for (int i = 0; i < listfile.size(); i++) {
					String imagePath = listfile.get(i);
					addImagePath(imagePath);
					Bitmap bitmap = MyTools.getSmallBitmap(imagePath);
					if (bitmap != null) {
						showImages(bitmap, imagePath);
					}
					bitmaps.add(bitmap);
				}
			} else {
			}

		} else if (requestCode == GET_PHOTO) {
			Bitmap photo = null;
			File file = new File(imagePath);
			if (file != null && file.exists()) {
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 8;
				photo = BitmapFactory.decodeFile(file.getPath(), option);
				addImagePath(imagePath);
			}
			if (photo != null) {
				showImages(photo, imagePath);

			}
			bitmaps.add(photo);
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
				.inflate(R.layout.image_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		view.setTag(path);
		imageView.setImageBitmap(bitmap);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = (String) v.getTag();
				removePath(tag);
				imageLayout.removeView(view);
				LogUtil.D("removePath: " + tag);
			}
		});
		imageLayout.addView(view);
	}

	// 弹出选择获取图片的pop
	public void getPicter(View view, String path) {
		PopupChoicePicter popupController = new PopupChoicePicter(this, path,
				GET_PHOTO, GET_PICTER);
		popupController.checkPopupWindow();
		popupController.getPopupWindow().setAnimationStyle(
				R.style.popwin_anim_style);
		popupController.getPopupWindow().showAtLocation(view, Gravity.BOTTOM,
				0, 0);
	}

	// 弹出选择图片界面
	public void choicePicter(View view) {
		if (imagePaths.size() >= 5) {
			MyToast.showCustomerToast("最多添加5张图片哦");
			return;
		}
		String path = getPath();
		imagePath = path;
		getPicter(view, path);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (pBiaoQing != null) {
			hidenPop();
			return false;
		} else {
			finish();
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void getImage(SpannableString spannableString) {
		if (spannableString != null) {
			contentEditText.append(spannableString);
		}
		hidenPop();
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

}
