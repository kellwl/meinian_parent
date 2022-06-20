package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-03 11:52
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    SetmealService setmealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmeals=setmealService.getSetmeal();
            return  new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }

@RequestMapping("/findById")
    public  Result findById(Integer id){
    try {
        Setmeal setmeal=setmealService.findSetmeal(id);
        return  new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    } catch (Exception e) {
        e.printStackTrace();
        return  new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

}
}