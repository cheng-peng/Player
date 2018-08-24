package com.cxp.player.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxp.player.R;
import com.cxp.player.video.SampleCoverVideo;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: AutoListAdapter
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 15:56
 * 描    述: 列表自动播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class AutoListAdapter extends RecyclerView.Adapter<AutoListAdapter.AutoListViewHolder> {

    public static final String TAG = "AutoListAdapter";

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    public AutoListAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public AutoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AutoListViewHolder holder = new AutoListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_auto_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull AutoListViewHolder holder, int position) {
        holder.mItemAutoListVideo.loadCoverImage("", Integer.valueOf(mDatas.get(position).get("img").toString()));
        //在点击播放的时候才进行真正setup (这个设置封面图不好使，两种解决方式，一是自定义播放器，二是用setUp)
        holder.mItemAutoListVideo.setUpLazy(mDatas.get(position).get("path").toString(), true, null, null, mDatas.get(position).get("title").toString());
        //设置返回键
        holder.mItemAutoListVideo.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.mItemAutoListVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mItemAutoListVideo.startWindowFullscreen(mContext, false, true);
            }
        });
        //防止错位设置
        holder.mItemAutoListVideo.setPlayTag(TAG);
        holder.mItemAutoListVideo.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        holder.mItemAutoListVideo.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        holder.mItemAutoListVideo.setReleaseWhenLossAudio(false);
        //全屏动画
        holder.mItemAutoListVideo.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.mItemAutoListVideo.setIsTouchWiget(false);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    static class AutoListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_auto_list_video)
        SampleCoverVideo mItemAutoListVideo;
        
        public AutoListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
