package com.hck.yanghua.downapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hck.yanghua.R;
import com.hck.yanghua.util.AppManager;
import com.hck.yanghua.util.MyToast;

public class UpdateManager {
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private static final int DOWN_ERROR_FILE_ERROR = 3;
    private File mSavePath;
    private int progress;
    private boolean cancelUpdate = false;

    private Context mContext;
    private ProgressBar mProgress;
    private Dialog loadingDialog;
    private String pkgName;
    private int id;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWNLOAD:
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                loadingDialog.dismiss();
                installApk();
                break;
            case 0:
                MyToast.showCustomerToast("网络异常,下载失败");
                AppManager.getAppManager().AppExit();
                break;
            case DOWN_ERROR_FILE_ERROR:
                MyToast.showCustomerToast("下载地址错误，无法下载");
            default:
                break;
            }
        };
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 下载apk文件
     */
    public void downloadApk(String url) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) view.findViewById(R.id.update_progress);
        loadingDialog = new Dialog(mContext,R.style.myDialogTheme);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        loadingDialog.show();
        new downloadApkThread(url).start();

    }
    

    private class downloadApkThread extends Thread {
        String urls = null;

        public downloadApkThread(String url) {
            this.urls = url;
        }

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    File file = Environment.getExternalStorageDirectory();

                    URL url = new URL(urls);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    mSavePath = new File(file + "/kedouzq/");
                    if (!mSavePath.exists()) {
                        mSavePath.mkdir();
                    }
                    pkgName = "/kedou" + System.currentTimeMillis() + ".apk";
                    File apkFile = new File(mSavePath + pkgName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            loadingDialog.dismiss();
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        fos.write(buf, 0, numread);
                        fos.flush();
                    } while (!cancelUpdate);
                    fos.close();
                    is.close();
                } else {
                    mHandler.sendEmptyMessage(0);
                    return;
                }
            } catch (MalformedURLException e) {
                mHandler.sendEmptyMessage(0);
                e.printStackTrace();
                Log.e("hck", "eee:" + e);
            } catch (IOException e) {
                mHandler.sendEmptyMessage(0);
                e.printStackTrace();
                Log.e("hck", "IOException:" + e);
            }

            loadingDialog.dismiss();
        }

    };

    /**
     * 安装APK文件
     */

    public void installApk() {

        File apkfile = new File(mSavePath, pkgName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

}
