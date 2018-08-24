package com.cxp.player.base;

import android.app.Application;

import com.cxp.player.utils.CrashHandler;

/**
 * 文 件 名: MyApplication
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 8:47
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //崩溃捕获
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());

//        //默认ijk播放内核
//        GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
//        //EXO 2 播放内核
//        GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKEXOPLAYER2);
//        //系统播放器
//        GSYVideoManager.instance().setVideoType(this, GSYVideoType.SYSTEMPLAYER);

    }
}
