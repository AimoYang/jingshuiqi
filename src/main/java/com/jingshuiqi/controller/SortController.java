package com.jingshuiqi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.SortService;

@Api(tags = {"分类信息"})
@RestController
public class SortController {
	
	@Autowired
	private SortService sortService;
	
	@ApiOperation(value = "查看一级分类信息" , notes = "根据url查看一级分类信息")
	@RequestMapping(value = "findOneSortInfo" , method = RequestMethod.POST)
	public JsonResult findOneSortInfo(){
		JsonResult r = sortService.findOneSortInfo();
		return r;
	}
}
