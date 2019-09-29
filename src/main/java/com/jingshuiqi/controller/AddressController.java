package com.jingshuiqi.controller;

import com.jingshuiqi.bean.Address;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.form.AddressForm;
import com.jingshuiqi.resubmit.DuplicateSubmitToken;
import com.jingshuiqi.service.AddressService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 15:24
 * @Description:
 */
@Api(tags = {"地址信息"})
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @DuplicateSubmitToken
    @ApiOperation(value = "新增地址信息" , notes = "根据url新增地址信息")
    @RequestMapping(value = "saveAddressInfo" , method = RequestMethod.POST)
    public JsonResult saveAddressInfo(@ApiParam(value = "地址信息") @RequestBody @Valid AddressForm addressForm, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        JsonResult r = addressService.saveAddressInfo(addressForm ,token);
        return r;
    }

    @ApiOperation(value = "查看我的所有地址" , notes = "根据url查看我的所有地址")
    @RequestMapping(value = "findUserAddressAll" , method = RequestMethod.POST)
    public JsonResult findUserAddressAll(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return addressService.findUserAddressAll(pageObject);
    }

    @ApiOperation(value = "查看我的地址详细信息" , notes = "根据url查看我的地址详细信息")
    @RequestMapping(value = "findUserAddressInfo" , method = RequestMethod.POST)
    public JsonResult findUserAddressInfo(@ApiParam(value = "地址的id") @RequestParam("id") Integer id){
        return addressService.findUserAddressInfo(id);
    }

    @DuplicateSubmitToken
    @ApiOperation(value = "更新我的地址信息" , notes = "根据url更新我的地址信息")
    @RequestMapping(value = "updateAddressInfo" , method = RequestMethod.POST)
    public JsonResult updateAddressInfo(@ApiParam(value = "地址信息") @RequestBody Address address, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        address.setOpenId(token);
        return addressService.updateAddressInfo(address);
    }

    @DuplicateSubmitToken
    @ApiOperation(value = "更新我的地址信息" , notes = "根据url更新我的地址信息")
    @RequestMapping(value = "deleteAddress" , method = RequestMethod.POST)
    public JsonResult deleteAddress(@ApiParam(value = "地址信息") @RequestParam("id") Integer id, HttpServletRequest request){
        return addressService.deleteAddress(id);
    }

    @DuplicateSubmitToken
    @ApiOperation(value = "更新个人默认地址信息" , notes = "根据url更新个人默认地址信息")
    @RequestMapping(value = "updateDefaultAddressInfo" , method = RequestMethod.POST)
    public JsonResult updateDefaultAddressInfo(@ApiParam(value = "地址的id") @RequestParam("id") Integer id ,HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        JsonResult r = addressService.updateDefaultAddressInfo(id, token);
        return r;
    }

}
