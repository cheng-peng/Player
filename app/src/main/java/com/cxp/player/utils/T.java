package com.cxp.player.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 文 件 名: T
 * 创 建 人: CXP
 * 创建日期: 2017-01-21 6:32
 * 描    述: Toast工具类
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class T {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    private static void makeText(Context mContext, String text) {
        try {
            mHandler.removeCallbacks(r);
            if (mToast != null)
                mToast.setText(text);
            else
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);

            // 出现的位置
            // mToast.setGravity(Gravity.BOTTOM, Gravity.CENTER,
            // mContext.getResources().getDimensionPixelSize(R.dimen.dp100));
            mHandler.postDelayed(r, 1000);

            mToast.show();
        } catch (Exception e) {
        }
    }

    /**
     * 文字提示
     *
     * @param txt
     */
    public static void show(Context context, String txt) {
        makeText(context, txt);
    }
}
