package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-21 11:24
 */
@RestController
@RequestMapping("/travelItem")
public class TravelItemController {

    @Reference//远程调用服务
    TravelItemService travelItemService;

    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return  pageResult;

    }



//表单项参数必须和实体对象的属性保持一致,提供对应set方法,框架创建对象并且封装数据
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add(@RequestBody  TravelItem travelItem){//从请求体获取数据
        try {
            travelItemService.add(travelItem);
            return  new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    //删除方法
    @RequestMapping("/del")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE123')")//权限校验，使用TRAVELITEM_DELETE123测试
    public  Result del(Integer id){
        try {
            travelItemService.del(id);
            return  new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }

    }


    //根据Id查找自由行信息
    @RequestMapping("/findById")
    public  Result findById(Integer id){
        try {
            TravelItem travelItem = travelItemService.findById(id);
            return  new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    //编辑自由行信息
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public  Result editTravelItem(@RequestBody TravelItem travelItem){
        try {
            travelItemService.edit(travelItem);
            return  new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL );
        }
    }

    //查找全部自由行
    @RequestMapping("/findAll")
    public Result findAll(){
        List<TravelItem>travelItems=travelItemService.findAll();
        return  new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItems);
    }







}