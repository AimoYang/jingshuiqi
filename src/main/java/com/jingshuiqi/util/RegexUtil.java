package com.jingshuiqi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jingshuiqi.bean.JsonResult;


public class RegexUtil {

	public static JsonResult isPhone(String phone) {
		JsonResult result = new JsonResult();
		if(phone == null){
			result.setResult(StatusCode.FAIL);
			result.setMsg("手机号不能为空");
			return result;
		}
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	    	result.setResult(StatusCode.FAIL);
	    	result.setMsg("手机号应为11位数");
	        return result;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        if (!isMatch) {
	        	result.setResult(StatusCode.FAIL);
	        	result.setMsg("请填入正确的手机号");
	        }else {
				result.setResult(StatusCode.SUCCESS);
				result.setMsg("ok");
			}
	    }
	    return result;
	}
}
