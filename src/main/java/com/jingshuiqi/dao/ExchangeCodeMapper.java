package com.jingshuiqi.dao;

import com.jingshuiqi.bean.ExchangeCode;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ExchangeCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCode record);

    int insertSelective(ExchangeCode record);

    ExchangeCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCode record);

    int updateByPrimaryKey(ExchangeCode record);

    ExchangeCode findCode(@Param("code") String code, @Param("goodsUuid") String goodsUuid);

    int reCode(@Param("code")String code, @Param("date") Date date);

    void updateCode(@Param("code")String code, @Param("goodsUuid")String goodsUuid);
}