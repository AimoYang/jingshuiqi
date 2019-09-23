package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Floor;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.vo.IndexFloor;
import com.jingshuiqi.vo.IndexFloorInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FloorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Floor record);

    int insertSelective(Floor record);

    Floor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Floor record);

    int updateByPrimaryKey(Floor record);

    List<IndexFloor> findIndexFloorInfo(@Param("pageObject") PageObject pageObject);

    int getIndexFloorRow(@Param("pageObject")PageObject pageObject);
}