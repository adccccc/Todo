package com.example.administrator.todo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoScrollViewPager viewPager;
    private BottomNavigationView navigation;
    private List<Fragment> listFragment;

    // 底部导航栏监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tasks:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_calendar:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_achievements:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);

        listFragment = new ArrayList<>();
        listFragment.add(new TaskFragment());       //添加Fragment进ViewPager的数据源
        listFragment.add(new CalendarFragment());
        listFragment.add(new AchievementFragment());

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), this, listFragment);
        viewPager.setAdapter(adapter);

    }
}
