package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.dto.GoodsOrderBean;
import com.jingshuiqi.dto.TypeDatePage;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    GoodsOrder findOrdersByUuid(@Param("uuid") String uuid);

    List<GoodsOrder> findUserOrdersInfo(@Param("pageObject") PageObject pageObject);

    int getUserOrdersRow(@Param("pageObject")PageObject pageObject);

    Integer countMemberOrders(@Param("openId")String openId,@Param("page") TypeDatePage page);

    List<GoodsOrderBean> selectMemberOrders(@Param("openId") String openId, @Param("page") TypeDatePage page);

    Integer countMemberRefundOrders(@Param("openId") String openId, @Param("page")TypeDatePage page);

    List<GoodsOrderBean> selectMemberRefundOrders(@Param("openId") String openId, @Param("page")TypeDatePage page);

    void updateOrdersComment(String orderUuid);
}