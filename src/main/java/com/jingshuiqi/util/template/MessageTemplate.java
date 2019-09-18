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
				+ "\"page\":\"" + dataMap.get("page") + "\","
				+ "\"form_id\":\"" + dataMap.get("form_id") + "\","
				+ "\"data\":{"
				
				+ "\"keyword1\": {\"value\":\"" 
				+ dataMap.get("keyword1") + "\"}," 
				
				+ "\"keyword2\": {\"value\":\""
				+ dataMap.get("keyword2") + "\"}" + "},"
				+ "\"emphasis_keyword\":\"" + dataMap.get("emphasis_keyword") + "\""
				+ "}";
        logger.info ("template is : " + template);
		return template;
	}

}
