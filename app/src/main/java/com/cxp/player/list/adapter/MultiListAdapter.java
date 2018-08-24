package com.cxp.player.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxp.player.R;
import com.cxp.player.video.SampleMultiVideo;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: MultiListAdapter
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 16:36
 * 描    述: 多任务列表
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class MultiListAdapter extends RecyclerView.Adapter<MultiListAdapter.MultiListViewHolder> {

    public static final String TAG = "MultiListAdapter";

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    private String fullKey = "null";

    public MultiListAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public MultiListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MultiListViewHolder holder = new MultiListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_item_multi_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull MultiListViewHolder holder, int position) {

        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.mItemMultiVideo.setPlayTag(TAG);
        holder.mItemMultiVideo.setPlayPosition(position);

        boolean isPlaying = holder.mItemMultiVideo.getCurrentPlayer().isInPlayingState();

        if (!isPlaying) {
            holder.mItemMultiVideo.setUpLazy(mDatas.get(position).get("path").toString(), false, null, null, mDatas.get(position).get("title").toString());
        }

        //增加title
        holder.mItemMultiVideo.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.mItemMultiVideo.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        holder.mItemMultiVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.mItemMultiVideo);
            }
        });
        holder.mItemMultiVideo.setRotateViewAuto(true);
        holder.mItemMultiVideo.setLockLand(true);
        holder.mItemMultiVideo.setReleaseWhenLossAudio(false);
        holder.mItemMultiVideo.setShowFullAnimation(true);
        holder.mItemMultiVideo.setIsTouchWiget(false);

        holder.mItemMultiVideo.setNeedLockFull(true);

        if (position % 2 == 0) {
            holder.mItemMultiVideo.loadCoverImage(mDatas.get(position).get("path").toString(), R.mipmap.cxp);
        } else {
            holder.mItemMultiVideo.loadCoverImage(mDatas.get(position).get("path").toString(), R.mipmap.cxp1);
        }

        holder.mItemMultiVideo.setVideoAllCallBack(new GSYSampleCallBack() {


            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                fullKey = "null";
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                holder.mItemMultiVideo.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                fullKey = holder.mItemMultiVideo.getKey();
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final SampleMultiVideo mItemMultiVideo) {
        mItemMultiVideo.startWindowFullscreen(mContext, false, true);
    }

    public String getFullKey() {
        return fullKey;
    }
    
    class MultiListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_multi_video)
        SampleMultiVideo mItemMultiVideo;

        public MultiListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
