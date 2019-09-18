package com.jingshuiqi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jingshuiqi.util.pay.Sha1Util;
import com.jingshuiqi.util.pay.TenpayUtil;

@Service
public class WeChatShareService {
	
	@Autowired
	private AccessTokenService accessTokenService;

	public Map<String, Object> WeChatShare(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		String timestamp = Sha1Util.getTimeStamp();
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		String ticket = accessTokenService.findTicket();
		String str = "jsapi_ticket="+ ticket +"&noncestr="+ strReq +"&timestamp="+ timestamp +"&url="+url;
		System.out.println(str);
		String signature = Sha1Util.SHA1(str);
		System.out.println(signature);
		map.put("timestamp", timestamp);
		map.put("nonceStr", strReq);
		map.put("signature", signature);
		return map;
	}

	
}
