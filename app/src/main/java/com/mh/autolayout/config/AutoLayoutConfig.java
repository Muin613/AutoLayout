package com.mh.autolayout.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.mh.autolayout.utils.Debug;
import com.mh.autolayout.utils.ScreenUtils;


/**
 * Created by zhy on 15/11/18.
 * 默认竖屏为主流
 */
public class AutoLayoutConfig {

    private static AutoLayoutConfig sIntance = new AutoLayoutConfig();


    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static final String KEY_DESIGN_HEIGHT = "design_height";

    private int mScreenWidth;
    private int mScreenHeight;

    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize;


    private AutoLayoutConfig() {
    }

    public void checkParams() {
        if (mDesignHeight <= 0 || mDesignWidth <= 0) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.");
        }
    }

    //这是使用清单设置的
    public AutoLayoutConfig useDeviceSize() {
        useDeviceSize = true;
        return this;
    }
// 对特殊机型设置

//屏幕翻转
    public AutoLayoutConfig windowChange() {
        mScreenWidth = mScreenWidth ^ mScreenHeight;
        mScreenHeight = mScreenWidth ^ mScreenHeight;
        mScreenWidth = mScreenWidth ^ mScreenHeight;

        mDesignWidth = mDesignWidth ^ mDesignHeight;
        mDesignHeight = mDesignWidth ^ mDesignHeight;
        mDesignWidth = mDesignWidth ^ mDesignHeight;
        return this;
    }
//屏幕横屏
    public AutoLayoutConfig windowLandSpace(){
        if (mScreenWidth < mScreenHeight) {
            mScreenWidth = mScreenWidth ^ mScreenHeight;
            mScreenHeight = mScreenWidth ^ mScreenHeight;
            mScreenWidth = mScreenWidth ^ mScreenHeight;
        }
        if (mDesignWidth < mDesignHeight) {
            mDesignWidth = mDesignWidth ^ mDesignHeight;
            mDesignHeight = mDesignWidth ^ mDesignHeight;
            mDesignWidth = mDesignWidth ^ mDesignHeight;
        }
        return this;
    }
//屏幕竖屏
    public AutoLayoutConfig windowPortrait() {
        if (mScreenWidth > mScreenHeight) {
            mScreenWidth = mScreenWidth ^ mScreenHeight;
            mScreenHeight = mScreenWidth ^ mScreenHeight;
            mScreenWidth = mScreenWidth ^ mScreenHeight;
        }
        if (mDesignWidth > mDesignHeight) {
            mDesignWidth = mDesignWidth ^ mDesignHeight;
            mDesignHeight = mDesignWidth ^ mDesignHeight;
            mDesignWidth = mDesignWidth ^ mDesignHeight;
        }
        return this;
    }

    public static AutoLayoutConfig getInstance() {
        return sIntance;
    }


    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getDesignWidth() {
        return mDesignWidth;
    }

    public int getDesignHeight() {
        return mDesignHeight;
    }


    public void init(Context context) {
        getMetaData(context);

        int[] screenSize = ScreenUtils.getScreenSize(context, useDeviceSize);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
        Debug.e(" screenWidth =" + mScreenWidth + " ,screenHeight = " + mScreenHeight);
    }

    private void getMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
        }

        Debug.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }


}
