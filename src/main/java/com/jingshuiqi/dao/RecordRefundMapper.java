package com.jingshuiqi.dao;

import com.jingshuiqi.bean.RecordRefund;
import org.apache.ibatis.annotations.Param;

public interface RecordRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordRefund record);

    int insertSelective(RecordRefund record);

    RecordRefund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordRefund record);

    int updateByPrimaryKey(RecordRefund record);

    RecordRefund selectByOrderUuid(@Param("orderUuid") String orderUuid);
}