package com.hck.yanghua.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hck.yanghua.R;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.location.MyLocation;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;



public class MyApplication extends Application {
	public static Context context;
    private static final int memoryCacheSize=1024*1024*5;
	@Override
	public void onCreate() {
		super.onCreate();
		// B.a(HCK());
		getLocation();
		LogUtil.isPrintLog = true;
		context = this;
		new MyPreferences(this);
		initImagerLoder();
	}

	static {
		System.loadLibrary("hck");
	}

	public native String HCK();

	private void initImagerLoder() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(false)
				.showImageOnFail(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(memoryCacheSize)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
				.build();
		ImageLoader.getInstance().init(config2);

	}
	private void getLocation() {
		MyLocation.startLocation(this, new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				MyData.bdLocation=arg0;
			}
		});
	}

}
