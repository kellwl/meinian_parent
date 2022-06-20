package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-14 15:22
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    AddressService addressService;

    @RequestMapping("/findAllMaps")
   public Map findAllMaps(){

        Map  map=new HashMap();//标记地址的经纬度
        List<Address> list=addressService.findAllMaps();//标记地址名称
        List<Map> gridnMaps=new ArrayList<>();
        List<Map> nameMaps=new ArrayList<>();
        for (Address address : list) {
            Map<String,String> mapname=new HashMap<>();
            Map gridnMap=new HashMap();
            String lng=address.getLng();
            String lat=address.getLat();
            String addressName = address.getAddressName();
            mapname.put("addressName",addressName);
            gridnMap.put("lng",lng);
            gridnMap.put("lat",lat);
            nameMaps.add(mapname);
            gridnMaps.add(gridnMap);
        }
        map.put("gridnMaps", gridnMaps);
        map.put("nameMaps",nameMaps);
        return  map;

   }


   @RequestMapping("/findPage")
   public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult= addressService.findPage(queryPageBean);
       return  pageResult;
   }

   @RequestMapping("/addAddress")
   public  Result addAddress(@RequestBody Address address){
       try {
           addressService.addAddress(address);
           return  new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
       } catch (Exception e) {
           e.printStackTrace();
           return  new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
       }
   }

   @RequestMapping("/deleteById")
   public  Result deleteById(Integer id){
       try {
           addressService.deleteById(id);
           return  new Result(true, MessageConstant.DELETE_ADDRESS_SUCCESS);
       } catch (Exception e) {
           e.printStackTrace();
           return  new Result(false, MessageConstant.DELETE_ADDRESS_FAIL);
       }
   }
}