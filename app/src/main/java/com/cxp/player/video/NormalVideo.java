package com.cxp.player.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cxp.player.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * 文 件 名: NormalVideo
 * 创 建 人: CXP
 * 创建日期: 2018-08-22 22:32
 * 描    述: 默认播放器
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class NormalVideo extends StandardGSYVideoPlayer {

    public NormalVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public NormalVideo(Context context) {
        super(context);
    }

    public NormalVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_normal;
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
        }
    }
}