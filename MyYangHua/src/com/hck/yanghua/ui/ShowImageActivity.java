package com.hck.yanghua.ui;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hck.yanghua.R;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.util.GetImageUtil;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.SaveImageUtil;
import com.hck.yanghua.view.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowImageActivity extends Activity {
	private List<String> imageList;
	private ViewPager mViewPager;
	private TextView sizeTextView;
	private int imageSize = 0;
	private int pos;
	private boolean isBenDi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image);
		imageList = getIntent().getStringArrayListExtra("images");
		isBenDi = getIntent().getBooleanExtra("isBenDi", false);
		imageSize = imageList.size();
		initView();
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(new SamplePagerAdapter());
		setListener();

	}

	public void back(View view) {
		finish();
	}

	public void saveImage(View view) {
		PhotoView photoView = (PhotoView) mViewPager.findViewById(pos);
		if (photoView != null) {
			Bitmap bitmap = photoView.getDrawingCache();
			if (bitmap != null) {
				new SaveImageUtil(this, bitmap, imageList.get(pos)).execute();
			}
		} else {
			Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
		}

	}

	private void initView() {
		sizeTextView = (TextView) findViewById(R.id.image_size);
		sizeTextView.setText(1 + "/" + imageSize);
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pos = arg0;
				int size = arg0 + 1;

				sizeTextView.setText(size + "/" + imageSize);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			final PhotoView photoView = new PhotoView(container.getContext());
			photoView.setId(position);
			photoView.setDrawingCacheEnabled(true);
			if (isBenDi) {
				ImageLoader.getInstance().displayImage(
						"file://" + imageList.get(position), photoView);
			} else {
				GetImageUtil.showImageDaTu(imageList.get(position), photoView);
			}
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
