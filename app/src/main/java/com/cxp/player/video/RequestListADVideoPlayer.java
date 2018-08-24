package com.cxp.player.video;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import java.util.Random;

/**
 * 文 件 名: RequestListADVideoPlayer
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 17:35
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class RequestListADVideoPlayer extends SampleADVideo {

    public RequestListADVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public RequestListADVideoPlayer(Context context) {
        super(context);
    }

    public RequestListADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void startPlayLogic() {
        //模拟请求
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int min = 1;
                int max = 10;
                Random random = new Random();
                int i = random.nextInt(max) % (max - min + 1) + min;
                if (i % 3 == 0) {
                    mOriginUrl = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
                    mUrl = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
                    mTitle = "ggg2";
                } else if (i % 4 == 0) {
                    //模拟请求失败
                    onError(0, 0);
                } else {
                    mOriginUrl = "http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4";
                    mUrl = "http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4";
                    mTitle = "ffff1";
                }
                mCache = false;
                RequestListADVideoPlayer.super.startPlayLogic();
            }
        }, 2000);
    }
}
