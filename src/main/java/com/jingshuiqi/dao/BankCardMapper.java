package com.jingshuiqi.dao;

import com.jingshuiqi.bean.BankCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BankCard record);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BankCard record);

    int updateByPrimaryKey(BankCard record);

    List<BankCard> findBankCardList(@Param("openId") String openId);
}