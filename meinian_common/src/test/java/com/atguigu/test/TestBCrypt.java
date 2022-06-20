package com.atguigu.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-09 15:53
 */
public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder bpe=new BCryptPasswordEncoder();
        String encode=bpe.encode("123");
        System.out.println(encode);
    }
}