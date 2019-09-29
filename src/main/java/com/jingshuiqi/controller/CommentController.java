package com.jingshuiqi.controller;

import com.jingshuiqi.bean.Comment;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.resubmit.DuplicateSubmitToken;
import com.jingshuiqi.service.CommentService;
import com.jingshuiqi.util.PageObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 14:20
 * @Description:
 */
@Api(tags = {"我的评价"})
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查看商品的评论信息" , notes = "根据url查看商品的评论信息")
    @RequestMapping(value = "findGoodsCommentInfo" , method = RequestMethod.POST)
    public JsonResult findGoodsCommentInfo(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject){
        return commentService.findGoodsCommentInfo(pageObject);
    }

    @ApiOperation(value = "查看我的评论信息" , notes = "根据url查看我的评论信息")
    @RequestMapping(value = "findUserCommentInfo" , method = RequestMethod.POST)
    public JsonResult findUserCommentInfo(@ApiParam(value = "分页信息") @RequestBody PageObject pageObject, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        pageObject.setOpenId(token);
        return commentService.findUserCommentInfo(pageObject);
    }

    @DuplicateSubmitToken
    @ApiOperation(value = "发表商品评论信息" , notes = "根据url发表商品评论信息")
    @RequestMapping(value = "saveCommentInfo" , method = RequestMethod.POST)
    public JsonResult saveCommentInfo(@ApiParam(value = "评论信息") @RequestBody Comment comment, HttpServletRequest request){
        String token = request.getHeader("x-access-token");
        comment.setOpenId(token);
        return commentService.saveCommentInfo(comment);
    }

}
