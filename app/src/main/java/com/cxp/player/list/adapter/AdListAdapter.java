package com.cxp.player.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxp.player.R;
import com.cxp.player.video.RequestListADVideoPlayer;
import com.cxp.player.video.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

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
public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.AdListViewHolder> {

    public static final String TAG = "AdListAdapter";

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    public AdListAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public AdListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdListViewHolder holder = new AdListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_ad_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull AdListViewHolder holder,final int position) {
        final String urlAD = "http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4";

        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.mItemAdVideo.setPlayTag(TAG);
        holder.mItemAdVideo.setPlayPosition(position);

        boolean isPlaying = holder.mItemAdVideo.getCurrentPlayer().isInPlayingState();

        if (!isPlaying) {
            holder.mItemAdVideo.setUpLazy(mDatas.get(position).get("path").toString(), false, null, null, mDatas.get(position).get("title").toString());
        }

        boolean isADPlaying = holder.mItemAdAdVideo.getCurrentPlayer().isInPlayingState();
        if (!isADPlaying) {
            holder.mItemAdAdVideo.setUpLazy(urlAD, false, null, null, "这是title");
        }


        //增加title
        holder.mItemAdVideo.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.mItemAdVideo.getBackButton().setVisibility(View.GONE);


        //设置全屏按键功能
        holder.mItemAdVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.mItemAdVideo);
            }
        });
        holder.mItemAdAdVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.mItemAdAdVideo);
            }
        });
        holder.mItemAdVideo.setRotateViewAuto(false);
        holder.mItemAdAdVideo.setRotateViewAuto(false);
        holder.mItemAdVideo.setLockLand(true);
        holder.mItemAdAdVideo.setLockLand(true);
        holder.mItemAdVideo.setReleaseWhenLossAudio(false);
        holder.mItemAdAdVideo.setReleaseWhenLossAudio(false);
        holder.mItemAdVideo.setShowFullAnimation(false);
        holder.mItemAdAdVideo.setShowFullAnimation(false);
        holder.mItemAdVideo.setIsTouchWiget(false);
        holder.mItemAdAdVideo.setIsTouchWiget(false);

        holder.mItemAdVideo.setNeedLockFull(true);

        if (position % 2 == 0) {
            holder.mItemAdVideo.loadCoverImage(mDatas.get(position).get("path").toString(), R.mipmap.cxp);
        } else {
            holder.mItemAdVideo.loadCoverImage(mDatas.get(position).get("path").toString(), R.mipmap.cxp1);
        }

        holder.mItemAdVideo.setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
                if (holder.mItemAdAdVideo.getGSYVideoManager().listener() != null) {
                    holder.mItemAdAdVideo.getGSYVideoManager().listener().onAutoCompletion();
                }
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                if (isNeedAdOnStart()) {
                    holder.mItemAdVideo.getCurrentPlayer().onVideoPause();
                    startAdPlay(holder.mItemAdAdVideo, holder.mItemAdVideo);
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                holder.mItemAdVideo.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });

        holder.mItemAdAdVideo.setVideoAllCallBack(new GSYSampleCallBack() {


            @Override
            public void onAutoComplete(String url, Object... objects) {
                //广告结束，释放
                holder.mItemAdAdVideo.getCurrentPlayer().release();
                holder.mItemAdAdVideo.onVideoReset();
                holder.mItemAdAdVideo.setVisibility(View.GONE);

                //开始播放原视频，根据是否处于全屏状态判断
                int playPosition = holder.mItemAdVideo.getGSYVideoManager().getPlayPosition();
                if (position == playPosition) {
                    holder.mItemAdVideo.getCurrentPlayer().startAfterPrepared();
                }

                if (holder.mItemAdAdVideo.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    holder.mItemAdAdVideo.removeFullWindowViewOnly();
                    if (!holder.mItemAdVideo.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                        resolveFullBtn(holder.mItemAdVideo);
                        holder.mItemAdVideo.setSaveBeforeFullSystemUiVisibility(holder.mItemAdAdVideo.getSaveBeforeFullSystemUiVisibility());
                    }
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                //退出全屏逻辑
                if (holder.mItemAdVideo.isIfCurrentIsFullscreen()) {
                    holder.mItemAdVideo.onBackFullscreen();
                }
            }

        });
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardmItemAdVideo) {
        standardmItemAdVideo.startWindowFullscreen(mContext, false, true);
    }


    /**
     * 显示播放广告
     */
    public void startAdPlay(RequestListADVideoPlayer gsymItemAdAdVideo, SampleCoverVideo normalPlayer) {
        gsymItemAdAdVideo.setVisibility(View.VISIBLE);
        gsymItemAdAdVideo.startPlayLogic();
        if (normalPlayer.getCurrentPlayer().isIfCurrentIsFullscreen()) {
            resolveFullBtn(gsymItemAdAdVideo);
            gsymItemAdAdVideo.setSaveBeforeFullSystemUiVisibility(normalPlayer.getSaveBeforeFullSystemUiVisibility());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    /**
     * 需要片头广告
     */
    public boolean isNeedAdOnStart() {
        return true;
    }

    static class AdListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_ad_video)
        SampleCoverVideo mItemAdVideo;
        @BindView(R.id.item_ad_ad_video)
        RequestListADVideoPlayer mItemAdAdVideo;


        public AdListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
