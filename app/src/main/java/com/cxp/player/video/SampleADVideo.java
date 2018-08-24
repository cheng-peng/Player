package com.cxp.player.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cxp.player.R;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;

/**
 * 文 件 名: SampleVideoAD
 * 创 建 人: CXP
 * 创建日期: 2018-08-22 22:02
 * 描    述: 广告播放器
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SampleADVideo extends GSYSampleADVideoPlayer {

    public SampleADVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleADVideo(Context context) {
        super(context);
    }

    public SampleADVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_sample_ad;
    }

    /**
     * 移除广告播放的全屏
     */
    public void removeFullWindowViewOnly() {
        ViewGroup vp = (ViewGroup) (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(getFullId());
        if (old != null) {
            if (old.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) old.getParent();
                vp.removeView(viewGroup);
            }
        }
        mIfCurrentIsFullscreen = false;
    }
}
