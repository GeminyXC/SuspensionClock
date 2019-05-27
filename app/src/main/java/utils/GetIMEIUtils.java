package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class GetIMEIUtils {

    public final static int REQUEST_READ_PHONE_STATE = 1;

    public static HashMap<String,Object> getIMEI(Context context){

        HashMap<String ,Object> map = new HashMap<String, Object>();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

            map.put("isAllowed",false);
            map.put("imei","");

        }else {

            //存储获取到的手机IMEI
            GeneralUtils.writeData(context,"imei",tm.getDeviceId());

            map.put("isAllowed",true);
            map.put("imei",tm.getDeviceId());

        }


        return map;


    }




}
