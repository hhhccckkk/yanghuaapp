package com.hck.yanghua.util;

import android.widget.ImageView;

import com.hck.yanghua.data.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GetImageUtil {
	// 显示大图
	public static void showImageDaTu(String imageName, ImageView imageView) {
		ImageLoader.getInstance().displayImage(
				Constant.IMAGE_DATU_PATH + imageName, imageView);
	}

	// 显示小图
	public static void showImageXiaoTu(String imageName, ImageView imageView) {
		ImageLoader.getInstance().displayImage(
				Constant.IMAGE_XIAOTU_PATH + imageName, imageView);

	}

	public static void showImage(String path, ImageView imageView) {
		ImageLoader.getInstance().displayImage(path, imageView,MyTools.getoptions());

	}

}
