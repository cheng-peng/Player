package com.cxp.player.simple;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.simple.adapter.SimpleListWinPlayerAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SimpleListWinPlayerActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 9:23
 * 描    述: 简单列表小窗播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimpleListWinPlayerActivity extends BaseActivity {

    @BindView(R.id.simple_list_recyclerview)
    RecyclerView mSimpleListRecyclerview;

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private SimpleListWinPlayerAdapter mSimpleListWinPlayerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    //视频辅助类
    private GSYVideoHelper smallVideoHelper;
    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    private int firstVisibleItem, lastVisibleItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_player);
        ButterKnife.bind(this);

        mContext = this;
        mDatas = new ArrayList<>();

        //创建小窗口帮助类
        smallVideoHelper = new GSYVideoHelper(this);
        //配置
        gsySmallVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
        gsySmallVideoHelperBuilder
                .setHideStatusBar(true)
                .setNeedLockFull(true)   //是否需要锁屏功能
                .setCacheWithPlay(true)   //是否需要遍缓存
                .setShowFullAnimation(false)   //是否使用全屏动画效果
                .setRotateViewAuto(false)   //是否开启自动旋转
                .setLockLand(true)   //全屏默认横屏
                .setIsTouchWiget(false)   //是否可以滑动界面改变进度，声音等
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onQuitSmallWidget(String url, Object... objects) {
                        super.onQuitSmallWidget(url, objects);
                        //大于0说明有播放,//对应的播放列表TAG
                        if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(SimpleListWinPlayerAdapter.TAG)) {
                            //当前播放的位置
                            int position = smallVideoHelper.getPlayPosition();
                            //不可视的是时候
                            if ((position < firstVisibleItem || position > lastVisibleItem)) {
                                //释放掉视频
                                smallVideoHelper.releaseVideoPlayer();
                                mSimpleListWinPlayerAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);

        //初始化数据
        initDatas();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mSimpleListWinPlayerAdapter = new SimpleListWinPlayerAdapter(mContext, mDatas,smallVideoHelper,gsySmallVideoHelperBuilder);
        mSimpleListRecyclerview.setLayoutManager(mLinearLayoutManager);
        mSimpleListRecyclerview.setAdapter(mSimpleListWinPlayerAdapter);
        mSimpleListRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(SimpleListWinPlayerAdapter.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(mContext, 150);
                            //actionbar为true才不会掉下面去  (显示窗口播放)
                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (smallVideoHelper.isSmall()) {
                            smallVideoHelper.smallVideoToNormal();
                        }
                    }
                }
            }
        });
    }

    //初始化数据
    private void initDatas() {

        for (int i = 0; i < 20; i++) {
            String path1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
            String path2 = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
            Map<String, Object> map = new HashMap<>();
            if (i % 2 == 0) {
                map.put("title", "程小鹏。");
                map.put("path", path1);
                map.put("img", R.mipmap.cxp);
            } else {
                map.put("title", "程小鹏是好人。");
                map.put("path", path2);
                map.put("img", R.mipmap.cxp1);
            }
            mDatas.add(map);
        }
    }

    @Override
    public void onBackPressed() {
        if (smallVideoHelper.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smallVideoHelper.releaseVideoPlayer();
        GSYVideoManager.releaseAllVideos();
    }
}
