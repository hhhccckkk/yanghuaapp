package com.hck.yanghua.downapp;

import org.json.JSONObject;

import android.content.Context;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.bean.BanBenBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;

public class UpdateUtil {
	private static final String ID = "1";
	private UpdateAppCallBack callBack;

	public interface UpdateAppCallBack {
		void backAppInfo(BanBenBean bean);
	}

	public void isUpdate(Context context) {
		callBack = (UpdateAppCallBack) context;
		getInfo();
	}

	private void getInfo() {
		RequestParams params = new RequestParams();
		params.put("id", ID);
		Request.getBanBenInfo(Constant.METHOD_GET_BANBEN, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						LogUtil.D("url: " + url);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: " + response.toString());
						try {
							int code = response.getInt(Constant.CODE);
							if (code == Request.REQUEST_SUCCESS) {
								BanBenBean bAppInfoBean = JsonUtils.parse(
										response.getString("data"),
										BanBenBean.class);
								callBack.backAppInfo(bAppInfoBean);
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("error: " + content);
						callBack.backAppInfo(null);
					}
				});
	}

}
