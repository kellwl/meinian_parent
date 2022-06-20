package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndTravelGroup(Map<String, Integer> paramData);

    Page findPage(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findTravelgroupIdBySetmealId(Integer id);

    void edit(Setmeal setmeal);

    void del(Integer setmealId);

    Long findCountBySetmealId(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findSetmeal(Integer id);

    List<Map> getSetmealReport();

}
