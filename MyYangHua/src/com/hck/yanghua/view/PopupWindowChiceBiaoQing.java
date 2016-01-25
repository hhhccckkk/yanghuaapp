package com.hck.yanghua.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PaintDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;

import com.hck.yanghua.R;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyTools;

public class PopupWindowChiceBiaoQing {
	private static final int BIAO_QING_SIZE = 45;
	public PopupWindow popupWindow;
	private GridView gridView;
	private int[] imageIds = new int[BIAO_QING_SIZE];
	private float density;
	List<String> contentList;
	private int listSize = 10; // 显示多少列
	private GetBiaoQing getBiaoQing;

	public interface GetBiaoQing {
		void getImage(SpannableString spannableString);
	}

	public void showFaTieView(View view, Context context,
			final GetBiaoQing getBiaoQing) {
		View pView = LayoutInflater.from(context).inflate(
				R.layout.pop_chice_biaoqing, null);
		this.getBiaoQing = getBiaoQing;
		initView(pView, context);
		int Width = MyTools.getScreenWidth(context);
		int Height = MyTools.getScreenHeight(context);
		LogUtil.D("hhh: "+Height);
		if (Height > 800) {
			Height = Height / 3 - 15;
		} else {
			Height = Height / 3;
		}
		popupWindow = new PopupWindow(pView, Width, Height);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setClippingEnabled(true);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				getBiaoQing.getImage(null);
			}
		});

	}

	private void initView(View view, final Context context) {
		gridView = (GridView) view.findViewById(R.id.grid);
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		density = outMetrics.density; // 像素密度

		// 根据item的数目，动态设定gridview的宽度,现假定每个item的宽度和高度均为100dp，列间距为5dp
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		int itemWidth = (int) (50 * density);
		int spacingWidth = (int) (3 * density);

		params.width = itemWidth * listSize + (listSize - 1) * spacingWidth;
		gridView.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式
		gridView.setNumColumns(listSize);
		gridView.setHorizontalSpacing(spacingWidth);
		gridView.setColumnWidth(itemWidth);
		gridView.setLayoutParams(params);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
		for (int i = 0; i < BIAO_QING_SIZE; i++) {
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
		gridView.setAdapter(simpleAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position++;
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(
						((Activity) context).getResources(), imageIds[position]);
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
				getBiaoQing.getImage(spannableString);

			}
		});
	}
}
