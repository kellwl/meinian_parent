package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-24 11:57
 */
@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;


    //添加跟团游
    @RequestMapping("/add")
    public Result add( Integer[] travelItemIds,@RequestBody TravelGroup travelGroup) {
        try {
            travelGroupService.add(travelItemIds, travelGroup);
           return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }

    }



    //分页展示跟团游
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return  pageResult;
    }


    //根据id查找跟团行     这里参数不需要加@RequestBody  因为这里传过来的知识一个id  并不是一个完整的对象
    @RequestMapping("/findById")
    public  Result findById( Integer id){
        try {
            TravelGroup travelGroup=travelGroupService.findById(id);
            return  new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }


    //根据跟团游id来查询所对应的自由Id们
    @RequestMapping("/findTravelItemIdByTravelgroupId")
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id){

        return   travelGroupService.findTravelItemIdByTravelgroupId(id);
    }

    //编辑跟团游
    @RequestMapping("/edit")
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup) {
        try {
            travelGroupService.edit(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }

    }


    //删除跟团游,有关联数据的不能删除
    @RequestMapping("/del")
    public  Result del(Integer id){
        try {
            travelGroupService.del(id);
            return  new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }


    //查找全部的跟团游
    @RequestMapping("/findAll")
    public  Result findAll(){
        try {
            List<TravelGroup> travelGroups=travelGroupService.findAll();
            return  new Result( true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }

    }
}