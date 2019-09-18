package com.jingshuiqi.dao;

import com.jingshuiqi.bean.ExchangeCode;

public interface ExchangeCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCode record);

    int insertSelective(ExchangeCode record);

    ExchangeCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCode record);

    int updateByPrimaryKey(ExchangeCode record);
}