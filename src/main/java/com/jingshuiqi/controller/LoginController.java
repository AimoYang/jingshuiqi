package com.jingshuiqi.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.LoginService;
import com.jingshuiqi.util.StatusCode;

@Api(tags = {"首页登录"})
@RestController
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@ApiOperation(value = "用户登录" , notes = "用户登录")
	@RequestMapping(value = "findUserInfoByCode" , method = RequestMethod.POST)
	public JsonResult findUserInfoByCode(@ApiParam(value = "用户的code") @RequestParam("code") String code){
		JsonResult r = new JsonResult();
		Map<String, Object> map = loginService.findUserInfoByCode(code);
		r.setResult(StatusCode.SUCCESS);
		r.setMsg("OK");
		r.setData(map);
		return r;
	}
	
	
	@ApiOperation(value = "用户再次登录" , notes = "用户再次登录")
	@RequestMapping(value = "findUserInfoByReCode" , method = RequestMethod.POST)
	public JsonResult findUserInfoByReCode(@ApiParam(value = "用户的登录uuid") @RequestParam("uuid") String uuid){
		JsonResult r = loginService.findUserInfoByReCode(uuid);
		return r;
	}
	

	@ApiOperation(value = "用户绑定" , notes = "用户绑定")
	@RequestMapping(value = "shareBind" , method = RequestMethod.POST)
	public JsonResult shareBind(@ApiParam(value = "用户的上级bindUuid") @RequestParam("bindUuid") String bindUuid, HttpServletRequest request){
		String token = request.getHeader("x-access-token");
		JsonResult r = loginService.shareBind(bindUuid ,token);
		return r;
	}
}
