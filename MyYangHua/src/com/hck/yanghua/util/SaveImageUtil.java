package com.hck.yanghua.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.hck.yanghua.data.Constant;

public class SaveImageUtil extends AsyncTask<Void, Void, String> {
	private static final String YANG_HUA = "yanghua";
	private Context context;
	private Bitmap bitmap;
	private String imageName;

	public SaveImageUtil(Context context, Bitmap bitmap, String imageName) {
		this.context = context;
		this.bitmap = bitmap;
		this.imageName = imageName;
	}

	@Override
	protected String doInBackground(Void... params) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			Toast.makeText(context, "sdcard不可用", Toast.LENGTH_SHORT).show();
		} else {
			MyTools.savePic(bitmap, YANG_HUA + imageName,
					Constant.YANG_HUA_PATH);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://" + Environment.getExternalStorageDirectory())));
		System.gc();
	}

}
