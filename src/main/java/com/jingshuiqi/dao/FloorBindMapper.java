package com.jingshuiqi.dao;

import com.jingshuiqi.bean.FloorBind;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.vo.IndexFloorInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FloorBindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FloorBind record);

    int insertSelective(FloorBind record);

    FloorBind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FloorBind record);

    int updateByPrimaryKey(FloorBind record);

    List<IndexFloorInfo> findFloorBind(Integer id);

    List<IndexFloorInfo> findIndexFloorInfoById(@Param("pageObject")PageObject pageObject);

    int getIndexFloorInfoByIdRow(@Param("pageObject")PageObject pageObject);
}