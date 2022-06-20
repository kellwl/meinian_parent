package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-05-30 01:13
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;


    @Override
    public void addBatch(List<OrderSetting> listData) {
        for (OrderSetting orderSetting : listData) {
            //如果日期对应设置存在,就修改,否则就添加
            int count = orderSettingDao.findOrderSettingByRrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                //存在执行修改操作
                orderSettingDao.edit(orderSetting);
            } else {
                //执行添加操作
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMouth(String date) {
        String startDate = date + "-1";
        String endDate = date + "-31";
        Map param = new HashMap();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMouth(param);
        List<Map> listData = new ArrayList<Map>();
        for (OrderSetting orderSetting : list) {
            Map map = new HashMap();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            listData.add(map);
        }
        return listData;
    }

    @Override
    public void editNumberByOrderDate(Map map) {
       /* value:value,
           orderDate:str*/
//        Integer count=

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strdate = (String)map.get("orderDate");
            Date date = sdf.parse(strdate);
            int count = orderSettingDao.findOrderSettingByRrderDate(date);
            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setOrderDate(date);
            orderSetting.setNumber(Integer.parseInt((String) map.get("value")));
            if (count > 0) {
                //数据库中存在执行修改操作
                orderSettingDao.edit(orderSetting);
            } else {
                //执行添加操作
                orderSettingDao.add(orderSetting);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}