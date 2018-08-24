package com.cxp.player.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: ListActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 16:24
 * 描    述: 列表播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class ListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.list_bt1, R.id.list_bt2, R.id.list_bt3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.list_bt1:
                //列表自动播放
                startActivity(AuToListActivity.class);
                break;
            case R.id.list_bt2:
                //列表多任务播放
                startActivity(MultiListActivity.class);
                break;
            case R.id.list_bt3:
                //列表带广告播放
                startActivity(AdListActivity.class);
                break;
        }
    }
}
