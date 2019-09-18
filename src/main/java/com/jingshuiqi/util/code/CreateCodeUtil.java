package com.jingshuiqi.util.code;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.jingshuiqi.util.upload.UploadFileUtils;



/**
 * 生成二维码
 * @author Administrator
 *
 */
public class CreateCodeUtil {
	
	public static String createCode(String scene, String access_token)
			throws Exception {
		URL url = new URL(
				"https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="
						+ access_token);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setRequestMethod("POST");// 提交模式
		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		// 发送POST请求必须设置如下两行
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter printWriter = new PrintWriter(
				httpURLConnection.getOutputStream());
		// 发送请求参数
		JSONObject paramJson = new JSONObject();
		/*paramJson.put("scene", scene);*/
		paramJson.put("path", "pages/index/index?scene=" + scene);
		paramJson.put("width", 430);
		paramJson.put("auto_color", true);
		printWriter.write(paramJson.toString());
		// flush输出流的缓冲
		printWriter.flush();
		// 开始获取数据
		BufferedInputStream bis = new BufferedInputStream(
				httpURLConnection.getInputStream());
		String path = "/usr/1.png"; ///usr/1.png
		OutputStream os = new FileOutputStream(new File(path));
		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			os.write(arr, 0, len);
			os.flush();
		}
		os.close();
		String msg = UploadFileUtils.saveLocalImgToQin(path, scene + "_code.png");
		return msg;
	}
	
	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(url, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, String.class);
		return responseEntity.getBody();
	}
	
}
