package com.hck.yanghua.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hck.yanghua.R;
import com.hck.yanghua.ui.FaTieActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
public class MyGridView extends GridView {
	private static final int BIAO_QING_SIZE = 45;
	private int[] imageIds = new int[BIAO_QING_SIZE];
	private GetBiaoQingCallBack callBack;
    public interface GetBiaoQingCallBack{
    	void getBiaoQing(SpannableString data);
    }
	public MyGridView(Context context) {
		super(context);
	}
    public void setGetBiaoQingCallBackListener(GetBiaoQingCallBack listener){
    	this.callBack=listener;
    }
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBiaoQing(context);
	}

	private void initBiaoQing(final Context context) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 1; i < BIAO_QING_SIZE; i++) {
			try {
				if (i < 10) {
					Field field = R.drawable.class
							.getDeclaredField("hck00" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else if (i < 100) {
					Field field = R.drawable.class.getDeclaredField("hck0" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else {
					Field field = R.drawable.class.getDeclaredField("f" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems,
				R.layout.pop_grid_item, new String[] { "image" },
				new int[] { R.id.biaoqing });
		this.setAdapter(simpleAdapter);
		this.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position++;
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						imageIds[position]);
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				String str = null;
				if (position < 10) {
					str = "hck00" + position;
				} else if (position < 100) {
					str = "hck0" + position;
				} else {
					str = "hck" + position;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 6,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				callBack.getBiaoQing(spannableString);

			}
		});

	}

}
