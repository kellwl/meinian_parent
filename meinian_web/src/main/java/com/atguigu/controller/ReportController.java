package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import com.atguigu.util.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: meinian_parent
 * @description:
 * @author: lwl
 * @create: 2022-06-09 23:38
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        try {
            List<String>   setmealNames= new ArrayList<>();
            List<Map>  setmealCount=setmealService.getSetmealReport();

            for (Map map : setmealCount) {
                setmealNames.add((String) map.get("name"));
            }
            Map map=new HashMap<>();
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", setmealCount);

            return  new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


    @RequestMapping("/getBusinessReportData")
    public  Result getBusinessReportData(){
        try {
            Map<String,Object> map=reportService.getBusinessReportData();
            return  new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public  void exportBusinessReport(HttpServletRequest request,HttpServletResponse response){
        try {
            //1.拿数据
            Map<String,Object> result=reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //2.获取模版文件
            String filepath=request.getSession().getServletContext().getRealPath("template")+ File.separator +"report_template.xlsx";
            //3.工作簿
            Workbook workbook=new XSSFWorkbook(new File(filepath));
            //拿第一个表
            Sheet sheet = workbook.getSheetAt(0);

            //4.写数据
            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数


            int rowNum=12;
            for (Map map : hotSetmeal) {//热门套餐
                String name= (String) map.get("name");
                Long setmeal_count=(Long)map.get("setmeal_count");
                BigDecimal proportion=(BigDecimal)map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }
            //5.输出文件,以流的形式,文件下载,另存为操作
            ServletOutputStream out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            //6.关闭流
            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.getRequestDispatcher("/pages/error/downerror.html").forward(request, response);
            } catch (ServletException servletException) {
                servletException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }



//    需要月份 和  会员数量
    @RequestMapping("/getMemberReport")
    public  Result getMemberReport(){
        //获取日历对象
        Calendar calendar=Calendar.getInstance();
        //两个参数  1.日历字段 2.要添加到字段中的日期或者时间
        //根据当前时间获取前12个月份的日历
        calendar.add(Calendar.MONTH, -12);
        List<String> list=new ArrayList<>();
        for (int i=0;i<12;i++){
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        Map<String,Object> map=new HashMap<>();
        map.put("months", list);
        List<Integer> memberCount=memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);
        return  new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }
}