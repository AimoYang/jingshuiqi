package com.jingshuiqi.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jingshuiqi.service.WeChatShareService;

@Api(tags = {"微信分享"})
@RestController
public class WeChatShareController {
	
	@Autowired
	private WeChatShareService weChatShareService;

	@ApiOperation(value = "微信分享" , notes = "微信分享")
	@RequestMapping(value = "WeChatShare" , method = RequestMethod.POST)
	public Map<String, Object> WeChatShare(@ApiParam(value = "分享链接") @RequestParam("url") String url ){
		 return weChatShareService.WeChatShare(url);
	}
}
