package com.jingshuiqi.dao;

import com.jingshuiqi.bean.UserBase;
import org.apache.ibatis.annotations.Param;

public interface UserBaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBase record);

    int insertSelective(UserBase record);

    UserBase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBase record);

    int updateByPrimaryKey(UserBase record);

    UserBase findUserInfo(@Param("openid") String oneOpenid);

    int updateUserInfo(UserBase userBase);

    int saveUserInfo(UserBase userBase);
}