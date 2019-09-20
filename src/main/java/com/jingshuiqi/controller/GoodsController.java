package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.GoodsService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/19 0019 18:32
 * @Description:
 */
@Api(tags = {"商品信息"})
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "查看商品信息" , notes = "根据url查看商品信息")
    @RequestMapping(value = "findGoodsInfo" , method = RequestMethod.POST)
    public JsonResult findGoodsInfo(@ApiParam(value = "商品的uuid") @RequestParam("uuid") String uuid, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        JsonResult r = goodsService.findGoodsInfo(uuid ,token);
        return r;
    }

    @ApiOperation(value = "取消/收藏商品" , notes = "根据url取消/收藏商品")
    @RequestMapping(value = "updateCollectInfo" , method = RequestMethod.POST)
    public JsonResult updateCollectInfo(@ApiParam(value = "商品的uuid") @RequestParam("uuid") String uuid, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        JsonResult r = goodsService.updateCollectInfo(uuid ,token);
        return r;
    }

    @ApiOperation(value = "查看我的收藏商品" , notes = "根据url查看我的收藏商品")
    @RequestMapping(value = "findCollectInfo" , method = RequestMethod.POST)
    public JsonResult findCollectInfo(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return goodsService.findCollectInfo(pageObject);
    }

}
