package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Address;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Address> findUserAddressAll(@Param("pageObject") PageObject pageObject);

    int getUserAddressAllRow(@Param("pageObject")PageObject pageObject);

    Address findUserAddressInfo(@Param("id")Integer id);

    int updateAddressInfoForZero(@Param("openId")String openId);

    int updateDefaultAddressInfo(@Param("id")Integer id);
}