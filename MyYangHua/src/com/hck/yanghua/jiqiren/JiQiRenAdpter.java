package com.hck.yanghua.jiqiren;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.yanghua.R;
import com.hck.yanghua.bean.UserBean;
import com.hck.yanghua.data.MyData;
import com.hck.yanghua.util.MyTools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JiQiRenAdpter extends BaseAdapter {
	private List<JiQiRenMsgBean> liaoTianBeans;
	private LayoutInflater layoutInflater;
	private String userTxString;

	public JiQiRenAdpter(List<JiQiRenMsgBean> liaoTianBeans, Context context) {
		this.liaoTianBeans = liaoTianBeans;
		layoutInflater = LayoutInflater.from(context);
		UserBean userBean = MyData.getData().getUserBean();
		if (userBean != null) {
			userTxString = userBean.getTouxiang();
		}

	}

	@Override
	public int getCount() {
		return liaoTianBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return liaoTianBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder = new Holder();
		if (liaoTianBeans.get(arg0).getState() == JiQiRenMsgBean.JIESHOU) {
			arg1 = layoutInflater.inflate(R.layout.chatting_item_msg_text_left,
					null);
		} else {
			arg1 = layoutInflater.inflate(
					R.layout.chatting_item_msg_text_right, null);
			holder.imageView = (ImageView) arg1.findViewById(R.id.iv_userhead);
			ImageLoader.getInstance().displayImage(userTxString,
					holder.imageView, MyTools.getImageOptions(35));
		}
		holder.contenTextView = (TextView) arg1
				.findViewById(R.id.tv_chatcontent);
		holder.contenTextView.setText(Html.fromHtml(liaoTianBeans.get(arg0).getMessage()));
		return arg1;
	}

	static class Holder {
		public TextView contenTextView;
		public ImageView imageView;
	}

}
