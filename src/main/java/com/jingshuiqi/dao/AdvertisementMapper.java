package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Advertisement;

import java.util.List;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertisement record);

    int insertSelective(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Advertisement record);

    int updateByPrimaryKey(Advertisement record);

    List<Advertisement> findBanner();
}