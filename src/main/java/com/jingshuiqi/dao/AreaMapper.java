package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Area;
import org.apache.ibatis.annotations.Param;

public interface AreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    String findAddress(Integer id);

    Area findArea(@Param("addressArea") String addressArea);
}