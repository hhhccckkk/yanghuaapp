package com.hck.yanghua.net;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.data.Constant;

public class Request {

	private static final int TIME_OUT = 15 * 1000;
	public static final int REQUEST_SUCCESS = 0;
	private static HCKHttpClient client = new HCKHttpClient();
	static {
		client.setTimeout(TIME_OUT);
	}

	private static void request(String method, RequestParams params,
			HCKHttpResponseHandler handler) {
		if (params == null) {
			client.post(Constant.MAINHOST+method, handler);
		}
		else {
			client.post(Constant.MAINHOST+method, params, handler);
		}
		
	}
    //获取版本信息
	public static void getBanBenInfo(String method, RequestParams params,
			HCKHttpResponseHandler handler) {
		request(method, params, handler);
		
	}
	//增加用户
	public static void addUser(String method, RequestParams params,
			HCKHttpResponseHandler handler){
		request(method, params, handler);
	}

}
