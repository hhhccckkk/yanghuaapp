package com.hck.yanghua.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MyLocation {
	public static LocationClient mLocationClient;
	private static LocationMode tempMode = LocationMode.Hight_Accuracy;
	private static String tempcoor = "gcj02";

	public static void startLocation(Context context,
			BDLocationListener bdLocationListener) {
		mLocationClient = new LocationClient(context); // 声明LocationClient类
		mLocationClient.registerLocationListener(bdLocationListener); // 注册监听函数
		initLocation();
	}

	public static void stopLocation() {
		if (mLocationClient!=null) {
			mLocationClient.stop();
			mLocationClient = null;
		}
		
	}

	private static void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
         if (mLocationClient!=null) {
        		mLocationClient.setLocOption(option);
        		mLocationClient.start();
		}
	
	}

}
