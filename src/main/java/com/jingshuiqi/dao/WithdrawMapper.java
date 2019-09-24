package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Withdraw;
import com.jingshuiqi.dto.TypePage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);

    Integer countWithdraws(@Param("openId") String openId,@Param("page") TypePage page);

    List<Withdraw> selectWithdraws(@Param("openId") String openId,@Param("page") TypePage page);

    Integer countTodayWithdraws(@Param("openId") String openId);
}