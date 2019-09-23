package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.dto.CommissionBean;
import com.jingshuiqi.dto.TypePage;
import com.jingshuiqi.dto.UserAgent;
import com.jingshuiqi.util.ArithUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * 计算佣金
     * @param openId 用户openid
     * @param skuId  规格id
     * @param quantity 数量
     * @param areaId 地区id
     * @return
     */
    public boolean calculateCommission(String orderDetailUuid, String openId,Integer skuId,Integer quantity,Integer areaId){
        Sku sku = skuMapper.selectByPrimaryKey(skuId);
        if(sku == null){
            return false;
        }
        Date now = new Date();
        OrderCommission orderCommission = new OrderCommission();
        //一级分佣
        Share share = shareMapper.findUserInfoForBind(openId);
        if(share != null && share.getIsDelete() == 0){
            //父级openId
            String parentOpenId = share.getParentOpenId();
            //父级分佣
            Double parentCommission = ArithUtil.mul(sku.getOneCommission(),quantity);
            orderCommission.setParentOpenId(parentOpenId);
            orderCommission.setParentCommission(parentCommission);
            //二级返佣
            Share share1 = shareMapper.findUserInfoForBind(parentOpenId);
            if(share1 != null && share1.getIsDelete() == 0){
                //父父级openId
                String grandpaOpenId = share1.getParentOpenId();
                //父父级分佣
                Double grandpaCommission = ArithUtil.mul(sku.getTwoCommission(),quantity);
                orderCommission.setGrandpaOpenId(grandpaOpenId);
                orderCommission.setGrandpaCommission(grandpaCommission);
            }
            orderCommission.setOrderDetailUuid(orderDetailUuid);
            orderCommission.setCreateTime(now);
            orderCommission.setIsSuccess((short)0);
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
            agentCommission.setCreateTime(now);
            agentCommission.setType(3);
            agentCommission.setIsSuccess((short)0);
            agentCommission.setOrderDetailUuid(orderDetailUuid);
        }
        //获取地区代理
        Agent agent = agentMapper.selectByAreaId(areaId);
        if(agent != null){
            AgentCommission agentCommission = new AgentCommission();
            agentCommission.setAgentOpenId(agent.getOpenId());
            Double commission = ArithUtil.mul(sku.getCityCommission(),quantity);
            agentCommission.setCommission(commission);
            agentCommission.setCreateTime(now);
            agentCommission.setType(2);
            agentCommission.setIsSuccess((short)0);
            agentCommission.setOrderDetailUuid(orderDetailUuid);
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
                    return  u.getOpenId();
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
    public  CommissionBean findCommission(String openId){
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
        Double waitCalculate1 = orderCommissionMapper.sumWaitCalculate(openId);
        Double waitCalculate2 = agentCommissionMapper.sumWaitCalculate(openId);
        Double waitCalculate = ArithUtil.add(waitCalculate1,waitCalculate2);
        CommissionBean commissionBean = new CommissionBean();
        BeanUtils.copyProperties(commission,commissionBean);
        commissionBean.setWaitCalculate(waitCalculate);
        return  commissionBean;
    }

    /**
     * 获取团队成员
     * @param openId
     * @param page   type 类型( -1全部 ；0普通；1金牌；2城市；3总裁)
     * @return
     */
    public Map<String,Object> findMembers(String openId, TypePage page){
        Integer allCount = userBaseMapper.countMembers(openId);
        List<UserBase> list = userBaseMapper.selectMembers(openId);
        List<UserBase> list2 = findMembersType(list,page.getType());
        page.setRowCount(list2 == null ? 0 : list2.size());

        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("page", page);
        map.put("info", findMembersPage(list2,page));
        map.put("allCount", allCount);
        return map;
    }

    //获取不同的成员
    private  List<UserBase> findMembersType(List<UserBase> list,Integer type){
        List<UserBase> list2 = new ArrayList<>();
        if(type == 0 || type == 2|| type == 3 || type == 4){
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
    public Map<String,Object> findWithdraws(String openId, TypePage page){
        Integer rowCount = withdrawMapper.countWithdraws(openId,page);
        page.setRowCount(rowCount);
        List<UserBase> list = withdrawMapper.selectWithdraws(openId,page);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("page", page);
        map.put("info", list);
        return map;
    }


}
