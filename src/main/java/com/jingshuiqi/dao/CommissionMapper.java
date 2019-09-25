package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Commission;
import org.apache.ibatis.annotations.Param;

public interface CommissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commission record);

    int insertSelective(Commission record);

    Commission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commission record);

    int updateByPrimaryKey(Commission record);

    Commission selectByOpenId(@Param("openId") String openId);

    void updateIncome(@Param("openId") String parentOpenId, @Param("Commission") Double actualParentCommission);

    void updateCity(@Param("openId") String agentOpenId, @Param("Commission") Double actualCommission);

    void updateTeam(@Param("openId")String agentOpenId, @Param("Commission")Double actualCommission);
}