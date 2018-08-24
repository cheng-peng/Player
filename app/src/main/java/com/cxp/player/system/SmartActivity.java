package com.cxp.player.system;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.model.SwitchVideoModel;
import com.cxp.player.video.SmartPickVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SmartActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-22 23:14
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SmartActivity extends BaseActivity {

    @BindView(R.id.smart_pick_video)
    SmartPickVideo mSmartPickVideo;

    //处理屏幕旋转逻辑
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart);
        ButterKnife.bind(this);
        //初始化
        init();
    }

    //初始化
    private void init() {

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

        mSmartPickVideo.setUp(list, false, "测试视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cxp1);
        mSmartPickVideo.setThumbImageView(imageView);

        //增加title
        mSmartPickVideo.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        mSmartPickVideo.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, mSmartPickVideo);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mSmartPickVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //是否可以滑动调整
        mSmartPickVideo.setIsTouchWiget(true);

        //设置返回按键功能
        mSmartPickVideo.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSmartPickVideo.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSmartPickVideo.onVideoResume();
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
            mSmartPickVideo.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mSmartPickVideo.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
