package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-30 01:13
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    //将预约表相关信息上传到数据库
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> listData = new ArrayList<>();
            for (String[] strArray : strings) {
                OrderSetting orderSetting = new OrderSetting();
                //设置预定日期
                orderSetting.setOrderDate(new Date(strArray[0]));
                //设置可预约数量
                Integer number = Integer.valueOf(strArray[1]);
                orderSetting.setNumber(number);
//                //初始化已预约人数为0
//                orderSetting.setReservations(0);
                listData.add(orderSetting);
            }
            //批量添加导入
            orderSettingService.addBatch(listData);

            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FALL);
        }


    }


    @RequestMapping("/getOrderSettingByMouth")
    public Result getOrderSettingByMouth(String date) {
        try {
            List<Map> list = orderSettingService.getOrderSettingByMouth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/editNumberByOrderDate")
    public Result editNumberByOrderDate(@RequestBody Map map) {
        try {
            orderSettingService.editNumberByOrderDate(map);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}