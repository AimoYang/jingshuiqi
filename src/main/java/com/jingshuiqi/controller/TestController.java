package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dto.TypeDatePage;
import com.jingshuiqi.dto.TypePage;
import com.jingshuiqi.service.DoCommissionService;
import com.jingshuiqi.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Administrator
 * @create 2019/9/23 0023 21:12
 */
@Api(tags = {"测试"})
@RestController
public class TestController {

    @Autowired
    private DoCommissionService doCommissionService;

    @ApiOperation(value = "测试" , notes = "测试")
    @GetMapping(value = "test")
    public JsonResult test(){
        TypeDatePage typePage = new TypeDatePage();
        typePage.setType(-1);
        return ResultUtil.success(doCommissionService.findMemberOrders("2",typePage));
    }
}
