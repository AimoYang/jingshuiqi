package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.dto.*;
import com.jingshuiqi.util.ArithUtil;
import com.jingshuiqi.util.RandomNum;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author Administrator
 * @create 2019/9/20 0020 11:05
 */
@Service
public class DoCommissionService {

    @Autowired
    private CommissionMapper commissionMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private OrderCommissionMapper orderCommissionMapper;
    @Autowired
    private AgentCommissionMapper agentCommissionMapper;
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private UserBaseMapper userBaseMapper;
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private RecordRefundMapper recordRefundMapper;
    /**
     * 计算佣金
     * @param openId 用户openid
     * @param skuId  规格id
     * @param quantity 数量
     * @param areaId 地区id
     * @return
     */
    public boolean calculateCommission(String orderDetailUuid, String openId,Integer skuId,Integer quantity,Integer areaId){
        System.out.print("3----------------------");
        Sku sku = skuMapper.selectByPrimaryKey(skuId);
        if(sku == null){
            return false;
        }
        Date now = new Date();
        OrderCommission orderCommission = new OrderCommission();
        //一级分佣
        Share share = shareMapper.findUserInfoForBind(openId);
        if(share != null && share.getIsDelete() == 0 && share.getParentOpenId() != null && !"".equals(share.getParentOpenId())){
            //父级openId
            String parentOpenId = share.getParentOpenId();
            orderCommission.setParentOpenId(parentOpenId);
            Agent parentAgent = agentMapper.selectByOpenId(parentOpenId);
            if(parentAgent == null){
                orderCommission.setParentCommission((double)0);
                orderCommission.setActualParentCommission((double)0);
            }else {
                if(openId.equals(parentOpenId)){
                    orderCommission.setParentCommission((double)0);
                    orderCommission.setActualParentCommission((double)0);
                }else {
                    //父级分佣
                    Double parentCommission = ArithUtil.mul(sku.getOneCommission(),quantity);
                    orderCommission.setParentCommission(parentCommission);
                    orderCommission.setActualParentCommission(parentCommission);
                }
            }
            //二级返佣
            Share share1 = shareMapper.findUserInfoForBind(parentOpenId);
            if(share1 != null && share1.getIsDelete() == 0  && share1.getParentOpenId() != null && !"".equals(share1.getParentOpenId())){
                //父父级openId
                String grandpaOpenId = share1.getParentOpenId();
                //父父级分佣
                orderCommission.setGrandpaOpenId(grandpaOpenId);
                Agent grandpaAgent = agentMapper.selectByOpenId(grandpaOpenId);
                if(grandpaAgent == null){
                    orderCommission.setGrandpaCommission((double)0);
                    orderCommission.setActualGrandpaCommission((double)0);
                }else {
                    if(openId.equals(grandpaOpenId)){
                        orderCommission.setGrandpaCommission((double)0);
                        orderCommission.setActualGrandpaCommission((double)0);
                    }else {
                        Double grandpaCommission = ArithUtil.mul(sku.getTwoCommission(),quantity);
                        orderCommission.setGrandpaCommission(grandpaCommission);
                        orderCommission.setActualGrandpaCommission(grandpaCommission);
                    }
                }
            }
            orderCommission.setOrderDetailUuid(orderDetailUuid);
            orderCommission.setCreateTime(now);
            orderCommission.setIsSuccess((short)0);
            //添加sql
            System.out.println("orderCommission ==================="+orderCommission.toString());
            orderCommissionMapper.insertSelective(orderCommission);
        }
        List<UserAgent> list = userBaseMapper.selectUserAgent();
        Map<String,String> map = new HashMap<>();
        //获取总裁代理
        String topOpenId = acquireTopOpenId(openId,list,map);
        if(topOpenId != null){
            AgentCommission agentCommission = new AgentCommission();
            agentCommission.setAgentOpenId(topOpenId);
            Double commission = ArithUtil.mul(sku.getTopCommission(),quantity);
            agentCommission.setCommission(commission);
            agentCommission.setActualCommission(commission);
            agentCommission.setCreateTime(now);
            agentCommission.setType(3);
            agentCommission.setIsSuccess((short)0);
            agentCommission.setOrderDetailUuid(orderDetailUuid);
            System.out.println("agentCommission总裁代理 ==================="+agentCommission.toString());
            //添加sql
            agentCommissionMapper.insertSelective(agentCommission);
        }
        if(areaId != null){
            //获取地区代理
            Agent agent = agentMapper.selectByAreaId(areaId);
            if(agent != null){
                AgentCommission agentCommission = new AgentCommission();
                agentCommission.setAgentOpenId(agent.getOpenId());
                Double commission = ArithUtil.mul(sku.getCityCommission(),quantity);
                agentCommission.setCommission(commission);
                agentCommission.setActualCommission(commission);
                agentCommission.setCreateTime(now);
                agentCommission.setType(2);
                agentCommission.setIsSuccess((short)0);
                agentCommission.setOrderDetailUuid(orderDetailUuid);
                System.out.println("agentCommission  地区代理 ==================="+agentCommission.toString());
                //添加sql
                agentCommissionMapper.insertSelective(agentCommission);
            }
        }
        return  true;
    }

