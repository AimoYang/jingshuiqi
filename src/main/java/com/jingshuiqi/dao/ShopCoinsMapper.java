package com.jingshuiqi.dao;

import com.jingshuiqi.bean.ShopCoins;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCoinsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopCoins record);

    int insertSelective(ShopCoins record);

    ShopCoins selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopCoins record);

    int updateByPrimaryKey(ShopCoins record);

    List<ShopCoins> findCoinsList(@Param("pageObject") PageObject pageObject);

    int getCoinsListRow(@Param("pageObject")PageObject pageObject);
}