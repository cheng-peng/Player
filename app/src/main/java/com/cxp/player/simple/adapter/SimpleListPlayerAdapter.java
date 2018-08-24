package com.cxp.player.simple.adapter;

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
 * 文 件 名: SimpleListPlayerAdapter
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 9:29
 * 描    述: 简单列表播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimpleListPlayerAdapter extends RecyclerView.Adapter<SimpleListPlayerAdapter.SimpleListPlayerViewHolder> {

    public static final String TAG = "SimpleListPlayerAdapter";

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    public SimpleListPlayerAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public SimpleListPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimpleListPlayerViewHolder holder = new SimpleListPlayerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_simple_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull SimpleListPlayerViewHolder holder, int position) {

        holder.mItemSimpleListPlayer.loadCoverImage("", Integer.valueOf(mDatas.get(position).get("img").toString()));
//        ImageView imageView = new ImageView(mContext);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(Integer.valueOf(mDatas.get(position).get("img").toString()));
//        //设置封面
//        holder.mItemSimpleListPlayer.setThumbImageView(imageView);
//        holder.mItemSimpleListPlayer.setUp(mDatas.get(position).get("path").toString(), true, "么么哒");
        //在点击播放的时候才进行真正setup (这个设置封面图不好使，两种解决方式，一是自定义播放器，二是用setUp)
        holder.mItemSimpleListPlayer.setUpLazy(mDatas.get(position).get("path").toString(), true, null, null, mDatas.get(position).get("title").toString());
        //设置返回键
        holder.mItemSimpleListPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.mItemSimpleListPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mItemSimpleListPlayer.startWindowFullscreen(mContext, false, true);
            }
        });
        //防止错位设置
        holder.mItemSimpleListPlayer.setPlayTag(TAG);
        holder.mItemSimpleListPlayer.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        holder.mItemSimpleListPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        holder.mItemSimpleListPlayer.setReleaseWhenLossAudio(false);
        //全屏动画
        holder.mItemSimpleListPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.mItemSimpleListPlayer.setIsTouchWiget(false);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    static class SimpleListPlayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_simple_list_player)
        SampleCoverVideo mItemSimpleListPlayer;

         SimpleListPlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
