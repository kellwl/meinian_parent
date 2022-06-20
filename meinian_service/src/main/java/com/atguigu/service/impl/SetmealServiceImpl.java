package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-27 01:40
 */
@Service(interfaceClass =SetmealService.class )
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    //添加套餐,有两部分  一部分是添加套餐  另一部分是 套餐和跟团游的关系表
    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupIds) {
        //1.添加套餐
        setmealDao.add(setmeal);//主键回填
        //获取套餐Id
        Integer  setmealId=setmeal.getId();
        //2.保存套餐 跟团游 关联数据
        setSetmealAndTravelGroup(setmealId,travelgroupIds);

        //3.  补充代码解决垃圾图片
        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page=setmealDao.findPage(queryString);
        return  new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
       return  setmealDao.findById(id);
    }

    @Override
    public List<Integer> findTravelgroupIdBySetmealId(Integer id) {
        return  setmealDao.findTravelgroupIdBySetmealId(id);
    }

    @Override
    public void edit(Integer[] travelgroupIds, Setmeal setmeal) {
        //1.先编辑表单中的内容
        setmealDao.edit(setmeal);
        //2.删除之前关系表的数据
        //获取套餐id
        Integer SetmealId=setmeal.getId();
        setmealDao.del(SetmealId);
        //3.再添加到关系表中
        setSetmealAndTravelGroup(SetmealId,travelgroupIds);
    }

    @Override
    public void del(Integer id) {
        //判断是否有关联关系
        Long count=setmealDao.findCountBySetmealId(id);
        if (count>0){
            //有关联关系,抛出异常
            throw  new RuntimeException("删除套餐游失败,存在关联数据,请先删除关联数据");
        }
        setmealDao.del(id);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return  setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findSetmeal(Integer id) {
        return  setmealDao.findSetmeal(id);
    }

    @Override
    public List<Map> getSetmealReport() {
        return  setmealDao.getSetmealReport();
    }

    private void setSetmealAndTravelGroup(Integer setmealId, Integer[] travelgroupIds) {
        //判断travelgroupIds
        if (travelgroupIds!=null && travelgroupIds.length>0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String,Integer>  paramData=new HashMap<>();
                paramData.put("travelgroupId",travelgroupId);
                paramData.put("setmealId",setmealId);
                setmealDao.setSetmealAndTravelGroup(paramData);
            }
        }
    }


}