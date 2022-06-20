package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] travelgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findById(Integer id);

    List<Integer> findTravelgroupIdBySetmealId(Integer id);

    void edit(Integer[] travelgroupIds, Setmeal setmeal);

    void del(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findSetmeal(Integer id);

    List<Map> getSetmealReport();
}
