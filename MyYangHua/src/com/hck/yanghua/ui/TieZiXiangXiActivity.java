package com.hck.yanghua.ui;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.adapter.HuiFuAdapter;
import com.hck.yanghua.bean.HuiTieBean;
import com.hck.yanghua.bean.TieZiBean;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.bean.ZanBean;
import com.hck.yanghua.data.Constant;
import com.hck.yanghua.data.HuiTieData;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.data.ZanData;
import com.hck.yanghua.fragment.NewTieZiFragment;
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.ExpressionUtil;
import com.hck.yanghua.util.GetImageUtil;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.TimeUtil;
import com.hck.yanghua.view.Pdialog;
import com.hck.yanghua.view.PopupChoicePicter;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing;
import com.hck.yanghua.view.PopupWindowChiceBiaoQing.GetBiaoQing;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TieZiXiangXiActivity extends BaseTitleActivity implements
		android.view.View.OnClickListener, GetBiaoQing {
	public static final int JING_HUA = 1; // 精华
	public static final int HUO = 20; // 火
	public static final int TUIJIAN = 1; // 推荐
	public static final int TYPE_HUI_FU_LOU_ZHU = 1; // 回复楼主
	private static final String HAS_IMAGE = "1";
	private LinearLayout layout, addressLayout;
	private ImageView touxiangImageView, xingbieImageView;
	private TextView userNameTextView, contentTextView, timeTextView,
			fensiTextView;
	private TextView huoTextView, jingTextView, tuijianTextView,
			addressTextView;
	private TieZiBean tieZiBean;
	private ListView pListView;
	private View convertView;
	private HuiFuAdapter adapter;
	private EditText editText;
	private View view = null;
	ImageView imageView = null;
	private ArrayList<String> imageStrings = new ArrayList<>();
	private View tupianView;
	private LinearLayout biaoqingLayout;
	private ImageView zanImageView;
	private long tId;
	private ZanData zanData;
	private LinearLayout zanLayout;
	private TextView zanSizeTextView;
	private PopupWindowChiceBiaoQing pBiaoQing;
	private static final String IMAGEFILE = "/yanghua/";
	ArrayList<String> listfile = new ArrayList<String>();
	private LinearLayout tupianLayout;
	public static TieZiXiangXiActivity tieZiXiangXiActivity;
	private int page = 1;
	private HuiTieData huiTieData = new HuiTieData();
	private int pos;
	private int type; // 1一般帖子 2出售帖子

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tieZiXiangXiActivity = this;
		setContentView(R.layout.activity_tiezixiangxi);
		tieZiBean = (TieZiBean) getIntent().getSerializableExtra("tiezi");
		type = getIntent().getIntExtra("type", -1);
		pos = getIntent().getIntExtra("pos", -1);
		tId = tieZiBean.getTid();
		initView();
		initTitleView("帖子详情");
		initHeaderView();
		initHeaderData();
		setListener();
		getZan();
		getHuiTie();
	}

	private void getHuiTie() {
		params = new RequestParams();
		params.put("tid", tId + "");
		params.put("page", page + "");
		Request.getHuiTie(Constant.METHOD_GET_HUITIE, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.D("onFailure: " + content);
						updateView();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: getHuiTie" + response.toString());
						try {
							huiTieData = JsonUtils.parse(response.toString(),
									HuiTieData.class);
							LogUtil.D("ddd: "
									+ huiTieData.getHuiTieBeans().size());
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.D("ExceptionException: " + e.toString());
						}
						updateView();
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
					}
				});
	}

	private void getZan() {
		params = new RequestParams();
		params.put("tid", tId + "");
		Request.getZan(Constant.METHOD_GET_ZAN, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + content);
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);

						try {
							zanData = JsonUtils.parse(response.toString(),
									ZanData.class);

						} catch (Exception e) {
						}
						updateZan();

					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
					}
				});

	}

	private void updateZan() {
		if (zanData == null || zanData.getZanBean() == null) {
			return;
		}
		zanLayout.setVisibility(View.VISIBLE);
		ImageView imageView = null;
		List<ZanBean> zanBean = zanData.getZanBean();
		LayoutParams params = new LayoutParams(50, 50);
		if (zanBean.size() > 0) {
			zanSizeTextView.setText(zanBean.size() + "人赞过");
			for (int i = 0; i < zanBean.size(); i++) {
				if (i > 15) {
					return;
				}
				imageView = new ImageView(this);
				imageView.setLayoutParams(params);
				imageView.setPadding(15, 3, 0, 0);
				GetImageUtil.showImage(zanBean.get(i).getImage(), imageView);
				zanLayout.addView(imageView);
			}
		} else {
			zanLayout.setVisibility(View.GONE);
		}

	}

	private void setListener() {
		zanImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					params = new RequestParams();
					params.put("id", tId + "");
					params.put("img", MyData.getData().getUserBean()
							.getTouxiang());
					Request.addZan(Constant.METHOD_ADD_ZAN, params,
							new JsonHttpResponseHandler() {
								@Override
								public void onFinish(String url) {
									super.onFinish(url);
								}

								@Override
								public void onFailure(Throwable error,
										String content) {
									super.onFailure(error, content);
									LogUtil.D("onFailure: " + error + content);
								}

								@Override
								public void onSuccess(int statusCode,
										JSONObject response) {
									super.onSuccess(statusCode, response);
									try {
										if (response.getInt(Constant.CODE) == Request.REQUEST_SUCCESS) {
											zanImageView.setEnabled(false);
											MyToast.showCustomerToast("赞+1");
											if (pos > 0) {

												sendBroadcast(pos, true);
											}
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
				} catch (Exception e) {
				}

			}
		});

		editText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				zanImageView.setVisibility(View.GONE);
				biaoqingLayout.setVisibility(View.VISIBLE);
				tupianView.setVisibility(View.VISIBLE);
				return false;
			}
		});

	}

	private static final int GET_PHOTO = 1;
	public static final int GET_PICTER = 2;
	public static final int PINGLUN_OK = 3;
	private ArrayList<String> imagePaths = new ArrayList<>();
	private List<Bitmap> bitmaps = new ArrayList<>();
	private String imagePath;

	// 弹出选择获取图片的pop
	public void getPicter(View view, String path) {
		PopupChoicePicter popupController = new PopupChoicePicter(this, path,
				GET_PHOTO, GET_PICTER);
		popupController.checkPopupWindow();
		popupController.getPopupWindow().setAnimationStyle(
				R.style.popwin_anim_style);
		popupController.getPopupWindow().showAtLocation(view, Gravity.BOTTOM,
				0, 0);
	}

	// 弹出选择图片界面
	public void choicePicter(View view) {
		if (imagePaths.size() >= 3) {
			MyToast.showCustomerToast("最多添加5张图片哦");
			return;
		}

		String path = getPath();
		imagePath = path;
		getPicter(view, path);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.D("requestCode: " + requestCode + ": " + requestCode);
		if (requestCode == GET_PICTER) {
			if (data != null) {
				listfile = data.getStringArrayListExtra("files");
				for (int i = 0; i < listfile.size(); i++) {
					String imagePath = listfile.get(i);
					LogUtil.D("imagePath: "+imagePath);
					addImagePath(imagePath);
					Bitmap bitmap = MyTools.getSmallBitmap(imagePath);
					if (bitmap != null) {
						showImages(bitmap, imagePath);
					}
					bitmaps.add(bitmap);
				}
			} else {
			}

		} else if (requestCode == GET_PHOTO) {
			Bitmap photo = null;
			File file = new File(imagePath);
			if (file != null && file.exists()) {
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 8;
				photo = BitmapFactory.decodeFile(file.getPath(), option);
				addImagePath(imagePath);
			}
			if (photo != null) {
				showImages(photo, imagePath);

			}
			bitmaps.add(photo);
		} else if (requestCode == PINGLUN_OK) {
			if (huiTieData.getHuiTieBeans().size() < 10) {
				HuiTieBean huiTieBean = new HuiTieBean();
				huiTieBean.setName(data.getStringExtra("name"));
				huiTieBean.setYuantie(data.getStringExtra("yt"));
				huiTieBean.setContent(data.getStringExtra("content"));
				addHuiFu(huiTieBean);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 新增回复后，判断当前显示的回复数量书否大于10条，小于则显示刚添加的回复
	private void addHuiFu(HuiTieBean huiTieBean1) {
		try {
			HuiTieBean huiTieBean = new HuiTieBean();
			if (MyData.bdLocation != null) {
				huiTieBean.setAddress(MyData.bdLocation.getCity()
						+ MyData.bdLocation.getDistrict()
						+ MyData.bdLocation.getStreet());
			}
			UserBean userBean = MyData.getData().getUserBean();
			huiTieBean.setUid(userBean.getUid());
			huiTieBean.setTime(new Timestamp(System.currentTimeMillis())
					.toString());
			huiTieBean.setFensi(userBean.getFensi());
			huiTieBean.setName(userBean.getName());
			huiTieBean.setTouxiang(userBean.getTouxiang());
			if (userBean.getXingbie() == 1) {
				huiTieBean.setXingbie(true);
			}
			if (huiTieBean1.getImage1()!=null) {
				huiTieBean.setImage1(huiTieBean1.getImage1());
			}
			if (huiTieBean1.getIamge2()!=null) {
				huiTieBean.setImage1(huiTieBean1.getIamge2());
			}
			if (huiTieBean1.getIamge3()!=null) {
				huiTieBean.setImage1(huiTieBean1.getIamge3());
			}
			huiTieBean.setHuifuUserName(huiTieBean1.getName());
			huiTieBean.setYuantie(huiTieBean1.getYuantie());
			huiTieBean.setContent(huiTieBean1.getContent());
			if (adapter != null) {
				adapter.updateView(huiTieBean);
			}

		} catch (Exception e) {
			LogUtil.D("addHuiFu eeee: " + e.toString());
		}
	}

	private void showImages(Bitmap bitmap, String path) {
		final LinearLayout view = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.image_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		view.setTag(path);
		imageView.setImageBitmap(bitmap);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = (String) v.getTag();
				removePath(tag);
				tupianLayout.removeView(view);
			}
		});
		tupianLayout.addView(view);
	}

	private void addImagePath(String ptah) {
		imagePaths.add(ptah);
	}

	private void removePath(String tag) {
		imagePaths.remove(tag);
	}

	private void destroyBitMap() {
		if (!bitmaps.isEmpty()) {
			for (int i = 0; i < bitmaps.size(); i++) {
				Bitmap bitmap = bitmaps.get(i);
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
	}

	// 获取一个路径，保存拍照后获取的照片
	public String getPath() {
		File sdDir = null;
		String path2;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			File file = null;
			File dir = new File(sdDir + IMAGEFILE);
			if (!dir.exists()) {
				dir.mkdir();
			}
			file = new File(dir, System.currentTimeMillis() + ".jpg");
			path2 = file.toString();
			return path2;
		} else {
			MyToast.showCustomerToast("没有sdcard");
			return null;
		}

	}

	// 弹出选择表情界面
	public void showPopChiceImage(View view) {
		if (pBiaoQing == null) {
			pBiaoQing = new PopupWindowChiceBiaoQing();
			pBiaoQing.showFaTieView(view, this, this);
		}

	}

	// 隐藏选择表情界面
	private void hidenPop() {
		if (pBiaoQing != null && pBiaoQing.popupWindow != null) {
			pBiaoQing.popupWindow.dismiss();
		}
		pBiaoQing = null;
	}

	private void hidenHuiTie() {
		zanImageView.setVisibility(View.VISIBLE);
		biaoqingLayout.setVisibility(View.GONE);
		tupianView.setVisibility(View.GONE);
		editText.setText("");

	}

	private void initView() {
		pListView = (ListView) findViewById(R.id.huifu_list_view);
		findViewById(R.id.tiezi_xiangxi_hufu_btn).setOnClickListener(this);
		editText = (EditText) findViewById(R.id.tiezi_huifu);
		biaoqingLayout = (LinearLayout) findViewById(R.id.huifu_biaoqing_lay);
		zanImageView = (ImageView) findViewById(R.id.huifu_zan);
		tupianLayout = (LinearLayout) findViewById(R.id.HuiTie_img);
		tupianView = findViewById(R.id.huiFu_ScrollView);

	}

	private void initHeaderView() {
		convertView = getLayoutInflater().inflate(
				R.layout.tiezi_xiangxi_header, null);
		touxiangImageView = (ImageView) convertView
				.findViewById(R.id.tiezi_xiangxi_touxiang);
		userNameTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_userName);
		contentTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_content);
		layout = (LinearLayout) convertView.findViewById(R.id.imageLay);
		// dingTextView = (TextView) convertView
		// .findViewById(R.id.tiezi_xiangxi_ding);
		// pinglunTextView = (TextView) convertView
		// .findViewById(R.id.tiezi_xiangxi_pinglun);
		xingbieImageView = (ImageView) convertView
				.findViewById(R.id.tiezi_xiangxi_xingbie);
		fensiTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_fensi);
		huoTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_huo);
		jingTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_jing);
		tuijianTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_tuijian);
		addressLayout = (LinearLayout) convertView
				.findViewById(R.id.tiezi_xiangxi_address_lay);
		addressTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_address);
		timeTextView = (TextView) convertView
				.findViewById(R.id.tiezi_xiangxi_time);
		zanLayout = (LinearLayout) convertView.findViewById(R.id.tiezi_zan);
		zanSizeTextView = (TextView) convertView
				.findViewById(R.id.tiezi_zan_size);
	}

	private void initHeaderData() {
		userNameTextView.setText(tieZiBean.getName());
		SpannableString spannableString = ExpressionUtil.getExpressionString(
				this, tieZiBean.getContent(), Constant.zhengze);
		contentTextView.setText(spannableString);
		// dingTextView.setText(tieZiBean.getDingsize() + "");
		// pinglunTextView.setText(tieZiBean.getPinglunsize() + "");
		fensiTextView.setText("丨粉丝:" + tieZiBean.getFensi());
		if (!TextUtils.isEmpty(tieZiBean.getAddress())) {
			addressTextView.setText(tieZiBean.getAddress());
			addressLayout.setVisibility(View.VISIBLE);
		} else {
			addressLayout.setVisibility(View.GONE);
		}
		if (tieZiBean.isNan()) {
			xingbieImageView.setImageResource(R.drawable.nan);
		} else {
			xingbieImageView.setImageResource(R.drawable.nv);
		}
		if (tieZiBean.getIsjinghua() == JING_HUA) {
			jingTextView.setVisibility(View.VISIBLE);
		} else {
			jingTextView.setVisibility(View.GONE);
		}
		if (tieZiBean.getIstuijian() == TUIJIAN) {
			tuijianTextView.setVisibility(View.VISIBLE);
		} else {
			tuijianTextView.setVisibility(View.GONE);
		}
		if (tieZiBean.getPinglunsize() > 20) {
			huoTextView.setVisibility(View.VISIBLE);
		} else {
			huoTextView.setVisibility(View.GONE);
		}
		timeTextView.setText(TimeUtil.forTime(tieZiBean.getHuifuTiem()));
		ImageLoader.getInstance().displayImage(tieZiBean.getTouxiang(),
				touxiangImageView, MyTools.getoptions());
		String image1, image2, image3, image4, image5;
		image1 = tieZiBean.getTupian1();
		image2 = tieZiBean.getTupian2();
		image3 = tieZiBean.getTupian3();
		image4 = tieZiBean.getTupian4();
		image5 = tieZiBean.getTupian5();

		if (!TextUtils.isEmpty(image1)) {
			imageStrings.add(image1);
			view = getLayoutInflater().inflate(R.layout.tiezixiang_image_view,
					null);
			imageView = (ImageView) view.findViewById(R.id.tiezi_xiangxi_img);
			GetImageUtil.showImageDaTu(image1, imageView);
			layout.addView(view);
			setListener(imageView);
		}

		if (!TextUtils.isEmpty(image2)) {
			imageStrings.add(image2);
			view = getLayoutInflater().inflate(R.layout.tiezixiang_image_view,
					null);
			imageView = (ImageView) view.findViewById(R.id.tiezi_xiangxi_img);
			GetImageUtil.showImageDaTu(image2, imageView);
			layout.addView(view);
			setListener(imageView);
		}
		if (!TextUtils.isEmpty(image3)) {
			imageStrings.add(image3);
			view = getLayoutInflater().inflate(R.layout.tiezixiang_image_view,
					null);
			imageView = (ImageView) view.findViewById(R.id.tiezi_xiangxi_img);
			GetImageUtil.showImageDaTu(image3, imageView);
			layout.addView(view);
			setListener(imageView);
		}
		if (!TextUtils.isEmpty(image4)) {
			imageStrings.add(image4);
			view = getLayoutInflater().inflate(R.layout.tiezixiang_image_view,
					null);
			imageView = (ImageView) view.findViewById(R.id.tiezi_xiangxi_img);
			GetImageUtil.showImageDaTu(image4, imageView);
			layout.addView(view);
			setListener(imageView);
		}
		if (!TextUtils.isEmpty(image5)) {
			imageStrings.add(image5);
			view = getLayoutInflater().inflate(R.layout.tiezixiang_image_view,
					null);
			imageView = (ImageView) view.findViewById(R.id.tiezi_xiangxi_img);
			GetImageUtil.showImageDaTu(image5, imageView);
			layout.addView(view);
			setListener(imageView);
		}

	}

	private void setListener(ImageView imageView) {
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putStringArrayListExtra("images", imageStrings);
				intent.setClass(TieZiXiangXiActivity.this,
						ShowImageActivity.class);
				startActivity(intent);
			}
		});
	}

	private void updateView() {
		pListView.addHeaderView(convertView);
		if (adapter == null) {
			adapter = new HuiFuAdapter(this, huiTieData.getHuiTieBeans());
			pListView.setAdapter(adapter);
		} else {
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tiezi_xiangxi_hufu_btn:
			sendPunLun();
			break;

		default:
			break;
		}

	}

	private void sendPunLun() {
		final String data = editText.getText().toString();
		if (TextUtils.isEmpty(data)) {
			MyToast.showCustomerToast("评论不能为空");
			return;
		}
		Pdialog.showDialog(this, "提交数据中..", false);
		StringBuffer content = new StringBuffer("");
		UserBean userBean = MyData.getData().getUserBean();
		BDLocation bdLocation = MyData.bdLocation;
		content.append("uid=" + userBean.getUid());
		if (bdLocation != null) {
			content.append("&address=" + bdLocation.getCity()
					+ bdLocation.getDistrict() + bdLocation.getStreet());
		}
		content.append("&content=" + data).append("&tid=" + tieZiBean.getTid())
				.append("&type=" + TYPE_HUI_FU_LOU_ZHU);
		if (imagePaths != null && !imagePaths.isEmpty()) {
			content.append("&hasImg=" + HAS_IMAGE);
		}
		content.append("&yunatie="+tieZiBean.getContent());
		content.append("&buid="+tieZiBean.getUid());
		LogUtil.D("buidbuidbuidbuid: "+tieZiBean.getUid());
		File file = null;
		try {
			for (int i = 0; i < imagePaths.size(); i++) {
				LogUtil.D("imagePath22: "+imagePaths.get(i));
				switch (i) {
				case 0:
					file = new File(imagePaths.get(0));
					params.put("file", file);
					
					break;
				case 1:
					file = new File(imagePaths.get(1));
					params.put("file1", file);
					break;

				case 2:
					file = new File(imagePaths.get(2));
					params.put("file2", file);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
		}
		Request.addHuiTie(Constant.METHOD_ADD_HUIFU + content.toString(),
				params, new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.D("onFailure: " + error + content);
						MyToast.showCustomerToast("回复失败");
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: cccccccc " + response);
						try {
							int code = response.getInt("code");
							if (code == 0) {
								if (huiTieData.getHuiTieBeans() == null
										|| huiTieData.getHuiTieBeans().size() < 10) {
									HuiTieBean huiTieBean = new HuiTieBean();
									huiTieBean.setContent(data);
									if (imagePaths.size() > 0) {
										huiTieBean.setImage1(imagePaths.get(0));
									}
									if (imagePaths.size() > 1) {
										huiTieBean.setImage1(imagePaths.get(0));
										huiTieBean.setIamge2(imagePaths.get(1));
									}
									if (imagePaths.size() > 2) {
										huiTieBean.setImage1(imagePaths.get(0));
										huiTieBean.setIamge2(imagePaths.get(1));
										huiTieBean.setIamge3(imagePaths.get(2));
									}
									addHuiFu(huiTieBean);
								}

								MyToast.showCustomerToast("回复成功");
								sendBroadcast(pos, false);
								hidenHuiTie();
								destroyBitMap();
								removeAllImagePath();

							} else {
								MyToast.showCustomerToast("回复失败");
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						Pdialog.hiddenDialog();

					}
				});
	}

	private void sendBroadcast(int pos, boolean isZAN) {
		Intent intent = new Intent();
		if (isZAN) {
			if (type == Constant.NEW_TIE_ZI) {
				intent.setAction(Constant.NEW_ADD_ZAN);
			} else if (type == Constant.HOT_TIE_ZI) {
				intent.setAction(Constant.HOT_ADD_ZAN);
			} else if (type == Constant.SALE_TIE_ZI) {
				intent.setAction(Constant.SALE_ADD_ZAN);
			}
			intent.putExtra("tag", 1);
		} else {
			if (type == Constant.NEW_TIE_ZI) {
				intent.setAction(Constant.NEW_ADD_PL);
			} else if (type == Constant.HOT_TIE_ZI) {
				intent.setAction(Constant.HOT_ADD_PL);
			} else if (type == Constant.SALE_TIE_ZI) {
				intent.setAction(Constant.SALE_ADD_PL);
			}
			intent.putExtra("tag", 2);
		}
		intent.putExtra("pos", pos);
		sendBroadcast(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (biaoqingLayout.getVisibility() == View.VISIBLE) {
				hidenHuiTie();
				destroyBitMap();
				removeAllImagePath();
				hidenPop();
				return false;
			} else {
				finish();
				return true;
			}
		}
		return true;

	}

	private void removeAllImagePath() {
		if (imagePaths != null) {
			imagePaths.clear();
		}
		if (tupianLayout != null) {
			tupianLayout.removeAllViews();
		}

	}

	@Override
	public void getImage(SpannableString spannableString) {
		if (spannableString != null) {
			editText.append(spannableString);
		}
		hidenPop();
	}

	public void huiFu(HuiTieBean huiTieBean) {
		Intent intent = new Intent();
		intent.putExtra("data", huiTieBean);
		intent.setClass(this, HuiFuActivity.class);
		intent.putExtra("type", type);
		startActivity(intent);
	}

}
