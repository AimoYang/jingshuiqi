package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.BannerService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 16:47
 * @Description:
 */
@Api(tags = {"广告信息"})
@RestController
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "查看广告绑定商品信息" , notes = "根据url查看广告绑定商品信息")
    @RequestMapping(value = "findBannerById" , method = RequestMethod.POST)
    public JsonResult findBannerById(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject){
        return bannerService.findBannerById(pageObject);
    }

    @ApiOperation(value = "查看首页楼层信息" , notes = "根据url查看首页楼层信息")
    @RequestMapping(value = "findIndexFloorInfo" , method = RequestMethod.POST)
    public JsonResult findIndexFloorInfo(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject){
        return bannerService.findIndexFloorInfo(pageObject);
    }

    @ApiOperation(value = "查看某个楼层的全部商品信息" , notes = "根据url查看某个楼层的全部商品信息")
    @RequestMapping(value = "findIndexFloorInfoById" , method = RequestMethod.POST)
    public JsonResult findIndexFloorInfoById(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject){
        return bannerService.findIndexFloorInfoById(pageObject);
    }

}
