package com.hck.yanghua.choiceimg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hck.yanghua.R;
import com.hck.yanghua.ui.BaseTitleActivity;
import com.hck.yanghua.ui.FaTieActivity;
import com.hck.yanghua.ui.TieZiXiangXiActivity;

public class ShowImageListActivity extends BaseTitleActivity {
	private ListView listView;
	private ImageUtil util;
	private ImgFileListAdapter listAdapter;
	List<FileTraversal> locallist;
    private int maxSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_imgae_list);
		maxSize=getIntent().getIntExtra("size", 3);
		initTitleView();
		setListener();
		getImageData();
	}

	private void getImageData() {
		listView = (ListView) findViewById(R.id.listView1);
		util = new ImageUtil(this);
		locallist = util.LocalImgFileList();
		List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
		Bitmap bitmap[] = null;
		if (locallist != null) {
			bitmap = new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("filecount", locallist.get(i).filecontent.size() + "张");
				map.put("imgpath",
						locallist.get(i).filecontent.get(0) == null ? null
								: (locallist.get(i).filecontent.get(0)));
				map.put("filename", locallist.get(i).filename);
				listdata.add(map);
			}
		}
		listAdapter = new ImgFileListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
	}

	private void initTitleView() {
		mTitleBar.setCenterText("选择图片");
		listView = (ListView) findViewById(R.id.listView1);
	}

	private void setListener() {
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent(ShowImageListActivity.this,
//						ImgsActivity.class);
//				intent.putExtra("size", maxSize);
//				Bundle bundle = new Bundle();
//				bundle.putParcelable("data", locallist.get(position));
//				intent.putExtras(bundle);
//				if (FaTieActivity.fatieActivity != null) {
//					FaTieActivity.fatieActivity.startActivityForResult(intent,
//							FaTieActivity.GET_PICTER);
//				} else {
//					TieZiXiangXiActivity.tieZiXiangXiActivity
//							.startActivityForResult(intent,
//									FaTieActivity.GET_PICTER);
//				}
//				finish();
//				System.gc();
//			}
//		});
	}

}
