package com.jingshuiqi.config;

public class Configure {
	private static String key = "你的商户的api秘钥";

	//小程序ID	
	private static String appID = "wx1923dcce28c19258";
	//商户号
	private static String mch_id = "1534649181";
	//
	private static String secret = "g789ewp5tygwengmsgfs9vmwrv4temq3";

	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		Configure.secret = secret;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static String getAppID() {
		return appID;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static String getMch_id() {
		return mch_id;
	}

	public static void setMch_id(String mch_id) {
		Configure.mch_id = mch_id;
	}

}
