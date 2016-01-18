package com.hck.yanghua.data;

public class Constant {

	public static final String MAINHOST = "http://172.22.131.36:8080/MyYangHua/";
	// public static final String MAINHOST =
	// "http://192.168.1.102:8080/MyYangHua/";
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
}
