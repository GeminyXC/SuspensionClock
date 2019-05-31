package utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralUtils {


    //1）写入数据：目前写入的数据有：inviteCode、presetMillisecond、imei
    public static void writeData(Context content, String key, String value) {

        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = content.getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(key, value);
        //步骤4：提交
        editor.commit();

    }


    //2）读取数据：
    public static String readData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String returnString = sharedPreferences.getString(key, "");
        return returnString;
    }

    //3）删除指定数据
    public static void deleteData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


    //4）清空数据
    public static void clearAllData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public static String getDateString(Context context, String timeStampString){

        long timeStamp = Long.parseLong(timeStampString);//Long.valueOf(timeStampString);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeStamp);
        String dateString = simpleDateFormat.format(date);

        return  dateString;
    }

}

