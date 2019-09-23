package com.jingshuiqi.controller;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 17:21
 * @Description:
 */
@Api(tags = {"订单管理"})
@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @ApiOperation(value = "微信支付订单" , notes = "微信支付订单")
    @RequestMapping(value = "payOrder" , method = RequestMethod.POST)
    public JsonResult payOrder(@ApiParam(value = "订单的uuid") @RequestParam("uuid") String uuid , HttpServletRequest request , HttpServletResponse response ,String url){
        return ordersService.payOrder(url, uuid, request, response);
    }

    @ApiOperation(value = "添加订单" , notes = "添加订单")
    @RequestMapping(value = "saveOrderCheck" , method = RequestMethod.POST)
    public JsonResult saveOrderCheck(@ApiParam(value = "订单信息") @RequestBody GoodsOrder goodsOrder, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        goodsOrder.setOpenId(token);
        return ordersService.saveOrderCheck(goodsOrder);
    }

}
