package com.jingshuiqi.vo;

import com.jingshuiqi.bean.Comment;
import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 18:54
 * @Description:
 */
@Data
public class CommentInfoVo extends Comment {

    private String nickName;

    private String icon;

}
