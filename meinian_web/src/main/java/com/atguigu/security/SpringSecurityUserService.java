package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-09 16:09
 */
@Component
public class SpringSecurityUserService  implements UserDetailsService {
    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息
        User user=userService.findUserByUsername(username);
        if (user==null){//不存在这个用户
            return  null;//返回Null给框架,框架会抛出异常,跳转到登录页面
        }
        //2.构建权限集合
        Set<GrantedAuthority> authorities=new HashSet<>();

        Set<Role> roles=user.getRoles(); //集合数据由 RoleDao帮忙方法得到的
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();//集合数据是由permissionDao帮忙方法来查询得到的
            for (Permission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));

            }
        }
        org.springframework.security.core.userdetails.User securityUser=new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        return  securityUser;//框架提供的user实现了Userdetails
    }
}