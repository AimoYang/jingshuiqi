package com.jingshuiqi.util.query;

import java.util.HashMap;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * 获取物流信息
 * @author Administrator
 *
 */
public class QueryUtil {
	
	public static String getWuLiu(String com, String num) {
		String param = "{\"com\":\"" + com + "\",\"num\":\"" + num + "\",\"from\":\"\",\"to\":\"\",\"resultv2\":0}";
		String customer = "1E6DAEE0BF5D7141EC8320602DE6734A";
		String key = "xRdkBZvX7085";
		String resp = "";
		try {
			String sign = MD5.encode(param + key + customer);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("param", param);
			params.put("sign", sign);
			params.put("customer", customer);
			resp = HttpRequest.post("http://poll.kuaidi100.com/poll/query.do",
					params, true).body();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
			resp = "查询出错了。";
		}
		return resp;
	}
}
