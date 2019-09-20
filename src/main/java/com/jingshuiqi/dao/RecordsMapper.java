package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Records;
import org.apache.ibatis.annotations.Param;

public interface RecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Records record);

    int insertSelective(Records record);

    Records selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Records record);

    int updateByPrimaryKey(Records record);

    int findRecordsInfo(@Param("uuid") String uuid, @Param("openId") String token);

    int deleteByCollectInfo(@Param("uuid")String uuid, @Param("openId")String token);
}