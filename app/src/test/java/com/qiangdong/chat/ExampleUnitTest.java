package com.qiangdong.chat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        List<Timer> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(new Timer());
//        }
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).cancel();
//        }
        Map<Integer, Timer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i, new Timer());
        }
        for (Integer id : map.keySet()) {
            Timer timer=map.get(id);
            assert timer != null;
            timer.cancel();
            timer=null;
        }
    }
}