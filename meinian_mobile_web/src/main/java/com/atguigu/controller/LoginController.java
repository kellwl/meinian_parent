package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-05 16:10
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;


    //使用手机号和验证码登录
    @RequestMapping("/check")
    public Result check(HttpServletResponse response,@RequestBody Map map){
        //获取相应参数     手机号  和  手机验证码
        String telephone= (String) map.get("telephone");
        String validateCode= (String) map.get("validateCode");

        //1.从redis中获取发送的验证码  进行验证
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (codeInRedis==null || !codeInRedis.equals(validateCode)){
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }else {
            //输入正确,判断是否为会员
            Member member = memberService.getMemberBytelephone(telephone);
            if (member==null){
                //非会员
                //自动完成注册
                member=new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //为会员则,登录成功
            //写入Cookie,跟踪用户
            Cookie cookie=new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            return  new Result(true, MessageConstant.LOGIN_SUCCESS);
        }


    }

}