    //获取总裁代理
    private String acquireTopOpenId(String openId,List<UserAgent> list,Map<String,String> map){
        String str = map.get(openId);
        if(str != null){
            return null;
        }
        for (UserAgent u: list) {
            if(openId.equals(u.getOpenId())){
                if(u.getUserType() == 1 && u.getAgentType() == 3){
                    Agent agent = agentMapper.selectByOpenId(u.getOpenId());
                    if(agent != null){
                        return  u.getOpenId();
                    }
                }
                map.put(openId,openId);
                if(u.getParentOpenId() == null || "".equals(u.getParentOpenId())){
                    return null;
                }else {
                    return acquireTopOpenId(u.getParentOpenId(),list,map);
                }
            }
        }
        return null;
    }


    /**
     * 获取分佣和代理金额
     * @param openId
     * @return
     */
    public  JsonResult findCommission(String openId){
        Commission commission = commissionMapper.selectByOpenId(openId);
        if(commission == null){
            commission = new Commission();
            commission.setOpenId(openId);
            commission.setAllMoney((double)0);
            commission.setRemain((double)0);
            commission.setWaitMoney((double)0);
            commission.setAlreadyMoney((double)0);
            commission.setCommonMoney((double)0);
            commission.setCityMoney((double)0);
            commission.setTeamMoney((double)0);
        }
        Double waitCalculate1 = orderCommissionMapper.sumOneWaitCalculate(openId);
        Double waitCalculate2 = orderCommissionMapper.sumTwoWaitCalculate(openId);
        Double waitCalculate3 = agentCommissionMapper.sumWaitCalculate(openId);
        Double waitCalculate4 = ArithUtil.add(waitCalculate1,waitCalculate2);
        Double waitCalculate = ArithUtil.add(waitCalculate3,waitCalculate4);
        CommissionBean commissionBean = new CommissionBean();
        BeanUtils.copyProperties(commission,commissionBean);
        commissionBean.setWaitCalculate(waitCalculate);
        return  ResultUtil.success(commissionBean);
    }

    /**
     * 获取团队成员
     * @param openId
     * @param page   type 类型( -1全部 ；0普通；1金牌；2城市；3总裁)
     * @return
     */
    public JsonResult findMembers(String openId, TypePage page){
        Integer allCount = userBaseMapper.countMembers(openId);
        List<UserBase> list = userBaseMapper.selectMembers(openId);
        List<UserBase> list2 = findMembersType(list,page.getType());
        page.setRowCount(list2 == null ? 0 : list2.size());

        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("page", page);
        map.put("info", findMembersPage(list2,page));
        map.put("allCount", allCount);
        return ResultUtil.success(map);
    }

    //获取不同的成员
    private  List<UserBase> findMembersType(List<UserBase> list,Integer type){
        List<UserBase> list2 = new ArrayList<>();
        if(type == 0 || type == 1 || type == 2 || type == 3){
            for (UserBase userBase: list) {
                if(userBase.getUserType().intValue() == type.intValue()){
                    list2.add(userBase);
                }
            }
        }else {
            return list;
        }
        return list2;
    }

    //获取分页
    private  List<UserBase> findMembersPage(List<UserBase> list,TypePage page){
        List<UserBase> list2 = new ArrayList<>();
        if(list != null && list.size() > 0){
            Integer j = page.getStartIndex() + page.getPageSize();
            if(j > list.size()){
                j = list.size();
            }
            for (int i = page.getStartIndex(); i < j; i++) {
                list2.add(list.get(i));
            }
        }
        return list2;
    }

