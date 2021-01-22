package com.iblogstreet.liveness;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.iblogstreet.liveness.sdk.activity.LivenessActivity;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.Market;
import io.dcloud.feature.uniapp.common.UniModule;

public class LivenessPlugin extends UniModule {

    static final int REQUEST_CODE_LIVENESS = 1000;
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private Context mContext;

    @JSMethod(uiThread = true)
    public void testPlugin(JSONObject mOptions, JSCallback callBack){
        Log.e("testPlugin", "成功调用!" );
        JSONObject data = new JSONObject();
        /***********************************************************/
        // 初始化方法不耗时，非必须在 application 中初始化，但要确保进入 LivenessActivity 之前完成调用
       /* Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            public void run() {
                *//*if (LivenessPlugin.this.mWXSDKInstance.getContext() instanceof Activity) {
                    LivenessPlugin.this.mContext = LivenessPlugin.this.mWXSDKInstance.getContext();
                    String license = mOptions.getString("license");
                    Log.e("testPlugin","获取到license");
                    if (license.length() == 0){
                        return;
                    }
                    GuardianLivenessDetectionSDK.init(((Activity)mContext).getApplication(), Market.Indonesia);
                    String checkResult = GuardianLivenessDetectionSDK.setLicenseAndCheck(license);
                    if ("SUCCESS".equals(checkResult)) {
                        // license 有效
                        checkPermissions();
                    } else {// license 不可用，过期或者格式错误
                        //Toast.makeText(MainActivity.this, checkResult, Toast.LENGTH_SHORT).show();
                        callBack.invoke("出错了");
                    }
                }*//*
            }
        });*/
        if (LivenessPlugin.this.mWXSDKInstance.getContext() instanceof Activity) {
            LivenessPlugin.this.mContext = LivenessPlugin.this.mWXSDKInstance.getContext();
            String license = mOptions.getString("license");
            Log.e("testPlugin","获取到license");
            if (license.length() == 0){
                return;
            }
            GuardianLivenessDetectionSDK.init(((Activity)mContext).getApplication(), Market.Indonesia);
            String checkResult = GuardianLivenessDetectionSDK.setLicenseAndCheck(license);
            checkPermissions();
//            if ("SUCCESS".equals(checkResult)) {
//                // license 有效
//
//            } else {// license 不可用，过期或者格式错误
//                //Toast.makeText(MainActivity.this, checkResult, Toast.LENGTH_SHORT).show();
//                callBack.invoke("出错了");
//            }
        }
       // callBack.invoke(data);
    }


    public void checkPermissions() {
        if (allPermissionsGranted()) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, getRequiredPermissions(), PERMISSIONS_REQUEST_CODE);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(mContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Got camera permissions
     */
    public void onPermissionGranted() {
        Intent intent = new Intent(mContext, LivenessActivity.class);
        mContext.startActivity(intent);
//        startActivityForResult(intent, REQUEST_CODE_LIVENESS);
    }

    public String[] getRequiredPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }
}
