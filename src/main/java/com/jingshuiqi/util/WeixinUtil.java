package com.jingshuiqi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import com.jingshuiqi.util.template.AccessToken;
import com.jingshuiqi.bean.Menu;
import com.jingshuiqi.bean.UserBase;
import com.jingshuiqi.util.http.HttpRequester;
import com.jingshuiqi.util.http.HttpRespons;
import com.jingshuiqi.util.pay.TrustAnyTrustManager;

//http://blog.csdn.net/lyq8479/article/details/9841371
//公众平台通用接口工具类
/**
 * requestUrl 请求地址 requestMethod 请求方法 outputStr 提交的数据
 * JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
 */

public class WeixinUtil {
	
	private static final Logger logger = Logger.getLogger("operation");  
	
	//测试                                                                 
	//public static final String appId = "wx0d84e4749e36cd4a";
	//public static final String appSecret = "6732b8ad290a02313b71c6ae57c3ac53";

	//正式
	public static final String appId = "wx9de51f80532538c9";
	public static final String appSecret = "35f0fe67baac92d4b5bca831d1c0adfb";
	public static final String mch_id = "1553291271";
	public static final String refundURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	public static final String WITHDRAW_URL= "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	//private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	public static String notify_url = "localhost";
	public static String key = "123456789987654321123456789";
	public static String partnerkey = "b56eun5v85945vn8945mv948504c584m";
	public static final String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESSTOKEN";
	public static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static final String code_access_token_url = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public static final String user_info_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public static final String refresh_access_token_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	private static final String template_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	public static final String CERT_PATH = "/usr/apiclient_cert.p12";
	public static final String mch_appid = "wx9de51f80532538c9";
	//客服消息url
	private static final String customer_service_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的accessToken
	 * @return 0 表示成功,其他值表示失败
	 */
	public static int createMenu(Menu menu, AccessToken accessToken) {
		int result = 0;
		String url = menu_create_url.replace("ACCESSTOKEN", accessToken.getToken());// 拼装创建菜单的url
		String jsonMenu = JSONObject.fromObject(menu).toString();// 将菜单对象转换成
																	// json 字符串
		logger.debug("传给微信的数据：" + jsonMenu);
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);// 调用接口创建菜单
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				logger.error("创建菜单失败 errcode:{} errmsg:{} " + jsonObject.getInt("errcode") + "  " + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}

	/**
	 * 只限本类使用的工具方法，获取AccessToken，防止过期
	 * 
	 *
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken accessToken =null; //tokenDao.getToken();
		
		if(accessToken==null){
			String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			// ===对返回值进行判断
			if (null != jsonObject) {
				try {
					accessToken = new AccessToken();
					accessToken.setToken(jsonObject.getString("access_token"));
					accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
				} catch (JSONException e) {
					accessToken = null;
					logger.error("获取token失败 errcode:{} errmsg:{}" +
							jsonObject.getInt("errcode") +
							jsonObject.getString("errmsg"));
				}
			}
			//tokenDao.setToken(accessToken);
		}
		return accessToken;
	}

	
	/**
	 * 只限本类使用的工具方法，获取AccessToken，防止过期
	 * 
	 * @param code
	 * @return accessToken
	 */
	public static AccessToken getCodeAccessToken(String code){
		AccessToken accessToken = null; //tokenDao.getToken();
		
		if(accessToken==null){
			String requestUrl = code_access_token_url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
			System.out.println(requestUrl);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			System.out.println(jsonObject);
			// ===对返回值进行判断
			if (null != jsonObject) {
				try {
					accessToken = new AccessToken();
					accessToken.setToken(jsonObject.getString("access_token"));
					accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
					accessToken.setOpenid(jsonObject.getString("openid"));
					accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
				} catch (JSONException e) {
					accessToken = null;
					logger.error("获取token失败 errcode:{} errmsg:{}" +
							jsonObject.getInt("errcode") +
							jsonObject.getString("errmsg"));
				}
			}
			//tokenDao.setToken(accessToken);
		}
		return accessToken;
	}
	
	/**
	 * 只限本类使用的工具方法，重新获取code
	 * 
	 * @param code
	 * @return accessToken
	 */
	public static AccessToken refreshCodeAccessToken(String code){
		AccessToken accessToken = null; //tokenDao.getToken();
		
		if(accessToken==null){
			String requestUrl = refresh_access_token_url.replace("APPID", appId).replace("REFRESH_TOKEN", code);
			System.out.println(requestUrl);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			System.out.println(jsonObject);
			// ===对返回值进行判断
			if (null != jsonObject) {
				try {
					accessToken = new AccessToken();
					accessToken.setToken(jsonObject.getString("access_token"));
					accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
					accessToken.setOpenid(jsonObject.getString("openid"));	
				} catch (JSONException e) {
					accessToken = null;
					logger.error("获取token失败 errcode:{} errmsg:{}" +
							jsonObject.getInt("errcode") +
							jsonObject.getString("errmsg"));
				}
			}
			//tokenDao.setToken(accessToken);
		}
		return accessToken;
	}
	
