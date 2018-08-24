package com.cxp.player.list;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.list.adapter.AutoListAdapter;
import com.cxp.player.utils.ScrollCalculatorHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: AuToListActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 15:52
 * 描    述: 列表自动播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class AuToListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private AutoListAdapter adapter;

    private LinearLayoutManager mLinearLayoutManager;

    private boolean mFull = false;
    private ScrollCalculatorHelper scrollCalculatorHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);

        mContext = this;

        mDatas = new ArrayList<>();

        //限定范围为屏幕一半的上下偏移180
        int playTop = CommonUtil.getScreenHeight(this) / 2 - CommonUtil.dip2px(this, 180);
        int playBottom = CommonUtil.getScreenHeight(this) / 2 + CommonUtil.dip2px(this, 180);
        //自定播放帮助类
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.item_auto_list_video, playTop, playBottom);

        //初始化数据
        initData();

        mLinearLayoutManager= new LinearLayoutManager(mContext);
        adapter = new AutoListAdapter(mContext, mDatas);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerview.setAdapter(adapter);

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                //这是滑动自动播放的代码
                if (!mFull) {
                    scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }

    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

}
