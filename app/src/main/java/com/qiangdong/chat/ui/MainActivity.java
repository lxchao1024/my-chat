package com.qiangdong.chat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qiangdong.chat.R;
import com.qiangdong.chat.adapter.ViewPagerAdapter;
import com.qiangdong.chat.modle.main.MainFragment;
import com.qiangdong.chat.ui.fragment.MessageFragment;
import com.qiangdong.chat.ui.fragment.MineFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView toolTitle;
    private String userID;
    private Fragment mainFragment, messageFragment, mineFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
//    private Fragment[] mFragments = new Fragment[]{new MainFragment(), new MessageFragment(), new MineFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        InjectFinder.injectView(this);
        userID = String.valueOf(getIntent().getIntExtra("userId",0));
        mainFragment = new MainFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();

        Bundle bundle = new Bundle();
        bundle.putString("userId", userID);
        mainFragment.setArguments(bundle);

        fragmentList.add(mainFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);

        toolTitle = findViewById(R.id.toolbar_title);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
        bottomNavigationView.setSelectedItemId(R.id.tab_two);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));

        toolTitle.setText(getResources().getString(R.string.tab_main));


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.tab_one:
                toolTitle.setText(getResources().getString(R.string.tab_main));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_two:
                toolTitle.setText(getResources().getString(R.string.tab_message));
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_three:
                toolTitle.setText(getResources().getString(R.string.tab_mine));
                viewPager.setCurrentItem(2);
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(position);
        menuItem.setChecked(true);
        switch (position) {
            case 0:
                toolTitle.setText(getResources().getString(R.string.tab_main));
                break;
            case 1:
                toolTitle.setText(getResources().getString(R.string.tab_message));
                break;
            case 2:
                toolTitle.setText(getResources().getString(R.string.tab_mine));
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView navigationView) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShifting(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
