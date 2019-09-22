package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.AddressService;
import com.jingshuiqi.service.UserService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 13:55
 * @Description:
 */
@Api(tags = {"我的信息"})
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "查看我的信息" , notes = "根据url查看我的信息")
    @RequestMapping(value = "findUserInfo" , method = RequestMethod.POST)
    public JsonResult findUserInfo(HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return userService.findUserInfo(token);
    }

}
