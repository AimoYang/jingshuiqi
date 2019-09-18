package com.jingshuiqi.util;


import java.io.InputStream;
import java.util.UUID;
 
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class UploadUtils {
 
	public static String upload(InputStream stream) {
		
		String url = "";
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		String accessKey = "-OLnW71ijZk1nXJ-X4pYD0_0HPkGjf-lsY6EsP93";
		String secretKey = "7zx88N46-MgAYCJtbnITZ2JBiXM_w1Ybc5sYZJnJ";
		String bucket = "file";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = UUID.randomUUID().toString().replaceAll("\\-", "");
		System.out.println(key);
		try {
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
				Response response = uploadManager.put(stream, key, upToken, null, null);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				url = "http://file.houtianfu.com/"+putRet.key;
				/*System.out.println(putRet.key);
				System.out.println(putRet.hash);*/
			} catch (QiniuException ex) {
				Response r = ex.response;
				System.err.println(r.toString());
				try {
					System.err.println(r.bodyString());
				} catch (QiniuException ex2) {
					ex2.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return url;
	}
}