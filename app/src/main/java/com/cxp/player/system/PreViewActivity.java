package com.cxp.player.system;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.video.PreViewVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: PreViewActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 0:03
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class PreViewActivity extends BaseActivity {

    @BindView(R.id.preview_video)
    PreViewVideo mPreviewVideo;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.web_top_layout)
    NestedScrollView mWebTopLayout;
    @BindView(R.id.web_top_layout_video)
    RelativeLayout mWebTopLayoutVideo;

    private int backupRendType;

    //处理屏幕旋转逻辑
    private OrientationUtils orientationUtils;

    private Context mContext;

    private boolean isSmall;

    //播放
    private boolean isPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        mContext = this;

        //打开小图预览
        mPreviewVideo.setOpenPreView(true);

        backupRendType = GSYVideoType.getRenderType();

        //设置为Surface播放模式，注意此设置是全局的
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mPreviewVideo);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        ImageView imageView = new ImageView(this);
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(true)
                .setAutoFullWithSize(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle("程小鹏")
                .setVideoAllCallBack(new GSYSampleCallBack(){
                        //加载成功
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                        }
                })
                .build(mPreviewVideo);

        mPreviewVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏幕切换
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mPreviewVideo.startWindowFullscreen(mContext, true, true);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com");

        mWebTopLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!mPreviewVideo.isIfCurrentIsFullscreen() && scrollY >= 0 && isPlay) {
                    if (scrollY > mPreviewVideo.getHeight()) {
                        //如果是小窗口就不需要处理
                        if (!isSmall) {
                            isSmall = true;
                            int size = CommonUtil.dip2px(mContext, 150);
                            mPreviewVideo.showSmallVideo(new Point(size, size), true, true);
                            orientationUtils.setEnable(false);
                        }
                    } else {
                        if (isSmall) {
                            isSmall = false;
                            orientationUtils.setEnable(true);
                            //必须
                            mWebTopLayoutVideo.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPreviewVideo.hideSmallVideo();
                                }
                            }, 50);
                        }
                    }
                    mWebTopLayoutVideo.setTranslationY((scrollY <= mWebTopLayoutVideo.getHeight()) ? -scrollY : -mWebTopLayoutVideo.getHeight());
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreviewVideo.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreviewVideo.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //设置为GL播放模式，才能支持滤镜，注意此设置是全局的
        GSYVideoType.setRenderType(backupRendType);
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
            mPreviewVideo.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mPreviewVideo.setVideoAllCallBack(null);
        super.onBackPressed();
    }


}
