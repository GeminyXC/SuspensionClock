package utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


public class CipherUtils {

    private static final String key = "geminy";


    //IMEI加密:
    //1.先进行原IMEI的base64加密;
    //2.添加前缀geminy,再进行一次加密.


    //获取用户的IMEI进行一次加密
    public static String imeiOnceEncrypt(Context context) {

        String imei = GeneralUtils.readData(context, "imei");

        //base64编码
        String encryptText = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT);

        return encryptText;

    }


    //获取用户注册码进行一次解密
    public static String imeiOnceDecrypt(Context context, String text) {

        //base64解码
        String decryptText = new String(Base64.decode(text.getBytes(), Base64.DEFAULT));

        return decryptText;
    }

    //对用户的注册码进行二次加密生成邀请码
    public static String registerCodeEncrypt(Context context, float validDays, String registerCode) {



        // 2) 生成中间字符
        long systemTimestamp = System.currentTimeMillis();
        long validMillisecond = (long)(validDays * 24 * 60 * 60 * 1000);
        long validTimestamp = systemTimestamp + validMillisecond;
        String midText = validTimestamp + key + registerCode;
        // 3) 第二次加密
        String secondEncryptText = Base64.encodeToString(midText.getBytes(), Base64.DEFAULT);
        // 4) 将二次加密字符的012号位字符跟345号位交换位置
        String subStr1 = secondEncryptText.substring(0, 3);
        String subStr2 = secondEncryptText.substring(3, 6);
        String subStr3 = secondEncryptText.substring(6);
        String finalEncryptText = subStr2 + subStr1 + subStr3;

        return finalEncryptText;

    }


    //获取用户的IMEI进行两次加密
    public static String imeiTwiceEncrypt(Context context, int validDays) {


        String imei = GeneralUtils.readData(context, "imei");

        //base64编码
        // 1）第一次加密
        String firstEncryptText = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT);
        // 2) 生成中间字符
        long validTimestamp = System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000;
        String midText = validTimestamp + key + firstEncryptText;
        // 3) 第二次加密
        String secondEncryptText = Base64.encodeToString(midText.getBytes(), Base64.DEFAULT);
        // 4) 将二次加密字符的012号位字符跟345号位交换位置
        String subStr1 = secondEncryptText.substring(0, 3);
        String subStr2 = secondEncryptText.substring(3, 6);
        String subStr3 = secondEncryptText.substring(6);
        String finalEncryptText = subStr2 + subStr1 + subStr3;

        return finalEncryptText;
    }


    //获取用户邀请码进行两次解密
    public static String imeiTwiceDecrypt(Context context, String text,boolean isGetIMEI) {

        String secondDecryptText = text;

        //base64解码
        // 1)将邀请码012号位和345号位恢复至原来位置
        if (text.length() < 7) {
            return secondDecryptText;
        }
        String subStr1 = text.substring(0, 3);
        String subStr2 = text.substring(3, 6);
        String subStr3 = text.substring(6);
        String initialDecryptText = subStr2 + subStr1 + subStr3;
        // 2) 第一次解密
        String firstDecryptText = new String(Base64.decode(initialDecryptText.getBytes(), Base64.DEFAULT));
        // 3) 去除中间字符
        String[] midTexts = firstDecryptText.split(key);
        if (midTexts.length < 2) {
            return secondDecryptText;
        }
        String firstText = midTexts[0];//该处获取到的就是时间戳
        String lastText = midTexts[1];
        // 2) 第二次解密，实际得到的就应该是IMEI
        secondDecryptText = new String(Base64.decode(lastText.getBytes(), Base64.DEFAULT));

        if (!isGetIMEI){//不是获取IMEI，则意味着获取时间戳
            return  firstText;
        }


        return secondDecryptText;
    }

    public static boolean verifyInviteCode(Context context, String inviteCode) {

        boolean isValid = false;
        //如果邀请码为空，则无效
        if (TextUtils.isEmpty(inviteCode)){
            return isValid;
        }
        //读取本地存入的IMEI
        String imei = GeneralUtils.readData(context, "imei");
        //如果获取不到本地存入的IMEI，则无效
        if (TextUtils.isEmpty(imei)){
            return isValid;
        }
        //
        //解析用户的邀请码生成IMEI
        String beVerifiedIMEI = imeiTwiceDecrypt(context, inviteCode,true);
        //解析用户的邀请码生成时间戳
        String timeStampString =  imeiTwiceDecrypt(context, inviteCode,false);
        long timeStamp = Long.parseLong(timeStampString);//Long.valueOf(timeStampString);

        //小于当前时间戳，则已失效
        if (timeStamp < System.currentTimeMillis()){
            return isValid;
        }
        if (beVerifiedIMEI.equals(imei)) {
            isValid = true;
        }


        return isValid;
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
