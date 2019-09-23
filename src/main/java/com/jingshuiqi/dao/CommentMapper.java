package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Comment;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.vo.CommentInfoVo;
import com.jingshuiqi.vo.UserCommentInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentInfoVo> findGoodsCommentInfo(@Param("pageObject") PageObject pageObject);

    int getGoodsCommentInfoRow(@Param("pageObject")PageObject pageObject);

    List<UserCommentInfoVo> findUserCommentInfo(@Param("pageObject")PageObject pageObject);

    int getUserCommentInfoRow(@Param("pageObject")PageObject pageObject);
}