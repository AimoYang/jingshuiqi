package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.controller.OrdersController;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.form.ListId;
import com.jingshuiqi.util.ArithUtil;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.RandomNum;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.util.pay.PayUtil;
import com.jingshuiqi.util.pay.Sha1Util;
import com.jingshuiqi.vo.OrderGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 15:34
 * @Description:
 */
@Service
public class OrdersService {

    protected static final Logger logger = LoggerFactory
            .getLogger(OrdersController.class);

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsCartMapper goodsCartMapper;
    @Autowired
    private DoCommissionService doCommissionService;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private UserBaseMapper userBaseMapper;
    @Autowired
    private RecordRefundMapper recordRefundMapper;

    public JsonResult payOrder(String url, String uuid , HttpServletRequest request , HttpServletResponse response) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(uuid);
        goodsOrder.setGoodsOrderDetails(goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid()));
        goodsOrder.setPayType(0);
        if (goodsOrder.getOrderStatus() == 5) {
            return ResultUtil.fail("支付超时");
        }
        Map<String, Object> map = PayUtil.getPayForCharge(request, response, goodsOrder.getOpenId(), goodsOrder.getActualPrice().toString() , goodsOrder.getUuid());
        String ticket = accessTokenService.findTicket();
        String str = "jsapi_ticket="+ ticket +"&noncestr="+ map.get("nonceStr") +"&timestamp="+ map.get("timeStamp")+"&url="+url;
        String signature = Sha1Util.SHA1(str);
        map.put("signature", signature);
        map.put("payOrder", goodsOrder);
        return ResultUtil.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveOrderCheck(GoodsOrder goodsOrder) {

        goodsOrder.setOrderPrice((double)0);
        String orderUuid = RandomNum.getRandomFileName();
        goodsOrder.setUuid(orderUuid);
        goodsOrder.setPayUuid(orderUuid);
        Address address = addressMapper.selectByPrimaryKey(goodsOrder.getAddressId());
        goodsOrder.setReceiverAddress(address.getAddressDetail());
        goodsOrder.setReceiverName(address.getName());
        goodsOrder.setReceiverPhone(address.getPhone());
        Area area = areaMapper.findArea(address.getAddressArea());
        List<GoodsOrderDetail> list = goodsOrder.getGoodsOrderDetails();
        try {
            for (GoodsOrderDetail goodsOrderDetail : list) {
                System.out.print("1----------------------");
                Sku sku = skuMapper.findSkuByUuid(goodsOrderDetail.getSkuUuid());
                if (sku.getStock() < goodsOrderDetail.getQuantity()) {
                    return ResultUtil.fail("库存不足");
                }

                Goods goods = goodsMapper.findGoodsInfoByUuid(sku.getGoodsUuid());
                if (goods == null) {
                    return ResultUtil.fail("该商品失效");
                }

                String uuid = "Zi" + RandomNum.getRandomFileName();
                goodsOrderDetail.setThumb(goods.getThumb());
                goodsOrderDetail.setProductName(goods.getGoodsName());
                goodsOrderDetail.setPrice(sku.getNewPrice());
                goodsOrderDetail.setAmount(sku.getNewPrice() * goodsOrderDetail.getQuantity()-goodsOrderDetail.getDeduction());
                goodsOrderDetail.setUuid(uuid);
                goodsOrderDetail.setOrderUuid(orderUuid);
                goodsOrderDetail.setProperties(sku.getProperties());
                goodsOrderDetail.setStatus(0);
                goodsOrder.setOrderPrice(goodsOrder.getOrderPrice() + goodsOrderDetail.getAmount());
                goodsOrderDetailMapper.insertSelective(goodsOrderDetail);
                if (goodsOrder.getReserve() != null) {
                    int row = goodsCartMapper.deleteGoodCart(goodsOrderDetail.getGoodsUuid(), goodsOrderDetail.getSkuUuid(), goodsOrder.getOpenId());
                    if (row <= 0) {
                        return ResultUtil.fail("清除购物车失败");
                    }
                }
                doCommissionService.calculateCommission(uuid,goodsOrder.getOpenId(),sku.getId(),goodsOrderDetail.getQuantity(),area == null ? null : area.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("订单生成失败");
        }
        goodsOrder.setIsComment(0);
        goodsOrder.setOrderPrice(ArithUtil.fun(goodsOrder.getOrderPrice()));
        goodsOrder.setActualPrice(goodsOrder.getOrderPrice());
        goodsOrder.setGoodsOrderDetails(list);
        goodsOrder.setCreateTime(new Date());

        int rows = goodsOrderMapper.insertSelective(goodsOrder);
        if (rows <= 0) {
            return ResultUtil.fail("生成订单失败");
        }
        return ResultUtil.success(goodsOrder);
    }


    public void updateGoodsNumber(String orderNum) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(orderNum);
        List<GoodsOrderDetail> ordersDetailsList = goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid());
        goodsOrder.setOrderStatus(1);
        goodsOrder.setTradeSuccessfulTime(new Date());
        goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
        try {
            for (GoodsOrderDetail goodsOrderDetail : ordersDetailsList) {
                Goods goods = goodsMapper.findGoodsInfoByUuid(goodsOrderDetail.getGoodsUuid());
                if (goods == null) {
                    logger.info(new Date().toString() + "更新订单数据错误:" + orderNum);
                    return;
                }
                if (goods.getGoodsType() == 2){
                    return;
                }
                Sku sku = skuMapper.findSkuByUuid(goodsOrderDetail.getSkuUuid());
                if (sku == null){
                    logger.info(new Date().toString() + "更新订单数据错误:" + orderNum);
                    return;
                }
                sku.setStock(sku.getStock()-goodsOrderDetail.getQuantity());
                skuMapper.updateByPrimaryKeySelective(sku);

                GoodsWithBLOBs goodsWithBLOBs = new GoodsWithBLOBs();
                goodsWithBLOBs.setId(goods.getId());
                goodsWithBLOBs.setShowCount(goods.getShowCount() + goodsOrderDetail.getQuantity());
                goodsMapper.updateByPrimaryKeySelective(goodsWithBLOBs);
            }
        } catch (Exception e) {
            logger.info(new Date().toString() + "更新订单数据错误:" + orderNum);
        }
    }

    public JsonResult saveOrderShop(String token,ListId listId) {
        double freight = 0;
        UserBase userBase = userBaseMapper.findUserInfo(token);
        int deductionMax = userBase.getShopCoins();
        double allPrice = 0;
        List<OrderGoods> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>(3);
        for (Integer id : listId.getId()) {
            OrderGoods orderGoods = new OrderGoods();
            GoodsCart goodsCart = goodsCartMapper.selectByPrimaryKey(id);
            Goods goods = goodsMapper.findGoodsInfoByUuid(goodsCart.getGoodsUuid());
            BeanUtils.copyProperties(goods,orderGoods);
            orderGoods.setSku(skuMapper.findSkuByUuid(goodsCart.getSkuUuid()));
            orderGoods.setQuantity(goodsCart.getQuantity());
            allPrice = allPrice + (orderGoods.getQuantity()*orderGoods.getSku().getNewPrice());
            list.add(orderGoods);
        }
        map.put("list",list);
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getUuid() == list.get(i).getUuid())  {
                    list.remove(j);
                }
            }
        }
        for (OrderGoods orderGoods : list){
            freight = freight + orderGoods.getFreight();
        }
        map.put("freight",freight);
        map.put("deductionMax",deductionMax);
        map.put("allPrice",allPrice);
        return ResultUtil.success(map);
    }

    public JsonResult findOrderFor(String uuid) {
        if (uuid.startsWith("Zi")){
            GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMapper.selectByUuid(uuid);
            if (goodsOrderDetail.getIsRefund() == 1){
                RecordRefund recordRefund = recordRefundMapper.selectByOrderUuid(uuid);
                BeanUtils.copyProperties(recordRefund,goodsOrderDetail);
            }
            GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(goodsOrderDetail.getOrderUuid());
            goodsOrder.setGoodsOrderDetails(Arrays.asList(goodsOrderDetail));
            return ResultUtil.success(goodsOrder);
        }else {
            GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(uuid);
            goodsOrder.setGoodsOrderDetails(goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid()));
            return ResultUtil.success(goodsOrder);
        }
    }

    public JsonResult updateState(GoodsOrder goodsOrder) {
        GoodsOrder goodsOrder2 = goodsOrderMapper.findOrdersByUuid(goodsOrder.getUuid());
        if (goodsOrder2 == null){
            return ResultUtil.fail("订单失效");
        }
        if (goodsOrder.getOrderStatus() == 5){
            if (goodsOrder2.getOrderStatus() > 0){
                return ResultUtil.fail("订单已付款，无法取消");
            }else {
                goodsOrder2.setOrderStatus(5);
                goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder2);
                return ResultUtil.success();
            }
        }
        if (goodsOrder.getOrderStatus() == 3){

        }
        return ResultUtil.success();
    }

    public JsonResult findUserOrdersComment(PageObject pageObject) {
        Map<String, Object> map = new HashMap<>(2);
        int row = goodsOrderDetailMapper.getUserOrdersCommentRow(pageObject);
        List<GoodsOrderDetail> list = goodsOrderDetailMapper.findOrderCommentDetail(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findUserOrdersInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<>(2);
        if (pageObject.getId() == -1){
            pageObject.setId(null);
        }
        List<GoodsOrder> list = goodsOrderMapper.findUserOrdersInfo(pageObject);
        int row = goodsOrderMapper.getUserOrdersRow(pageObject);
        for (GoodsOrder goodsOrder : list) {
            goodsOrder.setGoodsOrderDetails(goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid()));
        }
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findCancelOrders(PageObject pageObject) {
        Map<String, Object> map = new HashMap<>(2);
        List<GoodsOrderDetail> list = goodsOrderDetailMapper.findCancelOrders(pageObject);
        int row = goodsOrderDetailMapper.getCancelOrdersRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult refundOrderState(RecordRefund recordRefund) {
        GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMapper.selectByUuid(recordRefund.getOrderUuid());
        if (goodsOrderDetail.getIsRefund() != 0){
            return ResultUtil.fail("订单已被处理");
        }
        GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(goodsOrderDetail.getOrderUuid());
        if (goodsOrder.getOrderStatus() != 1 && goodsOrder.getOrderStatus() != 2 && goodsOrder.getOrderStatus() != 6){
            return ResultUtil.fail("无法退单");
        }
        recordRefund.setIsSuccess(0);
        recordRefund.setCreateTime(new Date());
        goodsOrderDetail.setIsRefund(1);
        goodsOrderDetail.setStatus(1);
        try {
            recordRefundMapper.insertSelective(recordRefund);
            goodsOrderDetailMapper.updateByPrimaryKeySelective(goodsOrderDetail);
            int row = goodsOrderDetailMapper.findRefundOrderDetail(goodsOrder.getUuid());
            if (row > 0){
                goodsOrder.setOrderStatus(6);
            }else {
                goodsOrder.setOrderStatus(4);
            }
            goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("申请失败");
        }
        return ResultUtil.success();
    }
}
