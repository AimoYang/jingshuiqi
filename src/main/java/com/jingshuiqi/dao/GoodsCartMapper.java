package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsCart;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.vo.ShopGoodsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCart record);

    int insertSelective(GoodsCart record);

    GoodsCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCart record);

    int updateByPrimaryKey(GoodsCart record);

    GoodsCart findGoods(@Param("goodsUuid") String goodsUuid, @Param("skuUuid") String skuUuid, @Param("openId") String token);

    List<ShopGoodsInfo> findGoodsCart(@Param("pageObject") PageObject pageObject);

    int getGoodsCartRow(@Param("pageObject") PageObject pageObject);
}