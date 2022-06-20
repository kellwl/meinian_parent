package com.atguigu.test;

import java.util.HashMap;
import java.util.Map;

import com.atguigu.util.HttpUtils;
import org.apache.http.HttpResponse;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-03 18:04
 */
public class TestSMS {
    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";  //必须post请求
        String appcode = "e5dc8d1a823c4892bc182a87d0389d92";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);//注意空格
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15881899517");
        querys.put("param", "code:9999");
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}