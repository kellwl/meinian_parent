package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-04 15:36
 */
@Transactional
@Service(interfaceClass =OrderService.class )
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderSettingDao orderSettingDao;


//5、预约成功，更新当日的已预约人数
    @Override
    public Result saveOrder(Map map) throws Exception {
//    1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        int setmealId= Integer.parseInt((String) map.get("setmealId"));

        //字符串转date
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
        if (orderSetting == null) {
            //预约设置不存在,不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else {
            //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约

            int number=orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if (reservations>=number){
                return  new Result(false, MessageConstant.ORDER_FULL);
            }
        }
        //3、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        String telephone= (String) map.get("telephone");
        Member member=memberDao.getMemberBytelephone(telephone);
        if (member==null){
            //不为会员,则进行注册操作
            member=new Member();
            String name= (String) map.get("name");
            String sex= (String) map.get("sex");
            String idCard= (String) map.get("idCard");
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);//需要主键回填  ,后面需要用到memberId
        }else {
            //是会员
            //4、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
            //封装为一个通用方法,可以实现动态查询
            Order orderParam=new Order();
            orderParam.setOrderDate(date);
            orderParam.setSetmealId(setmealId);
            orderParam.setMemberId(member.getId());
            List<Order> orderList =orderDao.findOrderByCondition(orderParam);

            if (orderList!=null  && orderList.size()>0){
                //该id已经预约过了,不能重复预约
                return  new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //修改预约设置  ordersetting
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //4.进行预约操作    ,(1)更新当日的已预约人数   (2)往订单表中添加数据
        Order order=new Order();
        order.setMemberId(member.getId());
        order.setSetmealId(setmealId);
        order.setOrderDate(date);
        order.setOrderStatus("未出游");
        order.setOrderType("微信预约");
        orderDao.add(order);




        return  new Result(true, MessageConstant.ORDER_SUCCESS,order);







    }


    /*
    会员姓名：{{orderInfo.member}}
旅游套餐：{{orderInfo.setmeal}}
旅游日期：{{orderInfo.orderDate}}
预约类型：{{orderInfo.orderType}}
    * */
    @Override
    public Map<String, Object> findById(Integer id) {
        try {
            Map<String,Object> map=orderDao.findById(id);
            Date date = (Date) map.get("orderDate");
            String orderDate = DateUtils.parseDate2String(date);
            map.put("orderDate",orderDate);
            return  map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


}
