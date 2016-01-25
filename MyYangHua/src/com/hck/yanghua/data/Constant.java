package com.hck.yanghua.data;

public class Constant {
	public static final String PASSWORD = "123456"; // 默认用户密码
	// public static final String MAINHOST =
	// "http://172.22.131.36:8080/MyYangHua/";
	public static final String MAINHOST = "http://218.244.150.164:8080/MyYangHua/";
	public static final String IMAGE_DATU_PATH = MAINHOST + "tiezi_images/";
	public static final String IMAGE_XIAOTU_PATH = MAINHOST
			+ "tiezi_images_xiaotu/";
	public static final String YANG_HUA_PATH = "/yanghua/yanghuaimg";
	public static final String CODE = "code";
	// 百度推送key
	public static final String BAIDU_PUSH_KEY = "MDdYlcDonFLf5OskpXNFpWB8";
	public static final String MAN = "男";
	public static final int NAN = 1;
	public static final int NV = 2;
	public static final String KEY_ISFATIEOK = "isFaTieOk";
	
	//Preferences
	public static final String PREFERENCES_KEY_MSG_ALL_SIZE="preferences_key_msg_all_size";
	public static final String PREFERENCES_KEY_MSG_LIAOTIAN_SIZE="preferences_key_msg_liaotian_size";
	public static final String PREFERENCES_KEY_MSG_HUIFU_SIZE="preferences_key_msg_huifu_size";
	public static final String PREFERENCES_KEY_MSG_TONGZHI_SIZE="preferences_key_msg_tz_size";
    
	//广播事件，有新的加好友信息
	public static final String MSG_INVENT = "new.msg.invitation.friend";
	
	// 广播事件,首尔最新帖子
	public static final String NEW_ADD_ZAN = "new.tiezi.add.zan";
	public static final String NEW_ADD_PL = "new.tiezi.add.pl";
	public static final String UPDATE_HOME_TIEZI_DATA = "new.tiezi.update.ui";

	// 广播事件,热门帖子
	public static final String UPDATE_HOT_TIEZI_DATA = "hot.tiezi.update.ui";
	public static final String HOT_ADD_ZAN = "hot.tiezi.add.zan";
	public static final String HOT_ADD_PL = "hot.tiezi.add.pl";

	// 广播事件,出售帖子
	public static final String UPDATE_SALE_TIEZI_DATA = "sale.tiezi.update.ui";
	public static final String SALE_ADD_ZAN = "sale.tiezi.add.zan";
	public static final String SALE_ADD_PL = "sale.tiezi.add.pl";
	
	public static final String HAS_NEW_MSG = "com.has.new.msg";
	
	public static final String CLEARN_NEW_MSG_SIZE = "com.clearn.msg.size";

	// 1,新帖，2热门排行帖子，3出售帖子
	public static final int NEW_TIE_ZI = 1;
	public static final int HOT_TIE_ZI = 2;
	public static final int SALE_TIE_ZI = 3;

	public static String zhengze = "hck0[0-9]{2}|hck10[0-7]";
	// 获取版本信息
	public static final String METHOD_GET_BANBEN = "getBanBenInfoP";
	// 用户登录
	public static final String METHOD_ADD_USER = "loginUser";
	// 增加用户pushid
	public static final String METHOD_ADD_USER_PUSHID = "addUserPushId";
	// 通过id获取用户信息
	public static final String METHOD_GET_USER_DATA = "getUser";

	// 增加帖子
	public static final String METHOD_ADD_TIEZI = "addTieZi?";

	// 获取帖子
	public static final String METHOD_GET_TIEZI = "getTieZi";
	// 增加回复
	public static final String METHOD_ADD_HUIFU = "addHuiTie?";
	// 增加赞
	public static final String METHOD_ADD_ZAN = "addZan";
	// 获取赞
	public static final String METHOD_GET_ZAN = "getZan";

	// 获取回帖
	public static final String METHOD_GET_HUITIE = "getHuiTie?";

	// 获取热帖，帖子按照评论排序
	public static final String METHOD_GET_GETHOTTIEZI = "getHotTieZi?";

	// 获取回复消息
	public static final String METHOD_GET_HUIFU_MSG = "getHuiFuMsg";
	
	//使用String uid获取用户
	public static final String METHOD_GET_USER_BY_STRING_ID="getUserByStringId";
	
	//添加好友
	public static final String METHOD_ADDFRIEND="addFriend";
	
	//获取好友
	public static final String METHOD_GETFRIEND="getFriend";
	
	//删除回复msg
	public static final String METHOD_DELETE_HUIFU_MSG="deleteMsg";
	
	
	//获取附近的人
	public static final String METHOD_GET_NEAR_USER="getNearUsers";
}
