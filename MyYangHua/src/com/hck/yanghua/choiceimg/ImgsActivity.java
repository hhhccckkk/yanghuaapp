package com.hck.yanghua.choiceimg;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.choiceimg.ImgsAdapter.OnItemClickClass;
import com.hck.yanghua.interfaces.ImgCallBack;
import com.hck.yanghua.ui.BaseTitleActivity;
import com.hck.yanghua.ui.FaTieActivity;
import com.hck.yanghua.ui.MainActivity;
import com.hck.yanghua.util.LogUtil;

public class ImgsActivity extends BaseTitleActivity {

	private Bundle bundle;
	private FileTraversal fileTraversal;
	private GridView imgGridView;
	private ImgsAdapter imgsAdapter;
	private ImageUtil util;
	private HashMap<Integer, ImageView> hashImage;
	private ArrayList<String> filelist;
	private TextView showChoiceSizeTextView;
	private int size;
	private static final int CHOICE_IMG_OK = 2;
    private int maxImgSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);
		maxImgSize=getIntent().getIntExtra("size", 3);
		initView();
		initTitleView();

	}

	private void initTitleView() {
		mTitleBar.setCenterText("可选"+maxImgSize+"张图片");
		mTitleBar.righButton.setText("取消");
	}

	private void initView() {
		imgGridView = (GridView) findViewById(R.id.gridView1);
		showChoiceSizeTextView = (TextView) findViewById(R.id.bottom_textview);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent,
				onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		hashImage = new HashMap<Integer, ImageView>();
		filelist = new ArrayList<String>();
		util = new ImageUtil(this);
		setListener();
	}

	private void setListener() {
		mTitleBar.getRightBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		showChoiceSizeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendfiles();
			}
		});
	}

	class BottomImgIcon implements OnItemClickListener {

		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}
	}

	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.default_img);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			filelist.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {

			String filapath = fileTraversal.filecontent.get(Position);
			
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				filelist.remove(filapath);
				size--;
				showChoiceSize();
			} else {
				try {
					if (size>=maxImgSize) {
						return;
					}
					checkBox.setChecked(true);
					ImageView imageView = iconImage(filapath, Position,
							checkBox);
					if (imageView != null) {
						hashImage.put(Position, imageView);
						filelist.add(filapath);
					}
					size++;
					showChoiceSize();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void showChoiceSize() {
		showChoiceSizeTextView.setText("确定(" + size + "/5)");
	}

	public void sendfiles() {
		Intent intent = new Intent(this, FaTieActivity.class);
		intent.putStringArrayListExtra("files", filelist);
		setResult(CHOICE_IMG_OK, intent);
		finish();
		System.gc();

	}
}
