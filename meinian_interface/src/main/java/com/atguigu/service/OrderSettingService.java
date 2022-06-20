package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addBatch(List<OrderSetting> listData);

    List<Map> getOrderSettingByMouth(String date);

    void editNumberByOrderDate(Map map);
}
