package com.jingshuiqi.util.template;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jingshuiqi.bean.Template;
import com.jingshuiqi.util.WeixinUtil;

/**
 * 发送小程序模板消息
 * @author Administrator
 *
 */
public class SendMpTemplate {

	public static String sendTemplateMessage(String openId, Template template,
			AccessToken token , String formId) {
		try {
			if (openId == null) {
				return "nullDbUser";
			}
			String fromUserName = openId;
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("form_id", formId); //小程序模板消息formid
			dataMap.put("page", "pages/index/index");
			dataMap.put("touser", openId); // 是
			dataMap.put("template_id",
					"n8IcCeMfzGGFWwUtAbM_hkHDwXzxZFcYBHmACzCVbbo"); // 是模板id
			dataMap.put("emphasis_keyword", "提取码"); //小程序模板放大关键词	
			dataMap.put("keyword1", template.getCode());
			dataMap.put("keyword2", template.getDate());

			MessageTemplate msg_loc = new MessageTemplate();

			// 发送模板消息
			JSONObject jsonObj_1 = WeixinUtil.msgToUser(
					msg_loc.perTicketOk(dataMap, fromUserName), token);
			String resultString = (String) jsonObj_1.get("errmsg");
			System.out.println(resultString);
			if (!"ok".equals(resultString)) {
				WeixinUtil.msgToUser(
						msg_loc.perTicketOk(dataMap, fromUserName), token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}
}
