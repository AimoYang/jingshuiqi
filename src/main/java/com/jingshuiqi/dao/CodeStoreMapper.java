package com.jingshuiqi.dao;

import com.jingshuiqi.bean.CodeStore;

public interface CodeStoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CodeStore record);

    int insertSelective(CodeStore record);

    CodeStore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CodeStore record);

    int updateByPrimaryKey(CodeStore record);
}