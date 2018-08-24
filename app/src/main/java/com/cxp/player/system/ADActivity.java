package com.cxp.player.system;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.video.SampleADVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: ADActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-22 21:22
 * 描    述: 带广告播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class ADActivity extends BaseActivity {

    @BindView(R.id.ad_one_player)
    SampleADVideo mAdOnePlayer;


    //屏幕旋转
    private OrientationUtils orientationUtils;

    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);

        mContext=this;

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mAdOnePlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        mAdOnePlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏幕切换
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mAdOnePlayer.startWindowFullscreen(mContext, true, true);
            }
        });

        ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> urls = new ArrayList<>();
        //广告1
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4",
                "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD));
        //正式内容1
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4",
                "正文1标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));
        //广告2
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4",
                "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD, true));
        //正式内容2
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4",
                "正文2标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));

        mAdOnePlayer.setAdUp(urls, true, 0);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp1);
        mAdOnePlayer.setThumbImageView(imageView);

        //增加title
        mAdOnePlayer.getTitleTextView().setVisibility(View.VISIBLE);
        mAdOnePlayer.getBackButton().setVisibility(View.VISIBLE);

        mAdOnePlayer.setIsTouchWiget(true);
        //关闭自动旋转
        mAdOnePlayer.setRotateViewAuto(false);
        mAdOnePlayer.setLockLand(false);
        mAdOnePlayer.setShowFullAnimation(false);
        mAdOnePlayer.setNeedLockFull(true);

        mAdOnePlayer.setVideoAllCallBack(new GSYSampleCallBack(){
            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                //隐藏调全屏对象的返回按键
                GSYVideoPlayer gsyVideoPlayer = (GSYVideoPlayer) objects[1];
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            }

            //退出全屏
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }

        });

        mAdOnePlayer.setLockClickListener(new LockClickListener() {
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
        mAdOnePlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAdOnePlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }



}
