package com.jingshuiqi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingshuiqi.bean.Message;
import com.jingshuiqi.bean.TestMessage;
import com.jingshuiqi.dao.MessageMapper;
import com.jingshuiqi.util.WeixinUtil;

@Service
public class CustomService {
	
	@Autowired
	private AccessTokenService accessTokenService;
	@Autowired
	private MessageMapper messageDao;
	
	protected static final Logger logger = LoggerFactory.getLogger(CustomService.class);

	public void content(String openid, String text){
		//获得accessToken
		String accessToken = accessTokenService.findAccessToken();
		TestMessage testMessage = new TestMessage();
		//设置消息的类型
		testMessage.setMsgtype("text");
		//发送的openid
		testMessage.setTouser(openid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", text);
		testMessage.setText(map);
		String jsonMessage = JSONObject.toJSONString(testMessage);
		net.sf.json.JSONObject jsonObject = WeixinUtil.cusMsgToUser(jsonMessage, accessToken);
		Message message = new Message();
		message.setOpenid(openid);
		message.setMeTime(new Date());
		message.setMessage(text);
		int row = messageDao.insertSelective(message);
		if (row <= 0 || 0 != jsonObject.getInt("errcode")){
				logger.error("信息未保存成功：" +"Time:"+ (new Date()).toString() +"------"+ text );
		}
		System.out.println(jsonObject);
	}
	
}
