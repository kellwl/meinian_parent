package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-04 15:35
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;


    /*
    * 由于表单数据来自多张表的数据操作,不太方便用pojo对象接收,接受不完整
    * */
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map){
        try {
            System.out.println("map="+map);
            //1.获取用户信息
            String telephone= (String) map.get("telephone");
            //获取验证码
            String validateCode= (String) map.get("validateCode");
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //2.验证手机验证码
            if (redisCode==null||!redisCode.equals(validateCode)){
                //验证不通过
                return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //3.调用业务进行预约
            Result result=orderService.saveOrder(map);
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    //根据订单id查询        参数为orderid
    @RequestMapping("/findById")
    public  Result findById(Integer id){
        try {
            Map<String,Object> map= null;
            map = orderService.findById(id);
            return  new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }

    }




}