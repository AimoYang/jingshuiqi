package com.jingshuiqi.dao;

import com.jingshuiqi.bean.AgentCommission;
import com.jingshuiqi.bean.OrderCommission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCommissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgentCommission record);

    int insertSelective(AgentCommission record);

    AgentCommission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgentCommission record);

    int updateByPrimaryKey(AgentCommission record);

    Double sumWaitCalculate(String openId);

    List<AgentCommission> selectByOrderDetailUuid(@Param("orderDetailUuid") String orderDetailUuid);
}