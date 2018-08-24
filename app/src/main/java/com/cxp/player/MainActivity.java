package com.cxp.player;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cxp.player.base.BaseActivity;
import com.cxp.player.list.ListActivity;
import com.cxp.player.simple.SimpleActivity;
import com.cxp.player.system.ADActivity;
import com.cxp.player.system.ControlActivity;
import com.cxp.player.system.DanmakuActivity;
import com.cxp.player.system.PlayActivity;
import com.cxp.player.system.PlayNextActivity;
import com.cxp.player.system.PreViewActivity;
import com.cxp.player.system.SmartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_bt2)
    Button mMainBt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.main_bt1, R.id.main_bt2, R.id.main_bt3, R.id.main_bt4, R.id.main_bt5,R.id.main_bt6,R.id.main_bt7,R.id.main_bt8,R.id.main_bt9})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_bt1:
                //简单播放
                startActivity(SimpleActivity.class);
                break;
            case R.id.main_bt2:
                //基本播放
                PlayActivity.startActivity(MainActivity.this, mMainBt2);
                break;
            case R.id.main_bt3:
                //控制播放
                startActivity(ControlActivity.class);
                break;
            case R.id.main_bt4:
                //带广告播放
                startActivity(ADActivity.class);
                break;
            case R.id.main_bt5:
                //无缝切换
                startActivity(SmartActivity.class);
                break;
            case R.id.main_bt6:
                //进度预览播放
                startActivity(PreViewActivity.class);
                break;
            case R.id.main_bt7:
                //下一集播放
                startActivity(PlayNextActivity.class);
                break;
            case R.id.main_bt8:
                //列表播放
                startActivity(ListActivity.class);
                break;
            case R.id.main_bt9:
                //弹幕播放
                startActivity(DanmakuActivity.class);
                break;

        }
    }

}