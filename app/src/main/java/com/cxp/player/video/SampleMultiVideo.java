package com.cxp.player.video;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cxp.player.video.manager.CustomManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

/**
 * 文 件 名: SampleMultiVideo
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 16:40
 * 描    述: 多任务播放器
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SampleMultiVideo extends SampleCoverVideo{

    private final static String TAG = "SampleMultiVideo";

    public SampleMultiVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleMultiVideo(Context context) {
        super(context);
    }

    public SampleMultiVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        break;
                }
            }
        };
    }

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        CustomManager.getCustomManager(getKey()).initContext(getContext().getApplicationContext());
        return CustomManager.getCustomManager(getKey());
    }

    @Override
    protected boolean backFromFull(Context context) {
        return CustomManager.backFromWindowFull(context, getKey());
    }

    @Override
    protected void releaseVideos() {
        CustomManager.releaseAllVideos(getKey());
    }


    @Override
    protected int getFullId() {
        return CustomManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return CustomManager.SMALL_ID;
    }

    public String getKey() {
        if (mPlayPosition == -22) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
        }
        if (TextUtils.isEmpty(mPlayTag)) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
        }
        return TAG + mPlayPosition + mPlayTag;
    }
}

