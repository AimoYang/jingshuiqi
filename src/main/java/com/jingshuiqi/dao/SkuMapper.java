package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sku record);

    int insertSelective(Sku record);

    Sku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    List<Sku> findSku(@Param("uuid") String uuid);
}