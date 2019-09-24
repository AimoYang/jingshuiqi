package com.jingshuiqi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingshuiqi.service.CodeService;

@Api(tags = {"生成二维码"})
@RestController
public class WeChatCodeController {
	
	@Autowired
	private CodeService codeService;

	@ApiOperation(value = "生成二维码" , notes = "生成二维码")
	@RequestMapping(value = "WeChatCode" , method = RequestMethod.POST)
	public String WeChatCode(@ApiParam(value = "分享链接") @RequestParam("url") String url ){
		 return codeService.Code(url);
	}
	
	@ApiOperation(value = "url转Base64" , notes = "url转Base64")
	@RequestMapping(value = "WeCode" , method = RequestMethod.POST)
	public String WeCode(@ApiParam(value = "分享链接") @RequestParam("url") String url ){
		 return codeService.WeCode(url);
	}
}
