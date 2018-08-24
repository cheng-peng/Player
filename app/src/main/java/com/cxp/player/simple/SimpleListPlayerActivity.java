package com.cxp.player.simple;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.simple.adapter.SimpleListPlayerAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SimpleListPlayerActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 9:23
 * 描    述: 简单列表播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimpleListPlayerActivity extends BaseActivity {

    @BindView(R.id.simple_list_recyclerview)
    RecyclerView mSimpleListRecyclerview;

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private SimpleListPlayerAdapter mSimpleListPlayerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_player);
        ButterKnife.bind(this);

        mContext = this;
        mDatas = new ArrayList<>();

        //初始化数据
        initDatas();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mSimpleListPlayerAdapter = new SimpleListPlayerAdapter(mContext, mDatas);
        mSimpleListRecyclerview.setLayoutManager(mLinearLayoutManager);
        mSimpleListRecyclerview.setAdapter(mSimpleListPlayerAdapter);
        mSimpleListRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(SimpleListPlayerAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState((Activity) mContext)) {
                            GSYVideoManager.releaseAllVideos();
                            mSimpleListPlayerAdapter.notifyDataSetChanged();
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
