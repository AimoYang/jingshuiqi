package com.jingshuiqi.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingshuiqi.service.OrdersService;
import com.jingshuiqi.util.WeixinUtil;
import com.jingshuiqi.util.pay.PayUtil;

@Api(tags = {"支付信息"})
@RestController
public class PayController {
	
	protected static final Logger logger = LoggerFactory
			.getLogger(PayController.class);

	@Autowired
	private OrdersService ordersService;
	
	
	@RequestMapping(value = "pay/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("================支付回调开始");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		// sb为微信返回的xml
		String notityXml = sb.toString();
		String resXml = "";
		System.out.println("接收到的报文：" + notityXml);

		Map<String, String> map = PayUtil.doXMLParse(notityXml);

		String returnCode = (String) map.get("return_code");
		
		if ("SUCCESS".equals(returnCode)) {
			// 验证签名是否正确
			Map<String, String> validParams = PayUtil.paraFilter(map); // 回调验签时需要去除sign和空值参数
			String validStr = PayUtil.createLinkString(validParams);// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
			String sign = PayUtil
					.sign(validStr, WeixinUtil.partnerkey, "utf-8")
					.toUpperCase();// 拼装生成服务器端验证的签名
			// 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

			// 根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
			if (sign.equals(map.get("sign"))) {
				logger.info("==============成功了");
				String orderNum = validParams.get("out_trade_no");
				ordersService.updateGoodsNumber(orderNum);
				resXml = "<xml>"
						+ "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				System.out.println("微信支付回调失败!签名不一致");
			}
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}

		System.out.println(resXml);
		System.out.println("微信支付回调数据结束");

		BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
	}

}
