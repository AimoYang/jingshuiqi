package com.jingshuiqi.service;

import com.alibaba.fastjson.JSONObject;
import com.jingshuiqi.bean.*;
import com.jingshuiqi.controller.OrdersController;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.form.ListId;
import com.jingshuiqi.util.*;
import com.jingshuiqi.util.pay.PayUtil;
import com.jingshuiqi.util.pay.Sha1Util;
import com.jingshuiqi.util.template.MessageTemplate;
import com.jingshuiqi.vo.OrderGoods;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 15:34
 * @Description:
 */
@Slf4j
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
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private CommissionMapper commissionMapper;
    @Autowired
    private ExchangeCodeMapper exchangeCodeMapper;
    @Autowired
    private LogisticsMapper logisticsMapper;
    @Autowired
    private OrderCommissionMapper orderCommissionMapper;
    @Autowired
    private AgentCommissionMapper agentCommissionMapper;
    @Autowired
    private ShopCoinsMapper shopCoinsMapper;
    @Autowired
    private CustomService customService;

    public JsonResult payOrder(String url, String uuid , HttpServletRequest request , HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(uuid);
        goodsOrder.setGoodsOrderDetails(goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid()));
        if (goodsOrder.getOrderStatus() == 5) {
            return ResultUtil.fail("支付超时");
        }
        map.put("payOrder", goodsOrder);
        if (goodsOrder.getOrderType() == 1){
            if (goodsOrder.getActualPrice() == 0){
                if (goodsOrder.getFreight() == 0){
                    updateGoodsNumber(goodsOrder.getUuid());
                    return ResultUtil.order(map);
                }
            }
        }
        map = PayUtil.getPayForCharge(request, response, goodsOrder.getOpenId(), goodsOrder.getActualPrice().toString() , goodsOrder.getUuid());
        String ticket = accessTokenService.findTicket();
        String str = "jsapi_ticket="+ ticket +"&noncestr="+ map.get("nonceStr") +"&timestamp="+ map.get("timeStamp")+"&url="+url;
        String signature = Sha1Util.SHA1(str);
        map.put("signature", signature);
        return ResultUtil.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveOrderCheck(GoodsOrder goodsOrder) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderUuid = RandomNum.getRandomFileName();

        goodsOrder.setOrderPrice((double)0);
        goodsOrder.setAllCoins(0);
        goodsOrder.setUuid(orderUuid);
        goodsOrder.setPayUuid(orderUuid);
        goodsOrder.setFreight(0);

        UserBase userBase = userBaseMapper.findUserInfo(goodsOrder.getOpenId());
        Address address = addressMapper.selectByPrimaryKey(goodsOrder.getAddressId());
        goodsOrder.setReceiverAddress(address.getAddressDetail());
        goodsOrder.setReceiverName(address.getName());
        goodsOrder.setReceiverPhone(address.getPhone());
        goodsOrder.setAddressArea(address.getAddressArea());
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
                if (goods.getGoodsType() == 1){
                    ExchangeCode exchangeCode = exchangeCodeMapper.findCode(goodsOrder.getCode(),goodsOrderDetail.getGoodsUuid());
                    if (exchangeCode == null){
                        return ResultUtil.fail("兑换码错误");
                    }else if (exchangeCode.getCodeType() == 0){
                        exchangeCodeMapper.updateCode(goodsOrder.getCode(),goodsOrderDetail.getGoodsUuid());
                    }else if (exchangeCode.getCodeType() == 4){
                        return ResultUtil.fail("兑换码正在使用中");
                    }else {
                        return ResultUtil.fail("兑换码错误");
                    }
                }
                goodsOrderDetail.setPrice(sku.getNewPrice());
                goodsOrderDetail.setAmount(sku.getNewPrice() * goodsOrderDetail.getQuantity()-goodsOrderDetail.getDeduction());

                String uuid = "Zi" + RandomNum.getRandomFileName();
                goodsOrderDetail.setThumb(goods.getThumb());
                goodsOrderDetail.setProductName(goods.getGoodsName());
                goodsOrderDetail.setUuid(uuid);
                goodsOrderDetail.setOrderUuid(orderUuid);
                goodsOrderDetail.setProperties(sku.getProperties());
                goodsOrderDetail.setStatus(0);
                goodsOrder.setAllCoins(goodsOrder.getAllCoins() + goodsOrderDetail.getDeduction());
                goodsOrder.setOrderPrice(goodsOrder.getOrderPrice() + goodsOrderDetail.getAmount());
                goodsOrder.setFreight(goodsOrder.getFreight() + goods.getFreight().intValue());
                goodsOrderDetailMapper.insertSelective(goodsOrderDetail);
                if (goodsOrder.getReserve() == "true") {
                    int row = goodsCartMapper.deleteGoodCart(goodsOrderDetail.getGoodsUuid(), goodsOrderDetail.getSkuUuid(), goodsOrder.getOpenId());
                    if (row <= 0) {
                        return ResultUtil.fail("清除购物车失败");
                    }
                }
                doCommissionService.calculateCommission(uuid,goodsOrder.getOpenId(),sku.getId(),goodsOrderDetail.getQuantity(),area == null ? null : area.getId());
                OrderCommission orderCommission = orderCommissionMapper.findCommission(goodsOrderDetail.getUuid());
                if (orderCommission.getParentOpenId() != null){
                    customService.content(orderCommission.getParentOpenId() , "亲，您的好友下了一笔订单"+"\n"+"\n"+"昵称: ("+userBase.getNickName()+")"+"\n"+"商品名称: ("+goodsOrderDetail.getProductName()+")"+"\n"+"时间: "+df.format(new Date())+"\n"+"\n"+"支付完成后，您将获得佣金" );
                }
            }
            goodsOrder.setIsComment(0);
            goodsOrder.setOrderPrice(ArithUtil.fun(goodsOrder.getOrderPrice()) + goodsOrder.getFreight());
            goodsOrder.setActualPrice(goodsOrder.getOrderPrice() - goodsOrder.getAllCoins());
            goodsOrder.setGoodsOrderDetails(list);
            goodsOrder.setCreateTime(new Date());

            int rows = goodsOrderMapper.insertSelective(goodsOrder);
            if (rows <= 0) {
                return ResultUtil.fail("生成订单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("订单生成失败");
        }

        return ResultUtil.success(goodsOrder);
    }


    public void updateGoodsNumber(String orderNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GoodsOrder goodsOrder = goodsOrderMapper.findOrdersByUuid(orderNum);
        List<GoodsOrderDetail> ordersDetailsList = goodsOrderDetailMapper.findOrderDetail(goodsOrder.getUuid());
        goodsOrder.setOrderStatus(1);
        goodsOrder.setTradeSuccessfulTime(new Date());
        goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
        if (goodsOrder.getOrderType() == 1){
            if (goodsOrder.getCode() == null){
                logger.info(new Date().toString()+"-----"+"兑换码为空"+"---"+orderNum);
            }else {
                int row = exchangeCodeMapper.reCode(goodsOrder.getCode(),new Date());
                if (row <= 0){
                    logger.info(new Date().toString()+"-----"+"兑换码更新失败"+"---"+orderNum);
                }
            }
        }
        if (goodsOrder.getOrderType() == 2){
            UserBase userBase = new UserBase();
            userBase.setUserType(1);
            userBase.setOpenId(goodsOrder.getOpenId());
            int row = userBaseMapper.updateUserInfo(userBase);
            if (row <= 0){
                logger.info("更新代理状态失败1"+"---" + orderNum);
            }
            Agent agent = agentMapper.findAgent(goodsOrder.getOpenId());
            if (agent == null){
                Agent agent2 = new Agent();
                agent2.setAgentType(1);
                agent2.setCreateTime(new Date());
                agent2.setOpenId(goodsOrder.getOpenId());
                int rows = agentMapper.insertSelective(agent2);
                if (rows <= 0){
                    logger.info("更新代理状态失败2"+"---"+orderNum);
                }
                customService.content(goodsOrder.getOpenId() , df.format(new Date()) + " : " + "您已成为金牌代理" );
            }
            Commission commission = commissionMapper.selectByOpenId(goodsOrder.getOpenId());
            if (commission == null){
                Commission commission2 = new Commission();
                commission2.setOpenId(goodsOrder.getOpenId());
                int ro = commissionMapper.insertSelective(commission);
                if (ro <= 0){
                    logger.info("更新代理状态失败3"+"---"+orderNum);
                }
            }
        }
        try {
            Integer coins = 0;
            String goodsName = "";
            int i = 0;
            UserBase usr = userBaseMapper.findUserInfo(goodsOrder.getOpenId());
            for (GoodsOrderDetail goodsOrderDetail : ordersDetailsList) {
                Goods goods = goodsMapper.findGoodsInfoByUuid(goodsOrderDetail.getGoodsUuid());
                if (goods == null) {
                    logger.info(new Date().toString() + "更新订单数据错误:" + orderNum);
                    return;
                }
                if (i == 0){
                    goodsName = goodsName + goods.getGoodsName();
                }else {
                    goodsName.concat(","+goods.getGoodsName());
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
                coins = coins + goodsOrderDetail.getDeduction();

                i++;
            }
            customService.content(goodsOrder.getOpenId() , "亲，您的微信支付 -"+goodsOrder.getActualPrice()+"元"+"\n"+"来源:  【微信支付】"+"\n"+"支付:  【支付成功】"+"\n"+"时间: "+df.format(new Date()));
            usr.setOpenId(goodsOrder.getOpenId());
            usr.setConsumeMoney(usr.getConsumeMoney()+goodsOrder.getActualPrice());
            usr.setShopCoins(usr.getShopCoins() - coins);
            userBaseMapper.updateUserInfo(usr);

            Map<String ,String> map = new HashMap<String ,String>();
            map.put("template_id",  "ZCsyf4P9ls2acIvUzwKDypddFWnwTR6rDLn3xukxvAs");
            map.put("url", "http://weixin.jinshanzhu.com");
            map.put("first", "恭喜你购买成功！");
            map.put("keyword1", goodsOrder.getUuid());
            map.put("keyword2", goodsName);
            map.put("keyword3", goodsOrder.getActualPrice().toString());
            map.put("keyword4", "微信支付");
            map.put("keyword5", df.format(new Date()));
            map.put("remark", "欢迎再次购买！");
            MessageTemplate messageTemplate = new MessageTemplate();
            String template = messageTemplate.perTicketOk(map,goodsOrder.getOpenId());
            net.sf.json.JSONObject json = WeixinUtil.msgToUser(template,WeixinUtil.getAccessToken());
            System.out.println(json);

        } catch (Exception e) {
            logger.info(new Date().toString() + "更新订单数据错误:" + orderNum);
        }
        return;
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
            freight = freight + orderGoods.getFreight();
            allPrice = allPrice + (orderGoods.getQuantity()*orderGoods.getSku().getNewPrice());
            list.add(orderGoods);
        }
        map.put("list",list);
        /*for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getUuid() == list.get(i).getUuid())  {
                    list.remove(j);
                }
            }
        }
        for (OrderGoods orderGoods : list){
            freight = freight + orderGoods.getFreight();
        }*/
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
                goodsOrderDetail.setRefundFee(recordRefund.getRefundFee());
                goodsOrderDetail.setRefundNum(recordRefund.getRefundNum());
                goodsOrderDetail.setRefundReason(recordRefund.getRefundReason());
                goodsOrderDetail.setRefuseReason(recordRefund.getRefuseReason());
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

    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateState(GoodsOrder goodsOrder) {
        if (goodsOrder.getIsDelete() != null){
            goodsOrderMapper.deleteOrder(goodsOrder);
            return ResultUtil.success();
        }
        try {
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
                int row = goodsOrderDetailMapper.findRefundNowOrder(goodsOrder.getUuid());
                if (row > 0){
                    return ResultUtil.fail("还有订单正在退款，不能确认");
                }
                goodsOrder2.setOrderStatus(3);
                goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder2);
                ShopCoins shopCoins = new ShopCoins();
                shopCoins.setCreateTime(new Date());
                shopCoins.setOpenId(goodsOrder2.getOpenId());
                shopCoins.setCoin(goodsOrder2.getAllCoins());
                shopCoins.setType(1);
                shopCoinsMapper.insertSelective(shopCoins);
                userBaseMapper.updateAddCoins(goodsOrder2.getAllCoins(),goodsOrder2.getOpenId());
                return OrderCommissionBeing(goodsOrder2.getUuid(),goodsOrder2.getOpenId());
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("操作失败");
        }
        return ResultUtil.fail("操作无效");
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult OrderCommissionBeing(String uuid, String openId){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserBase usr = userBaseMapper.findUserInfo(openId);
        List<GoodsOrderDetail> goodsOrderDetails = goodsOrderDetailMapper.findOrderDetail(uuid);
        try {
            for (GoodsOrderDetail g : goodsOrderDetails) {
                OrderCommission orderCommission = orderCommissionMapper.findCommission(g.getUuid());
                if (orderCommission != null){
                    if (orderCommission.getParentOpenId() != null){
                        Agent agent = agentMapper.findAgent(orderCommission.getParentOpenId());
                        commissionMapper.updateIncome(orderCommission.getParentOpenId(),orderCommission.getActualParentCommission());
                        customService.content(orderCommission.getParentOpenId() , "亲，您的佣金 +"+orderCommission.getActualParentCommission()+"\n"+"来源:  【支付成功】"+"\n"+"身份:  【"+getAgent(agent.getAgentType())+"】"+"\n"+"顾客: "+usr.getNickName()+"\n"+"性别: "+getSex(usr.getSex())+"\n"+"时间: "+df.format(new Date()));
                    }
                    if (orderCommission.getGrandpaOpenId() != null){
                        Agent agent2 = agentMapper.findAgent(orderCommission.getGrandpaOpenId());
                        commissionMapper.updateIncome(orderCommission.getGrandpaOpenId(),orderCommission.getActualGrandpaCommission());
                        customService.content(orderCommission.getParentOpenId() , "亲，您的佣金 +"+orderCommission.getActualParentCommission()+"\n"+"来源:  【支付成功】"+"\n"+"身份:  【"+getAgent(agent2.getAgentType())+"】"+"\n"+"顾客: "+usr.getNickName()+"\n"+"性别: "+getSex(usr.getSex())+"\n"+"时间: "+df.format(new Date()));
                    }
                    orderCommission.setIsSuccess((short)1);
                    orderCommission.setDoType((short)1);
                    orderCommission.setDoTime(new Date());
                    orderCommissionMapper.updateByPrimaryKey(orderCommission);
                }
                List<AgentCommission> list = agentCommissionMapper.findAgentCommission(g.getUuid());
                if (list.size() != 0) {
                    for (AgentCommission a : list) {
                        if (a.getType() == 2){
                            Agent agent = agentMapper.findAgent(a.getAgentOpenId());
                            commissionMapper.updateCity(a.getAgentOpenId(),a.getActualCommission());
                            customService.content(orderCommission.getParentOpenId() , "亲，您的佣金 +"+orderCommission.getActualParentCommission()+"\n"+"来源:  【支付成功】"+"\n"+"身份:  【"+getAgent(agent.getAgentType())+"】"+"\n"+"顾客: "+usr.getNickName()+"\n"+"性别: "+getSex(usr.getSex())+"\n"+"时间: "+df.format(new Date()));
                        }
                        if (a.getType() == 3){
                            Agent agent = agentMapper.findAgent(a.getAgentOpenId());
                            commissionMapper.updateTeam(a.getAgentOpenId(),a.getActualCommission());
                            customService.content(orderCommission.getParentOpenId() , "亲，您的佣金 +"+orderCommission.getActualParentCommission()+"\n"+"来源:  【支付成功】"+"\n"+"身份:  【"+getAgent(agent.getAgentType())+"】"+"\n"+"顾客: "+usr.getNickName()+"\n"+"性别: "+getSex(usr.getSex())+"\n"+"时间: "+df.format(new Date()));
                        }
                        a.setIsSuccess((short)1);
                        a.setDoType((short)1);
                        a.setDoTime(new Date());
                        agentCommissionMapper.updateByPrimaryKey(a);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info(new Date().toString() +"--------" + "更新分佣数据错误:" + uuid);
            return ResultUtil.fail("操作失败");
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
        if (goodsOrderDetail.getQuantity().equals(recordRefund.getRefundNum())){
            recordRefund.setDeduction(goodsOrderDetail.getDeduction());
        }else {
            recordRefund.setDeduction(0);
        }
        if (recordRefund.getRefundFee() != goodsOrderDetail.getAmount()*recordRefund.getRefundNum()/goodsOrderDetail.getQuantity()){
            return ResultUtil.fail("退款价格异常");
        }
        recordRefund.setUuid("Tu"+RandomNum.getRandomFileName());
        recordRefund.setIsSuccess(0);
        recordRefund.setCreateTime(new Date());
        goodsOrderDetail.setIsRefund(1);
        goodsOrderDetail.setStatus(1);
        try {
            recordRefundMapper.insertSelective(recordRefund);
            goodsOrderDetailMapper.updateByPrimaryKeySelective(goodsOrderDetail);
            goodsOrder.setOrderStatusPrefix(goodsOrder.getOrderStatus());
            int row = goodsOrderDetailMapper.findRefundOrderDetail(goodsOrder.getUuid());
            if (row > 0){
                if (goodsOrder.getOrderStatus() == 2){
                    goodsOrder.setOrderStatus(6);
                }
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

    public JsonResult findIndexOrderFor() {
        List<UserBase> list = userBaseMapper.findIndexOrderFor();
        return ResultUtil.success(list);
    }

    public JsonResult findUserOrdersDelivery(String uuid) throws Exception {
        Logistics logistics = logisticsMapper.findDelivery(uuid);
        if (logistics == null){
            ResultUtil.fail("查询无订单");
        }
        String result = KD100Util.getExpressageKey(logistics.getExpressageCom(), logistics.getExpressageId());
        if(result.equals("查询出错")){
            return new  JsonResult(StatusCode.FAIL, "查询出错", "");
        }else {
            JSONObject jsonObject = JSONObject.parseObject(result);
            return new  JsonResult(StatusCode.SUCCESS, "ok", jsonObject);
        }
    }

    private String getAgent(Integer id){

        String agent = "";
        switch (id){
            case 1: agent = "金牌代理";break;
            case 2: agent = "城市代理";break;
            case 3: agent = "总裁代理";break;
            default:
        }
        return agent;
    }

    private String getSex(Integer id){

        String sex = "";
        switch (id){
            case 1: sex = "男";break;
            case 2: sex = "女";break;
            default: sex = "未知";break;
        }
        return sex;
    }

}
