package com.jingshuiqi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingshuiqi.util.WeixinUtil;
import com.jingshuiqi.util.pay.Ticket;
import com.jingshuiqi.util.template.AccessToken;

@Service
public class AccessTokenService {

	@Autowired
	private RedisService redisService;
	
	//判断缓存中是否有accessToken，若有返回，若无重新获取
	public String findAccessToken (){
		boolean flag = redisService.exists("accessToken");
		if (!flag) {
			return refreshToken();
		}
		String token = (String)redisService.get("accessToken");
		return token;
	}
	
	//获取accessToken，将accessToken存入缓存,返回accessToken
	private String refreshToken(){
		AccessToken accessToken = WeixinUtil.getAccessToken();
		redisService.set("accessToken", accessToken.getToken(), (long)accessToken.getExpiresIn());
		return accessToken.getToken();
	}
	
	public String findTicket() {
		boolean flag = redisService.exists("ticket");
		if (!flag) {
			return refreshTicket();
		}
		String ticket = (String)redisService.get("ticket");
		return ticket;
	}
	
	private String refreshTicket(){
		String  ticket = Ticket.getTicket(findAccessToken());
		redisService.set("ticket", ticket , (long)7198);
		return ticket;
	}
}
