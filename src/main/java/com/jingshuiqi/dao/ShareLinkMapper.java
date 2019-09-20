package com.jingshuiqi.dao;

import com.jingshuiqi.bean.ShareLink;
import org.apache.ibatis.annotations.Param;

public interface ShareLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShareLink record);

    int insertSelective(ShareLink record);

    ShareLink selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShareLink record);

    int updateByPrimaryKey(ShareLink record);

    public void saveBindUuid(ShareLink shareLink);

    public ShareLink findBindUuid(@Param("openid")String openid);

    public ShareLink findOpenidByBindUuid(@Param("bindUuid")String bindUuid);
}