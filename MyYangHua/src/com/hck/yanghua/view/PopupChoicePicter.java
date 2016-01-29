package com.hck.yanghua.view;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hck.yanghua.R;
import com.hck.yanghua.choiceimg.ShowImageListActivity;
import com.hck.yanghua.util.MyToast;

public class PopupChoicePicter {

	private final int REQUEST_CODE_TAKE_PHOTO;
	private final int REQUEST_CODE_TAKE_PICTER;
	private Activity activity;
	private PopupWindow popupWindow;
	private Button takePhotoBtn;
	private Button choosePicBtn;
	private Button cancelBtn;
	private String path;
	private File file;
    private int imageSize;
	public PopupChoicePicter(Activity activity, String path,
			int take_photo_code, int REQUEST_CODE_TAKE_PICTER,int size) {
		this.activity = activity;
		this.REQUEST_CODE_TAKE_PHOTO = take_photo_code;
		this.REQUEST_CODE_TAKE_PICTER = REQUEST_CODE_TAKE_PICTER;
		this.imageSize=size;
		this.path = path;
	}

	public PopupWindow getPopupWindow() {
		return popupWindow;
	}

	/***
	 * 获取PopupWindow实例
	 */
	public void checkPopupWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	private void initPopuptWindow() {
		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = activity.getLayoutInflater().inflate(
				R.layout.pop_choice_picter, null, false);
		// 创建PopupWindow实例,宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		// pop.xml视图里面的控件
		takePhotoBtn = (Button) popupWindow_view.findViewById(R.id.take_photo);
		choosePicBtn = (Button) popupWindow_view.findViewById(R.id.choose_pic);
		cancelBtn = (Button) popupWindow_view.findViewById(R.id.cancel);

		takePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这里可以执行相关操作
				popupWindow.dismiss();
				popupWindow = null;
				String state = Environment.getExternalStorageState(); // 获取系统的存储状态
				if (state.equals(Environment.MEDIA_MOUNTED)) { // 被分区,有读和写的权限
					getPhoto();
				} else {
					MyToast.showCustomerToast("SD卡无读写权限");
				}
				/**
	         * 
	         */
			}
		});

		choosePicBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这里可以执行相关操作
				// popupWindow.dismiss();
				// popupWindow = null;
				// Intent intent = new Intent(Intent.ACTION_PICK, null);
				// intent.setDataAndType(
				// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// // 定义一个明确的类型
				// activity.startActivityForResult(intent,
				// REQUEST_CODE_TAKE_PICTER);
				popupWindow.dismiss();
				popupWindow = null;
				Intent intent =new Intent();
				intent.putExtra("size", imageSize);
				intent.setClass(activity, ShowImageListActivity.class);
				activity.startActivityForResult(intent,REQUEST_CODE_TAKE_PICTER);
			}
		});
		// 关闭
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这里可以执行相关操作
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}

	private void getPhoto() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			file = new File(path);
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e("hck", "IOException: " + e.toString());
				e.printStackTrace();
				Toast.makeText(activity, "照片创建失败 ,请检查sdcard是否可用!",
						Toast.LENGTH_LONG).show();
				return;
			}

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			intent.putExtra("data", file.toString());
			activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
		} else {
			Toast.makeText(activity, "sdcard无效或没有插入!", Toast.LENGTH_SHORT)
					.show();
		}

	}

}
