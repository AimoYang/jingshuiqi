package com.jingshuiqi.service;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.CommentMapper;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.vo.CommentInfoVo;
import com.jingshuiqi.vo.UserCommentInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
