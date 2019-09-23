package com.jingshuiqi.service;

import com.jingshuiqi.bean.GoodsCart;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.GoodsCartMapper;
import com.jingshuiqi.form.ShopGoodsForm;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.vo.ShopGoodsInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:20
 * @Description:
 */
@Service
public class ShopCarService {

    @Autowired
    private GoodsCartMapper goodsCartMapper;

    public JsonResult saveGoodsCart(ShopGoodsForm shopGoodsForm, String token) {

        GoodsCart goodsCart = new GoodsCart();
        BeanUtils.copyProperties(shopGoodsForm,goodsCart);
        goodsCart.setOpenId(token);

        GoodsCart goodsCart2 = goodsCartMapper.findGoods(goodsCart.getGoodsUuid(), goodsCart.getSkuUuid(), token);

        if (goodsCart2 != null) {
            goodsCart2.setQuantity(goodsCart2.getQuantity() + goodsCart.getQuantity());
            int row = goodsCartMapper.updateByPrimaryKeySelective(goodsCart2);
            if(row <= 0) {
                return ResultUtil.fail("添加失败");
            }
        } else {
            int row = goodsCartMapper.insertSelective(goodsCart);
            if(row <= 0){
                return ResultUtil.fail("添加失败");
            }
        }
        return ResultUtil.success();
    }

    public JsonResult updateGoodsCart(GoodsCart goodsCart) {
        int row = goodsCartMapper.updateByPrimaryKeySelective(goodsCart);
        if(row <= 0){
            return ResultUtil.fail("更新失败");
        }
        return ResultUtil.success();
    }

    public JsonResult showGoodsCart(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(3);
        List<ShopGoodsInfo> list = goodsCartMapper.findGoodsCart(pageObject);
        int total = 0;
        for (ShopGoodsInfo shopGoodsInfo : list) {
            total = total + shopGoodsInfo.getQuantity();
        }
        int row = goodsCartMapper.getGoodsCartRow(pageObject);
        pageObject.setRowCount(row);
        map.put("total", total);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

}
