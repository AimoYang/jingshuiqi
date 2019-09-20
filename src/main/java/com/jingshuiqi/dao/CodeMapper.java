package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Code;
import org.apache.ibatis.annotations.Param;

public interface CodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Code record);

    int insertSelective(Code record);

    Code selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Code record);

    int updateByPrimaryKey(Code record);

    public void saveCode(Code code2);

    public Code findCode(@Param("uuid")String uuid);
}