    /**
     * 获取提现流水
     * @param openId
     * @param page
     * @return
     */
    public JsonResult findWithdraws(String openId, TypePage page){
        Integer rowCount = withdrawMapper.countWithdraws(openId,page);
        page.setRowCount(rowCount);
        List<Withdraw> list = withdrawMapper.selectWithdraws(openId,page);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("page", page);
        map.put("info", list);
        return ResultUtil.success(map);
    }

    /**
     * 获取用户银行卡列表
     * @param openId
     * @return
     */
    public JsonResult findBankCardList(String openId){
        List<BankCard> list = bankCardMapper.findBankCardList(openId);
        return ResultUtil.success(list);
    }

    /**
     * 添加用户银行卡
     * @param openId
     * @param bankCard
     * @param bankName
     * @param ownerName
     * @return
     */
    public JsonResult addBankCard(String openId,String bankCard,String bankName,String ownerName){
        BankCard b = new BankCard();
        b.setBankCard(bankCard);
        b.setBankName(bankName);
        b.setOpenId(openId);
        b.setOwnerName(ownerName);
        b.setCreateTime(new Date());
        b.setIsDelete(0);
        if(bankCardMapper.insertSelective(b) <= 0){
            return  ResultUtil.fail("添加失败");
        }
        return ResultUtil.success();
    }


    /**
     * 提现
     * @param openId
     * @param bankCardId
     * @param money
     * @return
     */
    @Transactional
    public JsonResult withdrawDeposit(String openId,Integer bankCardId,Double money){
        UserBase userBase = userBaseMapper.findUserInfo(openId);
        if(userBase == null){
            return ResultUtil.fail("该用户不存在");
        }
        if(userBase.getUserType() == 0){
            return ResultUtil.fail("该用户不是代理");
        }
        Agent agent = agentMapper.selectByOpenId(openId);
        if(agent == null){
            return ResultUtil.fail("该用户不是代理");
        }
        Commission commission = commissionMapper.selectByOpenId(openId);
        if(commission == null){
            return ResultUtil.fail("无法提现");
        }
        if(commission.getRemain().doubleValue() < money.doubleValue()){
            return ResultUtil.fail("提现金额大于最大提现金额，无法提现");
        }
        Integer row = withdrawMapper.countTodayWithdraws(openId);
        if(row >  0){
            return ResultUtil.fail("今天已提现，无法再次提现");
        }
        BankCard bankCard = bankCardMapper.selectByPrimaryKey(bankCardId);
        if(bankCard == null || !openId.equals(bankCard.getOpenId())){
            return ResultUtil.fail("请重新选择银行卡");
        }
        Withdraw withdraw = new Withdraw();
        withdraw.setBankCard(bankCard.getBankCard());
        withdraw.setBankCardId(bankCard.getId());
        withdraw.setBankName(bankCard.getBankName());
        withdraw.setCreateTime(new Date());
        withdraw.setIsDelete(0);
        withdraw.setMoney(money);
        withdraw.setOpenId(openId);
        withdraw.setOwnerName(bankCard.getOwnerName());
        withdraw.setStatus(0);
        withdraw.setUuid(RandomNum.getRandomFileName());
        Double remain = ArithUtil.sub(commission.getRemain(),money);
        Double waitMoney = ArithUtil.add(commission.getWaitMoney(),money);
        commission.setRemain(remain);
        commission.setWaitMoney(waitMoney);
        commissionMapper.updateByPrimaryKey(commission);
        withdrawMapper.insertSelective(withdraw);
        return ResultUtil.success();
    }

    /**
     * 获取成员订单
     * @param openId
     * @param page
     * @return
     */
    public  JsonResult findMemberOrders(String openId, TypeDatePage page){
        Map<String, Object> map = new HashMap<String, Object>(2);
        if(page.getType() != 3){
            Integer rowCount = goodsOrderMapper.countMemberOrders(openId,page);
            page.setRowCount(rowCount);
            //查询订单信息
            List<GoodsOrderBean> list = goodsOrderMapper.selectMemberOrders(openId,page);
            map.put("page", page);
            map.put("info", dealOrderDetail(list,openId));
            return ResultUtil.success(map);
        }
        Integer rowCount = goodsOrderMapper.countMemberRefundOrders(openId,page);
        page.setRowCount(rowCount);
        List<GoodsOrderBean> list = goodsOrderMapper.selectMemberRefundOrders(openId,page);
        map.put("page", page);
        map.put("info", dealRefundOrderDetail(list,openId));
        return ResultUtil.success(map);
    }

