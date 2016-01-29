package com.hck.yanghua.jiqiren;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.yanghua.R;
import com.hck.yanghua.ui.BaseTitleActivity;

/**
 * 
 * @author 本例子来自：http://myapptg.com 更多资源请访问该网站
 * 
 */
public class JiqirenMainActivity extends BaseTitleActivity {

	private ListView listView;  //显示聊天信息
	private JiQiRenAdpter adpter;
	private List<JiQiRenMsgBean> liaoTianBeans;  //存放所有聊天数据的集合
	private EditText editText;
	private Button sendButton;
	private static final String API = "http://www.tuling123.com/openapi/api";//api地址
	private static final String KEY_STRING = "0a247cbe5ada92bfdc2a1125092e55fc";
	private static final String INIT_DATA="主人您好，我是无所不知的小可可，我可以讲笑话，比如你可以输入讲个笑话，我还可以讲故事，和你聊天，帮您解答问题，您可以输入各种花名，我可以为您介绍它们哦";
	
	private String sendMessage;   //你自己发送的单条信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiqiren);
		initTitleView("小可机器人");
		liaoTianBeans = new ArrayList<JiQiRenMsgBean>();
		initViews(); //初始化view和adpter
		setListener(); //绑定监听事件
		initData(); // 进入界面，随机显示机器人的欢迎信息
	}

	private void initData() {
      
		showData(INIT_DATA); //用随机数获取我们内置的信息，用户进入界面，机器人的首次聊天信息

	}

	public void initViews() { //这个不解释了
		listView = (ListView) findViewById(R.id.list);
		adpter = new JiQiRenAdpter(liaoTianBeans, this);
		editText = (EditText) findViewById(R.id.huihu_huifu);
		sendButton = (Button) findViewById(R.id.btn_send);
		listView.setAdapter(adpter);

	}

	public void setListener() {
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendData(); //点击发送按钮，触发
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void sendData() {
		sendMessage = editText.getText().toString(); //获取你输入的信息
		if (TextUtils.isEmpty(sendMessage)) { //判断是否为空
			Toast.makeText(this, "您还未输任何信息哦",Toast.LENGTH_LONG).show();
			return;
		}
		editText.setText("");
		sendMessage = sendMessage.replaceAll(" ", "").replaceAll("\n", "")
				.trim(); //替换空格和换行
		//LiaoTianBean是一个实体类，
		//封装我们发送的信息，然后加入集合，更新listview
		JiQiRenMsgBean liaoTianBean = new JiQiRenMsgBean(); 
		liaoTianBean.setMessage(sendMessage);
		liaoTianBean.setState(1); //1标示自己发送的信息
		liaoTianBeans.add(liaoTianBean); //把自己发送的信息，加入集合 
		adpter.notifyDataSetChanged(); //通知listview更新
		getDataFromServer(); //从服务器获取返回到额数据，机器人的信息
	}

	public void getDataFromServer() {
		RequestParams params = new RequestParams();//放置我们需要传递的数据
		params.put("key", KEY_STRING);
		params.put("info", sendMessage);
		new HCKHttpClient().get(API, params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) { //获取数据失败调用
				super.onFailure(error, content);
				showData("主人，您的网络有问题哦");
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {//获取数据成功调用
				super.onSuccess(statusCode, response);

				paresData(response);
			}

			@Override
			public void onFinish(String url) {//请求完成调用
				super.onFinish(url);

			}
		});
	}


	private void paresData(JSONObject jb) {  //解析返回的json数据
		Log.d("hck", "paresDataparesData: "+jb.toString());
		try {
			String content = jb.getString("text"); //获取的机器人信息
			int code = jb.getInt("code");//服务器状态码
			updateView(code, content); //更新界面
		} catch (JSONException e) {
			e.printStackTrace();
			showData("主人，你的网络不好哦");

		}

	}

	private void showData(String message) {
		//和上面一样，创建一个LiaoTianBean对象，传入数据，在把它放进集合
		JiQiRenMsgBean liaoTianBean = new JiQiRenMsgBean();
		liaoTianBean.setMessage(message);
		liaoTianBean.setState(2); //这里2标示机器人的信息，用于listview的adpter，显示不同的界面
		liaoTianBeans.add(liaoTianBean);
		adpter.notifyDataSetChanged();

	}

	private void updateView(int code, String content) {
		switch (code) {
		case 4004:
			showData("主人今天我累了，我要休息了，明天再来找我耍吧");
			break;
		case 40005:
			showData("主人，我听不懂你在说什么哦");
			break;
		case 40006:
			showData("主人，我今天闭关修炼哦，占不接客啦");
			break;
		case 40007:
			showData("主人，明天再和你耍啦，我吃坏肚子了，呜呜。。。");
			break;
		default:
			showData(content);
			break;
		}
	}

}
