package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.util.DateUtil;
import com.jingshuiqi.util.RandomNum;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.util.pay.PayUtil;
import com.jingshuiqi.util.pay.Sha1Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 15:34
 * @Description:
 */
@Service
public class OrdersService {

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

    public JsonResult saveOrderCheck(GoodsOrder goodsOrder) {

        goodsOrder.setOrderPrice((double)0);
        String orderUuid = RandomNum.getRandomFileName();
        goodsOrder.setUuid(orderUuid);
        goodsOrder.setPayUuid(orderUuid);
        List<GoodsOrderDetail> list = goodsOrder.getGoodsOrderDetails();
        try {
            for (GoodsOrderDetail goodsOrderDetail : list) {
                Sku sku = skuMapper.findSkuByUuid(goodsOrderDetail.getSkuUuid());
                if (sku.getStock() < goodsOrderDetail.getQuantity()) {
                    return ResultUtil.fail("库存不足");
                }

                Goods goods = goodsMapper.findGoodsInfoByUuid(goodsOrderDetail.getUuid());
                if (goods == null) {
                    return ResultUtil.fail("该商品失效");
                }

                String uuid = "Zi" + RandomNum.getRandomFileName();
                goodsOrderDetail.setThumb(goods.getThumb());
                goodsOrderDetail.setProductName(goods.getGoodsName());
                goodsOrderDetail.setPrice(sku.getNewPrice());
                goodsOrderDetail.setAmount(sku.getNewPrice() * goodsOrderDetail.getQuantity());
                goodsOrderDetail.setUuid(uuid);
                goodsOrderDetail.setOrderUuid(orderUuid);
                goodsOrderDetail.setProperties(sku.getProperties());
                goodsOrderDetail.setStatus(0);
                goodsOrder.setOrderPrice(goodsOrder.getOrderPrice() + goodsOrderDetail.getAmount());

            }
        }catch (Exception e){

        }
        return null;

           /* if (goodsOrder.get > 0) {
                int row = goodsCartDao.deleteGoodCart(productOrderDetail.getGoodsUuid(), productOrderDetail.getSkuId(), productOrder.getOpenId());
                if (row> 0) {
                    r.setResult(StatusCode.SUCCESS);
                    r.setMsg("OK");
                }else {
                    r.setResult(StatusCode.FAIL);
                    r.setMsg("清除购物车失败");
                    return r;
                }
            }
                productOrderDetailDao.insertSelective(productOrderDetail);

        } catch (Exception e) {
            e.printStackTrace();
            r.setResult(StatusCode.FAIL);
            r.setMsg("订单生成失败");
            return r;
        }
        productOrder.setOrderStatusPrefix(null);
        productOrder.setOrderPrice(ArithUtil.fun(productOrder.getOrderPrice()));
        productOrder.setActualPrice(productOrder.getOrderPrice());
        productOrder.setProductOrderDetails(list);
        productOrder.setCreateTime(new Date());

        if (productOrder.getDistributionType() == 0) {
            Address address = addressDao.selectByPrimaryKey(productOrder.getAddressId());
            Estate estate = estateDao.selectByPrimaryKey(address.getEstateId());
            productOrder.setNameSelf(address.getName());
            productOrder.setPhoneSelf(address.getPhone());
            productOrder.setAddress(estate.getEstateName()+address.getAddressDetail());
        }else {
            Shop shop = shopDao.selectByPrimaryKey(productOrder.getShopId());
            Kefu kefu = kefuDao.findKeFu(productOrder.getShopId());
            if (kefu != null) {
                if (kefu.getKefuPhone() != null) {
                    productOrder.setAddress(shop.getShopName() + "(联系电话："+ kefu.getKefuPhone() + ") - " + shop.getShopAdress());
                }
            }
        }
        int row = productOrderDao.findOrdersTime();
        productOrder.setTodayOn(row + 1);

        int rows = productOrderDao.insertSelective(productOrder);
        if (rows> 0) {
            r.setResult(StatusCode.SUCCESS);
            r.setMsg("OK");
        }else {
            r.setResult(StatusCode.FAIL);
            r.setMsg("生成订单失败");
            return r;
        }

        r.setResult(StatusCode.SUCCESS);
        r.setMsg("OK");
        r.setData(productOrder);
        return r;*/
    }


    public void updateGoodsNumber(String orderNum) {
    }
}
