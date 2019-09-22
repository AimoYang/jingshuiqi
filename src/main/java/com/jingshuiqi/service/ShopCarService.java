package com.jingshuiqi.service;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.form.ShopGoodsForm;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.stereotype.Service;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:20
 * @Description:
 */
@Service
public class ShopCarService {

    public JsonResult saveGoodsCart(ShopGoodsForm shopGoodsForm, String token) {
       return ResultUtil.success();
    }
}
