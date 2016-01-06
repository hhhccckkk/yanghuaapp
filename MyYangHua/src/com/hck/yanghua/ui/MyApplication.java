package com.hck.yanghua.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyPreferences;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;



public class MyApplication extends Application {
	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		// B.a(HCK());
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
				.cacheOnDisc(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
				.build();
		ImageLoader.getInstance().init(config2);

	}

}
