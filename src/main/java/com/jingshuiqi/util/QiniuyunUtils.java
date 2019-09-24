package com.jingshuiqi.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;

/**
 * @author Administrator
 */

@Slf4j
public class QiniuyunUtils {

    private static String ACCESS_KEY = "sywgziTe-WOL3_9Np9LPyuzRI5e0nNiZIv13_Kf9";
    private static String SECRET_KEY = "A1X-kUGZQjo3IpB5x-TQzfbJhM1NzJaE1DasQRHh";
    private static String BUCKET_NAME = "file";
    public static String DOMAIN_OF_BUCKET = "http://file.jinshanzhu.com/";

    /**
     * 根据文件相对路径获取绝对路径
     *
     * @param fileRelativePath
     * @return
     * @throws Exception
     */
    public static String fileLocation(String fileRelativePath) throws Exception {
        String encodedFileName = URLEncoder.encode(fileRelativePath, "utf-8");
        String absolutePath = String.format("%s/%s", DOMAIN_OF_BUCKET, encodedFileName);
        return absolutePath;
    }


    // 创建上传对象
    public static Boolean upload(MultipartFile multipartFile, String key) throws Exception {
        //上传配置
        /**
         * 华东	Zone.zone0()
         * 华北	Zone.zone1()
         * 华南	Zone.zone2()
         * 北美	Zone.zoneNa0()
         */
        Configuration c = new Configuration(Zone.zone0());
        // 创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        // 密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
        String upToken = auth.uploadToken(BUCKET_NAME, null, 7200, null, true);
        Boolean flag = false;
        try {
            byte[] file = multipartFile.getBytes();
            // 调用put方法上传
            Response res = uploadManager.put(file, key, upToken);
            if (res != null) {
                flag = true;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            log.error("【七牛云】上传失败，e={]",r.error);
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param key 要删除文件名
     * @return
     */
    public static Boolean deleteFile(String key) {
        Configuration configuration = new Configuration(Zone.zone0());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //...其他参数参考类注释
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            bucketManager.delete(BUCKET_NAME, key);
            return true;
        } catch (QiniuException ex) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(fileLocation("d8381cb1e46441ff83d3cb43c733f752.jpg"));
    }
}
