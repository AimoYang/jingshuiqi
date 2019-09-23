package com.jingshuiqi.dao;

import com.jingshuiqi.bean.UserBase;
import com.jingshuiqi.bean.Withdraw;
import com.jingshuiqi.dto.TypePage;

import java.util.List;

public interface WithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);

    Integer countWithdraws(String openId, TypePage page);

    List<UserBase> selectWithdraws(String openId, TypePage page);
}