package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-21 11:27
 */
@Service(interfaceClass =TravelItemService.class )//发布服务注册到zk注册中心
public class TravelItemServiceImpl implements TravelItemService {
    //dao本身不是一个服务 ,dao层在pom中被Service所依赖,所以不用@Reference
    @Autowired
    TravelItemDao travelItemDao;


    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //开启分页插件PageHelper
        PageHelper.startPage(currentPage, pageSize);
        Page  page=travelItemDao.findPage(queryString);
        return  new PageResult(page.getTotal(), page.getResult()); //返回两个参数, 1.总记录数   2.分页数据集合
    }

    @Override
    public void del(Integer id) {
        //若关联表中存在关联数据,则不能删除抛出异常
        long count=travelItemDao.findCountByTravelitemId(id);
        if (count>0){
            //说明存在关联数据
            throw  new RuntimeException("删除自由行失败,因为存在关联数据.先解除关系再删除");
        }
        travelItemDao.del(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return  travelItemDao.findById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
         travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return  travelItemDao.findAll();
    }
}