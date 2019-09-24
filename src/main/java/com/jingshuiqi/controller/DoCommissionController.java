package com.jingshuiqi.controller;

import com.jingshuiqi.bean.BankCard;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dto.TypePage;
import com.jingshuiqi.service.DoCommissionService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 18:33
 * @Description:
 */
@Api(tags = {"分佣接口"})
@RestController
public class DoCommissionController {

    @Autowired
    private DoCommissionService doCommissionService;

    @ApiOperation(value = "获取分佣和代理金额" , notes = "根据url获取分佣和代理金额")
    @RequestMapping(value = "findCommission" , method = RequestMethod.POST)
    public JsonResult findCommission(HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.findCommission(token);
    }

    @ApiOperation(value = "获取团队成员" , notes = "根据url获取团队成员")
    @RequestMapping(value = "findMembers" , method = RequestMethod.POST)
    public JsonResult findMembers(@ApiParam(value = "分页信息") @RequestBody TypePage page, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.findMembers(token,page);
    }

    @ApiOperation(value = "获取提现流水" , notes = "根据url获取提现流水")
    @RequestMapping(value = "findWithdraws" , method = RequestMethod.POST)
    public JsonResult findWithdraws(@ApiParam(value = "分页信息") @RequestBody TypePage page, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.findWithdraws(token,page);
    }

    @ApiOperation(value = "获取用户银行卡列表" , notes = "根据url获取用户银行卡列表")
    @GetMapping(value = "findBankCardList")
    public JsonResult findBankCardList(HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.findBankCardList(token);
    }

    @ApiOperation(value = "添加用户银行卡" , notes = "根据url获取用户银行卡列表")
    @RequestMapping(value = "addBankCard" , method = RequestMethod.POST)
    public JsonResult addBankCard(@ApiParam(value = "分页信息") @RequestBody BankCard bankCard, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.addBankCard(token,bankCard.getBankCard(),bankCard.getBankName(),bankCard.getOwnerName());
    }

    @ApiOperation(value = "提现到用户银行卡" , notes = "根据url提现到用户银行卡")
    @RequestMapping(value = "withdrawDeposit" , method = RequestMethod.POST)
    public JsonResult withdrawDeposit(@ApiParam(value = "银行卡的id") @RequestParam("id") Integer bankCardId,Double money, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        return doCommissionService.withdrawDeposit(token,bankCardId,money);
    }

}
