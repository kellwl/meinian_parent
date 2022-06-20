package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-05 16:12
 */
@Service(interfaceClass =MemberService.class )
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member getMemberBytelephone(String telephone) {
        return   memberDao.getMemberBytelephone(telephone);
    }

    @Override
    public void add(Member member) {
          memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer>  memerCountList=new ArrayList<>();
       //判断一下集合是否为空,或者数量是否大于0
        if (list!=null&&list.size()>0){
            for (String mouth : list) {
                //获取指定月份的最后一天
                String regTime=DateUtils.getLastDayOfMonth(mouth);
                Integer memberCount=memberDao.findMemberCountBeforeDate(regTime);
                memerCountList.add(memberCount);
            }
        }
        return  memerCountList;
    }
}