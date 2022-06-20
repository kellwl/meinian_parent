package com.atguigu.dao;

import java.util.List;
import java.util.Map;
import com.atguigu.pojo.Member;

public interface MemberDao {
    Member getMemberBytelephone(String telephone);

    void add(Member member);

    int getTodayNewMember(String date);
    int getTotalMember();
    int getThisWeekAndMonthNewMember(String date);

    Integer findMemberCountBeforeDate(String regTime);
}
