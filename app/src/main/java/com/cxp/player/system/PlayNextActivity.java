package com.cxp.player.system;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.video.PlayNextVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: PlayNextActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 9:59
 * 描    述: 播放下一集
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class PlayNextActivity extends BaseActivity {

    @BindView(R.id.play_next_video)
    PlayNextVideo mPlayNextVideo;

    //播放
    private boolean isPlay;
    //暂停
    private boolean isPause;

    //屏幕旋转
    private OrientationUtils orientationUtils;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_next);
        ButterKnife.bind(this);

        mContext=this;

        //String url = "http://baobab.wd jcdn.com/14564977406580.mp4";
        List<GSYVideoModel> urls = new ArrayList<>();
        urls.add(new GSYVideoModel("http://7xse1z.com1.z0.glb.clouddn.com/1491813192", "标题1"));
        urls.add(new GSYVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", "标题2"));
        urls.add(new GSYVideoModel("https://res.exexm.com/cw_145225549855002", "标题3"));
        urls.add(new GSYVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", "标题4"));
        mPlayNextVideo.setUp(urls, true, 0);
        
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp1);

        //增加title
        mPlayNextVideo.getTitleTextView().setVisibility(View.VISIBLE);
        mPlayNextVideo.getBackButton().setVisibility(View.VISIBLE);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mPlayNextVideo);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        mPlayNextVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏幕切换
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mPlayNextVideo.startWindowFullscreen(mContext, true, true);
            }
        });

        mPlayNextVideo.setIsTouchWiget(true);
        mPlayNextVideo.setRotateViewAuto(false);
        mPlayNextVideo.setLockLand(true);
        mPlayNextVideo.setShowFullAnimation(false);
        //mPlayNextVideo.setNeedLockFull(true);
        mPlayNextVideo.setAutoFullWithSize(false);

        mPlayNextVideo.setVideoAllCallBack(new GSYSampleCallBack(){

            //加载成功
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            //退出全屏
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                //隐藏调全屏对象的返回按键
                GSYVideoPlayer gsyVideoPlayer = (GSYVideoPlayer) objects[1];
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            }

        });

        mPlayNextVideo.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

    }

    @OnClick(R.id.play_next_bt)
    public void onViewClicked() {
        mPlayNextVideo.playNext();
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
        mPlayNextVideo.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        mPlayNextVideo.getCurrentPlayer().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            mPlayNextVideo.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mPlayNextVideo.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

}
