package org.tongwoo.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    public static void outExcel(List<String> list) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
        HSSFSheet sheet = wb.createSheet("VehicleNo");   //--->创建了一个工作簿
        HSSFDataFormat format= wb.createDataFormat();   //--->单元格内容格式
        //sheet.setColumnWidth((short)3, 20* 256);    //---》设置单元格宽度，因为一个单元格宽度定了那么下面多有的单元格高度都确定了所以这个方法是sheet的
        //sheet.setColumnWidth((short)4, 20* 256);    //--->第一个参数是指哪个单元格，第二个参数是单元格的宽度
        sheet.setDefaultRowHeight((short)300);    // ---->有得时候你想设置统一单元格的高度，就用这个方法
        sheet.setDefaultColumnWidth((short)20);

        //样式1
        HSSFCellStyle style = wb.createCellStyle(); // 样式对象
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        //设置标题字体格式
        Font font = wb.createFont();
        //设置字体样式
        font.setFontHeightInPoints((short)20);   //--->设置字体大小
        font.setFontName("Courier New");   //---》设置字体，是什么类型例如：宋体
        font.setItalic(true);     //--->设置是否是加粗
        style.setFont(font);     //--->将字体格式加入到style中
        //style.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        //style.setFillPattern(CellStyle.SOLID_FOREGROUND);设置单元格颜色
        style.setWrapText(true);   //设置是否能够换行，能够换行为true
        style.setBorderBottom((short)1);   //设置下划线，参数是黑线的宽度
        style.setBorderLeft((short)1);   //设置左边框
        style.setBorderRight((short)1);   //设置有边框
        style.setBorderTop((short)1);   //设置下边框
        style.setDataFormat(format.getFormat("￥#,##0"));    //--->设置为单元格内容为货币格式

        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));    //--->设置单元格内容为百分数格式



        int rowIndex = 0;
        for(String val:list) {
            sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) 15));
            HSSFRow row = sheet.createRow(rowIndex);
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue(val);
            cell.setCellStyle(style);
            rowIndex++;
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("g:\\vehicle.xls");
            wb.write(outputStream);
            System.out.println("write excel success");
        }finally {
                outputStream.close();
        }
    }

    public static void outExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件
        HSSFSheet sheet = wb.createSheet("driverBaseInfo");   //--->创建了一个工作簿
        HSSFDataFormat format= wb.createDataFormat();   //--->单元格内容格式
        //sheet.setColumnWidth((short)3, 20* 256);    //---》设置单元格宽度，因为一个单元格宽度定了那么下面多有的单元格高度都确定了所以这个方法是sheet的
        //sheet.setColumnWidth((short)4, 20* 256);    //--->第一个参数是指哪个单元格，第二个参数是单元格的宽度
        sheet.setDefaultRowHeight((short)300);    // ---->有得时候你想设置统一单元格的高度，就用这个方法
        sheet.setDefaultColumnWidth((short)20);

        //样式1
        HSSFCellStyle style = wb.createCellStyle(); // 样式对象
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        //设置标题字体格式
        Font font = wb.createFont();
        //设置字体样式
        font.setFontHeightInPoints((short)20);   //--->设置字体大小
        font.setFontName("Courier New");   //---》设置字体，是什么类型例如：宋体
        font.setItalic(true);     //--->设置是否是加粗
        style.setFont(font);     //--->将字体格式加入到style中
        //style.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        //style.setFillPattern(CellStyle.SOLID_FOREGROUND);设置单元格颜色
        style.setWrapText(true);   //设置是否能够换行，能够换行为true
        style.setBorderBottom((short)1);   //设置下划线，参数是黑线的宽度
        style.setBorderLeft((short)1);   //设置左边框
        style.setBorderRight((short)1);   //设置有边框
        style.setBorderTop((short)1);   //设置下边框
        style.setDataFormat(format.getFormat("￥#,##0"));    //--->设置为单元格内容为货币格式

        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));    //--->设置单元格内容为百分数格式


        //TITLE
        String[] title = new String[]{"姓名","性别","出生日期","证件类型","证件号码","手机号","居住地址","身份证地址","驾驶证号","准驾车型","驾驶证档案编号","初次领证日期"};
        HSSFRow titleRow = sheet.createRow(0);
        int titleSize = 0;
        for(String str:title) {
            HSSFCell titleCell = titleRow.createCell((short) titleSize++);
            titleCell.setCellValue(str);
        }

        int rowIndex = 1;
        for(Map<String, Object> childMap:list) {
            //sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) 15));
            HSSFRow row = sheet.createRow(rowIndex);
            int colum = 0;
            for(String key:childMap.keySet()){
                HSSFCell cell = row.createCell((short) colum++);
                cell.setCellValue(String.valueOf(childMap.get(key)));
                //cell.setCellStyle(style);
            }
            rowIndex++;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String(("公安治安驾驶员预审.xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        System.out.println("write excel success");

    }

}