	/**
	 * 只限本类使用的工具方法，获取用户信息
	 * 
	 * @param accessToken
	 * @param openid
	 * @return userBase
	 */
	public static UserBase findUserBase(String accessToken, String openid){
		UserBase userBase = new UserBase();
		String requestUrl = user_info_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
		System.out.println(requestUrl);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		System.out.println(jsonObject);
		try {
			userBase.setOpenId(jsonObject.getString("openid"));
			userBase.setNickName(jsonObject.getString("nickname"));
			userBase.setIcon(jsonObject.getString("headimgurl"));
			userBase.setSex(jsonObject.getInt("sex"));
		} catch (Exception e) {
			logger.error("获取token失败 errcode:{} errmsg:{}" +
					jsonObject.getInt("errcode") +
					jsonObject.getString("errmsg"));
		}
		
		return userBase;
	}
	
	
	/**
	 * 工具方法:发起https请求并获取结果
	 * @param requestUrl
	 * @param requestMethod
	 * @param orderDate
	 * @return
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String orderDate) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象 并使用我们制定的信任管理器初始化
			TrustManager[] tm = { new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式
			httpUrlConn.setRequestMethod(requestMethod);
			httpUrlConn.connect();
			if ("GET".equalsIgnoreCase(requestMethod)) {

			} else if ("POST".equalsIgnoreCase(requestMethod)) {
				// 当有数据需要提交时
				if (null != orderDate) {
					OutputStream outputStream = httpUrlConn.getOutputStream();
					// 防止中文乱码
					outputStream.write(orderDate.getBytes("UTF-8"));
					outputStream.close();
				}
			}

			// 将返回的输入流转成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
			// //System.out.println("微信返回："+jsonObject);
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.");
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 使用模板消息想用户发送通知
	 * @param jsonMessage
	 *     json格式的消息字符串
	 * @return
	 */
	public static JSONObject msgToUser(String jsonMessage,AccessToken accessToken) {
		System.out.println("正在使用的accessToken======" + accessToken.getToken());
		String requestUrl = template_msg_url.replace("ACCESS_TOKEN",accessToken.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonMessage);
		return jsonObject;
	}

	/**
	 * 向客户发送客服消息
	 * @param jsonMessage  json格式的消息字符串
	 * @return
	 */
	public static JSONObject cusMsgToUser(String jsonMessage,String accessToken) {
		String requestUrl = customer_service_message_url.replace("ACCESS_TOKEN",accessToken);
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonMessage);
		return jsonObject;
	}
	
	
	
	public static void main(String[] args) {
		/*String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=QSajEzpPGm_eoCCkRPS5lnMjJ2D8cgfs6zEDrFsBeLZq1muQiru3zL4RXXx-xysJPZESfthMTCwekPy6BoH7KYhAHjWodhwMFaa5eqOm41s";
		String data1 = 
			"{\"touser\":\"o0bklt0qi6NtCnQdEU38RXhAldSg\"," +
			 "\"msgtype\":\"text\"," +
			 "\"text\":{\"content\":\"亲，您终于来啦~我们等的花儿都谢了[凋谢][偷笑]。授权位置信息，才能获得更多精准推荐哦。\\n" +
			 		    "\"}" +
		    "}";
		JSONObject jsonObj1 = httpRequest(reqUrl, "POST", data1);
		System.out.println(jsonObj1);*/
		
		
	}

	public static String getJsApiTicket(String accessToken) {
		 //获取token
           
        String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";  
		HttpRespons re1 = null;
		try {
			re1 = HttpRequester.sendPost(urlStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String content1 = re1.getContent();
        String ticket = (String) JSONObject.fromObject(content1).get("ticket");  
        return  ticket;  
	}

	public static String sendPayInfo(String urlString) throws Exception{
		
		//String json = "http://pay.jlon.net.cn/lcsw/pay/100/jspay";
		
		//访问准备
		URL url =new URL(urlString);
		//post参数
		Map<String,Object> params=new LinkedHashMap<>();
		
		//params.put("geomInfo","");
		//params.put("f","json");
		
		
		params.put("pay_ver", "100");
		params.put("pay_type", "010");
		params.put("service_id", "012");
		params.put("merchant_no", "855100111000002");
		params.put("terminal_id", "10097325");
		params.put("terminal_trace", "20155513201555132015551320155513");
		params.put("terminal_time", "20180411122033");
		params.put("total_fee", "25.0");
		params.put("key_sign", "ebec0aa1cd024f24bebd042d3d13dea6");
		
		
		
		//开始访问
		StringBuilder postData =new StringBuilder();
		for(Map.Entry<String,Object> param:params.entrySet()) {
			if(postData.length()!=0){
				 postData.append('&');
			}
		postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
		postData.append('=');
		postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type","application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		StringBuilder sb =new StringBuilder();
		for(int c; (c = in.read())>=0;){
			sb.append((char)c);
		}
		String response = sb.toString();
		System.out.println(response);
		return response;
	}
	
}
