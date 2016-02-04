package com.hck.yanghua.ui;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.hck.yanghua.net.Request;
import com.hck.yanghua.util.ExpressionUtil;
import com.hck.yanghua.util.GetImageUtil;
import com.hck.yanghua.util.JsonUtils;
import com.hck.yanghua.util.LogUtil;
import com.hck.yanghua.util.MyToast;
import com.hck.yanghua.util.MyTools;
import com.hck.yanghua.util.ShareUtil;
import com.hck.yanghua.util.TimeUtil;
import com.hck.yanghua.view.AlertDialog;
import com.hck.yanghua.view.MyEditextView;
import com.hck.yanghua.view.MyGridView;
import com.hck.yanghua.view.MyGridView.GetBiaoQingCallBack;
import com.hck.yanghua.view.Pdialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TieZiXiangXiActivity extends BaseTitleActivity implements
		android.view.View.OnClickListener, GetBiaoQingCallBack {
	public static final int JING_HUA = 1; // 精华
	public static final int HUO = 20; // 火
	public static final int TUIJIAN = 1; // 推荐
	public static final int TYPE_HUI_FU_LOU_ZHU = 1; // 回复楼主
	public static final int TYPE_HUI_FU_USER = 2; // 回复用户
	private static final String HAS_IMAGE = "1";
	private static final int TYPE_ZAN = 10; // 赞
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
	private MyEditextView editText;
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
	private static final String IMAGEFILE = "/yanghua/";
	ArrayList<String> listfile = new ArrayList<String>();
	private LinearLayout tupianLayout;
	public static TieZiXiangXiActivity tieZiXiangXiActivity;
	private int page = 1;
	private HuiTieData huiTieData = new HuiTieData();
	private int pos;
	private int type; // 1一般帖子 2出售帖子
	private boolean isUpdateing;
	private boolean isDataOver;
	private View foodView;
	private ImageView footerImageView;
	private MyGridView gridView;

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
		Pdialog.showDialog(this, "加载数据中..", false);
		params = new RequestParams();
		params.put("tid", tId + "");
		params.put("page", page + "");
		Request.getHuiTie(Constant.METHOD_GET_HUITIE, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.D("onFailure: " + content);
						MyToast.showCustomerToast("获取评论失败");
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						LogUtil.D("onSuccess: getHuiTie" + response.toString());
						try {
							HuiTieData huiTieData1 = JsonUtils.parse(
									response.toString(), HuiTieData.class);
							if (huiTieData1 != null
									&& huiTieData1.getHuiTieBeans() != null) {
								huiTieData = huiTieData1;
							}
						} catch (Exception e) {
							e.printStackTrace();
							pListView.removeFooterView(foodView);
							isDataOver = true;
						}
						updateView();

					}

					@Override
					public void onFinish(String url) {
						super.onFinish(url);
						isUpdateing = false;
						foodView.setVisibility(View.GONE);
						Pdialog.hiddenDialog();
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
						zanImageView.setEnabled(false);

					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);

						try {
							zanData = JsonUtils.parse(response.toString(),
									ZanData.class);

						} catch (Exception e) {
						}
						zanImageView.setEnabled(true);
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
		UserBean userBean = MyData.getData().getUserBean();
		zanLayout.setVisibility(View.VISIBLE);
		ImageView imageView = null;
		List<ZanBean> zanBean = zanData.getZanBean();
		if (zanBean.size() > 0) {
			zanSizeTextView.setText(zanBean.size() + "人赞过");
			for (int i = 0; i < zanBean.size(); i++) {
				if (userBean.getUid() == zanBean.get(i).getUid()) {
					zanImageView.setEnabled(false); // 用户已赞过，则不能再赞
				}
				if (i > 15) {
					continue;
				}
				View view = LayoutInflater.from(this).inflate(
						R.layout.zan_img_item, null);
				imageView = (ImageView) view.findViewById(R.id.zan_img);
				imageView.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(
						zanBean.get(i).getImage(), imageView,
						MyTools.getImageOptions(45));
				imageView.setTag(zanData.getZanBean().get(i).getUid());
				setOnClickTxListener(imageView);
				zanLayout.addView(view);
			}
		} else {
			zanLayout.setVisibility(View.GONE);
		}

	}

	private void setOnClickTxListener(View view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("uid", (Long) v.getTag());
				intent.setClass(TieZiXiangXiActivity.this,
						ShowOneUserActivity.class);
				startActivity(intent);
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				MyToast.showCustomerToast("分享成功");
			}
		};
	};

	private void setListener() {
		righButton.setText("分享");
		righButton.setVisibility(View.VISIBLE);
		righButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareUtil.share(TieZiXiangXiActivity.this,
						tieZiBean.getContent(),
						(String[]) imagePaths.toArray(new String[imagePaths.size()]), handler);
			}
		});
		zanImageView.setOnClickListener(new OnClickListener() {
			/**
			 * String yuantie=getStringData("yuantie"); long
			 * beiZanUserId=getLongData("buid"); int type=getIntData("type");
			 */
			@Override
			public void onClick(View v) {
				try {
					params = new RequestParams();
					params.put("id", tId + "");
					params.put("img", MyData.getData().getUserBean()
							.getTouxiang());
					params.put("yuantie", tieZiBean.getContent());
					params.put("buid", tieZiBean.getUid() + "");
					params.put("type", Constant.TYPE_ZAN + "");
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
				return false;
			}
		});
		pListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& !isUpdateing && totalItemCount >= 14 && !isDataOver) {
					isUpdateing = true;
					page++;
					foodView.setVisibility(View.VISIBLE);
					startAnimation();
					getHuiTie();
				}
			}

		});

	}

	private void startAnimation() {
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				TieZiXiangXiActivity.this, R.anim.loading_animation);
		footerImageView.startAnimation(hyperspaceJumpAnimation);
	}

	private static final int GET_PHOTO = 1;
	public static final int GET_PICTER = 2;
	public static final int PINGLUN_OK = 3;
	private ArrayList<String> imagePaths = new ArrayList<>();
	private List<Bitmap> bitmaps = new ArrayList<>();

	private ArrayList<String> mSelectPath;

	// 弹出选择图片界面
	public void choicePicter(View view) {
		hidenPop();
		if (imagePaths.size() >= 3) {
			MyToast.showCustomerToast("最多3张图，点击可删除");
			return;
		}
		int imageSize = imagePaths.size();

		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
				3 - imageSize);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_MULTI);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.D("requestCode: " + requestCode + ": " + requestCode);
		if (requestCode == PINGLUN_OK) {

			HuiTieBean huiTieBean = new HuiTieBean();
			huiTieBean.setName(data.getStringExtra("name"));
			huiTieBean.setYuantie(data.getStringExtra("yt"));
			huiTieBean.setContent(data.getStringExtra("content"));
			addHuiFu(huiTieBean);

		}
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				for (String imgPath : mSelectPath) {
					Bitmap bitmap = MyTools.getSmallBitmap2(imgPath);
					if (bitmap != null) {
						addImagePath(imgPath);
						showImages(bitmap, imgPath);
					}
					bitmaps.add(bitmap);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

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
			if (huiTieBean1.getImage1() != null) {
				huiTieBean.setImage1(huiTieBean1.getImage1());
			}
			if (huiTieBean1.getIamge2() != null) {
				huiTieBean.setIamge2(huiTieBean1.getIamge2());
			}
			if (huiTieBean1.getIamge3() != null) {
				huiTieBean.setIamge3(huiTieBean1.getIamge3());
			}
			huiTieBean.setHuifuUserName(huiTieBean1.getName());
			huiTieBean.setYuantie(huiTieBean1.getYuantie());
			huiTieBean.setContent(huiTieBean1.getContent());
			huiTieBean.setBenDi(true);
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
		tupianView.setVisibility(View.VISIBLE);
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

	// 弹出选择表情界面
	public void showPopChiceImage(View view) {
		tupianView.setVisibility(View.GONE);
		if (gridView.getVisibility() == View.VISIBLE) {
			gridView.setVisibility(View.GONE);
		} else {
			gridView.setVisibility(View.VISIBLE);

		}
	}

	// 隐藏选择表情界面
	private void hidenPop() {
		gridView.setVisibility(View.GONE);
		if (imagePaths.size() > 0) {
			tupianView.setVisibility(View.VISIBLE);
		}
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
		editText = (MyEditextView) findViewById(R.id.tiezi_huifu);
		biaoqingLayout = (LinearLayout) findViewById(R.id.huifu_biaoqing_lay);
		zanImageView = (ImageView) findViewById(R.id.huifu_zan);
		zanImageView.setEnabled(false);
		tupianLayout = (LinearLayout) findViewById(R.id.HuiTie_img);
		tupianView = findViewById(R.id.huiFu_ScrollView);
		foodView = LayoutInflater.from(this).inflate(R.layout.list_item_foot,
				null);
		footerImageView = (ImageView) foodView.findViewById(R.id.img);
		gridView = (MyGridView) findViewById(R.id.fatie_gridview);
		gridView.setGetBiaoQingCallBackListener(this);

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
		fensiTextView.setText("粉丝:" + tieZiBean.getFensi());
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
		if (TextUtils.isEmpty(tieZiBean.getHuifuTiem())) {
			timeTextView.setText(tieZiBean.getTime());
		} else {
			timeTextView.setText(TimeUtil.forTime(tieZiBean.getHuifuTiem()));
		}

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

	public void onclickImg(HuiTieBean huiTieBean) {
		ArrayList<String> dataArrayList = new ArrayList<>();
		if (!TextUtils.isEmpty(huiTieBean.getImage1())) {
			dataArrayList.add(huiTieBean.getImage1());
		}
		if (!TextUtils.isEmpty(huiTieBean.getIamge2())) {
			dataArrayList.add(huiTieBean.getIamge2());
		}
		if (!TextUtils.isEmpty(huiTieBean.getIamge3())) {
			dataArrayList.add(huiTieBean.getIamge3());
		}

		Intent intent = new Intent();
		intent.putExtra("isBenDi", huiTieBean.isBenDi());
		intent.putStringArrayListExtra("images", dataArrayList);
		intent.setClass(TieZiXiangXiActivity.this, ShowImageActivity.class);
		startActivity(intent);
	}

	private void updateView() {
		pListView.addHeaderView(convertView);
		pListView.addFooterView(foodView);
		foodView.setVisibility(View.GONE);
		if (adapter == null) {
			adapter = new HuiFuAdapter(this, huiTieData.getHuiTieBeans());
			pListView.setAdapter(adapter);
		} else {
			if (huiTieData != null && huiTieData.getHuiTieBeans() != null
					&& !huiTieData.getHuiTieBeans().isEmpty()) {
				adapter.updateView(huiTieData.getHuiTieBeans());
			}
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
		content.append("&yunatie=" + tieZiBean.getContent());
		content.append("&buid=" + tieZiBean.getUid());
		File file = null;
		try {
			for (int i = 0; i < imagePaths.size(); i++) {
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

								HuiTieBean huiTieBean = new HuiTieBean();
								huiTieBean.setBenDi(true);
								huiTieBean.setContent(data);
								LogUtil.D("imagesize: " + imagePaths.size());

								for (int i = 0; i < imagePaths.size(); i++) {
									if (i == 0) {
										String img1 = imagePaths.get(0);
										huiTieBean.setImage1(img1);
									} else if (i == 1) {
										String img1 = imagePaths.get(1);
										huiTieBean.setIamge2(img1);
									} else if (i == 2) {
										String img1 = imagePaths.get(2);
										huiTieBean.setIamge3(img1);
									}
								}

								addHuiFu(huiTieBean);

								MyToast.showCustomerToast("回复成功");
								if (pos > 0) {
									sendBroadcast(pos, false);
								}
								hidenHuiTie();
								destroyBitMap();
								removeAllImagePath();

							} else {
								MyToast.showCustomerToast("回复失败");
							}
						} catch (Exception e) {
							LogUtil.D("eeeeeeeeeeeeeeeeee: " + e.toString());
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
		if (pos > 0) {
			intent.putExtra("pos", pos);
		}

		sendBroadcast(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (gridView.getVisibility() == View.VISIBLE) {
				LogUtil.D("111111111111");
				hidenPop();
				return false;
			} else if (tupianView.getVisibility() == View.VISIBLE) {
				tupianView.setVisibility(View.GONE);
				LogUtil.D("22222222222222");
				return false;
			} else {
				LogUtil.D("3333333333333");
				hidenHuiTie();
				destroyBitMap();
				removeAllImagePath();
				hidenPop();
				finish();
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

	private HuiTieBean huiTieBean;

	public void huiFu(HuiTieBean huiTieBean) {
		this.huiTieBean = huiTieBean;
		alertD();
	}

	private void startShowUserActivity() {
		Intent intent = new Intent();
		intent.putExtra("data", huiTieBean);
		intent.setClass(this, HuiFuActivity.class);
		intent.putExtra("type", type);
		startActivity(intent);
	}

	public void alertD() {
		AlertDialog alertDialog = new AlertDialog(this);
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.showAlert("选择你的操作", "回复帖子", "开启聊天", "举报帖子");
		alertDialog.setOnBottomListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		alertDialog.setOnTopListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startShowUserActivity();
			}
		});

		alertDialog.setOnCenterListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
	}

	@Override
	public void getBiaoQing(SpannableString data) {
		if (data != null) {
			editText.append(data);
		}
		hidenPop();
	}

}
