package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Share;
import org.apache.ibatis.annotations.Param;

public interface ShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Share record);

    int insertSelective(Share record);

    Share selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Share record);

    int updateByPrimaryKey(Share record);

    public Share findUserInfoForBind(@Param("openid")String openid);

    public void saveShareBind(Share share2);

    public int findCountForBind(@Param("oneOpenid")String oneOpenid);
}