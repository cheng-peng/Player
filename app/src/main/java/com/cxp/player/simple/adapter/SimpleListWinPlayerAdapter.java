package com.cxp.player.simple.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cxp.player.R;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SimpleListWinPlayerAdapter
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 9:29
 * 描    述: 简单列表窗口播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimpleListWinPlayerAdapter extends RecyclerView.Adapter<SimpleListWinPlayerAdapter.SimpleListWinPlayerViewHolder> {

    public static final String TAG = "SimpleListWinPlayerAdapter";

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    //视频辅助类
    private GSYVideoHelper smallVideoHelper;
    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public SimpleListWinPlayerAdapter(Context context, List<Map<String, Object>> datas, GSYVideoHelper smallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder) {
        mContext = context;
        mDatas = datas;
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }

    @NonNull
    @Override
    public SimpleListWinPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimpleListWinPlayerViewHolder holder = new SimpleListWinPlayerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_simple_list_win, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull SimpleListWinPlayerViewHolder holder,final int position) {

        //增加封面
        ImageView mImagView=new ImageView(mContext);
        mImagView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImagView.setImageResource(Integer.valueOf(mDatas.get(position).get("img").toString()));


        //动态添加视频播放
        smallVideoHelper.addVideoPlayer(position, mImagView, TAG, holder.mListItemContainer, holder.mListItemBtn);
        holder.mListItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                smallVideoHelper.setPlayPositionAndTag(position, TAG);
                gsySmallVideoHelperBuilder.setVideoTitle(mDatas.get(position).get("title").toString())
                        .setUrl(mDatas.get(position).get("path").toString());
                smallVideoHelper.startPlay();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    static class SimpleListWinPlayerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_container)
        FrameLayout mListItemContainer;
        @BindView(R.id.list_item_btn)
        ImageView mListItemBtn;

        SimpleListWinPlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
