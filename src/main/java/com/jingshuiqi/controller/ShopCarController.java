package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.form.ShopGoodsForm;
import com.jingshuiqi.service.ShopCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:19
 * @Description:
 */
@Api(tags = {"购物车信息"})
@RestController
public class ShopCarController {

    @Autowired
    private ShopCarService shopCarService;

    @ApiOperation(value = "添加商品到购物车" , notes = "添加商品到购物车")
    @RequestMapping(value = "saveGoodsCart" , method = RequestMethod.POST)
    public JsonResult saveShopCar(@ApiParam("加入购物车的商品信息") @RequestBody @Valid ShopGoodsForm shopGoodsForm, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return shopCarService.saveGoodsCart(shopGoodsForm, token);
    }

}
