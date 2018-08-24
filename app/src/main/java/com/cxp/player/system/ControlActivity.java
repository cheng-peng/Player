package com.cxp.player.system;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.model.SwitchVideoModel;
import com.cxp.player.utils.CommonUtil;
import com.cxp.player.utils.T;
import com.cxp.player.video.SampleVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoGifSaveListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.GifCreateHelper;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: ControlActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 17:49
 * 描    述: 控制播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class ControlActivity extends BaseActivity {

    @BindView(R.id.control_video)
    SampleVideo mControlVideo;
    @BindView(R.id.loadingView)
    RelativeLayout mLoadingView;
    @BindView(R.id.control_change_speed)
    Button mControlChangeSpeed;

    private Context mContext;

    private OrientationUtils orientationUtils;

    //gif管理类
    private GifCreateHelper mGifCreateHelper;

    private String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    //private String url = "http://livecdn1.news.cn/Live_MajorEvent01Phone/manifest.m3u8";
    //private String url = "https://ruigongkao.oss-cn-shenzhen.aliyuncs.com/transcode/video/source/video/8908d124aa839d0d3fa9593855ef5957.m3u8";
    //private String url2 = "http://ruigongkao.oss-cn-shenzhen.aliyuncs.com/transcode/video/source/video/3aca1a0db8db9418dcbc765848c8903e.m3u8";

    private float speed = 1;

    //播放
    private boolean isPlay;
    //暂停
    private boolean isPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);

        mContext = this;

        //初始化
        init();

        //初始化gif配置
        initGifHelper();
    }

    private void init() {
        String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

        //String url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
        //需要路径的
        //mControlVideo.setUp(url, true, new File(FileUtils.getPath()), "");

        //借用了jjdxm_ijkplayer的URL
        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        String name2 = "清晰";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);

        mControlVideo.setUp(list, true, "测试视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp1);
        mControlVideo.setThumbImageView(imageView);

        //根据尺寸自己选择横竖屏
        mControlVideo.setAutoFullWithSize(false);
        //默认全屏横屏
        mControlVideo.setLockLand(true);
        //增加title
        mControlVideo.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        mControlVideo.getBackButton().setVisibility(View.GONE);

        //全屏动画
        mControlVideo.setShowFullAnimation(false);

        //锁频功能
        mControlVideo.setNeedLockFull(true);

        //设置旋转
        orientationUtils = new OrientationUtils(this, mControlVideo);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mControlVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏幕切换
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mControlVideo.startWindowFullscreen(mContext, true, true);
            }
        });

        //mControlVideo.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        //mControlVideo.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
        //mControlVideo.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
        //mControlVideo.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
        //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
        //mControlVideo.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

        //是否可以滑动调整
        mControlVideo.setIsTouchWiget(true);

        //设置返回按键功能
        mControlVideo.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mControlVideo.setVideoAllCallBack(new GSYSampleCallBack(){
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
        });

        mControlVideo.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });
    }


    @OnClick({R.id.control_change_speed, R.id.control_shot, R.id.control_start_gif, R.id.control_stop_gif})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.control_change_speed:
                //播放速度比例
                resolveTypeUI();
                break;
            case R.id.control_shot:
                //获取截图
                mControlVideo.taskShotPic(new GSYVideoShotListener() {
                    @Override
                    public void getBitmap(Bitmap bitmap) {
                        if (bitmap != null) {
                            try {
                                CommonUtil.saveBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                T.show(mContext, "save fail ");
                                e.printStackTrace();
                                return;
                            }
                            T.show(mContext, "save success ");
                        } else {
                            T.show(mContext, "get bitmap fail ");
                        }
                    }
                });
                break;
            case R.id.control_start_gif:
                //开始缓存各个帧
                mGifCreateHelper.startGif(new File(FileUtils.getPath()));
                break;
            case R.id.control_stop_gif:
                mLoadingView.setVisibility(View.VISIBLE);
                mGifCreateHelper.stopGif(new File(FileUtils.getPath(), "CXP-Z-" + System.currentTimeMillis() + ".gif"));
                break;
        }
    }


    /**
     * 播放速度比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (speed == 1) {
            speed = 1.5f;
        } else if (speed == 1.5f) {
            speed = 2f;
        } else if (speed == 2) {
            speed = 0.5f;
        } else if (speed == 0.5f) {
            speed = 0.25f;
        } else if (speed == 0.25f) {
            speed = 1;
        }
        mControlChangeSpeed.setText("播放速度：" + speed);
        //设置播放速度
        mControlVideo.setSpeedPlaying(speed, true);
    }

    /**
     * 初始化gif配置
     */
    private void initGifHelper() {
        mGifCreateHelper = new GifCreateHelper(mControlVideo, new GSYVideoGifSaveListener() {
            @Override
            public void result(boolean success, File file) {
                mControlVideo.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingView.setVisibility(View.GONE);
                        Toast.makeText(mControlVideo.getContext(), "创建成功", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void process(int curPosition, int total) {
                Debuger.printfError(" current " + curPosition + " total " + total);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mControlVideo.onVideoPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mControlVideo.onVideoResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            mControlVideo.getCurrentPlayer().release();
        }
        mGifCreateHelper.cancelTask();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mControlVideo.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }


}
