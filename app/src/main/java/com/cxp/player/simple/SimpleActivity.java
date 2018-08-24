package com.cxp.player.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: SimpleActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 8:48
 * 描    述: 简单播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SimpleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.simple_player, R.id.simple_list_1, R.id.simple_list_2, R.id.simple_detail_1, R.id.simple_detail_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.simple_player:
                //简单直接播放
                startActivity(SimplePlayerActivity.class);
                break;
            case R.id.simple_list_1:
                //简单列表播放
                startActivity(SimpleListPlayerActivity.class);
                break;
            case R.id.simple_list_2:
                //简单列表小窗播放
                startActivity(SimpleListWinPlayerActivity.class);
                break;
            case R.id.simple_detail_1:
                //简单详情播放1
                startActivity(SimpleDetailOneActivity.class);
                break;
            case R.id.simple_detail_2:
                //简单详情播放2
                startActivity(SimpleDetailTwoActivity.class);
                break;
        }
    }
}
