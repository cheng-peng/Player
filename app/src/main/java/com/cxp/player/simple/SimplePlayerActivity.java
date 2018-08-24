package com.cxp.player.simple;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SimplePlayerActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 8:59
 * 描    述: 简单直接播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimplePlayerActivity extends BaseActivity {

    @BindView(R.id.simple_player)
    StandardGSYVideoPlayer mSimplePlayer;

    //处理屏幕旋转逻辑
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
        ButterKnife.bind(this);
        //初始化
        init();
    }

    //初始化
    private void init() {

        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        mSimplePlayer.setUp(source1, true, "么么哒");

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp);
        //增加封面 （不自动播放时显示）
        mSimplePlayer.setThumbImageView(imageView);
        //增加title
        mSimplePlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        mSimplePlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, mSimplePlayer);
        //是否可以滑动调整 （默认为true）
        mSimplePlayer.setIsTouchWiget(true);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mSimplePlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //设置返回按键功能
        mSimplePlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //开始播放 (自动播放)
        mSimplePlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimplePlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimplePlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放所有
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            //释放屏幕旋转监听
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mSimplePlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mSimplePlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
