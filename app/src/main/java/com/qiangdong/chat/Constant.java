package com.qiangdong.chat;

import android.graphics.Color;

/**
 * Created by Meiji on 2017/4/13.
 */

public class Constant {

    public static final String USER_AGENT_MOBILE = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Mobile Safari/537.36";

    public static final String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public static final int[] TAG_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };

    public static final int[] ICONS_DRAWABLES = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher_round};

    public static final String[] ICONS_TYPE = new String[]{"circle", "rect", "square"};

    public static final int SLIDABLE_DISABLE = 0;
    public static final int SLIDABLE_EDGE = 1;
    public static final int SLIDABLE_FULL = 2;

    public static final String AS = "as";
    public static final String CP = "cp";

    public static final int NEWS_CHANNEL_ENABLE = 1;
    public static final int NEWS_CHANNEL_DISABLE = 0;

    /**
     * 初始化配置信息
     */
    public static String UPTOKEN;
    public static int chatNumber;
    public static int dokyPay;
    public static int googlePay;
    public static int rabbit;
    public static int sayHelloNumber;
    public static String channelId="1101";
}
