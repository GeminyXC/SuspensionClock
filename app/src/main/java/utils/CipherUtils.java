package utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;


public class CipherUtils {

    private static final String key = "geminy";



    //IMEI加密:
    //1.先进行原IMEI的base64加密;
    //2.添加前缀geminy,再进行一次加密.


    //获取用户的IMEI进行一次加密
    public static String imeiOnceEncrypt(Context context, String text) {

        String imei = GeneralUtils.readData(context, "imei");

        //base64编码
        String encryptText = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT);

        return encryptText;

    }


    //获取用户的IMEI进行一次解密
    public static String imeiOnceDecrypt(Context context, String text) {

        //base64解码
        String decryptText = new String(Base64.decode(text.getBytes(), Base64.DEFAULT));

        return decryptText;
    }






    //获取用户的IMEI进行两次加密
    public static String imeiTwiceEncrypt(Context context, String text) {


        String imei = GeneralUtils.readData(context, "imei");

        //base64编码
        String firstEncryptText = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT);
        String midText = key + firstEncryptText;
        String secondEncryptText = Base64.encodeToString(midText.getBytes(), Base64.DEFAULT);

        Log.i("111","imeiTwiceEncrypt:1:"+firstEncryptText);
        Log.i("111","imeiTwiceEncrypt:2:"+midText);
        Log.i("111","imeiTwiceEncrypt:3:"+secondEncryptText);


        return secondEncryptText;
    }


    //获取用户的IMEI进行两次解密
    public static String imeiTwiceDecrypt(Context context, String text) {

        //base64解码
        String decryptText = new String(Base64.decode(text.getBytes(), Base64.DEFAULT));
        String midText = decryptText.replace(key, "");
        String secondDecryptText = new String(Base64.decode(midText.getBytes(), Base64.DEFAULT));

        Log.i("222","imeiTwiceDecrypt:1:"+decryptText);
        Log.i("222","imeiTwiceDecrypt:2:"+midText);
        Log.i("222","imeiTwiceDecrypt:3:"+secondDecryptText);

        return secondDecryptText;
    }




//    //加密
////    encrypt
//    public static String encrypt(Context context, String text) {
//
//
//        return null;
//    }
//
//
//    //解密
//    // decrypt
//    public static String decrypt(String text) {
//
//        return null;
//    }


}
