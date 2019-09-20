package com.jingshuiqi.interceptors;

import com.google.gson.Gson;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.service.RedisService;
import com.jingshuiqi.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisService redisService;
	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		}
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers",
				"Authorization, Content-Type, X-Requested-With, token,Origin,x-access-token");
		response.setHeader("Access-Control-Allow-Methods",
				"GET, HEAD, OPTIONS, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		String token = request.getHeader("x-access-token");
		System.out.println("------token:" + token);

		/*// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// 登录不做拦截
		if (request.getRequestURI().contains("login")) {
			return true;
		}

		if (request.getRequestURI().contains("swagger")) {
			return true;
		}

		if (request.getRequestURI().contains("Code")) {
			return true;
		}

		if (request.getRequestURI().contains("pay")) {
			return true;
		}

		if (token == null || token.equals("")) {
			System.out.println("token == null");
			printJson(response,StatusCode.INVALID,"token为空");
			return false;
		}
		boolean flag = redisService.exists(token);
		if (!flag) {
			System.out.println("token not exists");
			printJson(response,StatusCode.INVALID,"token已过期,请重新登录");
			return false;
		}*/
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
						   HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	private static void printJson(HttpServletResponse response,Integer status,String msg) {
		Gson gson = new Gson();
		String content = gson.toJson(new JsonResult(status,msg));
		printContent(response, content);
	}

	public static void printContent(HttpServletResponse response,String message){
		try {
			response.setStatus(HttpStatus.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			PrintWriter writer = response.getWriter();
			writer.write(message);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
