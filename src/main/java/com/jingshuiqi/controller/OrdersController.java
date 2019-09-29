package com.jingshuiqi.controller;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.bean.GoodsOrderDetail;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.bean.RecordRefund;
import com.jingshuiqi.form.ListId;
import com.jingshuiqi.resubmit.DuplicateSubmitToken;
import com.jingshuiqi.service.OrdersService;
import com.jingshuiqi.util.PageObject;
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
    @RequestMapping(value = "/pay/payall" , method = RequestMethod.POST)
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

    @ApiOperation(value = "添加购物车订单" , notes = "添加购物车订单")
    @RequestMapping(value = "saveOrderShop" , method = RequestMethod.POST)
    public JsonResult saveOrderShop(@ApiParam(value = "购物车id的list") @RequestBody ListId listId,HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return ordersService.saveOrderShop(token,listId);
    }

    @ApiOperation(value = "查看订单详情信息" , notes = "查看订单详情信息")
    @RequestMapping(value = "findOrderFor" , method = RequestMethod.POST)
    public JsonResult findOrderFor(@ApiParam(value = "总订单的uuid")@RequestParam("uuid") String uuid ,HttpServletRequest request){
        return ordersService.findOrderFor(uuid);
    }

    @ApiOperation(value = "查看首页订单信息" , notes = "查看首页订单信息")
    @RequestMapping(value = "findIndexOrderFor" , method = RequestMethod.GET)
    public JsonResult findIndexOrderFor(){
        return ordersService.findIndexOrderFor();
    }

    @ApiOperation(value = "更新订单状态", notes = "更新订单状态")
    @RequestMapping(value = "updateOrderState", method = RequestMethod.POST)
    public JsonResult updateOrderState(
            @ApiParam("订单uuid,state") @RequestBody GoodsOrder goodsOrder, HttpServletRequest request) {
        String token = request.getHeader("x-access-token");
        goodsOrder.setOpenId(token);
        return ordersService.updateState(goodsOrder);
    }

    @ApiOperation(value = "申请订单退款", notes = "申请订单退款")
    @RequestMapping(value = "refundOrderState", method = RequestMethod.POST)
    public JsonResult refundOrderState(
            @ApiParam("订单uuid,state") @RequestBody RecordRefund recordRefund, HttpServletRequest request) {
        return ordersService.refundOrderState(recordRefund);
    }

    @ApiOperation(value = "查看我的各个状态商品订单" , notes = "根据url查看我的各个状态商品订单")
    @RequestMapping(value = "findUserOrdersInfo" , method = RequestMethod.POST)
    public JsonResult findUserOrdersInfo(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return ordersService.findUserOrdersInfo(pageObject);
    }

    @ApiOperation(value = "查看我的待评价商品订单" , notes = "根据url查看我的待评价商品订单")
    @RequestMapping(value = "findUserOrdersComment" , method = RequestMethod.POST)
    public JsonResult findUserOrdersComment(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return ordersService.findUserOrdersComment(pageObject);
    }

    @ApiOperation(value = "查看我的已取消商品订单" , notes = "根据url查看我的已取消商品订单")
    @RequestMapping(value = "findCancelOrders" , method = RequestMethod.POST)
    public JsonResult findCancelOrders(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return ordersService.findCancelOrders(pageObject);
    }

    @ApiOperation(value = "查看我的商品订单快递" , notes = "根据url查看我的商品订单快递")
    @RequestMapping(value = "findUserOrdersDelivery" , method = RequestMethod.POST)
    public JsonResult findUserOrdersDelivery(@ApiParam(value = "总订单的uuid")@RequestParam("uuid") String uuid ,HttpServletRequest request) throws Exception {
        return ordersService.findUserOrdersDelivery(uuid);
    }

}
