package com.jingshuiqi.util;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jingshuiqi.bean.JsonResult;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public JsonResult bindExceptionHandler1(MethodArgumentNotValidException ex) {
		JsonResult r = new JsonResult();
		BindingResult result = ex.getBindingResult();
		List<ObjectError> list = result.getAllErrors();
		StringBuffer errorMessage = new StringBuffer();
		for (ObjectError error : list) {
			errorMessage.append(error.getDefaultMessage());
			System.out.println(errorMessage);
			break;
		}
		r.setResult(StatusCode.FAIL);
		r.setMsg("参数错误");
		r.setData(errorMessage);
		return r;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JsonResult execption(Exception ex) {
		JsonResult r = new JsonResult();
		r.setResult(StatusCode.FAIL);
		r.setMsg("服务器异常");
		return r;
	}


}
