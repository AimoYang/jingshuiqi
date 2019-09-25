package com.jingshuiqi.dao;

import com.jingshuiqi.bean.UserBase;
import com.jingshuiqi.dto.TypePage;
import com.jingshuiqi.dto.UserAgent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserBaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBase record);

    int insertSelective(UserBase record);

    UserBase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBase record);

    int updateByPrimaryKey(UserBase record);

    UserBase findUserInfo(@Param("openid") String openId);

    int updateUserInfo(UserBase userBase);

    int saveUserInfo(UserBase userBase);

    List<UserAgent> selectUserAgent();

    Integer countMembers(@Param("openId") String openId);

    List<UserBase> selectMembers(@Param("openId") String openId);

    List<UserBase> findIndexOrderFor();

    void updateAddCoins(@Param("allCoins") Integer allCoins, @Param("openId")String openId);
}