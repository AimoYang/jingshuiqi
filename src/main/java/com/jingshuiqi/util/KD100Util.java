package com.jingshuiqi.util;


import com.github.kevinsawicki.http.HttpRequest;
import org.springframework.util.DigestUtils;

import java.net.URL;
import java.util.HashMap;

/**
 * @Auther: mirror_huang
 * @Date: 2018/9/3 0003 17:44
 * @QQ: 1755496180
 * @Description:快递100工具类
 */
public class KD100Util {

    private static String KEY = "vSMrDIyO9655";

    private static String CUSTOMER = "5B0B1D325BB4BAF4AF9C9437B2344811";

    //private static String secret = "dc3017e17f6a42088d5efce2f9ba6c6a";


    /**
     * 获取物流信息
     *
     * @param com
     * @param num
     * @return
     * @throws Exception
     */
    public static String getExpressageKey(String com, String num) throws Exception {
        String param = "{\"com\":\"" + com + "\",\"num\":\"" + num + "\",\"from\":\"\",\"to\":\"\",\"resultv2\":0}";
        String sign = DigestUtils.md5DigestAsHex((param + KEY + CUSTOMER).getBytes());
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("param", param);
        params.put("sign", sign.toUpperCase());
        params.put("customer", CUSTOMER);
        try {
            String resp = HttpRequest.post("http://poll.kuaidi100.com/poll/query.do", params, true).body();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return "查询出错";
        }
    }

    /**
     * 根据快递单号查询快递公司编号
     *
     * @param num
     * @return
     */
    public static String getCom(String num) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("num", num);
        params.put("key", KEY);
        params.put("customer", CUSTOMER);
        try {
            String resp = HttpRequest.get("http://poll.kuaidi100.com/poll/query.do", params, true).body();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return "查询出错";
        }
    }

    public static String getExpress100(String deliveryCode, String expressNo) throws Exception {
        //根据物流公司中文名去查询其公司编号
        StringBuilder stringBuilder = new StringBuilder("https://m.kuaidi100.com/query?");
        stringBuilder.append("type=").append(deliveryCode).append("&").append("postid=").append(expressNo);
        URL url = new URL(stringBuilder.toString());
        String content = HttpRequest.post(url).body();
        return content;
    }

   public static void test() throws Exception {
        System.out.println(getExpressageKey("zhongtong", "75105659337406"));
        System.out.println(getExpress100("zhongtong", "75105659337406"));
        System.out.println(getCom("3519290137444"));
    }

}
