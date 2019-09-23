package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.dto.GoodsOrderBean;
import com.jingshuiqi.dto.TypeDatePage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    Integer countMemberOrders(@Param("openId")String openId,@Param("page") TypeDatePage page);

    List<GoodsOrderBean> selectMemberOrders(@Param("openId") String openId,@Param("page") TypeDatePage page);

    Integer countMemberRefundOrders(String openId, TypeDatePage page);

    List<GoodsOrderBean> selectMemberRefundOrders(String openId, TypeDatePage page);
}