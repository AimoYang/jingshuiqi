/**
 * Copyright (C), 2017-2019, 苏州立昌科技有限公司
 * FileName: ResultUtil
 * Author:   mirror_huang
 * Date:     2019/2/26 0026 09:38
 * Description: 结果封装
 * History:
 * <author>          <qq>          <version>
 * mirror_huang     1755496180       版本号
 */
package com.jingshuiqi.util;


import com.jingshuiqi.bean.JsonResult;

/**
 * 〈一句话功能简述〉<br> 
 * 〈结果封装〉
 *
 * @author mirror_huang
 * @create 2019/2/26 0026 09:38
 * @since 1.0.0
 */
public class ResultUtil {
    public static JsonResult success(Object object){
        JsonResult resultVO = new JsonResult();
        resultVO.setData(object);
        resultVO.setResult(0);
        resultVO.setMsg("请求成功");
        return resultVO;
    }

    public static JsonResult success(Object object,String msg){
        JsonResult resultVO = new JsonResult();
        resultVO.setData(object);
        resultVO.setResult(0);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static JsonResult success(){
        return success(null);
    }

    public static JsonResult fail(String msg){
        JsonResult resultVO = new JsonResult();
        resultVO.setResult(1);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static JsonResult set(Integer code,String msg){
        JsonResult resultVO = new JsonResult();
        resultVO.setResult(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static JsonResult order(Object object){
        JsonResult resultVO = new JsonResult();
        resultVO.setData(object);
        resultVO.setResult(3);
        resultVO.setMsg("请求成功");
        return resultVO;
    }

}
