package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-24 11:58
 */
@Service(interfaceClass =TravelGroupService.class)
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        //TravelGroup在保存跟团游
        travelGroupDao.add(travelGroup);
        Integer travelGroupId=travelGroup.getId();
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);

    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize,String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page=travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TravelGroup findById(Integer id) {
        return  travelGroupDao.findById(id);
    }

    @Override
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id) {
        return travelGroupDao.findTravelItemIdByTravelgroupId(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        //修改跟团游表单

            travelGroupDao.edit(travelGroup);
        //修改跟团游自由行选项
        Integer travelGroupId=travelGroup.getId();
        //1.先删除之前自由行选项
        travelGroupDao.del(travelGroupId);
        //2.添加 自由行
        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);
    }

    @Override
    public void del(Integer id) {
        //删除之前先判断是否与套餐游有关联关系
        Integer count=travelGroupDao.findCountBytravelGoupId(id);
        if (count>0){
            //存在关联关系抛出异常
            throw new  RuntimeException("删除跟团游失败,因为存在关联数据,请先删除关联数据");
        }
        travelGroupDao.delTravelGroup(id);
    }

    @Override
    public List<TravelGroup> findAll() {
        return  travelGroupDao.findAll();
    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        //在TravelGroupAndTravelItem表保存
        if (travelItemIds!=null&&travelItemIds.length>0){
            //准备dao层需要的参数,利用map集合作为参数传递数据
            for (Integer travelItemId : travelItemIds) {
                Map<String,Integer> paramData=new HashMap<>();
                paramData.put("travelGroupId",travelGroupId);
                paramData.put("travelItemId",travelItemId);
                travelGroupDao.addTravelGroupAndTravelItem(paramData);
            }
        }
    }
}