    private  List<GoodsOrderBean> dealOrderDetail(List<GoodsOrderBean> list,String openId){
        List<GoodsOrderBean> rlist = new ArrayList<>();
        for (GoodsOrderBean g: list) {
            List<GoodsOrderDetail> list1 = goodsOrderDetailMapper.selectByOrderUuid(g.getUuid());
            double agentCommission = 0;
            double cityCommission = 0;
            double topCommission = 0;
            for (GoodsOrderDetail d: list1) {
                List<AgentCommission> list2 = agentCommissionMapper.selectByOrderDetailUuid(d.getUuid());
                agentCommission = calculateOrderCommission(agentCommission,d.getUuid(),openId);
                cityCommission = calculateAgentCommission(cityCommission,list2,openId,2);
                topCommission = calculateAgentCommission(topCommission,list2,openId,3);
            }
            g.setTopCommission(topCommission);
            g.setCityCommission(cityCommission);
            g.setAgentCommission(agentCommission);
            g.setList(list1);
            rlist.add(g);
        }
        return  rlist;
    }

    private  List<GoodsOrderBean> dealRefundOrderDetail(List<GoodsOrderBean> list,String openId){
        List<GoodsOrderBean> rlist = new ArrayList<>();
        for (GoodsOrderBean g: list) {
            GoodsOrderDetail d = goodsOrderDetailMapper.selectByUuid(g.getDetailUuid());
            RecordRefund recordRefund = recordRefundMapper.selectByOrderUuid(g.getDetailUuid());
            if(recordRefund != null){
                d.setQuantity(recordRefund.getRefundNum());
                g.setActualPrice(ArithUtil.sub(recordRefund.getRefundNum(),d.getPrice()));
            }
            List<AgentCommission> list2 = agentCommissionMapper.selectByOrderDetailUuid(d.getUuid());
            double agentCommission = calculateOrderCommission(0,d.getUuid(),openId);
            double cityCommission = calculateAgentCommission(0,list2,openId,2);
            double topCommission = calculateAgentCommission(0,list2,openId,3);
            g.setTopCommission(topCommission);
            g.setCityCommission(cityCommission);
            g.setAgentCommission(agentCommission);
            List<GoodsOrderDetail> list1 = new ArrayList<>();
            list1.add(d);
            g.setList(list1);
            rlist.add(g);
        }
        return  rlist;
    }


    private  Double calculateOrderCommission(double agentCommission,String orderDetailUuid,String openId){
        OrderCommission orderCommission = orderCommissionMapper.selectByOrderDetailUuid(orderDetailUuid);
        if(orderCommission != null){
            if(openId.equals(orderCommission.getParentOpenId())){
                if(orderCommission.getIsSuccess() == 1){
                    agentCommission = ArithUtil.add(orderCommission.getActualParentCommission() == null ? 0 :orderCommission.getActualParentCommission(),agentCommission);
                }else {
                    agentCommission = ArithUtil.add(orderCommission.getParentCommission(),agentCommission);
                }
            }
            if(orderCommission.getGrandpaOpenId() != null && openId.equals(orderCommission.getGrandpaOpenId()) ){
                if(orderCommission.getIsSuccess() == 1){
                    agentCommission = ArithUtil.add(orderCommission.getActualGrandpaCommission() == null ? 0 :orderCommission.getActualGrandpaCommission(),agentCommission);
                }else {
                    agentCommission = ArithUtil.add(orderCommission.getGrandpaCommission(),agentCommission);
                }
            }
        }
        return  agentCommission;
    }

    private  Double calculateAgentCommission(double commission,List<AgentCommission> list,String openId,Integer type){
        for (AgentCommission a: list) {
            if (openId.equals(a.getAgentOpenId())){
                if(a.getType().intValue() == type.intValue()){
                    if(a.getIsSuccess() == 1){
                        commission = ArithUtil.add(a.getActualCommission() == null ? 0 :a.getActualCommission(),commission);
                    }else {
                        commission = ArithUtil.add(a.getCommission(),commission);
                    }
                }
            }
        }
        return  commission;
    }


}
