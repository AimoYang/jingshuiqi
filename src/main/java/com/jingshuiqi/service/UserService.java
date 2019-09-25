package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 13:59
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserBaseMapper userBaseMapper;
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private DoCommissionService doCommissionService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private ShareLinkMapper shareLinkMapper;
    @Autowired
    private ShopCoinsMapper shopCoinsMapper;

    public JsonResult findUserInfo(String token) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        UserBase userBase = userBaseMapper.findUserInfo(token);
        if (userBase.getUserType() != 0) {
            if (userBase.getIsCode() != null) {
                userBase.setIsCode(codeService.WeCode(userBase.getIsCode()));
            }else {
                ShareLink shareLink = shareLinkMapper.findBindUuid(userBase.getOpenId());
                if (shareLink == null) {
                    userBase.setBindUuid(UUIDGenerator.generate());
                    ShareLink shareLink2 = new ShareLink();
                    shareLink2.setOpenid(userBase.getOpenId());
                    shareLink2.setUuid(userBase.getBindUuid());
                    shareLinkMapper.saveBindUuid(shareLink2);
                    userBase.setIsCode(codeService.Code("http://weixin.jinshanzhu.com?state=" + userBase.getBindUuid()));
                    userBaseMapper.updateUserInfo(userBase);
                }else {
                    userBase.setIsCode(codeService.Code("http://weixin.jinshanzhu.com?state=" + shareLink.getUuid()));
                    userBaseMapper.updateUserInfo(userBase);
                }
                userBase.setIsCode(codeService.WeCode(userBase.getIsCode()));
            }
        }
        userBase.setReserve(null);
        if (userBase.getUserType() == 1){
            Agent agent = agentMapper.findAgent(userBase.getOpenId());
            userBase.setUserType(agent.getAgentType());
            if (agent.getAgentType() > 1 && agent.getAreaId() != null){
                String address = areaMapper.findAddress(agent.getAreaId());
                userBase.setReserve(address);
            }
            userBase.setAgentTime(agent.getCreateTime());
        }
        JsonResult r = doCommissionService.findCommission(token);
        map.put("UserBase",userBase);
        map.put("Commission",r.getData());
        return ResultUtil.success(map);
    }

    public JsonResult findUserCoins(String token) {
        UserBase userBase = userBaseMapper.findUserInfo(token);
        return ResultUtil.success(userBase.getShopCoins());
    }

    public JsonResult findCoinsList(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<ShopCoins> list = shopCoinsMapper.findCoinsList(pageObject);
        int row = shopCoinsMapper.getCoinsListRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }
}
