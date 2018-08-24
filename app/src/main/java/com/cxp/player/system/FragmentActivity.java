package com.cxp.player.system;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cxp.player.R;
import com.cxp.player.base.BaseActivity;

/**
 * 文 件 名: FragmentActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-23 17:41
 * 描    述: Fragment 播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class FragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
}
