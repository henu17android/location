package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证数据
 * @author qmn
 */
public class ValidateUtil {

     public static boolean isMobileNo(String mobiles) {

         boolean flag = false;
         try{
             Pattern regex = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");
             Matcher m = regex .matcher(mobiles);
             flag = m.matches();
         }catch(Exception e){
             flag = false;
         }
         return flag;

     }

     public static boolean isPWD(String password) {
         boolean flag= false;
         if (password == null || password.length()<=8) {
             return false;
         }

         Pattern regex = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
         Matcher matcher = regex.matcher(password);
         flag = matcher.matches();
         return  flag;
     }


}
