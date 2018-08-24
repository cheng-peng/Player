package com.cxp.player.system;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.video.DanmakuVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: DanmakuActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 17:42
 * 描    述: 弹幕播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class DanmakuActivity extends BaseActivity {

    @BindView(R.id.danmaku_video)
    DanmakuVideo mDanmakuVideo;

    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmaku);
        ButterKnife.bind(this);

        mContext=this;

        //使用自定义的全屏切换图片，!!!注意xml布局中也需要设置为一样的
        //必须在setUp之前设置
        mDanmakuVideo.setShrinkImageRes(R.drawable.custom_shrink);
        mDanmakuVideo.setEnlargeImageRes(R.drawable.custom_enlarge);

        String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        mDanmakuVideo.setUp(url, true, null, "测试视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp1);
        mDanmakuVideo.setThumbImageView(imageView);

        //增加title
        mDanmakuVideo.getTitleTextView().setVisibility(View.GONE);
        mDanmakuVideo.getBackButton().setVisibility(View.GONE);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mDanmakuVideo);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        mDanmakuVideo.setIsTouchWiget(true);
        //关闭自动旋转
        mDanmakuVideo.setRotateViewAuto(false);
        mDanmakuVideo.setLockLand(false);
        mDanmakuVideo.setShowFullAnimation(false);
        mDanmakuVideo.setNeedLockFull(true);

        //detailPlayer.setOpenPreView(true);
        mDanmakuVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mDanmakuVideo.startWindowFullscreen(mContext, true, true);
            }
        });

        mDanmakuVideo.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        mDanmakuVideo.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mDanmakuVideo.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (mDanmakuVideo.getFullWindowPlayer() != null) {
            return  mDanmakuVideo.getFullWindowPlayer();
        }
        return mDanmakuVideo;
    }
}
