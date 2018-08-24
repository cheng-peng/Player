package com.cxp.player.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * 文 件 名: BaseActivity
 * 创 建 人: CXP
 * 创建日期: 2018-08-20 8:37
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class BaseActivity extends AppCompatActivity {

    protected void startActivity(Class cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
}
