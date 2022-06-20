package com.atguigu.dao;

import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(Map<String, Integer> paramData);

    Page findPage(String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void del(Integer travelGroupId);

    void delTravelGroup(Integer id);

    List<TravelGroup> findAll();

    Integer findCountBytravelGoupId(Integer id);


    //根据套餐id查询出所有的跟团游信息
    List<TravelGroup> findTravelGroupByid(Integer id);


}
