package com.cxp.player.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;
import com.cxp.player.list.adapter.MultiListAdapter;
import com.cxp.player.simple.adapter.SimpleListPlayerAdapter;
import com.cxp.player.video.manager.CustomManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: MultiListActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 16:33
 * 描    述: 多任务列表
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class MultiListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    private LinearLayoutManager mLinearLayoutManager;
    private MultiListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);

        mContext=this;

        mDatas = new ArrayList<>();

        //初始化数据
        initData();

        mLinearLayoutManager= new LinearLayoutManager(mContext);
        mAdapter = new MultiListAdapter(mContext, mDatas);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {


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
                            mAdapter.notifyDataSetChanged();
                        }
                    }
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
    public void onBackPressed() {
        if (CustomManager.backFromWindowFull(this, mAdapter.getFullKey())) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomManager.onPauseAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomManager.onResumeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomManager.clearAllVideo();
    }
}
