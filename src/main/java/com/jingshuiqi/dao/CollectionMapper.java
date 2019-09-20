package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Collection;
import org.apache.ibatis.annotations.Param;

public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    int findRecordsInfo(@Param("uuid") String uuid, @Param("openId") String token);

    int deleteByCollectInfo(String uuid, String token);
}