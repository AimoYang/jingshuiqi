package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Agent;
import org.apache.ibatis.annotations.Param;

public interface AgentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Agent record);

    int insertSelective(Agent record);

    Agent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Agent record);

    int updateByPrimaryKey(Agent record);

    Agent selectByAreaId(@Param("areaId") Integer areaId);

    Agent selectByOpenId(@Param("openId")String openId);
}