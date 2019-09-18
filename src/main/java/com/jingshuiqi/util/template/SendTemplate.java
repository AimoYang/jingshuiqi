package com.jingshuiqi.util.template;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import com.jingshuiqi.bean.Template;
import com.jingshuiqi.util.template.AccessToken;
import com.jingshuiqi.util.template.MessageTemplate;
import com.jingshuiqi.util.WeixinUtil;

public class SendTemplate {
	
	public static String sendTemplateInfo(String openId, Template template ,AccessToken token) {
		try {
			if (openId == null) {
				return "nullDbUser";
			}
			String fromUserName = openId;
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("appid", WeixinUtil.appId);
			dataMap.put("pagepath", "pages/index/index");
			dataMap.put("touser", openId); // 是
			dataMap.put("template_id",
					"n8IcCeMfzGGFWwUtAbM_hkHDwXzxZFcYBHmACzCVbbo"); // 是模板id
			/*
			 * dataMap.put("url",
			 * "http://www.fada518.com/punch/punch!toMyCourse.action?courseId="
			 * +courseId);
			 */// 模板跳转链接
			dataMap.put("first", "提取码");
			dataMap.put("keyword1", template.getCode());
			dataMap.put("keyword2", template.getDate());
			dataMap.put("remark", "详情请点击");
			/*
			 * logger.info("当前店家名称："+message.getSellerName());
			 * logger.info("当前时间："+message.getCostTime());
			 */
			// dataMap.put("picurl",
			// "http://47.95.239.19/ "+message.getImgUrl()==null?"":message.getImgUrl());
			// dataMap.put("content", message.getContent());

			MessageTemplate msg_loc = new MessageTemplate();

			// 发送模板消息
			JSONObject jsonObj_1 = WeixinUtil.msgToUser(
					msg_loc.perTicketOk(dataMap, fromUserName), token);
			// logger.info ("发送模板消息结果：" + jsonObj_1.toString());

			// JSONObject jsonObj_2 =
			// WeixinUtil.cusMsgToUser(msg_loc.textMsg(dataMap, fromUserName),
			// token);// 发送消息
			// JSONObject jsonObj_3 =
			// WeixinUtil.cusMsgToUser(msg_loc.imgMsg(dataMap,fromUserName,token.getToken()),
			// token);// 发送消息
			String resultString = (String) jsonObj_1.get("errmsg");
			System.out.println(resultString);
			if (!"ok".equals(resultString)) {
				WeixinUtil.msgToUser(
						msg_loc.perTicketOk(dataMap, fromUserName),
						token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ok";

	}
}
