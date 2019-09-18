package com.jingshuiqi.util.pay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.util.StatusCode;
import com.jingshuiqi.util.WeixinUtil;

/**
 * 企业向个人付款
 * @author Administrator
 *
 */
public class BusinessPayUtil {
	private byte[] certData;

	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public BusinessPayUtil() throws Exception {
		String certPath = WeixinUtil.CERT_PATH;
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		this.certData = new byte[(int) file.length()];
		certStream.read(this.certData);
		certStream.close();
	}

	public static JsonResult businessPay(Map<String, String> params,
			HttpServletRequest request) {
		JsonResult r = new JsonResult();
		try {
			System.out.println("--------------------开始返利");
			String result = withdrawRequestOnce(params, 3000, 3000, true,
					request);
			Map<String, String> resultMap = OtherUtils.readStringXmlOut(result);

			if (resultMap.containsKey("result_code")
					&& "SUCCESS".equals(resultMap.get("result_code"))) {
				r.setResult(StatusCode.SUCCESS);
				r.setMsg("ok");
				r.setData(resultMap);
			} else {
				r.setResult(StatusCode.FAIL);
				r.setMsg("error");
				r.setData(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 
	 * 提现 请求，只请求一次，不做重试
	 * 
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String withdrawRequestOnce(Map<String, String> params,
			int connectTimeoutMs, int readTimeoutMs, boolean useCert,
			HttpServletRequest request) throws Exception {
		System.out.println("-------------------------返利");
		// 订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		// 开发阶段，用时间戳代替订单号
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
		String randNo = format.format(new Date());
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("mch_appid", WeixinUtil.mch_appid);
		paraMap.put("mchid", WeixinUtil.mch_id);
		paraMap.put("nonce_str", OtherUtils.getNonceStr());
		paraMap.put("partner_trade_no", randNo);
		paraMap.put("openid", params.get("openId"));// "o5mZ40yBjIqco2NzKc19k9oIBI9o");
		// 校验用户姓名选项 NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
		paraMap.put("check_name", "NO_CHECK");
		paraMap.put("amount", params.get("money"));// "100");params.get("money")
		// 企业付款操作说明信息。必填。
		paraMap.put("desc", "代理返利");
		paraMap.put("spbill_create_ip", spbill_create_ip);

		String url = OtherUtils.formatUrlMap(paraMap, false, false);
		url = url + "&key=" + WeixinUtil.partnerkey;
		System.out.println("--------------------------" + url);
		String sign = MD5Utils.MD5Encoding(url).toUpperCase();

		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		for (Map.Entry<String, String> entry : paraMap.entrySet()) {
			xml.append("<" + entry.getKey() + ">");
			xml.append(entry.getValue());
			xml.append("</" + entry.getKey() + ">" + "\n");
		}
		xml.append("<sign>");
		xml.append(sign);
		xml.append("</sign>");
		xml.append("</xml>");
		System.out.println(xml.toString());
		if (useCert) {
			System.out.println("------------------检验证书");
			// 证书
			char[] password = WeixinUtil.mch_id.toCharArray();
			InputStream certStream = new BusinessPayUtil().getCertStream();
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(certStream, password);

			// 实例化密钥库 & 初始化密钥工厂
			KeyManagerFactory kmf = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);

			// 创建 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					sslContext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

			
			CloseableHttpClient httpClient =  HttpClients.custom()  
	                .setSSLSocketFactory(sslConnectionSocketFactory)  
	                .build(); 
			
			HttpPost httpPost = new HttpPost(WeixinUtil.WITHDRAW_URL);

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(readTimeoutMs)
					.setConnectTimeout(connectTimeoutMs).build();
			httpPost.setConfig(requestConfig);
			
			System.out.println("------------检测sdk");
			StringEntity postEntity = new StringEntity(xml.toString(), "UTF-8");
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 "
					+ WeixinUtil.mch_id); // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
			httpPost.setEntity(postEntity);
			System.out.println("-------------发送结束");
			System.out.println(httpPost);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println("-------------httResponse");
			HttpEntity httpEntity = httpResponse.getEntity();
			System.out.println("----------------返利结束");
			return EntityUtils.toString(httpEntity, "UTF-8");
		}
		System.out.println("-------------------准备返利");
		 
		/*HttpClient httpClient = HttpClientBuilder
				.create().setConnectionManager(connManager).build();*/
		return "";
	}
}
