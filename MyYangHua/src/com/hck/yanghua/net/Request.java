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

	private static void getData(String method, RequestParams params,
			HCKHttpResponseHandler handler) {
		if (params == null) {
			client.post(Constant.MAINHOST+method, handler);
		}
		else {
			client.get(Constant.MAINHOST+method, params, handler);
		}
		
	}

	public static void getBanBenInfo(String method, RequestParams params,
			HCKHttpResponseHandler handler) {
		getData(method, params, handler);
		
	}

}
