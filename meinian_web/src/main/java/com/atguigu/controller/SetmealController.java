package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-27 01:39
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    //上传图片到七牛云
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            //1.获取原文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //2.生成新的文件名称(防止上传同名文件被覆盖)
            int index = originalFilename.lastIndexOf(".");
            String suffx = originalFilename.substring(index);
            String filename = UUID.randomUUID().toString() + suffx;
            //3.调用工具类,上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);

//            解决:七牛云上垃圾图片问题的     上传到七牛云时保存到redis一个集合中    在保存到数据库时,将图片名称保存到redis另一个set集合中然后用差集  结果就是垃圾图片
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);

            //4.返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    //添加套餐  和  套餐和跟团游关系
    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.add(setmeal, travelgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    //分页展示套餐
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    //通过id查找套餐游信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return  new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findTravelgroupIdBySetmealId")
    public  List<Integer> findTravelgroupIdBySetmealId(Integer id){
        List<Integer>    travelgroupIds=setmealService.findTravelgroupIdBySetmealId(id);
        return  travelgroupIds;
    }

    //编辑套餐游
    @RequestMapping("/edit")
    public  Result edit(Integer []travelgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.edit(travelgroupIds,setmeal);
            return  new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }


    @RequestMapping("/del")
    public  Result del(Integer id){
        try {
            setmealService.del(id);
            return  new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }

    }



}