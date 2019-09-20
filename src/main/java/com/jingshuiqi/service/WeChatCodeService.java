package com.jingshuiqi.service;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jingshuiqi.util.WeixinUtil;

@Service
public class WeChatCodeService {
	
	@Autowired
	private AccessTokenService accessTokenService;

	public String CreateWeChatCode(String uuid) throws Exception{
		String accessToken = accessTokenService.findAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
		String sceneStr = uuid;
		String params = "{\"expire_seconds\":600, \"action_name\":\"QR_STR_SCENE\", \"action_info\":{\"scene\":{\"scene_str\":\"" + sceneStr + "\"}}}";
		JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", params);
		System.out.println(jsonObject);
		if (jsonObject.getString("ticket") != null) {
			String qrcodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + jsonObject.getString("ticket");
			System.out.println(qrcodeUrl);
		}
		return jsonObject.getString("url");
	}
	
}
