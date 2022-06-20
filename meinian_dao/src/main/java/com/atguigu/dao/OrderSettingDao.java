package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);

    int findOrderSettingByRrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMouth(Map param);



    //添加预约设置
    void  insert(String date);



    void editReservationsByOrderDate(OrderSetting orderSetting);

    OrderSetting getOrderSettingByOrderDate(Date date);
}
