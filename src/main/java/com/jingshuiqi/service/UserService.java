package com.jingshuiqi.service;

import com.jingshuiqi.bean.*;
import com.jingshuiqi.dao.AgentMapper;
import com.jingshuiqi.dao.AreaMapper;
import com.jingshuiqi.dao.UserBaseMapper;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public JsonResult findUserInfo(String token) {
        UserBase userBase = userBaseMapper.findUserInfo(token);
        userBase.setReserve(null);
        if (userBase.getUserType() == 1){
            Agent agent = agentMapper.findAgent(userBase.getOpenId());
            userBase.setUserType(agent.getAgentType());
            if (agent.getAgentType() > 1 && agent.getAreaId() != null){
                String address = areaMapper.findAddress(agent.getAreaId());
                userBase.setReserve(address);
            }
        }
        return ResultUtil.success(userBase);
    }

}
