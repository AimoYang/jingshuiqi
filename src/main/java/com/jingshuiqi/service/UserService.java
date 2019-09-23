package com.jingshuiqi.service;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.bean.UserBase;
import com.jingshuiqi.dao.UserBaseMapper;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 13:59
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserBaseMapper userBaseMapper;

    public JsonResult findUserInfo(String token) {
        UserBase userBase = userBaseMapper.findUserInfo(token);
        return ResultUtil.success(userBase);
    }

}
