package com.jingshuiqi.service;

import com.jingshuiqi.bean.Comment;
import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.bean.GoodsOrderDetail;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.CommentMapper;
import com.jingshuiqi.dao.GoodsOrderDetailMapper;
import com.jingshuiqi.dao.GoodsOrderMapper;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.vo.CommentInfoVo;
import com.jingshuiqi.vo.UserCommentInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 18:34
 * @Description:
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;

    public JsonResult findGoodsCommentInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<CommentInfoVo> list = commentMapper.findGoodsCommentInfo(pageObject);
        int row = commentMapper.getGoodsCommentInfoRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findUserCommentInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<UserCommentInfoVo> list = commentMapper.findUserCommentInfo(pageObject);
        int row = commentMapper.getUserCommentInfoRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveCommentInfo(Comment comment) {
        comment.setCreateTime(new Date());
        try {
            commentMapper.insertSelective(comment);
            goodsOrderDetailMapper.updateOrdersDetailComment(comment.getOrderUuid());
            GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMapper.selectByUuid(comment.getOrderUuid());
            int row = goodsOrderDetailMapper.findOrderCommentDetailNum(goodsOrderDetail.getOrderUuid());
            if (row == 0) {
                goodsOrderMapper.updateOrdersComment(goodsOrderDetail.getOrderUuid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("保存失败");
        }
        return ResultUtil.success();
    }

}
