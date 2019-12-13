package com.example.util;

import android.content.Context;
import android.widget.Toast;

import com.example.bean.GroupSignInMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
//    public static WritableFont arial14font = null;
//    public static WritableCellFormat arial14format = null;
    public static WritableFont arial10font = null;
    public static WritableCellFormat arial10format = null;
//    public static WritableFont arial12font = null;
//    public static WritableCellFormat arial12format = null;

    public final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式 背景颜色等
     */
    public static void format(){
        try{
//            arial14font = new WritableFont(WritableFont.ARIAL,14,WritableFont.BOLD);
//            arial14font.setColour(Colour.LIGHT_BLUE);
//            arial14format = new WritableCellFormat(arial14font);
//            arial14format.setAlignment(Alignment.CENTRE);
//            arial14format.setBorder(Border.ALL,BorderLineStyle.THIN);
//            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(Alignment.CENTRE);
            arial10format.setBorder(Border.ALL,BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

//            arial12font = new WritableFont(WritableFont.ARIAL,10);
//            arial12format = new WritableCellFormat(arial12font);
//            arial12format.setAlignment(Alignment.CENTRE);
//            arial12format.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN);
        }catch (WriteException e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     */
    public static void initExcel(String fileName,String[] colName){
        format();
        WritableWorkbook workbook = null;
        try{
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("签到记录",0);
            sheet.addCell(new Label(0,0,fileName,arial10format));
            for(int col = 0; col < colName.length; col++){
                sheet.addCell(new Label(col,0,colName[col],arial10format));
            }
            workbook.write();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(workbook != null){
                try {
                    workbook.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T>  boolean writeObjListToExcel(List<T> objList, String fileName){
        if(objList != null && objList.size() > 0){
            WritableWorkbook writebook = null;
            InputStream in = null;
            try{
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName),workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for(int j = 0; j < objList.size(); j++){
                    GroupSignInMessage gm = (GroupSignInMessage) objList.get(j);
                    List<String> list = new ArrayList<>();
                    list.add(String.valueOf(j+1));
                    list.add(gm.getReceiverId());
                    list.add(TimeTransform.stampToTime(gm.getStartTime()));
                    if(gm.isDone())
                        list.add("签到成功");
                    else
                        list.add("签到失败");
                    for(int i = 0; i < list.size(); i++){
                        sheet.addCell(new Label(i,j+1,list.get(i),arial10format));
                    }
                }
                writebook.write();
                return true;
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(writebook != null){
                    try{
                        writebook.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(in != null){
                    try{
                        in.close();
                    } catch (IOException ie){
                        ie.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}

