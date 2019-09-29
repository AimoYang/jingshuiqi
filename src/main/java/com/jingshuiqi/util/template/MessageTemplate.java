package com.jingshuiqi.util.template;

import java.util.Map;

import org.apache.log4j.Logger;

public class MessageTemplate {
	private static final Logger logger = Logger.getLogger("operation");  
	
	/**
	 * 订票成功通知
	 * 
	 * @param dataMap
	 * @return
	 */
	
	
	public String perTicketOk(Map<String, String> dataMap, String openId) {
		String template = "{" + "\"touser\":\"" + openId + "\","
				+ "\"template_id\":\"" + dataMap.get("template_id") + "\","
				+ "\"url\":\"" + dataMap.get("url") + "\","
				+ "\"data\":{"

				+ "\"first\": {\"value\":\""
				+ dataMap.get("first") + "\"," +
				"\"color\":\"#173177\"" +
				"},"

				+ "\"keyword1\": {\"value\":\"" 
				+ dataMap.get("keyword1") + "\"," +
				"\"color\":\"#173177\"" +
				"},"

				+ "\"keyword2\": {\"value\":\""
				+ dataMap.get("keyword2") + "\"," +
				"\"color\":\"#173177\"" +
				"},"

				+ "\"keyword3\": {\"value\":\""
				+ dataMap.get("keyword3") + "\"," +
				"\"color\":\"#173177\"" +
				"},"

				+ "\"keyword4\": {\"value\":\""
				+ dataMap.get("keyword4") + "\"," +
				"\"color\":\"#173177\"" +
				"},"

				+ "\"keyword5\": {\"value\":\""
				+ dataMap.get("keyword5") + "\"," +
				"\"color\":\"#173177\"" +
				"},"
				
				+ "\"remark\": {\"value\":\""
				+ dataMap.get("remark") + "\"," +
				"\"color\":\"#173177\"" +
				"}" + "}"
				+ "}";
        logger.info ("template is : " + template);
		return template;
	}

}
